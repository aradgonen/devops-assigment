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
        System.out.println("Starting ---Twitter Producer---");
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
                if(line.length()==19){
                    sendTweetToKafka(this.topic,line,line,line);
                    continue;
                }
                JSONObject curTweet = new JSONObject(line);
                ArrayList<String> curHashtags = (ArrayList<String>) curTweet.getJSONObject("data").getJSONObject("entities").get("hashtags");
                String curUsername = curTweet.getJSONObject("data").getJSONObject("entities").get("username").toString();
                String curTweetId = curTweet.getJSONObject("data").getJSONObject("entities").get("id").toString();
                sendTweetToKafka(this.topic,curUsername,curTweetId,curHashtags.toString());
            }
        }
        catch (Exception e){
            startTweeterStream();//change to restart always
        }

    }
    private void sendTweetToKafka(String topic, String username, String tweetId, String hashtags) throws Exception{
        this.producer.send(new ProducerRecord<>(topic,username+tweetId+hashtags),(event, ex) -> {
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
