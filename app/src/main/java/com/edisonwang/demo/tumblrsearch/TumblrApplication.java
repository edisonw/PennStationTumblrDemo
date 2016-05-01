package com.edisonwang.demo.tumblrsearch;

import android.app.Application;

import com.edisonwang.ps.lib.EventService;
import com.edisonwang.ps.lib.PennStation;

/**
 * @author edi
 */
public class TumblrApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PennStation.init(this, new PennStation.PennStationOptions(EventService.class));
    }
}
