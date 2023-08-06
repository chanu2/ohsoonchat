FROM openjdk:17-alpine AS builder

EXPOSE 8080

COPY ./build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]