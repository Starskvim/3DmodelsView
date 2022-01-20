FROM openjdk:17-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY target/3DmodelView-0.0.1-SNAPSHOT.jar testApp.jar
EXPOSE 8189
ENTRYPOINT ["java","-jar","/app.jar"]
