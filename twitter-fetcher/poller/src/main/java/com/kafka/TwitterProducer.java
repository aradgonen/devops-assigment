package com.kafka;

import org.apache.kafka.clients.producer.*;
import java.io.*;
import java.util.*;

import com.twitterfetcher.TwitterPoller;
import org.json.JSONObject;

public class TwitterProducer {
    private String topic;
    private Properties properties;
    private Producer<String,String> producer;
    private String hashtag;
    public TwitterProducer(String topic, Properties properties,String hashtag) {
        this.topic = topic;
        this.properties = properties;
        this.producer = new KafkaProducer<>(properties);
        this.hashtag = hashtag;
    }

    public void start(){
        try{
            startTweeterStream();
        }
        catch (Exception e){
            System.out.println(e.getStackTrace());
        }
    }

    private void startTweeterStream() throws IOException {
        TwitterPoller twitterPoller = new TwitterPoller(this.hashtag);
        try {
            BufferedReader reader = twitterPoller.getTweetStreamByHashtag();
            String line = reader.readLine();
            while (line != null){
                line = reader.readLine();
                JSONObject curTweet = new JSONObject(line);
                ArrayList<String> curHashtags = (ArrayList<String>) curTweet.getJSONObject("data").getJSONObject("entities").get("hashtags");
                //pass username
                //pass tweetID
                this.producer.send(new ProducerRecord<>(this.topic,curHashtags.toString()),(event, ex) -> {
                    if (ex != null){
                        ex.printStackTrace();
                    }
                    else {
                        System.out.println("Sent tweet to topic:"+this.topic);
                    }
                });
                this.producer.flush();
            }
        }
        catch (Exception e){
            startTweeterStream();
        }

    }

}