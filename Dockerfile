# Usando imagem base com Java 21
FROM eclipse-temurin:21-jre-alpine

# Diretório da aplicação dentro do container
WORKDIR /app

# Copiar o JAR para o container
COPY target/geospatial-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que o Spring Boot vai rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
