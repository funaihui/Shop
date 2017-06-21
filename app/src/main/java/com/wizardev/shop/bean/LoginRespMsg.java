package com.wizardev.shop.bean;

/**
 * Created by wizardev on 17-6-21.
 */

public class LoginRespMsg<T> extends BaseRespMsg {


    private String token;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}