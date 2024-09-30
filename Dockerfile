# Usar uma imagem base para Java (openjdk)
FROM openjdk:17-jdk-slim

# Definir o diretório de trabalho dentro do container
WORKDIR /app

# Copiar o arquivo JAR gerado para dentro do container
COPY target/token-validator.jar app.jar

# Expor a porta em que a aplicação Spring Boot irá rodar (ajuste conforme necessário)
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
