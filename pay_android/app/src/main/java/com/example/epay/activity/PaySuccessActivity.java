package com.example.epay.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.ScanQrBean;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class PaySuccessActivity extends BaseActivity {
    @Bind(R.id.sucees_paid)
    TextView paidText;
    @Bind(R.id.sucees_sale)
    TextView saleText;
    @Bind(R.id.success_mq)
    TextView mqText;
    @Bind(R.id.success_pay_type)
    TextView typeText;
    @Bind(R.id.success_pay_info)
    TextView infoText;

    ScanQrBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        ButterKnife.bind(this);

        bean=(ScanQrBean)getIntent().getSerializableExtra("bean");
        paidText.setText("￥"+bean.getPaidMoney());
        saleText.setText("￥"+bean.getSaleMoney());
        mqText.setText(bean.getPayNO());
        if(bean.getPayType()==1) {
            typeText.setText("微信支付顾客");
            infoText.setText("微信消费"+bean.getTotalCount()+"次，共"+bean.getTotalMonry()+"元");
        }else if(bean.getPayType()==2){
            typeText.setText("支付宝支付顾客");
            infoText.setText("支付宝消费"+bean.getTotalCount()+"次，共"+bean.getTotalMonry()+"元");
        }

    }

    @OnClick(R.id.sucees_qr)
    public void qr()
    {
       // onBackPressed();
        startActivity(PaySuccessActivity.this,DeskManageActivity.class);
        finish();
    }


    @Override
    public void onBackPressed() {
        startActivity(PaySuccessActivity.this,DeskManageActivity.class);
        finish();
    }

    @OnClick(R.id.sucees_cash)
    public void cash()
    {
        startActivity(this,CashflowActivity.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("收款成功"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("收款成功"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
