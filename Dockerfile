#use an official maven image to build the spring boot app
FROM maven:3.8.4-openjdk-17 AS build

#set the working directory
WORKDIR /app

#copy the pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

#copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

#use an official OpenJDK image to run the application
FROM openjdk:17-slim

#set the working directory
WORKDIR /app

#copy the built JAR file from the build stage
COPY --from=build /app/target/livora-0.0.1-SNAPSHOT.jar .

#expose 8080
EXPOSE 8080

#specify the command to run the application
ENTRYPOINT ["java", "-jar", "/app/livora-0.0.1-SNAPSHOT.jar"]