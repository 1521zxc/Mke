package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.CashDetailBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.DateUtil;
import com.example.epay.util.TypeUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class CashDetailActivity extends BaseActivity {
    @Bind(R.id.cash_detail_co_state)
    TextView coState;
    @Bind(R.id.cash_detail_ac)
    TextView ac;
    @Bind(R.id.cash_detail_address)
    TextView address;
    @Bind(R.id.cash_detail_co_money)
    TextView coMoney;
    @Bind(R.id.cash_detail_icon)
    ImageView icon;
    @Bind(R.id.cash_detail_money)
    TextView money;
    @Bind(R.id.cash_detail_name)
    TextView name;
    @Bind(R.id.cash_detail_number)
    TextView number;
    @Bind(R.id.cash_detail_co_ok)
    TextView ok;
    @Bind(R.id.cash_detail_state)
    TextView state;
    @Bind(R.id.cash_detail_time)
    TextView time;
    @Bind(R.id.cash_detail_tui)
    TextView tuiText;


    private String datas;
    CashDetailBean cashDetailBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_detail);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        cashDetailBean=new CashDetailBean();
        int id=getIntent().getIntExtra("id",0);
        Log.i(TAG, "initView: "+id);
        JSONObject data = new JSONObject();
        data.put("ID",id+"");
        datas = data.toString();
        doDetail(datas);
    }

    //登录
    private void doDetail(final String data1) {

        httpUtil.HttpServer(this, data1, 82, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                cashDetailBean=gson.fromJson(data, CashDetailBean.class);
                if(cashDetailBean!=null) {
                    load(cashDetailBean.getIconURL(), icon, 0);
                    name.setText(cashDetailBean.getNickName());
                    TypeUtil.load(CashDetailActivity.this, cashDetailBean.getPayType(), coState, "%s收款");
                    coMoney.setText("实收" + cashDetailBean.getPaidMoney() + "元");
                    money.setText(cashDetailBean.getPaidMoney() + "元");
                    if(cashDetailBean.getStatus().equals("1")) {
                        state.setText("已支付");
                    }else{
                        state.setText("未支付");
                    }
                    time.setText(DateUtil.format2(cashDetailBean.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                    number.setText(cashDetailBean.getPayNO());
                    address.setText(cashDetailBean.getBrandName());
                    ok.setText("无");
                    ac.setText("主账号");
                    if(cashDetailBean.getPaidMoney()<0.01) {
                        tuiText.setVisibility(View.GONE);
                    }
                }else {
                    showMessage("没有数据");
                }
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage(Message);
            }
        });
    }

    @OnClick(R.id.cash_detail_title)
    public void member()
    {
        if(cashDetailBean.getBuyerID()!=0&&!cashDetailBean.getNickName().equals("")) {
            Intent intent = new Intent(CashDetailActivity.this, MemberDetailActivity.class);
            intent.putExtra("members", cashDetailBean.getBuyerID()+"");
            startActivity(intent);
        }
    }

    @OnClick(R.id.cash_detail_tui)
    public void tui()
    {
        Intent intent=new Intent(this,RefundActivity.class);
        intent.putExtra("cash",cashDetailBean);
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("流水详情"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("流水详情"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}