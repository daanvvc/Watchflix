FROM openjdk:25-jdk-slim as build
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY ./build/libs/Gateway-1.0-SNAPSHOT.jar ./
ENTRYPOINT ["java","-jar","/app/Gateway-1.0-SNAPSHOT.jar"]