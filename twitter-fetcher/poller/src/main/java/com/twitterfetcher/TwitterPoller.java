/**
 * Written By Arad Gonen 2022
 */
package com.twitterfetcher;

import okhttp3.*;
import okio.Buffer;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;


public class TwitterPoller implements Runnable{
    private static final String BASE_URL = "https://api.twitter.com"; //change it
    private static final String BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAANd%2BbgEAAAAAkZn1dU%2FI9ZBeqcyOhWIlH4Xp10I%3DzRmzrJju30hMlRvFULn99XW3eCFQeNFajwlCOMvXI29NQ5DsgG";
    OkHttpClient okHttpClient = new OkHttpClient();
    private String hashtag;

    public TwitterPoller(String hashtag) {
        this.hashtag = hashtag;
    }

    private JSONObject getTweetStreamByHashtag() throws IOException {
        Request request = new Request.Builder().url(BASE_URL+"/2/tweets/search/stream").header("Authorization","Bearer " + BEARER_TOKEN).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        }
        catch (IOException e) {
        }
            String responseString = response.body().string();
            JSONObject jsonObject = new JSONObject(responseString);
            return jsonObject;
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
//            String responseString = response.body().string();
//            JSONObject jsonObject = new JSONObject(responseString);
            Buffer buffer = new Buffer();
            while (!response.)
        }
    }

    public void run() {
        try{
            System.out.println("Creating rule");
            setFilteredStreamRule(this.hashtag);
            System.out.println("Listening to tweets");
            JSONObject jsonObject = getTweetStreamByHashtag();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
