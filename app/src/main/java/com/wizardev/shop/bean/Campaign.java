package com.wizardev.shop.bean;

/**
 * Created by xiaohui on 2016/10/30.
 */

public class Campaign {
    private long id;
    private String title;
    private String imgUrl;

    public Campaign(long id, String imgUrl, String title) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
