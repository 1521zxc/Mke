package com.example.epay.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.EnvelopesBean;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;

public class EnvelopeDataActivity extends BaseActivity {
    @Bind(R.id.envelope_gz01)
    TextView gz1;
    @Bind(R.id.envelope_gz03)
    TextView gz3;
    @Bind(R.id.envelope_gz04)
    TextView gz4;
    @Bind(R.id.data_sy_time)
    TextView syTime;
    @Bind(R.id.data_share)
    TextView share;
    @Bind(R.id.data_baog)
    TextView baog;
    @Bind(R.id.data_new_member)
    TextView newMember;
    @Bind(R.id.data_h)
    TextView h;
    @Bind(R.id.data_sy_money)
    TextView syMoney;
    @Bind(R.id.enve_check)
    CheckBox checkBox;
    @Bind(R.id.data_lll)
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envelope_data);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        Drawable drawable = getResources().getDrawable(R.drawable.enve_btn);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable.setBounds(0, 0, (int) (0.05 * width), (int) (0.035 * width));
        //drawable   第一个是文字TOP
        checkBox.setCompoundDrawables(null, null, drawable, null);
        EnvelopesBean envelopesBean = (EnvelopesBean) getIntent().getSerializableExtra("enve");
        String a1 = "活动已经行<font color='#ffb83c'>" + "1天" + "</font>,据结束7天";
        syTime.setText(Html.fromHtml(a1));
        String a2 = "<font color='#fd5c5c'><big>" + "0次" + "</big></font><br/>分享次数";
        share.setText(Html.fromHtml(a2));
        String a3 = "<font color='#fd5c5c'><big>" + "0次" + "</big></font><br/>曝光次数";
        baog.setText(Html.fromHtml(a3));
        String a4 = "<font color='#fd5c5c'><big>" + "0次" + "</big></font><br/>新会员数";
        newMember.setText(Html.fromHtml(a4));
        String a5 = "<font color='#fd5c5c'><big>" + "0次" + "</big></font><br/>刺激消费";
        h.setText(Html.fromHtml(a5));
        String a6 = "剩余预算：<font color='#fd5c5c'>400元</font>";
        syMoney.setText(Html.fromHtml(a6));

        gz1.setText(getString(R.string.envelope_gz1, envelopesBean.getLingquSum()));
        gz3.setText(getString(R.string.envelope_gz3, envelopesBean.getMoneyTime()));
        gz4.setText(getString(R.string.envelope_gz4, envelopesBean.getShiSum()));


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    layout.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("会员红包详情"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("会员红包详情"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}
