package com.twitterfetcher;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;


public class TwitterPoller implements Runnable{
    private static final String BASE_URL = "twitter"; //change it
    OkHttpClient okHttpClient = new OkHttpClient();

    public int getCount() throws IOException {
        int count = 0;
        Request request = new Request.Builder().url(BASE_URL+"something").build();
        try(Response response = okHttpClient.newCall(request).execute()){
            ResponseBody responseBody = response.body();
            JSONObject jsonObject = new JSONObject(responseBody.toString());
            JSONArray jsonArray = (JSONArray) jsonObject.get("max_count");
            JSONObject firstElement = (JSONObject) jsonArray.get(0);
            count = (Integer) firstElement.get("total");
        }
        return count;
    }

    public void run() {
        try{
            int count = this.getCount();
            System.out.println(count);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}