FROM openjdk:11-jre-slim
VOLUME /tmp
COPY build/libs/*-standalone.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
