# Use an official OpenJDK runtime as a parent image
FROM openjdk:24-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled class files to the container
COPY out/production/Mixnet /app/out

COPY run_java_EP.sh /app/run_java_EP.sh
COPY run_java_user.sh /app/run_java_user.sh

RUN chmod +x /app/run_java_EP.sh
RUN chmod +x /app/run_java_user.sh

# Command to start a shell (interactive mode)
CMD ["bash"]