FROM maven:3.8-openjdk-18-slim as build

WORKDIR /tmp/dockerapp

COPY pom.xml .
COPY src ./src/
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:18-jdk-jammy

COPY --from=build /tmp/dockerapp/target/*.jar /app/app.jar

WORKDIR /app
CMD ["java", "-jar", "/app/app.jar"]
