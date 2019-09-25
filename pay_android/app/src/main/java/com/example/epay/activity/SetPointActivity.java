package com.example.epay.activity;

import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class SetPointActivity extends BaseActivity {
    @Bind(R.id.member_name)
    TextView name;
    @Bind(R.id.set_point_statue1)
    TextView statue1;
    @Bind(R.id.set_point_statue2)
    TextView statue2;
    @Bind(R.id.set_point_statue)
    TextView statue;
    @Bind(R.id.member_total)
    TextView total;
    @Bind(R.id.member_total_sum)
    TextView suma;
    @Bind(R.id.member_total_on)
    TextView ona;
    int rd=0;

    String startTime="2018.02.14",endTime="2018.03.14",sum="5",dotSum="3",cSUm="5",date="2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_point);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        statue1.setText(getString(R.string.point_activity_text,startTime,endTime));
        String a1="会员集点活动将于<font color='#F09D3E'>"+date+"</font>天后过期，点击修改活动演唱活动日期";
        statue2.setText(Html.fromHtml(a1));
        String a2="共有<font color='#fd5c5c'>"+sum+"</font>个会员参与";
        total.setText(Html.fromHtml(a2));
        String a3="已积攒<br/><font color='#fd5c5c'><big>"+dotSum+"点</big></font>";
        suma.setText(Html.fromHtml(a3));
        String a4="已兑换<br/><font color='#fd5c5c'><big>"+cSUm+"次</big></font>";
        ona.setText(Html.fromHtml(a4));
        Drawable drawable=getResources().getDrawable(R.drawable.member_icon);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable.setBounds(0,0,(int)(0.035*width),(int)(0.035*width));
        //drawable   第一个是文字TOP
        name.setCompoundDrawables(drawable,null,null,null);
        rd=Math.random()>0.5?1:0;
        if(rd==1)
        {
            hideView(R.id.set_point_statue1);
            showView(R.id.set_point_statue2);
            hideView(R.id.cash_member);
            statue.setText("礼品兑换");
        }else {
            hideView(R.id.set_point_statue2);
            showView(R.id.set_point_statue1);
            showView(R.id.cash_member);
            statue.setText("创建新会员集点活动");
        }
    }

    @OnClick(R.id.set_point_statue)
    public void statue()
    {
        if(rd==1) {
            startActivity(this,ExchangeActivity.class);
        }else {
            startActivity(this,SetPointsActivity.class);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("会员集点"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("会员集点"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}

