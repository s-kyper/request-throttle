FROM openjdk:11-jre
EXPOSE 8080
COPY target/request-throttle-0.0.1.jar request-throttle-0.0.1.jar
ENTRYPOINT ["java","-jar","/request-throttle-0.0.1.jar"]
