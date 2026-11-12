# ============================================
# ETAPA 1: BUILD
# ============================================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia primeiro só o pom.xml para aproveitar cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código e builda
COPY src ./src
RUN mvn clean package -DskipTests

# ============================================
# ETAPA 2: RUNTIME
# ============================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/*.jar app.jar

# Porta que o Render vai usar (ele injeta $PORT, mas expor 8080 é boa prática)
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]