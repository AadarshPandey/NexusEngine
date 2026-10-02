# Stage 1: Build the Application
FROM eclipse-temurin:25-jdk AS builder
RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY . /app
RUN mvn clean package -Dmaven.test.skip=true

# Stage 2: Create runtime image for nexus-application
FROM eclipse-temurin:25-jdk AS nexus-application
WORKDIR /app
COPY --from=builder /app/nexus-application/target/nexus-application-1.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Stage 3: Create runtime image for nexus-search
FROM eclipse-temurin:25-jdk AS nexus-search
WORKDIR /app
COPY --from=builder /app/nexus-search/target/nexus-search-1.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
