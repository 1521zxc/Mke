package com.example.epay.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.CashDetailBean;
import com.example.epay.bean.MemberLBean;
import com.example.epay.bean.MemberMListBean;
import com.example.epay.doHttp.CuncResponse;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.doHttp.ReturnResquest;
import com.example.epay.doHttp.Server;
import com.example.epay.util.DateUtil;
import com.example.epay.util.TypeUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;

public class MemberDetailActivity extends BaseActivity {
    @Bind(R.id.member_detail_icon)
    ImageView icon;
    @Bind(R.id.member_detail_birthDate)
    TextView birth;
    @Bind(R.id.member_detail_mobile)
    TextView mobile;
    @Bind(R.id.member_detail_name2)
    TextView name2;
    @Bind(R.id.member_detail_time)
    TextView time;
    @Bind(R.id.member_detail_talsum)
    TextView talSum;
    @Bind(R.id.member_detail_amoney)
    TextView amoney;
    @Bind(R.id.member_detail_talmoney)
    TextView talMoney;
    @Bind(R.id.name1)
    TextView name;
    @Bind(R.id.sex)
    ImageView sex;

    private String datas;
    MemberLBean memberLBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        Drawable drawable4=getResources().getDrawable(R.drawable.icon_broth);
        drawable4.setBounds(0,0,(int)(0.05*width),(int)(0.05*width));
        birth.setCompoundDrawables(drawable4,null,null,null);
        Drawable drawable5=getResources().getDrawable(R.drawable.icon_mobile);
        drawable5.setBounds(0,0,(int)(0.05*width),(int)(0.05*width));
        mobile.setCompoundDrawables(drawable5,null,null,null);
        String id=getIntent().getStringExtra("members");
        Log.i(TAG, "initView: "+id);
        JSONObject data = new JSONObject();
        data.put("ID",id);
        datas = data.toString();
        doDetail(datas);

    }
    //登录
    private void doDetail(final String data1) {
        httpUtil.HttpServer(this, data1, 44, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                memberLBean=gson.fromJson(data, MemberLBean.class);
                if(memberLBean!=null){
                    loadCircle(memberLBean.getIconURL(),icon,0);
                    // birth.setText(DateUtil.format2(memberLBean.getBirthDate(),"yyyy-MM-dd HH:mm:ss"));
                    name.setText(memberLBean.getNickName());
                    //name2.setText(memberLBean.getRealName());
                    // mobile.setText(memberLBean.getMobile());
                    time.setText(DateUtil.format2(memberLBean.getLatestBillTime(),"yyyy-MM-dd HH:mm:ss"));
                    talSum.setText(memberLBean.getTotalNum()+"笔");
                    talMoney.setText(memberLBean.getTotalSum()+"元");
                    if(memberLBean.getTotalNum()>0) {
                        amoney.setText((memberLBean.getTotalSum() / memberLBean.getTotalNum()) + "元");
                    }else{
                        amoney.setText("0元");
                    }
                    if(memberLBean.getGender().equals("1"))
                    {
                        sex.setBackground(MemberDetailActivity.this.getResources().getDrawable(R.drawable.man));
                    }else{
                        sex.setBackground(MemberDetailActivity.this.getResources().getDrawable(R.drawable.woman));
                    }
                }else{
                    showMessage("没有数据");
                }
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage(Message);
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("会员详情"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("会员详情"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
