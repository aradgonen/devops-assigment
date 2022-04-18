/**
 * Written By Arad Gonen 2022
 */
package com.twitterfetcher;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class App{
    public static void main(String [] args){
        TwitterPoller twitterPoller = new TwitterPoller();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.scheduleAtFixedRate(twitterPoller, 0, 5, TimeUnit.SECONDS);
    }
}

