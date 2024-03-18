FROM openjdk:17-alpine
EXPOSE 8000
COPY build/libs/content-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]