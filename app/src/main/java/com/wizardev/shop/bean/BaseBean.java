package com.wizardev.shop.bean;

import java.io.Serializable;

/**
 * Created by xiaohui on 2016/10/29.
 */

public class BaseBean implements Serializable {
    private long id;

    public BaseBean(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
