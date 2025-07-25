# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the data file into the container in /app directory
COPY vehicleDB.txt .

# Copy the source code to the container, maintaining the src directory
COPY src ./src

# Compile the Java code, specifying the source path to maintain package structure
RUN javac -d out --source-path src $(find src -name '*.java')

# Define the entry point for the container
# The classpath is the 'out' directory where compiled classes are
ENTRYPOINT ["java", "-cp", "out", "app.Main"]
