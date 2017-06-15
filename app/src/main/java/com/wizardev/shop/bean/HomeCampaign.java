package com.wizardev.shop.bean;

/**
 * Created by xiaohui on 2016/10/30.
 */

public class HomeCampaign {
    private long id;
    private String title;
    private Campaign cpOne;
    private Campaign cpTwo;
    private Campaign cpThree;

    public HomeCampaign(Campaign cpOne, Campaign cpThree, Campaign cpTwo, long id, String title) {
        this.cpOne = cpOne;
        this.cpThree = cpThree;
        this.cpTwo = cpTwo;
        this.id = id;
        this.title = title;
    }

    public Campaign getCpOne() {
        return cpOne;
    }

    public void setCpOne(Campaign cpOne) {
        this.cpOne = cpOne;
    }

    public Campaign getCpThree() {
        return cpThree;
    }

    public void setCpThree(Campaign cpThree) {
        this.cpThree = cpThree;
    }

    public Campaign getCpTwo() {
        return cpTwo;
    }

    public void setCpTwo(Campaign cpTwo) {
        this.cpTwo = cpTwo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
