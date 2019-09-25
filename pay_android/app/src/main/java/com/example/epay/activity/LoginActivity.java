package com.example.epay.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.BaseApplication;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.HomeListBean;
import com.example.epay.bean.User;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @Bind(R.id.account)
    EditText edit_account;
    @Bind(R.id.password)
    EditText edit_password;
    @Bind(R.id.login_layout)
    LinearLayout layout;

    private String datas;
    User user;
    HomeListBean homeBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
    }
    @OnClick(R.id.login_layout)
    public void Layout()
    {
        //关闭输入框
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
    @OnClick(R.id.login)
    public void login(View view)
    {
        String account = edit_account.getText().toString().trim();
        String password = edit_password.getText().toString().trim();
        if ("".equals(account)) {
            showMessage("请输入账号");
            return;
        }
        if ("".equals(password)) {
            showMessage("请输入密码");
            return;
        }
        JSONObject data = new JSONObject();
        data.put("mobile",account);
        data.put("password",password);
        data.put("app","10");
        data.put("devicetoken",CacheData.getClientId(this));
        data.put("deviceinfo",android.os.Build.VERSION.RELEASE+","+android.os.Build.VERSION.RELEASE);
        datas = data.toString();
        doLogin(datas);
    }
    @OnClick(R.id.find_pass)
    public void findpass(View v) {
        startActivity(LoginActivity.this,FindPasswordActivity.class);
    }

    //登录
    private void doLogin(final String data) {
        httpUtil.HttpServer(this, data, 31, true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                user = gson.fromJson(data1, User.class);
                if(user!=null){
                    CacheData.setId(LoginActivity.this,user.getUserID());
                    CacheData.cacheUser(LoginActivity.this,user,user.getUserID());
                    startActivity(LoginActivity.this, MainActivity.class);
                    finish();
                }else{
                    showMessage("登录失败");
                }
            }
            @Override
            public void fail(String Message, int code,String data) {
                showMessage(Message);
            }
        });
    }

    @Override
    protected void onResume() {
        if(BaseApplication.getInstance().getUserId() > 0){
            LoginActivity.this.finish();

        }
        MobclickAgent.onPageStart("登录"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
        super.onResume();
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("登录"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}

