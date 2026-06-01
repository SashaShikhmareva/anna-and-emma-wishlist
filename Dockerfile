FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .
RUN ./mvnw clean package -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]