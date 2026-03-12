# 1. Build stage
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Gradle 래퍼와 설정 파일들을 먼저 복사 (캐시 효율화)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Windows 환경에서 작성된 gradlew의 줄바꿈 문제를 해결하고 권한 부여
RUN tr -d '\r' < gradlew > gradlew_unix && \
    mv gradlew_unix gradlew && \
    chmod +x gradlew

# 소스 코드 복사
COPY src src

# 빌드 실행 (상세 로그 확인을 위해 --info 추가 가능)
RUN ./gradlew clean bootJar -x test --no-daemon

# 2. Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]