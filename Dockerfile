# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the executable jar file from the host to the container
COPY target/process_future_movement-0.0.1-SNAPSHOT.jar /app/process-future-movement.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "process-future-movement.jar"]
