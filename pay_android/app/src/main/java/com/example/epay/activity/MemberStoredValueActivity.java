package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.adapter.MemberStoredValueAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.MemberBean;
import com.example.epay.bean.MemberStoredBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemberStoredValueActivity extends BaseActivity {

    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.tv_Member_name)
    TextView tvMemberName;
    @Bind(R.id.tv_balance)
    TextView tvBalance;
    @Bind(R.id.ry_amount_of_money)
    RecyclerView ryAmountOfMoney;

    private List<MemberStoredBean.ItemsBean> itemsBeans=new ArrayList<>();
    private MemberStoredValueAdapter adapter;
    private double money=0;
    private int teIndex=-1;
    private StringBuilder sb;
    MemberBean memberBean;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_stored_value);
        ButterKnife.bind(this);
        doScan();
    }

    public void doScan() {
        httpUtil.HttpServer(this, "", 56, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                MemberStoredBean mb = gson.fromJson(data, MemberStoredBean.class);
                if(mb.getItems()!=null) {
                    itemsBeans = mb.getItems();
                }
                if(itemsBeans.size()>0) {
                    itemsBeans.get(0).setIsCheck(true);
                    money = 0;
                    teIndex = 0;
                }else{
                    teIndex=-1;
                }
                initialization();
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }
    private void initialization() {
        ryAmountOfMoney.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new MemberStoredValueAdapter(itemsBeans, this);
        ryAmountOfMoney.setAdapter(adapter);
        adapter.setRecyclerItemClickListener(new MemberStoredValueAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(int Position, List<MemberStoredBean.ItemsBean> dataBeanList) {
                if(Position!=itemsBeans.size()){
                    MemberStoredValueActivity.this.money=0;
                    teIndex=Position;
                    adapter.setList(dataBeanList);
                }else{
                    teIndex=-1;
                    adapter.setList(dataBeanList);
                }
            }

            @Override
            public void onText(double money) {
                MemberStoredValueActivity.this.money=money;
            }
        });
    }

    @OnClick({R.id.tv_ok, R.id.but_recharge,R.id.et_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                if(!etPhone.getText().toString().equals("")) {
                    httpUtil.HttpServer(this, "{\"checkNO\":\"" + etPhone.getText().toString() + "\"}", 47, false, new HttpCallBack() {
                        @Override
                        public void back(String data) {
                            if(!data.equals("")&&data!=null) {
                                memberBean = gson.fromJson(data, MemberBean.class);
                                tvMemberName.setText(memberBean.getUserName());
                                tvBalance.setText("￥" + memberBean.getPrice());
                            }
                        }

                        @Override
                        public void fail(String Message, int code, String data) {
                            showMessage(Message);
                        }
                    });
                }else{
                    showMessage("请输入会员卡号或者手机号");
                }
                break;
            case R.id.but_recharge:
                if(tvMemberName.getText().toString().equals("")){
                    if(!etPhone.getText().toString().equals("")) {
                        httpUtil.HttpServer(this, "{\"checkNO\":\"" + etPhone.getText().toString() + "\"}", 47, false, new HttpCallBack() {
                            @Override
                            public void back(String data) {
                                if(!data.equals("")&&data!=null) {
                                    memberBean = gson.fromJson(data, MemberBean.class);

                                    if (memberBean.getType() == 0) {
                                        showMessage("此卡不可储值");
                                    } else if (memberBean.getFlag() != 1) {
                                        showMessage("此卡异常");
                                    } else {
                                        if (teIndex > -1 || money > 0) {
                                            setData();
                                            Intent intent = new Intent(MemberStoredValueActivity.this, PaymentListActivity.class);
                                            intent.putExtra("data", sb.toString());
                                            intent.putExtra("money", money);
                                            startActivity(intent);
                                        } else {
                                            showMessage("异常");
                                        }
                                    }
                                }
                            }

                            @Override
                            public void fail(String Message, int code, String data) {
                                showMessage(Message);
                            }
                        });
                    }else{
                        showMessage("请输入会员卡号或者手机号");
                    }
                }else{
                    if(memberBean.getType()==0){
                        showMessage("此卡不可储值");
                    }else if(memberBean.getFlag()!=1){
                        showMessage("此卡异常");
                    }else {
                        if (teIndex>-1||money>0) {
                            setData();
                            Intent intent = new Intent(MemberStoredValueActivity.this, PaymentListActivity.class);
                            intent.putExtra("data", sb.toString());
                            intent.putExtra("money", money);
                            startActivity(intent);
                        }else{
                            showMessage("异常");
                        }
                    }
                }
                break;
            case R.id.et_phone:
                etPhone.setFocusable(true);
                break;
        }
    }


    public void setData()
    {
        sb=new StringBuilder();
        if (teIndex>-1) {
            sb.append("templateID=").append(itemsBeans.get(teIndex).getID());
            sb.append("&");
            money=itemsBeans.get(teIndex).getFullMoney();
        } else if(money>0) {
            sb.append("money=").append(money);
            sb.append("&");
            int index=-1;
            for(int i=0;i<itemsBeans.size();i++){
                if(money>=itemsBeans.get(i).getFullMoney()&&itemsBeans.get(i).getType()==1)
                {

                    if (index > -1) {
                        if (itemsBeans.get(index).getPrice() < itemsBeans.get(i).getPrice()) {
                            index = i;
                        }
                    } else {
                        index = i;
                    }
                }
            }
            if(index>-1){
                sb.append("freeMoney=").append(itemsBeans.get(index).getPrice());
                sb.append("&");
            }
        }
        sb.append("muuid=").append(CacheData.getUser(MemberStoredValueActivity.this, String.valueOf(CacheData.getId(MemberStoredValueActivity.this))).getMuuid());
        sb.append("&");
        sb.append("phone=").append(etPhone.getText().toString());

    }
}
