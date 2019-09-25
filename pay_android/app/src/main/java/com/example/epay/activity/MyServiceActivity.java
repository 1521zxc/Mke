package com.example.epay.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.view.EPayDialog;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class MyServiceActivity extends BaseActivity {

    @Bind(R.id.service_pay)
     TextView type0;
    @Bind(R.id.service_bas)
    TextView type1;
    @Bind(R.id.service_cz)
    TextView type2;
    @Bind(R.id.service_noti)
    TextView type3;
    @Bind(R.id.service_include)
    LinearLayout layout;

    EPayDialog dialog;
    TextView type4,type5,type6,type7,type8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);
        ButterKnife.bind(this);
        dialog=new EPayDialog(this,R.layout.service_dialog);
        initView();
    }

    @OnClick(R.id.up_service)
    public void Up()
    {
        dialog.show();
        View v=dialog.getView();
        final TextView textView=v.findViewById(R.id.dialog_up);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MyServiceActivity.this,UpServiceActivity.class);
            }
        });
    }
    @OnClick(R.id.service_list)
    public void pay()
    {
       startActivity(MyServiceActivity.this,MyServicePayActivity.class);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的服务"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        dialog.dismiss();
        MobclickAgent.onPageEnd("我的服务"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void initView() {
        super.initView();
        type4=(TextView) layout.findViewById(R.id.clude_service_pay);
        type5=(TextView) layout.findViewById(R.id.clude_service_bas);
        type6=(TextView) layout.findViewById(R.id.clude_service_cz);
        type7=(TextView) layout.findViewById(R.id.clude_service_noti);
        type8=(TextView) layout.findViewById(R.id.clude_service_push);
        changeDrawable();

    }

    public void changeDrawable()
    {
        Drawable drawable=getResources().getDrawable(R.drawable.service_pay);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable.setBounds(0,0,(int)(0.075*width),(int)(0.075*width));
        //drawable   第一个是文字TOP
        type0.setCompoundDrawables(null,drawable,null,null);
        Drawable drawable1=getResources().getDrawable(R.drawable.service_bas);
        drawable1.setBounds(0,0,(int)(0.075*width),(int)(0.075*width));
        type1.setCompoundDrawables(null,drawable1,null,null);
        Drawable drawable2=getResources().getDrawable(R.drawable.service_cz);
        drawable2.setBounds(0,0,(int)(0.075*width),(int)(0.075*width));
        type2.setCompoundDrawables(null,drawable2,null,null);
        Drawable drawable3=getResources().getDrawable(R.drawable.service_noti);
        drawable3.setBounds(0,0,(int)(0.075*width),(int)(0.075*width));
        type3.setCompoundDrawables(null,drawable3,null,null);
        Drawable drawable4=getResources().getDrawable(R.drawable.service_pay);
        drawable4.setBounds(0,0,(int)(0.075*width),(int)(0.075*width));
        type4.setCompoundDrawables(null,drawable4,null,null);
        Drawable drawable5=getResources().getDrawable(R.drawable.service_bas);
        drawable5.setBounds(0,0,(int)(0.075*width),(int)(0.075*width));
        type5.setCompoundDrawables(null,drawable5,null,null);
        Drawable drawable6=getResources().getDrawable(R.drawable.service_cz);
        drawable6.setBounds(0,0,(int)(0.075*width),(int)(0.075*width));
        type6.setCompoundDrawables(null,drawable6,null,null);
        Drawable drawable7=getResources().getDrawable(R.drawable.service_noti);
        drawable7.setBounds(0,0,(int)(0.075*width),(int)(0.075*width));
        type7.setCompoundDrawables(null,drawable7,null,null);
        Drawable drawable8=getResources().getDrawable(R.drawable.service_push);
        drawable8.setBounds(0,0,(int)(0.075*width),(int)(0.075*width));
        type8.setCompoundDrawables(null,drawable8,null,null);
    }



}
