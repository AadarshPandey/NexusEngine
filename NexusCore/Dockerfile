# syntax=docker/dockerfile:1

# ─── Stage 1: Build JARs ──────────────────────────────────────────────────────
FROM maven:3.9.9-eclipse-temurin-25 AS builder
WORKDIR /workspace

# Copy POM files for dependency caching
COPY pom.xml ./
COPY nexus-common/pom.xml nexus-common/
COPY nexus-domain/pom.xml nexus-domain/
COPY nexus-security/pom.xml nexus-security/
COPY nexus-admin-api/pom.xml nexus-admin-api/
COPY nexus-portal-api/pom.xml nexus-portal-api/
COPY nexus-search/pom.xml nexus-search/
COPY nexus-ai/pom.xml nexus-ai/
COPY nexus-application/pom.xml nexus-application/

# Copy source and build
COPY . .
RUN mvn clean package -DskipTests -B

# ─── Stage 2: Runtime for nexus-application ───────────────────────────────────
FROM eclipse-temurin:25-jre-alpine AS nexus-application
WORKDIR /app

RUN addgroup -g 10001 appgroup && \
    adduser -u 10001 -G appgroup -s /bin/sh -D appuser && \
    chown -R appuser:appgroup /app

USER 10001

COPY --from=builder --chown=appuser:appgroup /workspace/nexus-application/target/nexus-application-1.0-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --retries=5 --start-period=30s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health/liveness || exit 1

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
