package com.edisonwang.demo.tumblrsearch.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author @wew
 */
public interface Tumblr {

    @GET("/v2/tagged")
    Call<TagSearchResult> tagged(@Query("tag")String tag, @Query("api_key")String key);

}