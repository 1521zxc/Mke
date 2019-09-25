package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.doHttp.HttpCallBack;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMembershipActivity extends BaseActivity {

    @Bind(R.id.add_members_name)
    EditText addMembersName;
    @Bind(R.id.add_members_phone)
    EditText addMembersPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_membership);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rz_ok)
    public void ok(){
        if(addMembersName.getText().toString().equals("")){
            showMessage("请输入姓名");
        }else if(addMembersPhone.getText().toString().equals("")){
            showMessage("请输入会员手机号");
        }else  if (!isMobileNO(addMembersPhone.getText().toString())) {
            showMessage("手机号不规范");
        }else{
            httpUtil.HttpServer(this, "{\"memberName\":\""+addMembersName.getText().toString()+"\",\"phone\":\""+addMembersPhone.getText().toString()+"\"}", 17,  true, new HttpCallBack() {
                @Override
                public void back(String data) {
                    Intent intent=new Intent(AddMembershipActivity.this, ShopActivtyActivity.class);
                    intent.putExtra("orderNO",getIntent().getStringExtra("orderID"));
                    intent.putExtra("vipCode", addMembersPhone.getText().toString());
                    startActivity(intent);
                    finish();
                }

                @Override
                public void fail(String Message, int code, String data) {
                    showMessage(Message);
                }
            });
        }
    }
}
