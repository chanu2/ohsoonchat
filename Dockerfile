#FROM openjdk:17-alpine

#EXPOSE 8080

#COPY ./build/libs/*.jar app.jar
#
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:17-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080/tcp

ENTRYPOINT ["java","-jar","/app.jar"]