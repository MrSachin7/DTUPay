FROM amazoncorretto:21-alpine

WORKDIR /usr/src

COPY target/quarkus-app /usr/src/quarkus-app

CMD ["java", "-Xmx64m", "-jar", "quarkus-app/quarkus-app.jar"]
