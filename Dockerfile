# Use a Maven image with Amazon Corretto 11 to build the application
FROM maven:3.9.6-amazoncorretto-11 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package
RUN ls /app/target/*.jar


FROM amazoncorretto:11

WORKDIR /app

COPY --from=build /app/target/*.jar azship.jar

EXPOSE 8080

CMD ["java", "-jar", "azship.jar"]
