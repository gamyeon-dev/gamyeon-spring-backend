# 1. Build stage
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY . .
# 실행 권한 부여 및 빌드 (테스트는 CI 단계에서 제어하거나 제외)
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar -x test

# 2. Run stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# 빌드 스테이지에서 생성된 jar 파일만 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]