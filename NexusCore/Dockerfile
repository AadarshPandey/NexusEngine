# Stage 1: Build the Application
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# Stage 2: Create runtime image for nexus-application
FROM eclipse-temurin:21-jre-alpine AS nexus-application
WORKDIR /app
COPY --from=builder /app/nexus-application/target/nexus-application-1.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Stage 3: Create runtime image for nexus-search
FROM eclipse-temurin:21-jre-alpine AS nexus-search
WORKDIR /app
COPY --from=builder /app/nexus-search/target/nexus-search-1.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
