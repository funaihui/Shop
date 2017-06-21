package com.wizardev.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pingplusplus.android.Pingpp;
import com.wizardev.shop.adapter.WareOrderAdapter;
import com.wizardev.shop.adapter.layoutmanager.FullyLinearLayoutManager;
import com.wizardev.shop.bean.Wares;
import com.wizardev.shop.dao.CartDao;

import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.recycler_view)
    private RecyclerView productRecycle;
    @ViewInject(R.id.rl_alipay)
    private RelativeLayout rAlipay;
    @ViewInject(R.id.rl_bd)
    private RelativeLayout rBaidu;
    @ViewInject(R.id.rl_wechat)
    private RelativeLayout rWechat;
    private static final String TAG = "OrderDetailActivity";
    @ViewInject(R.id.rb_alipay)
    private RadioButton rb_alipay;
    @ViewInject(R.id.rb_webchat)
    private RadioButton rb_webchat;
    @ViewInject(R.id.rb_bd)
    private RadioButton rb_bd;

    @ViewInject(R.id.btn_createOrder)
    private Button mCreateOrder;

    @ViewInject(R.id.txt_total)
    private TextView totalPrice;
    @ViewInject(R.id.simple_pic)
    private SimpleDraweeView mProductPic;
    private CartDao cartDao;
    private float amount;

    /**
     * 开发者需要填一个服务端URL 该URL是用来请求支付需要的charge。务必确保，URL能返回json格式的charge对象。
     * 服务端生成charge 的方式可以参考ping++官方文档，地址 https://pingxx.com/guidance/server/import
     * <p>
     * 【 http://218.244.151.190/demo/charge 】是 ping++ 为了方便开发者体验 sdk 而提供的一个临时 url 。
     * 该 url 仅能调用【模拟支付控件】，开发者需要改为自己服务端的 url 。
     */
    private static String YOUR_URL = "http://218.244.151.190/demo/charge";
    public static final String CHARGE_URL = YOUR_URL;
    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_QPAY = "qpay";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";
    private String payChannel = CHANNEL_ALIPAY;
    private HashMap<String, RadioButton> channels = new HashMap<>();
    private WareOrderAdapter wareOrderAdapter;
    private Wares mWare;
    private int mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Serializable serializableExtra = getIntent().getSerializableExtra(Contants.WARES);
        mCategory = getIntent().getIntExtra(Contants.ACTIVITY, 0);

        mWare = (Wares) serializableExtra;
        x.view().inject(this);
        showData();
        init();
    }

    private void showData() {
        cartDao = CartDao.getInstance();
        if (mCategory==Contants.HTML){
            mProductPic.setVisibility(View.VISIBLE);
            mProductPic.setImageURI(mWare.getImgUrl());

            amount = mWare.getPrice();
        }else {
            Log.i(TAG, "size: "+cartDao.selectAllShop().size());
            mProductPic.setVisibility(View.GONE);
            wareOrderAdapter = new WareOrderAdapter(this, cartDao.selectAllShop(), R.layout.ware_item);
            FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
            layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
            productRecycle.setLayoutManager(layoutManager);

            productRecycle.setAdapter(wareOrderAdapter);
            amount = wareOrderAdapter.totalPrice();

        }
        totalPrice.setText("应付款： ￥" + amount);

    }

    private void init() {
        channels.put(CHANNEL_ALIPAY, rb_alipay);
        channels.put(CHANNEL_WECHAT, rb_webchat);
        channels.put(CHANNEL_BFB, rb_bd);
        rAlipay.setOnClickListener(this);
        rWechat.setOnClickListener(this);
        rBaidu.setOnClickListener(this);
        rb_alipay.setOnClickListener(this);
        rb_webchat.setOnClickListener(this);
        rb_bd.setOnClickListener(this);
        Statebar.setStatuebarColor(this, 0xeb4f38);
        mCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNewOrder();
            }
        });
    }

    @Override
    public void onClick(View view) {
        String tag = view.getTag().toString();
        payChannel = tag;
        for (Map.Entry<String, RadioButton> entry : channels.entrySet()) {
            RadioButton radioButton = entry.getValue();
            if (payChannel.equals(entry.getKey())) {
                boolean checked = radioButton.isChecked();
                radioButton.setChecked(!checked);
            } else {
                radioButton.setChecked(false);

            }
        }
    }

    /*@Event(R.id.btn_createOrder)
    public void createOrder(View v){
       // Pingpp.createPayment(OrderDetailActivity.this,"1");
        Toast.makeText(this,"提交订单",Toast.LENGTH_SHORT).show();
        postNewOrder();
    }*/

    private void postNewOrder() {
        //  String amountText = totalPrice.getText().toString().substring(4);
        String amountText = "0.1";
        if (amountText.equals("")) return;
        String replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA).getCurrency().getSymbol(Locale.CHINA));

        String cleanString = amountText.toString().replaceAll(replaceable, "");

        float amount = Float.valueOf(new BigDecimal(cleanString).toString());
        new PaymentTask().execute(new PaymentRequest(payChannel, amount));


    }

    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

        @Override
        protected void onPreExecute() {
            //按键点击之后的禁用，防止重复点击
            mCreateOrder.setEnabled(false);
        }

        @Override
        protected String doInBackground(PaymentRequest... pr) {

            PaymentRequest paymentRequest = pr[0];
            String data = null;
            try {
                JSONObject object = new JSONObject();
                object.put("channel", paymentRequest.channel);
                object.put("amount", paymentRequest.amount);
                String json = object.toString();
                //向Your Ping++ Server SDK请求数据
                data = postJson(CHARGE_URL, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * 获取charge
         *
         * @param urlStr charge_url
         * @param json   获取charge的传参
         * @return charge
         * @throws IOException
         */
        private String postJson(String urlStr, String json) throws IOException {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.getOutputStream().write(json.getBytes());

            if (conn.getResponseCode() == 200) {
                BufferedReader
                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
            return null;
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if (null == data) {
                showMsg("请求出错", "请检查URL", "URL无法获取charge");
                return;
            }
            Log.d("charge", data);

            //除QQ钱包外，其他渠道调起支付方式：
            //参数一：Activity  当前调起支付的Activity
            //参数二：data  获取到的charge或order的JSON字符串
            Pingpp.createPayment(OrderDetailActivity.this, data);

            //QQ钱包调用方式
            //参数一：Activity  当前调起支付的Activity
            //参数二：data  获取到的charge或order的JSON字符串
            //参数三：“qwalletXXXXXXX”需与AndroidManifest.xml中的scheme值一致
            //Pingpp.createPayment(ClientSDKActivity.this, data, "qwalletXXXXXXX");
        }

    }

    /*private void openPaymentActivity(String s) {
        Pingpp.createPayment(OrderDetailActivity.this, s);
    }*/

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null != msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mCreateOrder.setEnabled(true);

        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                showMsg(result, errorMsg, extraMsg);
            }
        }
    }


    class PaymentRequest {
        String channel;
        float amount;

        public PaymentRequest(String channel, float amount) {
            this.channel = channel;
            this.amount = amount;
        }
    }

}
