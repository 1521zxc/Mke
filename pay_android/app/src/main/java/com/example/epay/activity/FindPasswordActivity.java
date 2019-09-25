package com.example.epay.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.view.TimeButton;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class FindPasswordActivity extends BaseActivity {

    @Bind(R.id.get_check)
    TimeButton getCheck;
    @Bind(R.id.find_account)
    EditText phone;
    @Bind(R.id.find_code)
    EditText code;
    @Bind(R.id.find_password)
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.get_check)
    public void getCheck(View v) {
        if ("".equals(phone.getText().toString().trim())) {
            showMessage("请输入你的手机号");
            getCheck.setOnCli(false);
            return;
        }
        if(!isMobileNO(phone.getText().toString().trim()))
        {
            showMessage("请输入正确的手机号");
            getCheck.setOnCli(false);
            return;
        }
        getCheck.setOnCli(true);
        showMessage("输入验证码");
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("重置密码"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("重置密码"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
