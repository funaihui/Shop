package com.wizardev.shop.bean;

/**
 * Created by xiaohui on 2016/10/29.
 */

public class HomeCategoryBean {
    private String textTitle;
    private int imageBig;
    private int imageSmallTop;
    private int imageSmallBottom;

    public HomeCategoryBean(int imageBig, int imageSmallBottom, int imageSmallTop, String textTitle) {
        this.imageBig = imageBig;
        this.imageSmallBottom = imageSmallBottom;
        this.imageSmallTop = imageSmallTop;
        this.textTitle = textTitle;
    }

    public int getImageBig() {
        return imageBig;
    }

    public void setImageBig(int imageBig) {
        this.imageBig = imageBig;
    }

    public int getImageSmallBottom() {
        return imageSmallBottom;
    }

    public void setImageSmallBottom(int imageSmallBottom) {
        this.imageSmallBottom = imageSmallBottom;
    }

    public int getImageSmallTop() {
        return imageSmallTop;
    }

    public void setImageSmallTop(int imageSmallTop) {
        this.imageSmallTop = imageSmallTop;
    }

    public String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
    }
}
