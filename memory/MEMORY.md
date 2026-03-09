# Gamyeon Backend — Claude Memory

## 사용자 워크플로우 규칙

- **커밋 / PR은 사용자가 직접 수행** — Claude가 `git commit`, `git push`, PR 생성을 하지 않는다.
- 스테이징(`git add`)까지는 OK, 그 이후 커밋은 금지.

## 프로젝트 개요

- Spring Boot 3.5.11 / Java 17, 단일 Gradle 모듈
- 아키텍처: Monolith + Half-Hexagonal (DDD)
- 패키지 루트: `com.gamyeon`
- 포트: 8080

## 담당 범위

- **이 개발자**: Phase 0 (공통 인프라), Phase 1 (User 도메인), Phase 2 (Security)
- **Phase 3~5** (Interview, Report, Admin): 타 담당자 — 건드리지 않음

## 주요 파일 구조 (구현 완료)

- `common/exception/` — BaseException, ErrorCode (interface)
- `common/response/` — SuccessResponse<T>, ErrorResponse, FieldError (ApiResponse 제거됨)
- `common/web/` — HealthController
- `user/` — domain, application/port, application/service, infrastructure 전체 구현

## 응답 클래스 컨벤션

- 성공: `SuccessResponse<T>.of(data)` / `SuccessResponse.of(message, data)`
- 실패: `ErrorResponse.of(errorCode)` / `ErrorResponse.of(code, message)` / `ErrorResponse.validation(errors)`
- 기존 `ApiResponse<T>` 완전 제거 (git staged 삭제 완료)

## .gitignore 주요 항목

- `CLAUDE.md`, `docs/` — git 추적 안 됨 (gitignore)
- `**/application-local.yml` — 환경변수 파일, 커밋 금지
