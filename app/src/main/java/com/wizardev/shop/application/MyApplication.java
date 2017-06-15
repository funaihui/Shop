package com.wizardev.shop.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.xutils.x;


/**
 * Created by wizardev on 17-6-1.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        x.Ext.init(this);
    }
}
