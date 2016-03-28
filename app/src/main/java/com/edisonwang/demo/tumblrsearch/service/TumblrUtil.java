package com.edisonwang.demo.tumblrsearch.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author @wew
 */
public class TumblrUtil {

    public static Tumblr getTumblrService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.tumblr.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(Tumblr.class);
    }

    public static String apiKey() {
        return "";
    }
}
