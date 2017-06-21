package com.wizardev.shop;

/**
 * Created by Ivan on 15/10/5.
 */
public class Contants {

    public static final String WARES="wares";
    public static final String ACTIVITY="Activity";
    public static final int HTML=1;
    public static final String USER_JSON="user_json";
    public static final String TOKEN="token";
    public  static final int REQUEST_CODE=0;
    public  static final int REQUEST_CODE_PAYMENT=1;
    public  static final String DES_KEY="Cniao5_123456";
    public static class API{


        public static final String BASE_URL="http://112.124.22.238:8081/course_api/";

        public static final String CAMPAIGN_HOME=BASE_URL + "campaign/recommend";
        public static final String ORDER_CREATE=BASE_URL +"order/create";

        public static final String WARES_HOT=BASE_URL + "wares/hot";
        public static final String WARES_DETAIL=BASE_URL +"wares/detail.html";

        public static final String BANNER=BASE_URL +"banner/query";

        public static final String WARES_LIST=BASE_URL +"wares/list";

        public static final String CATEGORY_LIST=BASE_URL +"category/list";
        public static final String LOGIN=BASE_URL +"auth/login";

    }
}
