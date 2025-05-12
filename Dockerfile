# Stage 1: Build the application
# Use official Maven image with JDK 21 as the build stage
FROM maven:3.8.6-openjdk-21 AS build

# Set working directory inside the container
WORKDIR /app

# First copy only the POM file to leverage Docker cache
# This way, dependencies are only downloaded when POM changes
COPY pom.xml .

# Download all dependencies (offline mode for faster subsequent builds)
RUN mvn dependency:go-offline

# Copy the actual source code
COPY src ./src

# Build the application package (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Runtime environment
# Use slim JDK 21 image for runtime to reduce final image size
FROM openjdk:21-jdk-slim

# Set working directory for runtime
WORKDIR /app

# Copy only the built JAR from the build stage
# The wildcard (*) helps avoid hardcoding the JAR filename
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application when container starts
# Using array syntax (exec form) for better signal handling
ENTRYPOINT ["java", "-jar", "app.jar"]