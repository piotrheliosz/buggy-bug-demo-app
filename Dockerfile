FROM openjdk:17-jdk-slim

WORKDIR /app

COPY /target/*.jar /app/buggy-bug-app.jar

ENTRYPOINT ["java", "-jar", "/app/buggy-bug-app.jar"]

EXPOSE 8080
