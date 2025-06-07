# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

# Etapa de execução
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/target/contactbook-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
