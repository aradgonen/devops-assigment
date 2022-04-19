FROM maven:3.8.5-openjdk-11
COPY ./ ./
RUN mvn clean package
CMD ["java", "-jar", "target/TwitterProducer-1.0-SNAPSHOT.jar"]
