package com.example.epay.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.UserBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class AccountNameActivity extends BaseActivity {
    @Bind(R.id.account_name)
    EditText text;
    private String beforeText = "";
    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_name);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        userBean = CacheData.getMyBeans(this);
        text.setText(userBean.getBrandName());
        beforeText = text.getText().toString();
    }

    @OnClick(R.id.account_up)
    public void up() {
        if (beforeText.equals(text.getText().toString())) {
            showMessage("请修改店铺名称");
        } else {
            userBean.setBrandName(text.getText().toString());
            doUp(gson.toJson(userBean));
        }
    }

    @OnClick(R.id.up_account_pay)
    public void upPay() {
        if (beforeText.equals(text.getText().toString())) {
            showMessage( "请修改店铺名称");
        } else {
            userBean.setBrandName(text.getText().toString());
            doUp(gson.toJson(userBean));
        }
    }

    //更新
    private void doUp(final String data1) {
        httpUtil.HttpServer(this, data1, 42, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                if (userBean != null) {
                    CacheData.cacheMyBeans(AccountNameActivity.this, userBean);
                    startActivity(AccountNameActivity.this, AccoutActivity.class);
                } else {
                    showMessage("没有数据");
                }
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage(Message);
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("店铺名称"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("店铺名称"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}