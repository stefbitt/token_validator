FROM openjdk:17-alpine

WORKDIR /app

COPY target/token-validator.jar app.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "app.jar"]
