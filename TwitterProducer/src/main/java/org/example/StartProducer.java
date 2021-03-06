package org.example;

import org.apache.kafka.clients.producer.ProducerConfig;

import java.io.IOException;
import java.util.Properties;

public class StartProducer {
    public static void main(String[] args) throws IOException {

        String topic = "tweets";
        String hashtag = "#DevOps";
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,System.getenv("BOOTSTRAP_SERVERS_CONFIG"));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        TwitterProducer twitterProducer = new TwitterProducer(topic,properties,hashtag);
        twitterProducer.start();
    }
}
