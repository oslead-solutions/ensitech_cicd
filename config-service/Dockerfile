FROM eclipse-temurin:21-jdk
WORKDIR /app
# Installer netcat (nc) pour tester la disponibilité du Discovery Service
RUN apt-get update && apt-get install -y netcat-openbsd
COPY target/*.jar app.jar

EXPOSE 9999
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Attendre que le Discovery Service réponde sur le port 8761 avant de démarrer
CMD sh -c "while ! nc -z localhost 8761; do echo 'Waiting for Discovery...'; sleep 3; done && \
           echo 'Starting Config service...' && java -jar app.jar"