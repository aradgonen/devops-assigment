/**
 * Written By Arad Gonen 2022
 */
package org.example;

import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;


public class TwitterPoller implements Runnable{
    private static final String BASE_URL = "https://api.twitter.com"; //change it
    private static final String BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAANd%2BbgEAAAAAkZn1dU%2FI9ZBeqcyOhWIlH4Xp10I%3DzRmzrJju30hMlRvFULn99XW3eCFQeNFajwlCOMvXI29NQ5DsgG";
    private OkHttpClient okHttpClient;

    private String hashtag;

    public TwitterPoller(String hashtag) {
        this.hashtag = hashtag;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(50000, TimeUnit.MILLISECONDS);
        builder.readTimeout(20000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(60000, TimeUnit.MILLISECONDS);
        this.okHttpClient = new OkHttpClient(builder);
    }

    public BufferedReader getTweetStreamByHashtag() throws IOException {
        Request request = new Request.Builder().url(BASE_URL+"/2/tweets/search/stream?tweet.fields=entities&user.fields=username&expansions=entities.mentions.username").header("Authorization","Bearer " + BEARER_TOKEN).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        }
        catch (IOException e) {
            System.out.println("Got error - restarting...");
            getTweetStreamByHashtag();
        }
        return new BufferedReader(new InputStreamReader((response.body().byteStream())));
    }
    private void getRules(){
        //not needed right now
        // will get the rules
    }
    private void deleteRule(){
        //not needed right now
        // get the current defined rules and delete them
    }
    private void setFilteredStreamRule(String rule) throws IOException {
        String jsonBody = "{ \"add\":[{\"value\":"+'"'+rule+'"'+"}]}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jsonBody);
        Request request = new Request.Builder().url(BASE_URL+"/2/tweets/search/stream/rules").method("POST",requestBody).header("Authorization","Bearer " + BEARER_TOKEN).build();
        Response response = null;
        Call call = null;
        try {
            call = okHttpClient.newCall(request);
            response = call.execute();
            System.out.println("");
        }
        catch (IOException e) {
            String responseString = response.body().string();
            JSONObject jsonObject = new JSONObject(responseString);
        }
    }

    public void run() {
        try{
            System.out.println("Creating rule");
            setFilteredStreamRule(this.hashtag); // run only once
            System.out.println("Listening to tweets");
            getTweetStreamByHashtag();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
