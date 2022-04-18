FROM maven:latest
COPY . .
RUN mvn clean package
CMD ["java", "-cp", "target/api-poller-1.0-SNAPSHOT.jar","com.kafka.StartProducer"]
