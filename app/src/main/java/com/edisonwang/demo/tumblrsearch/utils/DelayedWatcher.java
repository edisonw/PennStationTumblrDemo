package com.edisonwang.demo.tumblrsearch.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author edi
 */
public class DelayedWatcher implements TextWatcher {

    private final Handler mHandler;
    private final long mDelay;
    private final OnTextChanged mOnTextChanged;
    private final Runnable mTextUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            mOnTextChanged.onTextChanged(mLastUpdate);
        }
    };
    private String mLastUpdate;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mLastUpdate = s.toString();
        mHandler.removeCallbacks(mTextUpdateRunnable);
        mHandler.postDelayed(mTextUpdateRunnable, mDelay);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface OnTextChanged {
        void onTextChanged(String text);
    }

    public static void addTo(EditText et, OnTextChanged onTextChanged, final long delay) {
        et.addTextChangedListener(new DelayedWatcher(onTextChanged, delay));
    }

    public DelayedWatcher(OnTextChanged onTextChanged, final long delay) {
        mHandler = new Handler(Looper.getMainLooper());
        mOnTextChanged = onTextChanged;
        mDelay = delay;
    }
}
