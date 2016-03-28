package com.edisonwang.demo.tumblrsearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.edisonwang.demo.tumblrsearch.service.Tumblr;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author edi
 */
public class SearchAdapter extends RecyclerView.Adapter<TumblrPost> {

    private final ArrayList<Tumblr.Post> mPosts = new ArrayList<>();
    private final LayoutInflater mInflater;

    public SearchAdapter(LayoutInflater inflater) {
        mInflater = inflater;
    }

    @Override
    public TumblrPost onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TumblrPost(mInflater.inflate(R.layout.post, parent, false));
    }

    @Override
    public void onBindViewHolder(TumblrPost holder, int position) {
        holder.bind(mPosts.get(position));
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void replaceWith(Tumblr.Post[] results) {
        mPosts.clear();
        mPosts.addAll(Arrays.asList(results));
        notifyDataSetChanged();
    }
}
