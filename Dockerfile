# =============================
# Etapa 1: Build (compila o JAR)
# =============================
FROM eclipse-temurin:21 AS build

WORKDIR /app

# Copiar arquivos Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Baixa dependências
RUN ./mvnw dependency:resolve

# Copiar o código-fonte
COPY src ./src

# Compila o JAR sem rodar testes (opcional: pode remover -DskipTests se quiser testar)
RUN ./mvnw package -DskipTests

# =============================
# Etapa 2: Runtime (imagem enxuta)
# =============================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia apenas o JAR compilado da etapa anterior
COPY --from=build /app/target/geospatial-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que o Spring Boot vai rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]