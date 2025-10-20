# Stage 1: Build
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Gradle 설정 파일만 먼저 복사 (캐싱 최적화)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# 의존성 다운로드 (변경 없으면 캐시 사용)
RUN gradle dependencies --no-daemon || true

# 소스 코드 복사 및 빌드
COPY src ./src
RUN gradle build --no-daemon -x test

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# JAR 파일만 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# prod 프로필로 실행 (application-prod.properties 사용)
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
