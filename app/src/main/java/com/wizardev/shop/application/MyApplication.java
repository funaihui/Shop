package com.wizardev.shop.application;

import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mob.MobApplication;
import com.wizardev.shop.bean.User;
import com.wizardev.shop.utils.UserLocalData;

import org.litepal.LitePal;
import org.xutils.x;


/**
 * Created by wizardev on 17-6-1.
 */

public class MyApplication extends MobApplication {
    private User user;
    private static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        LitePal.initialize(this);
        mInstance = this;
        x.Ext.init(this);
    }
    public static MyApplication getInstance(){
        return  mInstance;
    }


    private void initUser(){

        this.user = UserLocalData.getUser(this);
    }


    public User getUser(){

        return user;
    }


    public void putUser(User user,String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user =null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);


    }


    public String getToken(){

        return  UserLocalData.getToken(this);
    }



    private Intent intent;
    public void putIntent(Intent intent){
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void jumpToTargetActivity(Context context){

        context.startActivity(intent);
        this.intent =null;
    }
}
