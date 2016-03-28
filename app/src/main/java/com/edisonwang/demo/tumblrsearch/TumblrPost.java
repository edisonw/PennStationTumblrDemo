package com.edisonwang.demo.tumblrsearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.edisonwang.demo.tumblrsearch.service.Tumblr;

/**
 * @author edi
 */
public class TumblrPost extends RecyclerView.ViewHolder {

    private final TextView mBlogTitle;

    public TumblrPost(View itemView) {
        super(itemView);
        mBlogTitle = (TextView) itemView.findViewById(R.id.blog_title);
    }

    public void bind(Tumblr.Post post) {
        mBlogTitle.setText(post.blogName);
    }
}
