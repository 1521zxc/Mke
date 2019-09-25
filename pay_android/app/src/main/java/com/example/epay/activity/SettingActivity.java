package com.example.epay.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

import com.example.epay.R;
import com.example.epay.activity.fragment.MeFragment;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.User;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.CuncResponse;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.doHttp.ReturnResquest;
import com.example.epay.doHttp.Server;
import com.example.epay.view.ToggleButton;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @Bind(R.id.set_toggle)
    ToggleButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        button.setImageResource(R.drawable.switch_true,
                R.drawable.switch_false, R.drawable.handle);
        button.setSwitchState(CacheData.isOpen(this));
        button.setOnSwitchStateListener(new ToggleButton.OnSwitchListener() {
            @Override
            public void onSwitched(boolean isSwitchOn) {
                button.setSwitchState(isSwitchOn);
                if (isSwitchOn) {
                    CacheData.setOpen(SettingActivity.this, true);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//允许屏幕常亮
                } else {
                    CacheData.setOpen(SettingActivity.this, false);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//禁止屏幕常亮
                }
            }
        });
    }

    //    @OnClick(R.id.set_my)
//    public void my(){
//        startActivity(this, WoMenActivity.class);
//    }
    @OnClick(R.id.set_bank)
    public void Exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确定退出登录");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doLogin("{\"token\":" + CacheData.getToken(SettingActivity.this) + "}");

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    //退出登录
    private void doLogin(final String data) {
        httpUtil.HttpServer(this, data, 32, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                CacheData.setLoginstate(SettingActivity.this, false);
                startActivity(SettingActivity.this, LoginActivity.class);
            }

            @Override
            public void fail(String Message, int code, String data) {
                CacheData.setLoginstate(SettingActivity.this, false);
                startActivity(SettingActivity.this, LoginActivity.class);
                finish();
            }
        });
    }


    @OnClick(R.id.set_pass)
    public void FindPass() {
        startActivity(SettingActivity.this, FindPasswordActivity.class);
    }


    @OnClick(R.id.set_pay_pass)
    public void SetpayPass() {
        startActivity(SettingActivity.this, PayPassActivity.class);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("设置"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("设置"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
