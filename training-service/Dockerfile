
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Installer netcat (nc) pour tester la disponibilité du Discovery Service
RUN apt-get update && apt-get install -y netcat-openbsd
COPY target/*.jar app.jar

EXPOSE 8084
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Attendre que le Discovery Service réponde sur le port 8761 avant de démarrer
CMD sh -c "while ! nc -z ensitech-container-discovery 8761; do echo 'Waiting for Discovery...'; sleep 3; done && \
           echo 'Starting Training service...' && java -jar app.jar"
