package com.example.epay.activity;

import android.os.Bundle;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

public class WoMenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wo_men);
        ButterKnife.bind(this);
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("关于我们"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关于我们"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
