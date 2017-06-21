package com.wizardev.shop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.wizardev.shop.application.MyApplication;
import com.wizardev.shop.bean.LoginRespMsg;
import com.wizardev.shop.bean.User;
import com.wizardev.shop.customView.ClearEditText;
import com.wizardev.shop.customView.MyToolbar;
import com.wizardev.shop.http.OkHttpHelper;
import com.wizardev.shop.http.SpotsCallback;
import com.wizardev.shop.utils.DESUtil;
import com.wizardev.shop.utils.ToastUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    @ViewInject(R.id.mytoolbar)
    private MyToolbar mToolBar;
    @ViewInject(R.id.etxt_phone)
    private ClearEditText mEtxtPhone;
    @ViewInject(R.id.etxt_pwd)
    private ClearEditText mEtxtPwd;



    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);

        initToolBar();
    }


    private void initToolBar(){
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginActivity.this.finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Event(R.id.btn_login)
    private void login(View view){


        String phone = mEtxtPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        String pwd = mEtxtPwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.show(this,"请输入密码");
            return;
        }


        Map<String,String> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY,pwd));

        okHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallback<LoginRespMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                Log.i(TAG, "token: "+userLoginRespMsg.getToken()+"data: "+userLoginRespMsg.getData());
                MyApplication application =  MyApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                if(application.getIntent() == null){
                    setResult(RESULT_OK);
                    finish();
                }else{

                    application.jumpToTargetActivity(LoginActivity.this);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }
}
