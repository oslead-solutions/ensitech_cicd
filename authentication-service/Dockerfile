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
# Installer netcat (nc) pour tester la disponibilité du Discovery Service
RUN apt-get update && apt-get install -y netcat-openbsd
COPY target/*.jar app.jar

EXPOSE 8083
#ENTRYPOINT ["java", "-jar", "app.jar"]
# Attendre que le Discovery Service réponde sur le port 8761 avant de démarrer
CMD sh -c "while ! nc -z ensitech-container-discovery 8761; do echo 'Waiting for Discovery...'; sleep 3; done && \
           echo 'Starting Authentication service...' && java -jar app.jar"