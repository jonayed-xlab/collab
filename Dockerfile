FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
VOLUME /tmp
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
