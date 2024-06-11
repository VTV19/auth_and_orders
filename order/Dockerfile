FROM openjdk:21-slim as build

COPY /order app/
WORKDIR /app
RUN ["./mvnw", "clean", "package"]

FROM openjdk:21-slim

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]