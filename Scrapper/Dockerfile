FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./pom.xml
COPY Bot/pom.xml ./Bot/pom.xml
COPY Scrapper/pom.xml ./Scrapper/pom.xml

RUN mvn dependency:go-offline -pl Scrapper -am -B

COPY Scrapper/src ./Scrapper/src
RUN mvn package -pl Scrapper -am -DskipTests -B

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/Scrapper/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
