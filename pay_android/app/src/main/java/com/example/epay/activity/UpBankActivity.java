package com.example.epay.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.cache.CacheData;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class UpBankActivity extends BaseActivity {
    @Bind(R.id.up_bank_name)
    TextView name;
    @Bind(R.id.up_bank_up)
    TextView up;
    @Bind(R.id.up_bank_type)
    TextView type;
    Dialog bottomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_bank);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        name.setText(CacheData.getMyBeans(this).getBrandName());
    }

    @OnClick(R.id.up_bank_type)
    public void type()
    {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.buttom_dialog, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        TextView layout=(TextView)contentView.findViewById(R.id.code_layout);
        layout.setText("请选择账号类型");
        TextView zfb=(TextView)contentView.findViewById(R.id.zfb);
        zfb.setText("对公");
        TextView wx=(TextView)contentView.findViewById(R.id.wx);
        wx.setText("对私");
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        WindowManager.LayoutParams lp=bottomDialog.getWindow().getAttributes();
        lp.alpha=1.0f;
        bottomDialog.getWindow().setAttributes(lp);
        bottomDialog.show();

        zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
                type.setText("对公");
            }
        });
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
                type.setText("对私");
            }
        });
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("更改收款银行账户"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("更改收款银行账户"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}
