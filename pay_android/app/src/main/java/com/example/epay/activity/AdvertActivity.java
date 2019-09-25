package com.example.epay.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.cache.CacheData;
import com.example.epay.view.CountDownProgressView;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;

public class AdvertActivity extends BaseActivity {
    @Bind(R.id.tv)
    CountDownProgressView dp;
    @Bind(R.id.advert_img)
    ImageView img;
    String urlString="http://file.jqepay.com/AD_Default-568h@2x.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);
        ButterKnife.bind(this);
        initView();
    }
    @Override
    public void initView() {
        super.initView();
        loadLogo(urlString,img);
        dp.start();
        dp.setProgressListener(progress -> {
            if(progress==0) {
                dp.stop();
                if(CacheData.getLoginstate(AdvertActivity.this)) {
                    startActivity(AdvertActivity.this, MainActivity.class);
                    finish();
                }else{
                    startActivity(AdvertActivity.this, LoginActivity.class);
                    finish();
                }
            }
        });

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CacheData.getLoginstate(AdvertActivity.this)) {
                    startActivity(AdvertActivity.this, MainActivity.class);
                    finish();
                }else{
                    startActivity(AdvertActivity.this, LoginActivity.class);
                    finish();
                }
            }
        });
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("广告页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("广告页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
