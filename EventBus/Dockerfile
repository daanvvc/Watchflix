FROM openjdk:17-jdk-slim as build
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY ./build/libs/EventBus-1.0-SNAPSHOT.jar ./
ENTRYPOINT ["java","-jar","/app/EventBus-1.0-SNAPSHOT.jar"]