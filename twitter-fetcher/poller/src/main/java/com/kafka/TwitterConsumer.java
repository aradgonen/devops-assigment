package com.kafka;

import org.apache.kafka.clients.consumer.*;
import org.json.JSONObject;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class TwitterConsumer {
    private String topic;
    private Properties properties;
    private Consumer<String,String> consumer;

    public TwitterConsumer(String topic, Properties properties) {
        this.topic = topic;
        this.properties = properties; //initialize when creating object
        this.consumer = new KafkaConsumer<>(properties);
    }

    public void start(){
        System.out.println("Starting ---Twitter Consumer---");
        try {
            this.consumer.subscribe(Arrays.asList(topic));
            consumeFromTopic(consumer, topic);
        } finally {
            this.consumer.close();
        }
    }

    static void consumeFromTopic(Consumer<String, String> consumer, String topic) {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                String tweetId = record.key();
                String username = new JSONObject(record.value()).toMap().get("username").toString();
                String hashtags = new JSONObject(record.value()).toMap().get("hashtags").toString();
                System.out.println(String.format("Consumed event from topic %s: tweetID = %s username = %s hashtags = %s ", topic, tweetId, username, hashtags));
            }
        }
    }

}
