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
                line=reader.readLine();
                if(line.length()==19 || line.length()==0){
//                    sendTweetToKafka(this.topic,line,line,line);
                    continue;
                }
                else{
                    JSONObject curTweet = new JSONObject(line);
                    String curHashtags = curTweet.getJSONObject("data").getJSONObject("entities").get("hashtags").toString();
                    String curUsername = ((HashMap)curTweet.getJSONObject("includes").getJSONArray("users").toList().get(0)).get("username").toString();
                    String curTweetId = curTweet.getJSONObject("data").get("id").toString();
                    sendTweetToKafka(this.topic,curUsername,curTweetId,curHashtags);
                }
            }
        }
        catch (Exception e){
                e.printStackTrace();
        }

    }
    private void sendTweetToKafka(String topic, String username, String tweetId, String hashtags) throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",username);
        jsonObject.put("hashtags",hashtags);
        this.producer.send(new ProducerRecord<>(topic,tweetId,jsonObject.toString()),(event, ex) -> {
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
