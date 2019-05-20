FROM openjdk:8-jdk-alpine
LABEL maintainer="leandrotula@gmail.com"
VOLUME /tmp
EXPOSE 8081
ARG JAR_FILE=target/spring-microservice-app-ws-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} spring-microservice-app-ws.jar
ENTRYPOINT ["java","-jar","/spring-microservice-app-ws.jar"]