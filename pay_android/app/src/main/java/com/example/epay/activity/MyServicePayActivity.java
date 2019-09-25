package com.example.epay.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;

public class MyServicePayActivity extends BaseActivity {
    @Bind(R.id.service_ListView)
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service_pay);
        ButterKnife.bind(this);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("购买记录"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("购买记录"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
