# Use an official Maven image as the build environment
FROM maven:3.8.1-openjdk-11 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# Use an official OpenJDK image as the run environment
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/my-mqtt-1.0-SNAPSHOT.jar /usr/local/lib/my-mqtt-1.0-SNAPSHOT.jar

# Set the entry point to run your application
ENTRYPOINT ["java","-jar","/usr/local/lib/my-mqtt-1.0-SNAPSHOT.jar"]
