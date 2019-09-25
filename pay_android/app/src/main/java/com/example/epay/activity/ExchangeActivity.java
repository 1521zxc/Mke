package com.example.epay.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class ExchangeActivity extends BaseActivity {
    @Bind(R.id.exchange_qr)
    TextView qr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        Drawable drawable=getResources().getDrawable(R.drawable.ic_gathering);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable.setBounds(0,0,(int)(0.07*width),(int)(0.07*width));
        //drawable   第一个是文字TOP
        qr.setCompoundDrawables(drawable,null,null,null);
    }
    @OnClick(R.id.exchange_qr)
    public void qr()
    {
        Intent intent=new Intent(this,MemberCodeActivity.class);
        intent.putExtra("title",1);
        startActivity(intent);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("集点兑换"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("集点兑换"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
