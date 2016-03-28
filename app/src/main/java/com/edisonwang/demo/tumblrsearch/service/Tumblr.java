package com.edisonwang.demo.tumblrsearch.service;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author @wew
 */
public interface Tumblr {

    @Parcel
    final class Meta {
        int status;
        String message;
    }

    @Parcel
    final class TagSearchResult {
        Meta meta;
        Post[] response;
    }

    @Parcel
    final class Post {

        public String id;

        @SerializedName("post_url")
        public String postUrl;

        public String summary;

        public String caption;

        public long timestamp;

        @SerializedName("note_count")
        public int noteCount;

        @SerializedName("blog_name")
        public String blogName;

        public String type;
    }

    @GET("/v2/tagged")
    Call<TagSearchResult> tagged(@Query("tag")String tag, @Query("api_key")String key);

}
