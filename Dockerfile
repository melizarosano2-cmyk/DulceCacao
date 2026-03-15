# Etapa de construcción (Build)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compilar el proyecto empaquetándolo en un JAR, saltando las pruebas para mayor rapidez
RUN mvn clean package -DskipTests

# Etapa de ejecución (Run)
FROM openjdk:17-slim
WORKDIR /app
# Copiar el JAR generado en la etapa anterior
COPY --from=build /app/target/shop-0.0.1-SNAPSHOT.jar app.jar
# Exponer el puerto que usa Spring Boot
EXPOSE 8080
# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
