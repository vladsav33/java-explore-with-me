FROM amazoncorretto:11-alpine-jdk
COPY statSvc/target/statSvc-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]