package com.example.epay.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.epay.R;
import com.example.epay.adapter.BillListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.BillBean;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;

public class BillStoredActivity extends BaseActivity {
    @Bind(R.id.bill_stared_listView)
    ListView listView;
    BillListAdapter adapter;
    ArrayList<BillBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_stored);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        list=new ArrayList<BillBean>();
        adapter=new BillListAdapter(this,list);
        listView.setAdapter(adapter);
        BillBean billBean=new BillBean();
        billBean.setStatus("手动储值");
        billBean.setName("不知道这里是啥意思");
        billBean.setStartTime("2018.02.11");
        billBean.setEndTime("2018.12.31");
        billBean.setMoney("55.12");
        list.add(billBean);
        list.add(billBean);
        adapter.setList(list);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("储值账单"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("储值账单"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
