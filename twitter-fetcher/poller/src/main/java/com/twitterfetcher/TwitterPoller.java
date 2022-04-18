/**
 * Written By Arad Gonen 2022
 */
package com.twitterfetcher;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;


public class TwitterPoller implements Runnable{
    private static final String BASE_URL = "https://reqres.in"; //change it
    OkHttpClient okHttpClient = new OkHttpClient();

    public int getCount() throws IOException {
        int count = 0;
        Request request = new Request.Builder().url(BASE_URL+"/api/users?page=2").build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        }
        catch (IOException e) {
        }
            String responseString = response.body().string();
            JSONObject jsonObject = new JSONObject(responseString);
            count = (Integer) jsonObject.get("total");
        return count;
    }

    public void run() {
        try{
            int count = this.getCount();
            System.out.println(count);
            System.out.println("Hello");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
