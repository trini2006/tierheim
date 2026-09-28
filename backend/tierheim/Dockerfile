FROM eclipse-temurin:21-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} tierheim.jar
ENTRYPOINT ["java", "-jar", "/tierheim.jar"]
EXPOSE 8085