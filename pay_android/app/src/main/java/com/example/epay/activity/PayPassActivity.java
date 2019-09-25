package com.example.epay.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.view.PasswordView;

import butterknife.ButterKnife;
import butterknife.Bind;

public class PayPassActivity extends BaseActivity {
    @Bind(R.id.pay_pass_title)
    TextView passTitle;
    @Bind(R.id.pwd_view)
    PasswordView pwdView;

    int index=0;
    JSONObject object=new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pass);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        pwdView.setOnFinishInput(new PasswordView.OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
              if(index==0)
              {
                  object.clear();
                  object.put("oldRefundPwd",pwdView.getStrPassword());
                  passTitle.setText("设置新支付密码");

                  pwdView.clear();
                  pwdView.show();
                  index=1;
              }else{
                  object.put("newRefundPwd",pwdView.getStrPassword());
                  passTitle.setText("支付密码");
                  index=0;
                  doReduct();
              }
            }
        });
    }


    //活动
    private void doReduct() {
        httpUtil.HttpServer(this, object.toString(), 28, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                finish();
            }

            @Override
            public void fail(String Message,int code,String data) {
                pwdView.clear();
                pwdView.show();
                showMessage(Message);
            }
        });
    }
}
