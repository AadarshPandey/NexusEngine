FROM eclipse-temurin:25-jdk AS nexus-application
WORKDIR /app
COPY ./nexus-application/target/nexus-application-1.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

FROM eclipse-temurin:25-jdk AS nexus-search
WORKDIR /app
COPY ./nexus-search/target/nexus-search-1.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
