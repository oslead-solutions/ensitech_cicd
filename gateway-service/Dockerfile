
FROM eclipse-temurin:21-jdk
WORKDIR /app
# Installer netcat pour tester la disponibilité des autres services
RUN apt-get update && apt-get install -y netcat-openbsd
COPY target/*.jar app.jar
EXPOSE 8888
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Attendre tous les services avant de démarrer la gateway
CMD sh -c "
  wait_for_service() {
    SERVICE_NAME=\$1
    HOST=\$2
    PORT=\$3
    echo \"Waiting for \$SERVICE_NAME on \$HOST:\$PORT...\"
    until nc -z \$HOST \$PORT; do echo \"...\$SERVICE_NAME not ready\"; sleep 3; done
    echo \"\$SERVICE_NAME is UP\"
  }

  wait_for_service 'Discovery' ensitech-container-discovery 8761
  wait_for_service 'Config' ensitech-container-config 9999
  wait_for_service 'Academic' ensitech-container-academic 8085
  wait_for_service 'Authentication' ensitech-container-authentication 8083
  wait_for_service 'Registration' ensitech-container-registration 8086
  wait_for_service 'Training' ensitech-container-training 8084
  wait_for_service 'User' ensitech-container-user 8081

  echo 'All services are UP! Starting Gateway...'
  exec java -jar app.jar
"