# ===== STAGE 1: BUILD =====
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia apenas arquivos necessários para o build
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd

# Baixa dependências (cache)
RUN ./mvnw -B dependency:go-offline

# Copia o código-fonte
COPY src src

# Gera o jar (sem testes)
RUN ./mvnw -B clean package -DskipTests

# ===== STAGE 2: RUNTIME =====
FROM eclipse-temurin:21
WORKDIR /app

# Copia somente o jar gerado
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
