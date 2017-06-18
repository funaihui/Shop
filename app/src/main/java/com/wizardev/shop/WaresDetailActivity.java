package com.wizardev.shop;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wizardev.shop.bean.Wares;
import com.wizardev.shop.customView.MyToolbar;
import com.wizardev.shop.dao.CartDao;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;

import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;

public class WaresDetailActivity extends AppCompatActivity {
    @ViewInject(R.id.mytoolbar)
    private MyToolbar myToolbar;
    @ViewInject(R.id.web_detail)
    private WebView mWebDetail;
    private Wares mWare;
    private AppInterface mAppInterface;
    private SpotsDialog mDialog;
    private CartDao cartDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wares_detail);
        x.view().inject(this);
        cartDao = CartDao.getInstance();
        Serializable serializable = getIntent().getSerializableExtra(Contants.WARES);
        mWare = (Wares) serializable;
        if (mWare == null) {
            this.finish();
        }

        mDialog = new SpotsDialog(this, "loading....");
        mDialog.show();

        initToolbar();
        initWebView();
        share();
    }

    private void share() {
        myToolbar.getRightButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
            }
        });
    }


    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://www.wizardev.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mWare.getName());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(mWare.getImgUrl());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(mWare.getName());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.wizardev.com");

        // 启动分享GUI
        oks.show(this);
    }
    private void initWebView() {
        WebSettings settings = mWebDetail.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setBlockNetworkImage(false);

        mWebDetail.loadUrl(Contants.API.WARES_DETAIL);
        mAppInterface = new AppInterface(this);
        mWebDetail.addJavascriptInterface(mAppInterface, "appInterface");
        mWebDetail.setWebViewClient(new MyWebClient());
    }

    private void initToolbar() {
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Statebar.setStatuebarColor(this,0xeb4f38);
        myToolbar.setTitle(getString(R.string.detail));
        myToolbar.setRightButtonText(getString(R.string.share));

    }

    //设置返回键的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    class AppInterface {
        private Context context;
        public AppInterface(Context context){
            this.context = context;
        }

        @JavascriptInterface
        public void showDetail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebDetail.loadUrl("javascript:showDetail(" + mWare.getId() + ")");
                }
            });
        }

        @JavascriptInterface
        public void buy() {

        }

        @JavascriptInterface
        public void addToCart(long id) {
            Toast.makeText(context,"添加购物车成功",Toast.LENGTH_SHORT).show();
            cartDao.add2Cart(mWare);
        }


    }

    class MyWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mAppInterface.showDetail();
        }
    }
}
