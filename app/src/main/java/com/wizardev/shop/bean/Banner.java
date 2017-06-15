package com.wizardev.shop.bean;

/**
 * Created by xiaohui on 2016/10/29.
 */

public class Banner extends BaseBean {
    private String name;
    private String imgUrl;
    private String description;

    public Banner(long id) {
        super(id);
    }

    public Banner(long id, String description, String name, String imgUrl) {
        super(id);
        this.description = description;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
