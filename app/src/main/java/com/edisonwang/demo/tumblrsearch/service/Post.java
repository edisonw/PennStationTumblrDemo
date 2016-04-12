package com.edisonwang.demo.tumblrsearch.service;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * @author edi
 */
@Parcel
public final class Post {

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
