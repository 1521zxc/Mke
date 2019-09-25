package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;

public class SetPointrActivity extends BaseActivity {
    @Bind(R.id.point_price)
    TextView priceText;
    @Bind(R.id.point_text)
    TextView textText;
    @Bind(R.id.point_time)
    TextView timeText;

     int Nomber=0;
     String money="",name="",price="",startTime="",endTime="";
      boolean s=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pointr);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent=getIntent();
        Nomber=intent.getIntExtra("Nomber",0);
        money=intent.getStringExtra("money");
        name=intent.getStringExtra("name");
        price=intent.getStringExtra("price");
        startTime=intent.getStringExtra("startTime");
        endTime=intent.getStringExtra("endTime");
        s=intent.getBooleanExtra("is",false);
        priceText.setText(getString(R.string.pointr_price,money));
        textText.setText(getString(R.string.pointr_text,Nomber+"",name));
        timeText.setText(startTime+"-"+endTime);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("集点活动预览"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("集点活动预览"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
