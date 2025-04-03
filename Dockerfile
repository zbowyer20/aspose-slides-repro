FROM openjdk:21-jdk-slim

RUN apt-get update && \
    apt-get install -y libfreetype6 fontconfig && \
    fc-cache -fv && \
    rm -rf /var/lib/apt/lists/*

RUN mkdir -p /pdfs
VOLUME ["/pdfs"]

RUN mkdir -p /fonts
RUN mkdir -p /fonts-external

ARG JAR_FILE=target/aspose-demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]