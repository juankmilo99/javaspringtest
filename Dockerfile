# Dockerfile para la API de Envío de Pedidos ACME
FROM openjdk:17-jdk-slim

# Información del mantenedor
LABEL maintainer="ACME Supply Chain Team"
LABEL description="API REST para el ciclo de abastecimiento - Tienda Carrera 70"

# Instalar Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Crear directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Copiar código fuente
COPY src src

# Descargar dependencias (para optimizar el cache de Docker)
RUN mvn dependency:go-offline -B

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Exponer el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/api/v1/health || exit 1