# Use Maven to build
#FROM maven:3.9.6-eclipse-temurin-21 AS build
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -DskipTests

# Use JDK to run
#FROM eclipse-temurin:21-jdk
#WORKDIR /app
#COPY --from=build /app/target/*.jar app.jar
#EXPOSE 8083
#ENTRYPOINT ["java", "-jar", "app.jar"]

#FROM eclipse-temurin:21-jdk
#WORKDIR /app
#COPY target/*.jar app.jar
#EXPOSE 8084
#ENTRYPOINT ["java", "-jar", "app.jar"]


FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/*.jar app.jar
#COPY .env .env
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]