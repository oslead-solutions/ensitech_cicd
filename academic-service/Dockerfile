#Stage 1 : Build
FROM eclipse-temurin:21-jdk-noble As builder
LABEL authors="Jovy Nedved"

WORKDIR /app

COPY mvnw .

COPY .mvn .mvn

COPY pom.xml .

COPY src src

# Skip running tests
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre-noble

WORKDIR /app

COPY --from=builder /app/target/academic-service-*.jar academic-service.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar","academic-service.jar"]