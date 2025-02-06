# Use an official Maven image for the build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy the pom.xml separately to leverage Docker caching for dependencies
COPY pom.xml .

# Download dependencies (without building the project)
RUN mvn dependency:go-offline

# Copy the source code
COPY src /app/src

# Build the project (skip tests to speed up the build)
RUN mvn clean package -DskipTests

# Use a minimal JDK runtime for the final image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
