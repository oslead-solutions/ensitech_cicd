#Stage 1: Build the application with a full JDK
# FROM eclipse-temurin:21-jdk-alpine AS build
#LABEL authors="jovy"
# Copy the build file (e.g., pom.xml or build.gradle) and dependencies
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
# Copy the rest of the source code
#COPY src/ ./src


# Build the final JAR
#RUN ./mvnw clean package -DskipTests

#Stage 2: Create the final image with a lightweight JRE
#FROM eclipse-temurin:21-jre-alpine
#Set the working directory inside the container
#WORKDIR /app

# Define a build argument for the JAR file name
#ARG JAR_FILE=registration-service-0.0.1-SNAPSHOT.jar

#Copy the Spring Boot JAR file into the container
#Copy the JAR from the build stage using the dynamic argument.
#JAR_FILE could be equal to registration-service-0.0.1-SNAPSHOT.jar (<artifact-id>-<version>.jar)
#COPY --from=build target/${JAR_FILE} app.jar
#Expose the port your application runs on
#EXPOSE 8086
#ENTRYPOINT ["java", "-jar", "app.jar"]
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]

