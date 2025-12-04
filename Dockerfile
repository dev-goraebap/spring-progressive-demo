# Stage 1: Frontend Build
FROM node:20-alpine AS frontend-build
WORKDIR /app

# package 파일 먼저 복사 (캐싱 최적화)
COPY src/main/frontend/package*.json ./src/main/frontend/
RUN cd src/main/frontend && npm ci

# frontend 폴더 전체 복사 (템플릿 + 프론트엔드 소스)
COPY src/main/frontend ./src/main/frontend
RUN cd src/main/frontend && npm run build

# Stage 2: Backend Build
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Gradle 설정 파일만 먼저 복사 (캐싱 최적화)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# 의존성 다운로드 (변경 없으면 캐시 사용)
RUN gradle dependencies --no-daemon || true

# 소스 코드 복사
COPY src ./src

# Frontend 빌드 결과물 복사 (builds + .vite manifest)
COPY --from=frontend-build /app/src/main/resources/static/builds ./src/main/resources/static/builds
COPY --from=frontend-build /app/src/main/resources/static/.vite ./src/main/resources/static/.vite

# 빌드
RUN gradle build --no-daemon -x test

# Stage 3: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# JAR 파일만 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# prod 프로필로 실행 (application-prod.properties 사용)
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
