# CLAUDE.md — Gamyeon Backend

> **아키텍처 결정 이력**: 멀티모듈 MSA → **모놀리식 Half-Hexagonal** (2026-03-08)
> 배경: 코드 25개 파일 수준에서 6모듈 Gradle 그래프는 과도한 오버헤드. AI 서버는 언어가 달라 어차피 외부 통신이므로 모놀리식 여부와 무관.

---

## 프로젝트 개요

면접 연습 서비스 **가연(Gamyeon)** 의 Spring Boot 백엔드.

- **Framework**: Spring Boot 3.5.11, Java 17
- **Build**: Gradle (단일 모듈)
- **Group**: `com.gamyeon`
- **Architecture**: Monolith + Half-Hexagonal (DDD)
- **Port**: 8080 (단일)

---

## 패키지 구조

```
com.gamyeon/
├── common/                          # 공통 유틸
│   ├── exception/
│   │   ├── BaseException.java
│   │   └── ErrorCode.java           # interface
│   └── response/
│       ├── SuccessResponse.java     # 성공 응답 래퍼
│       ├── ErrorResponse.java       # 에러 응답 래퍼
│       └── FieldError.java
│
├── user/                            # 유저 도메인
│   ├── domain/
│   │   ├── User.java                # @Entity, 도메인 로직 포함
│   │   ├── OAuthProvider.java
│   │   ├── UserStatus.java
│   │   ├── RefreshToken.java        # @Entity
│   │   ├── BaseTimeEntity.java      # @MappedSuperclass
│   │   └── UserDomainException.java
│   ├── application/
│   │   ├── port/
│   │   │   ├── inbound/             # UseCase 인터페이스 + 입출력 DTO
│   │   │   └── outbound/            # UserRepository, OAuthPort (interface)
│   │   └── service/                 # AuthService, UserService, NicknameResolver
│   └── infrastructure/
│       ├── persistence/             # UserJpaRepository, UserRepositoryImpl
│       ├── external/                # OAuth WebClient 어댑터
│       ├── web/                     # AuthController, UserController, GlobalExceptionHandler
│       ├── security/                # JwtAuthenticationFilter, InternalApiKeyFilter
│       └── di/                      # @Configuration Bean 등록
│
├── interview/                       # 면접 도메인
│   ├── domain/
│   │   └── session/                 # InterviewSession (PENDING/READY/IN_PROGRESS/DONE)
│   ├── application/
│   │   ├── port/
│   │   │   ├── inbound/             # QuestionGenerationUseCase 등
│   │   │   └── outbound/            # InterviewRepository, AiQuestionPort (interface)
│   │   └── service/
│   └── infrastructure/
│       ├── persistence/
│       ├── external/                # AiQuestionAdapter (WebClient → Python)
│       └── web/
│           ├── InterviewController.java
│           └── QuestionCallbackController.java  # Python 웹훅 수신
│
├── report/                          # 리포트 도메인
│   ├── domain/
│   │   └── task/                    # ReportTask (PENDING/PROCESSING/DONE/FAILED + UUID)
│   ├── application/
│   │   ├── port/
│   │   │   ├── inbound/
│   │   │   └── outbound/            # AiReportPort (interface)
│   │   └── service/
│   └── infrastructure/
│       ├── persistence/             # PostgreSQL(메타) + MongoDB(리포트 본문)
│       ├── external/                # AiReportAdapter (WebClient → Python)
│       └── web/
│           ├── ReportController.java
│           └── ReportCallbackController.java    # Python 웹훅 수신
│
└── admin/                           # 백오피스 (어드민)
    ├── application/service/
    └── infrastructure/web/          # /admin/** 경로, ROLE_ADMIN 필요
```

---

## 외부 시스템 연동

| 시스템 | 역할 | 통신 방식 |
|--------|------|----------|
| Python FastAPI (AI 서버) | 질문 생성, 리포트 분석 | HTTP REST (WebClient) + Webhook 콜백 |
| PostgreSQL | 주 관계형 DB | JPA |
| MongoDB | 면접 리포트 Document | Spring Data MongoDB |
| AWS S3 | 이력서/포트폴리오 파일 | AWS SDK |

---

## 인증 방식

- **자체 회원가입/로그인 없음** — OAuth 2.0 소셜 로그인 전용
- 지원 Provider: **Google**, **Kakao**
- JWT 기반 인증 (Access + Refresh Token)

### 소셜 로그인 닉네임 규칙

- Provider 제공 닉네임이 조건(1~8자, 허용 문자)에 부합 → 그대로 사용
- 부합하지 않을 때 → 이메일 `@` 앞 부분을 닉네임으로 사용
- 프로필 정보 동의 거부 시 → 이메일 `@` 앞 부분을 닉네임으로 사용

---

## 보안 구조

Spring Security 필터 체인이 인증·인가·CORS를 단일 프로세스에서 처리한다.

```
Request
  │
  ├── [1] CorsFilter              — CORS 처리
  ├── [2] InternalApiKeyFilter    — /api/internal/** 경로: X-Internal-API-Key 검증
  ├── [3] JwtAuthenticationFilter — 일반 경로: JWT 검증 + SecurityContext 설정
  └── [4] Controller
```

