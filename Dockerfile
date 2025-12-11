# ============================================
# Multi-Stage Dockerfile for Spring Boot Application
# ============================================
# This Dockerfile uses a two-stage build process:
# 1. Build stage: Compiles the application using Maven
# 2. Runtime stage: Runs the compiled application

# ============================================
# STAGE 1: Build Stage
# ============================================
# Use an official Maven runtime with OpenJDK 17 as a parent image
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file first (for better layer caching)
# Docker caches layers, so if pom.xml hasn't changed, dependencies won't be re-downloaded
COPY JavaProject/pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy the rest of the application's source code
COPY JavaProject/src ./src

# Build the application and create a JAR file
# -DskipTests: Skip running tests during build (you can remove this to run tests)
# clean: Remove previous build files
# package: Compile code and package it into a JAR
RUN mvn clean package -DskipTests

# ============================================
# STAGE 2: Runtime Stage
# ============================================
# Use a lightweight JRE image for running the application
FROM eclipse-temurin:17-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Create a non-root user for security best practices
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the compiled JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Create directory for H2 database persistence
RUN mkdir -p /app/data && chown -R spring:spring /app

# Switch to non-root user
USER spring:spring

# Expose the port the app runs on
# This documents that the container listens on port 8080
EXPOSE 8080

# Define the command to run your app
# This is the default command executed when the container starts
CMD ["java", "-jar", "app.jar"]
