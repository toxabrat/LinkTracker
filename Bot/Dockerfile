
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./pom.xml
COPY Bot/pom.xml ./Bot/pom.xml
COPY Scrapper/pom.xml ./Scrapper/pom.xml

RUN mvn dependency:go-offline -pl Bot -am -B

COPY Bot/src ./Bot/src
RUN mvn package -pl Bot -am -DskipTests -B

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/Bot/target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
