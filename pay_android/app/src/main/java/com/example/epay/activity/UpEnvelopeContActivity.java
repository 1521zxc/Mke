package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class UpEnvelopeContActivity extends BaseActivity {
    @Bind(R.id.cont_money_all)
    EditText moneyAll;
    @Bind(R.id.cont_money_small)
    EditText moneySmall;
    @Bind(R.id.cont_money_old)
    EditText moneyOld;
    @Bind(R.id.cont_money_lingqu)
    EditText moneyLingqu;
    @Bind(R.id.cont_money_shi)
    EditText moneyShi;
    @Bind(R.id.cont_money_time)
    EditText moneyTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_envelope_cont);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.envelope_cont_up)
    public void up()
    {
        Intent intent=new Intent(this,UpEnvelopeActivity.class);
        intent.putExtra("name",getIntent().getStringExtra("name"));
        intent.putExtra("start",getIntent().getStringExtra("start"));
        intent.putExtra("end",getIntent().getStringExtra("end"));
        intent.putExtra("type",getIntent().getStringExtra("type"));
        intent.putExtra("type1",getIntent().getBooleanExtra("type1",true));
        intent.putExtra("moneyAll",moneyAll.getText().toString());
        intent.putExtra("moneySmall",moneySmall.getText().toString());
        intent.putExtra("moneyOld",moneyOld.getText().toString());
        intent.putExtra("moneyLingqu",moneyLingqu.getText().toString());
        intent.putExtra("moneyShi",moneyShi.getText().toString());
        intent.putExtra("moneyTime",moneyTime.getText().toString());
        startActivity(intent);
    }



    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("设置会员红包"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("设置会员红包"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
