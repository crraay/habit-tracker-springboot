# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# Runtime: run JAR with eclipse-temurin:17-jdk
FROM eclipse-temurin:17-jdk
# curl is present on eclipse-temurin images; used by HEALTHCHECK
RUN useradd -r -u 10001 -m appuser
WORKDIR /app
COPY --from=build /app/target/habit-tracker-springboot-0.0.1-SNAPSHOT.jar /app/app.jar
RUN chown -R appuser:appuser /app
USER appuser
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=90s --retries=5 \
  CMD curl -f http://127.0.0.1:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
