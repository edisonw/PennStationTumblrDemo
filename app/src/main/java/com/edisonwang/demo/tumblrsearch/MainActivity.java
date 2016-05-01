package com.edisonwang.demo.tumblrsearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import com.edisonwang.demo.tumblrsearch.service.SearchByTag;
import com.edisonwang.demo.tumblrsearch.service.SearchByTag_.PsSearchByTag;
import com.edisonwang.demo.tumblrsearch.service.events.SearchByTagFailure;
import com.edisonwang.demo.tumblrsearch.service.events.SearchByTagSuccess;
import com.edisonwang.ps.annotations.EventListener;
import com.edisonwang.ps.lib.PennStation;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

@EventListener(producers = {
        SearchByTag.class
})
public class MainActivity extends AppCompatActivity {

    private final MainActivityEventListener mListener = new MainActivityEventListenerImpl();

    private SearchAdapter mPostAdapter;
    private Subscription mSub;

    @BindView(R.id.editText) EditText mSearchBox;
    @BindView(R.id.taggedPosts) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPostAdapter = new SearchAdapter(getLayoutInflater());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mPostAdapter);
    }

    /**
     * With proper Java 8 Support & Lambda Support

     mSub = RxTextView.afterTextChangeEvents(mSearchBox)
                      .filter(event -> event.editable().length() > 0)
                      .throttleLast(200, TimeUnit.MILLISECONDS)
                      .flatMap(event -> SearchByTagSuccess.Rx.from(PsSearchByTag.helper(event.editable().toString())))
                      .subscribe(event -> { mPostAdapter.replaceWith(event.results);}
     );

     */
    @Override
    public void onResume() {
        super.onResume();
        PennStation.registerListener(mListener);
        mSub = RxTextView.afterTextChangeEvents(mSearchBox)
                .filter(new Func1<TextViewAfterTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(TextViewAfterTextChangeEvent event) {
                        return event.editable().length() > 0;
                    }})
                .throttleLast(200, TimeUnit.MILLISECONDS)
                .flatMap(new Func1<TextViewAfterTextChangeEvent, Observable<SearchByTagSuccess>>() {
                    @Override
                    public Observable<SearchByTagSuccess> call(TextViewAfterTextChangeEvent event) {
                        return SearchByTagSuccess.Rx.from(PsSearchByTag.helper(event.editable().toString()));
                    }})
                .subscribe(new Action1<SearchByTagSuccess>() {
                    @Override
                    public void call(SearchByTagSuccess event) {
                        mPostAdapter.replaceWith(event.results);
                    }
                });

    }

    @Override
    public void onPause() {
        super.onPause();
        PennStation.unRegisterListener(mListener);
        mSub.unsubscribe();
    }

    private class MainActivityEventListenerImpl implements MainActivityEventListener {

        @Override
        public void onEventMainThread(SearchByTagFailure event) {
            Log.i("EventResult", "Failed.");
        }

        @Override
        public void onEventMainThread(SearchByTagSuccess event) {
            Log.i("EventResult", "Success was also caught here, we can update other parts.");
        }
    }
}