| 경로 패턴 | 인증 방식 | 비고 |
|-----------|----------|------|
| `/api/v1/auth/**` | 인증 불필요 | 로그인, 토큰 갱신 |
| `/api/v1/**` | JWT (Bearer) | 일반 사용자 API |
| `/api/internal/**` | X-Internal-API-Key | Python AI 서버 → Spring 웹훅 수신 전용 |
| `/admin/**` | JWT + ROLE_ADMIN | 백오피스 |
| `/health`, `/actuator/**` | 인증 불필요 | 헬스체크 |

---

## AI 연동 패턴

AI 서버 호출은 반드시 **Port 인터페이스**를 통해 이루어진다. 구현체는 `infrastructure/external/`에만 위치한다.

```java
// application/port/outbound/
public interface AiQuestionPort {
    String requestQuestionGeneration(QuestionGenerationCommand command); // task_id 반환
}

// infrastructure/external/
public class AiQuestionAdapter implements AiQuestionPort {
    // WebClient로 Python POST /ai/question/generate 호출
    // 202 Accepted → task_id 반환
}
```

웹훅 수신 엔드포인트는 `X-Internal-API-Key`로만 접근 가능하다.

```
[질문 생성 완료 콜백]
Python ──POST /api/internal/question/callback──▶ QuestionCallbackController
  → application service → InterviewSession.status = READY

[리포트 분석 완료 콜백]
Python ──POST /api/internal/report/callback──▶ ReportCallbackController
  → ReportTask.status = DONE → MongoDB 저장
```

> 상세: [`docs/ai-integration-design.md`](docs/ai-integration-design.md)

---

## 핵심 정책 요약

| 항목 | 정책 |
|------|------|
| 닉네임 | 1~8자, 한글/영어/숫자, 중복 허용 |
| 인증 | OAuth 2.0 전용 (Google, Kakao). 자체 비밀번호 없음 |
| Access Token | 유효기간 1시간 |
| Refresh Token | 유효기간 7일 |
| 탈퇴 | Soft delete (`WITHDREW`) |
| 유저 상태 | `ACTIVE`, `WITHDREW`, `WARNED`, `BANNED` |

---

## 레이어 규칙

| 레이어 | 의존 가능 | 금지 |
|--------|----------|------|
| `domain` | 순수 Java. JPA 어노테이션 허용 | HTTP, 외부 서비스 직접 호출 |
| `application` | `domain`, `port` 인터페이스 | `infrastructure` 직접 import |
| `infrastructure` | `application`, `domain` | 비즈니스 로직 |

**매핑 전략**

| 경계 | 전략 |
|------|------|
| Domain / JPA Entity | No Mapping (Entity = Domain) |
| Web ↔ Application | Two-Way Mapping (Request/Response DTO) |
| Application ↔ 영속성 | Two-Way Mapping (Port → Repository 구현체) |
| AI 서버 어댑터 | Two-Way Mapping (외부 스펙 변경 격리) |

---

## 개발 컨벤션

- Entity 필드 검증은 도메인 레벨에서 수행
- DTO ↔ Entity 변환은 정적 팩토리 메서드 (`from()`, `of()`)
- 예외는 커스텀 예외 클래스 + `@RestControllerAdvice` GlobalExceptionHandler
- API 응답은 성공 시 `SuccessResponse<T>`, 실패 시 `ErrorResponse` 사용 (단일 래퍼 `ApiResponse` 미사용)
- 환경변수는 `application-local.yml` 분리 (Git 커밋 금지)
- 도메인 패키지 간 직접 Java 호출 지양 — application layer를 통해 협력

## 코드 배치 판단 기준

```
Q: DB 쿼리는 어디에?
A: infrastructure/persistence/ → Repository 구현체

Q: "세션은 READY 상태에서만 시작 가능" 로직은?
A: domain/ → InterviewSession 도메인 메서드

Q: Python AI 서버 호출 코드는?
A: infrastructure/external/ → AiQuestionAdapter

Q: "질문 7개 생성" 유스케이스 흐름은?
A: application/service/ → QuestionGenerationService (Port 인터페이스만 의존)

Q: Python 콜백 수신 Controller는?
A: infrastructure/web/ → QuestionCallbackController (inbound adapter)

Q: Request/Response DTO는?
A: application/port/inbound/ (UseCase 입출력)

Q: Bean 등록/설정은?
A: infrastructure/di/ → @Configuration 클래스
```

---

## 참조 문서

| 문서 | 내용 |
|------|------|
| `docs/tech-stack.md` | 기술 스택, 전체 아키텍처, 패키지 구조 상세 |
| `docs/user-design.md` | 유저 도메인 설계 (OAuth, JWT, 닉네임 정책) |
| `docs/security-design.md` | Spring Security 필터 체인, JWT/내부 API Key 설계 |
| `docs/ai-integration-design.md` | AI 서버 연동, 비동기 웹훅, 재시도 정책 |
| `docs/work-phases.md` | 구현 단계별 작업 명세 |
