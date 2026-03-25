# Build
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /home/app
COPY . .
RUN mvn package -DskipTests

# Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /home/app
COPY --from=builder /home/app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]