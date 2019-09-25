package com.example.epay.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class MemberStoredActivity extends BaseActivity {
    @Bind(R.id.stored_allmoney)
    TextView allMoney;
    @Bind(R.id.stored_symoney)
    TextView syMoney;
    @Bind(R.id.stored_layout)
    LinearLayout layout;
    @Bind(R.id.stored_p)
    TextView ple;
    @Bind(R.id.stored_money)
    TextView Money;
    @Bind(R.id.stored_allsum)
    TextView allSum;
    @Bind(R.id.stored_amoney)
    TextView avMoney;
    @Bind(R.id.stored_allmoney1)
    TextView allMoney1;
    @Bind(R.id.stored_img1)
    ImageView img1;
    @Bind(R.id.stored_img2)
    ImageView img2;
    @Bind(R.id.stored_img3)
    ImageView img3;
    @Bind(R.id.stored_img4)
    ImageView img4;
    @Bind(R.id.stored_img5)
    ImageView img5;

    String money1="1",money2="1.00",r="0",Sum="2",money3="0",money4="0.22",money5="0.54",url1="https://timgsa.baidu.com/timg?image&amp;quality=80&amp;size=b9999_10000&amp;sec=1524898764474&amp;di=8a6a70e2672698c1befe6cdc037d64ba&amp;imgtype=0&amp;src=http%3A%2F%2Fdl.bizhi.sogou.com%2Fimages%2F2013%2F07%2F24%2F350788.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_stored);
        ButterKnife.bind(this);
        initView();
    }


    @Override
    public void initView() {
        super.initView();
        String a="累计储值<br/><big><big>"+money1+"</big></big>元";
        allMoney.setText(Html.fromHtml(a));
        String a1="未消费金额<br/><big><big>"+money2+"</big></big>元";
        syMoney.setText(Html.fromHtml(a1));
        String a3="<font color='#fd5c5c'>"+r+"人</font>>";
        ple.setText(Html.fromHtml(a3));
        String a4="<font color='#fd5c5c'>"+money3+"元</font>>";
        Money.setText(Html.fromHtml(a4));
        allSum.setText(Sum+"笔");
        avMoney.setText(money4+"元");
        allMoney1.setText(money5+"元");
        loadCircle(url1,img1,0);
        loadCircle(url1,img2,0);
        loadCircle(url1,img3,0);
        loadCircle(url1,img4,0);
        loadCircle(url1,img5,0);
    }


    @OnClick(R.id.stored_tv)
    public void zhangdan(){
        startActivity(this,BillStoredActivity.class);
    }

    @OnClick(R.id.stored_layout)
    public void all(){
        Intent intent=new Intent(this,StoredMemberActivity.class);
        intent.putExtra("type","all");
        startActivity(intent);
    }
    @OnClick(R.id.stored_p)
    public void activityAll(){
        Intent intent=new Intent(this,StoredMemberActivity.class);
        intent.putExtra("type","activity");
        startActivity(intent);
    }
    @OnClick(R.id.stored_money)
    public void money(){
        startActivity(this,StoredMoneyActivity.class);
    }
    @OnClick(R.id.stored_activity)
    public void activity()
    {
        startActivity(this,StoredActivityActivity.class);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("会员储值"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("会员储值"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
