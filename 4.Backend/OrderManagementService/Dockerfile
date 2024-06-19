FROM maven:3.8.4-openjdk-11 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM adoptopenjdk:11-jre-hotspot

VOLUME /tmp

COPY --from=build /app/target/*.jar Order-Management-Service-0.0.1.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","/Order-Management-Service-0.0.1.jar"]

