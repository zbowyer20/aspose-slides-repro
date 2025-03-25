FROM maven:3.9.5-eclipse-temurin-21 as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package

FROM openjdk:21-slim-bullseye

RUN apt-get update && \
    apt-get install -y --no-install-recommends libfreetype6 fontconfig && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    mkdir -p /pdfs /fonts /fonts-external /testFiles

VOLUME ["/pdfs", "/fonts", "/fonts-external", "/testFiles"]

COPY --from=build /app/target/aspose-demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]