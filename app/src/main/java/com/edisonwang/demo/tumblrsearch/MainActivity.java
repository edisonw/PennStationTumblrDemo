package com.edisonwang.demo.tumblrsearch;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import com.edisonwang.demo.tumblrsearch.service.SearchByTag;
import com.edisonwang.demo.tumblrsearch.service.SearchByTagEventFailure;
import com.edisonwang.demo.tumblrsearch.service.SearchByTagEventSuccess;
import com.edisonwang.demo.tumblrsearch.service.SearchByTag_.PsSearchByTag;
import com.edisonwang.demo.tumblrsearch.utils.DelayedWatcher;
import com.edisonwang.ps.annotations.EventListener;
import com.edisonwang.ps.lib.EventService;
import com.edisonwang.ps.lib.PennStation;

@EventListener(producers = {
        SearchByTag.class
})
public class MainActivity extends AppCompatActivity {

    private final MainActivityEventListener mListener = new MainActivityEventListener() {

        @Override
        public void onEventMainThread(SearchByTagEventFailure event) {
            Log.i("EventResult", "Failed.");
        }

        @Override
        public void onEventMainThread(SearchByTagEventSuccess event) {
            if (event.getResponseInfo().mRequestId.equals(mRequestId)) {
                mPostAdapter.replaceWith(event.results);
            }
        }
    };

    private final DelayedWatcher.OnTextChanged mSearchByTager = new DelayedWatcher.OnTextChanged() {
        @Override
        public void onTextChanged(String text) {
            if (text.length() > 0) {
                mRequestId = PennStation.requestAction(PsSearchByTag.helper(text));
            }
        }
    };

    private SearchAdapter mPostAdapter;
    private String mRequestId;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PennStation.init(getApplication(),
                new PennStation.PennStationOptions(EventService.class));
        setContentView(R.layout.activity_main);
        DelayedWatcher.addTo((EditText) findViewById(R.id.editText), mSearchByTager, 100);
        mPostAdapter = new SearchAdapter(getLayoutInflater());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.taggedPosts);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mPostAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        PennStation.registerListener(mListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        PennStation.unRegisterListener(mListener);
    }
}
