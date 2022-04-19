#Split to stages, this is build stage
FROM maven:3.8.5-openjdk-11 AS build
COPY ./ ./
RUN mvn clean package

#Build the smaller image
FROM openjdk:11-jre-slim
COPY --from=build target/TwitterProducer-1.0-SNAPSHOT.jar /usr/local/lib/TwitterProducer.jar
CMD ["java", "-jar", "/usr/local/lib/TwitterProducer.jar"]
