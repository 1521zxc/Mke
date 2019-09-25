package com.example.epay.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.doHttp.HttpCallBack;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentListActivity extends BaseActivity {

    private String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);
        ButterKnife.bind(this);
        data=getIntent().getStringExtra("data");
        Log.e(TAG, "onCreate: "+data);
    }

    @OnClick(R.id.personal_fl_id)
    public void cash()
    {
        httpUtil.HttpServer(this, "cash/vip/pay", data,true, new HttpCallBack() {
            @Override
            public void back(String data2) {
                showMessage("充值成功");
                startActivity(PaymentListActivity.this,MainActivity.class);
                finish();
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage(Message);
            }
        });
    }

    @OnClick(R.id.personal_fl_security)
    public void wx()
    {
        Intent intent=new Intent(this,ScanQrCodeActivity.class);
        intent.putExtra("data",data);
        intent.putExtra("type",1);
        intent.putExtra("money",getIntent().getDoubleExtra("money",0));
        startActivity(intent);
    }

    @OnClick(R.id.personal_fl_control)
    public void ali()
    {
        Intent intent=new Intent(this,ScanQrCodeActivity.class);
        intent.putExtra("data",data);
        intent.putExtra("type",0);
        intent.putExtra("money",getIntent().getDoubleExtra("money",0));
        startActivity(intent);
    }
}
