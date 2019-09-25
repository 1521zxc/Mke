package com.example.epay.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;

public class UpEnvelopeActivity extends BaseActivity {
    @Bind(R.id.envelope_gz1)
    TextView gz1;
    @Bind(R.id.envelope_gz3)
    TextView gz3;
    @Bind(R.id.envelope_gz4)
    TextView gz4;
    @Bind(R.id.up_envelope_name)
    TextView name;
    @Bind(R.id.up_envelope_money_all)
    TextView moneyAll;
    @Bind(R.id.up_envelope_money_old)
    TextView moneyOld;
    @Bind(R.id.up_envelope_money_small)
    TextView moneySmall;
    @Bind(R.id.up_envelope_sum)
    TextView sum;
    @Bind(R.id.up_envelope_start)
    TextView startTime;
    @Bind(R.id.up_envelope_end)
    TextView endTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_envelope);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        name.setText(getIntent().getStringExtra("name"));
        moneyAll.setText(getIntent().getStringExtra("moneyAll"));
        moneyOld.setText(getIntent().getStringExtra("moneyOld"));
        moneySmall.setText(getIntent().getStringExtra("moneySmall"));
        sum.setText("222");
        startTime.setText(getIntent().getStringExtra("start"));
        endTime.setText(getIntent().getStringExtra("end"));
        gz1.setText(getString(R.string.envelope_gz1,getIntent().getStringExtra("moneyLingqu")));
        gz3.setText(getString(R.string.envelope_gz3,getIntent().getStringExtra("moneyTime")));
        gz4.setText(getString(R.string.envelope_gz4,getIntent().getStringExtra("moneyShi")));
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("会员红包预览"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("会员红包预览"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
