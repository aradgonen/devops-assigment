package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.io.IOException;
import java.util.Properties;

public class StartConsumer {
    public static void main(String[] args) throws IOException {

        String topic = "tweets";
        Properties properties = new Properties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "tweeter-consumers");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,System.getenv("BOOTSTRAP_SERVERS_CONFIG"));

        TwitterConsumer twitterConsumer = new TwitterConsumer(topic,properties);
        twitterConsumer.start();
    }
}
