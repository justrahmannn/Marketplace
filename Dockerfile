# Build stage
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY JavaProject/pom.xml .
COPY JavaProject/src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
