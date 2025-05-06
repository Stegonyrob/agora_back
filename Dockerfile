# Utilizar una imagen base con Java 21.
# Basado en Debian, que nos permite instalar locales fácilmente.
FROM eclipse-temurin:21-jre-jammy



# Actualizar los paquetes e instalar locales
RUN apt-get update && apt-get install -y locales

# Generar el locale español (España) con codificación UTF-8.
# Puedes ajustar el locale si tu aplicación tiene requisitos específicos de idioma.
RUN locale-gen es_ES.UTF-8

# Establecer las variables de entorno para usar el locale español UTF-8.
ENV LANG=es_ES.UTF-8
ENV LANGUAGE=es_ES:es
ENV LC_ALL=es_ES.UTF-8

# Crear el directorio de trabajo dentro del contenedor.
WORKDIR /app

# Copiar el archivo JAR de la aplicación desde el host al contenedor.
# Asumimos que construyes el JAR con Maven y está en la carpeta 'target'.
COPY target/*.jar app.jar

# Exponer el puerto en el que escucha tu aplicación Spring Boot (por defecto es 8080).
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot.
# 'app.jar' debe ser el nombre del archivo JAR construido por Maven.
CMD ["java", "-jar", "app.jar"]

# Puedes añadir más configuraciones o dependencias si tu aplicación las necesita.
# Por ejemplo, copiar archivos de configuración adicionales, etc.