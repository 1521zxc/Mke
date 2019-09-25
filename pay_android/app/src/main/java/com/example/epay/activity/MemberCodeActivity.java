package com.example.epay.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.google.zxing.Result;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;

public class MemberCodeActivity extends BaseActivity{
    @Bind(R.id.rootView1)
    RelativeLayout rootView;
    @Bind(R.id.surfaceView1)
    SurfaceView surfaceView;
    @Bind(R.id.capture_scan_line1)
    ImageView scanLine;
//    @Bind(R.id.scan_image1)
//    MyImageView rlCropView;
    @Bind(R.id.qr_code_title)
    TextView title;
    @Bind(R.id.top_mask)
    TextView topTitle;

   // ScanManager scanManager;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_code);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        int titles=getIntent().getIntExtra("title",0);
        if(titles==0) {
            title.setText("扫描识别会员");
            topTitle.setText("请扫描会员二维码识别会员\\n（会员可通过公众号-会员中心查看）");
        }else if(titles==1)
        {
            title.setText("会员集点兑换");
            topTitle.setText("将兑换二维码放入框内，即可自动扫描");
        }
        doScan();
    }

    public void doScan()
    {
//        scanManager = new ScanManager(this, surfaceView, rootView, rlCropView, scanLine, DecodeThread.QRCODE_MODE, new ScanListener() {
//            @Override
//            public void scanResult(Result rawResult, Bundle bundle) {
//                showMessage(rawResult.getText());
//            }
//            @Override
//            public void scanError(Exception e) {
//                showMessage(e.getMessage());
//            }
//        });

    }

    public void onResume() {
        super.onResume();
       // scanManager.onResume();
        MobclickAgent.onPageStart("扫描会员"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
       // scanManager.onPause();
        MobclickAgent.onPageEnd("扫描会员"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
