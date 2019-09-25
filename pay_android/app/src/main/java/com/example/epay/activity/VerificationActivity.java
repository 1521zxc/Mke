package com.example.epay.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.CuncResponse;
import com.example.epay.doHttp.ReturnResquest;
import com.example.epay.doHttp.Server;
import com.example.epay.view.TimeButton;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class VerificationActivity extends BaseActivity {
    @Bind(R.id.verifi_account)
    TextView ac;
    @Bind(R.id.verifi_code)
    EditText passtext;
    @Bind(R.id.verifi_get)
    TimeButton getCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        ac.setText(CacheData.getMyBeans(this).getMobile());

    }

    @OnClick(R.id.verifi_get)
    public void getCheck(View v) {
        getCheck.setOnCli(true);
    }

    @OnClick(R.id.verifi_up)
    public void up()
    {
        if(passtext.getText().toString().equals("")||passtext.getText().toString()==null)
        {
            showMessage("请输入验证码");
            return;
        }
        startActivity(VerificationActivity.this,UpBankActivity.class);
        //upPass();
    }

    //银行卡列表
    String message = "";
    private void upPass() {
        Server server = new Server(VerificationActivity.this, "") {
            @Override
            protected Integer doInBackground(String... params) {
                ReturnResquest returnResquest = new ReturnResquest();
                try {
                    CuncResponse resp = returnResquest.request(VerificationActivity.this, "", 61);
                    message = resp.errorMsg;
                    return resp.RespCode;
                } catch (Exception e) {
                    return -1;
                }
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                startActivity(VerificationActivity.this,UpBankActivity.class);
            }
        };
        server.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("验证账户"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("验证账户"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
