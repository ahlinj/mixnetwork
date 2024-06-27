# Use an official OpenJDK runtime as a parent image
FROM openjdk:24-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled class files to the container
COPY out/production/Mixnet /app/out

# Command to start a shell (interactive mode)
CMD ["bash"]