FROM gradle:7.5-jdk17-alpine AS builder
WORKDIR /app
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle
COPY src /app/src

RUN ./gradlew build --stacktrace

FROM openjdk:11-jre-slim
WORKDIR /app
COPY file /app/file
COPY --from=builder /app/build/libs/tumblbug-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]