# Use the official OpenJDK 21 image as a base
FROM eclipse-temurin:21

# Set the working directory inside the container
WORKDIR /app

RUN apt-get update && apt-get install -y \
    curl \
    vim \
    git \
    && rm -rf /var/lib/apt/lists/*

# Copy the Gradle wrapper and build files
COPY gradlew build.gradle settings.gradle ./
COPY gradle gradle

# Copy the source code
COPY src src

# Install Gradle dependencies
RUN ./gradlew build -x test

# RUN groupadd -r devcontainer && useradd -r -g devcontainer -m devcontainer

# USER devcontainer

# Expose the port that the application will run on
EXPOSE 8082

# Command to run the application in development mode
# CMD ["./gradlew", "bootRun"]
