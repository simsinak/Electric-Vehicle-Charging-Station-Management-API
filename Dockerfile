FROM openjdk:8-jdk-alpine

LABEL maintainer="sina.askarnejad@gmail.com"

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} restapi.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/restapi.jar"]