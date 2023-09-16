FROM openjdk:11-slim-buster

WORKDIR /app

ARG ORIGINAL_JAR_FILE=./build/libs/funding-service-1.0.0.jar

COPY ${ORIGINAL_JAR_FILE} funding-service.jar

CMD ["java", "-jar", "/app/funding-service.jar"]
