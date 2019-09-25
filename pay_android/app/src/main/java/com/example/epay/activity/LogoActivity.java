package com.example.epay.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.epay.R;
import com.example.epay.RxUtil.RxPermissions;
import com.example.epay.base.BaseActivity;
import com.example.epay.service.MyPushIntentService;
import com.umeng.analytics.MobclickAgent;


import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import rx.Subscriber;

public class LogoActivity extends BaseActivity {
    //    Manifest.permission.READ_EXTERNAL_STORAGE,
//    Manifest.permission.WRITE_EXTERNAL_STORAGE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        ButterKnife.bind(this);
        startService(new Intent(this, MyPushIntentService.class));
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        MobclickAgent.openActivityDurationTrack(false);
        /** 设置是否对日志信息进行加密, 默认false(不加密). */
        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后
//        Timer timer = new Timer();
//        TimerTask timertask = new TimerTask() {
//            @Override
//            public void run() {
////                if(){
////
////                }else {
//                    startActivity(LogoActivity.this, AdvertActivity.class);
//                    finish();
// //               }
//            }
//        };
//        timer.schedule(timertask, 3000);
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Timer timer = new Timer();
                            TimerTask timertask = new TimerTask() {
                                @Override
                                public void run() {
//                if(){
//
//                }else {
                                    startActivity(LogoActivity.this, AdvertActivity.class);
                                    finish();
                                    //               }
                                }
                            };
                            timer.schedule(timertask, 3000);
                        } else {
                            showMessage("请设置权限，才可启动软件");
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("程序入口"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("程序入口"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
