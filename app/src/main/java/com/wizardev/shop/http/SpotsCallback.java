package com.wizardev.shop.http;

import android.content.Context;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xiaohui on 2016/10/30.
 */

public abstract class SpotsCallback<T> extends BaseCallback<T> {
    private Context mContext;
    private SpotsDialog mDialog;

    public SpotsCallback(Context context){
        mContext = context;
        mDialog = new SpotsDialog(mContext,"拼命加载中...");
    }
    public void showDialog(){
        mDialog.show();
    }

    public void dismissDialog(){
        mDialog.dismiss();
    }


    public void setLoadMessage(int resId){
        mDialog.setMessage(mContext.getString(resId));
    }


    @Override
    public void onBeforeCallback(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, Exception e) {
        dismissDialog();
    }


    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }

}
