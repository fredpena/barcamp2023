FROM eclipse-temurin:17-jre
MAINTAINER fredpena.dev
COPY target/barcamp.jar barcamp.jar
EXPOSE 32531
ENTRYPOINT ["java", "-jar", "/barcamp.jar"]

