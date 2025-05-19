FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
VOLUME /tmp
COPY target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
