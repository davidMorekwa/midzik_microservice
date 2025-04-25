# # Stage 1: Build the application
# FROM eclipse-temurin:21-jdk-jammy AS builder
# WORKDIR /app
# COPY . .
# RUN ./gradlew clean build -x test

# # Stage 2: Create the runtime image
# FROM eclipse-temurin:21-jre-jammy
# WORKDIR /app
# COPY --from=builder /app/build/libs/*.jar authentication.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "authentication.jar"]
# FROM eclipse-temurin:21-jre-alpine
FROM eclipse-temurin:21
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} authentication.jar
ENTRYPOINT ["java","-jar","/authentication.jar"]