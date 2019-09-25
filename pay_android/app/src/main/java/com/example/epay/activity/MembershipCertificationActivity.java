package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.MemberBean;
import com.example.epay.doHttp.HttpCallBack;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class MembershipCertificationActivity extends BaseActivity {
    @Bind(R.id.rz_edit)
    EditText rzEdit;
    double money=0;
    String orderID;
    double activityMoney=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_certification);
        ButterKnife.bind(this);
        money=getIntent().getDoubleExtra("money",0);
        orderID=getIntent().getStringExtra("orderID");
    }


    @OnClick(R.id.rz_ok)
    public void ok() {
        httpUtil.HttpServer(this, "{\"checkNO\":\""+rzEdit.getText().toString()+"\",\"money\":"+money+",\"orderNO\":\""+orderID+"\"}", 47, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                if(getIntent().getBooleanExtra("gift",true)) {
                    showMessage("认证会员成功，价格计算为会员价");
                    MemberBean memberBean = gson.fromJson(data, MemberBean.class);
                    Intent intent = new Intent(MembershipCertificationActivity.this, OrderPayActivity2.class);
                    intent.putExtra("isVip", true);
                    intent.putExtra("vipCode", rzEdit.getText().toString());
                    intent.putExtra("conpone", memberBean.getItems());
                    if (memberBean.getGifts().size() > 0) {
                        for (int i = 0; i < memberBean.getGifts().size(); i++) {
                            activityMoney = activityMoney + memberBean.getGifts().get(i).getVipMoney();
                        }
                    }
                    intent.putExtra("activity", activityMoney);
                    intent.putExtra("activityList", memberBean.getGifts());
                    setResult(10111, intent);
                    finish();
                }else{
                    Intent intent=new Intent(MembershipCertificationActivity.this, ShopActivtyActivity.class);
                    intent.putExtra("orderNO",orderID);
                    intent.putExtra("vipCode", rzEdit.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void fail(String Message, int code,String data) {
                showMessage(Message);
            }
        });
    }
}
