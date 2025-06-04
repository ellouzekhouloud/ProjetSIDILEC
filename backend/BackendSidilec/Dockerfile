# Étape 1 : Build avec Maven + JDK
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Runtime avec JDK uniquement
FROM openjdk:21-jdk-slim
WORKDIR /app

# Installer dos2unix temporairement, convertir le script, puis nettoyer
RUN apt-get update && apt-get install -y --no-install-recommends dos2unix \
    && rm -rf /var/lib/apt/lists/*

# Copier le script wait-for-it.sh et le convertir
COPY wait-for-it.sh wait-for-it.sh
RUN dos2unix wait-for-it.sh && chmod +x wait-for-it.sh

# Copier le WAR depuis l'image build
COPY --from=build /app/target/*.war app.war

EXPOSE 8080

# Utiliser wait-for-it pour attendre la base de données
ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "--timeout=60", "--strict", "--", "java", "-jar", "app.war"]
