
FROM eclipse-temurin:21-jdk
WORKDIR /app
# Installer netcat pour tester la disponibilité des autres services
RUN apt-get update && apt-get install -y netcat-openbsd
COPY target/*.jar app.jar
EXPOSE 8888
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Attendre tous les services avant de démarrer la gateway
CMD sh -c "
  echo 'Waiting for Discovery...'; until nc -z ensitech-container-discovery 8761; do echo '...Discovery not ready'; sleep 3; done;
  echo 'Discovery is UP';

  echo 'Waiting for Config...'; until nc -z ensitech-container-config 9999; do echo '...Config not ready'; sleep 3; done;
  echo 'Config is UP';

  echo 'Waiting for Academic...'; until nc -z ensitech-container-academic 8085; do echo '...Academic not ready'; sleep 3; done;
  echo 'Academic is UP';

  echo 'Waiting for Authentication...'; until nc -z ensitech-container-authentication 8083; do echo '...Authentication not ready'; sleep 3; done;
  echo 'Authentication is UP';

  echo 'Waiting for Registration...'; until nc -z ensitech-container-registration 8086; do echo '...Registration not ready'; sleep 3; done;
  echo 'Registration is UP';

  echo 'Waiting for Training...'; until nc -z ensitech-container-training 8084; do echo '...Training not ready'; sleep 3; done;
  echo 'Training is UP';

  echo 'Waiting for User...'; until nc -z ensitech-container-user 8081; do echo '...User not ready'; sleep 3; done;
  echo 'User is UP';

  echo 'All services are UP! Starting Gateway...';
  exec java -jar app.jar
"
