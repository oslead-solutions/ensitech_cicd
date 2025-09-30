FROM eclipse-temurin:21-jdk
WORKDIR /app
# Installer netcat pour tester la disponibilité des autres services
RUN apt-get update && apt-get install -y netcat-openbsd
COPY target/*.jar app.jar
EXPOSE 8086
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Attendre Discovery et Config avant de démarrer
CMD sh -c "while ! nc -z localhost 8761; do echo 'Waiting for Discovery...'; sleep 3; done && \
           while ! nc -z localhost 9999; do echo 'Waiting for Config...'; sleep 3; done && \
           echo 'Starting registration service...' && java -jar app.jar"

