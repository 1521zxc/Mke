package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.adapter.ConfirmAddListAdapter;
import com.example.epay.adapter.ConfirmOrderAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.ConfirmBean;
import com.example.epay.bean.MealListBean;
import com.example.epay.bean.OrderInfoBean;
import com.example.epay.doHttp.HttpCallBack;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class ConfirmAddActivity extends BaseActivity {
    @Bind(R.id.add_cata_list)
    ListView addListView;
    @Bind(R.id.order_cata_list)
    ListView orderListView;

    ConfirmBean bean;
    String data="";
    ArrayList<MealListBean.MealRight> list=new ArrayList<>();
    ArrayList<OrderInfoBean.ProductSimple> lists=new ArrayList<>();
    ConfirmAddListAdapter addAdapter;
    ConfirmOrderAdapter orderAdapter;
    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_add);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        data=getIntent().getStringExtra("data");
        id= (int) JSONObject.parseObject(data).get("ID");
        list= (ArrayList<MealListBean.MealRight>) getIntent().getSerializableExtra("list");
        addAdapter=new ConfirmAddListAdapter(this,list);
        addListView.setAdapter(addAdapter);
        orderAdapter=new ConfirmOrderAdapter(this,lists);
        orderListView.setAdapter(orderAdapter);
        doSpecial();
    }

    @OnClick(R.id.add)
    public void add()
    {
        doHttp1();
    }

    @OnClick(R.id.quxiao)
    public void qu()
    {
        startActivity(this,DeskManageActivity.class);
    }


    //活动
    private void doSpecial() {
        httpUtil.HttpServer(this, "{\"ID\":"+id+"}", 86, true, new HttpCallBack() {
                    @Override
                    public void back(String data1) {
                        OrderInfoBean listBean = gson.fromJson(data1, OrderInfoBean.class);
                        lists = listBean.getAttached();
                        orderAdapter.setList(lists);
                    }

                    @Override
                    public void fail(String Message, int code,String data) {
                        showMessage(Message);
                    }
                }
        );
    }







    public void doHttp1()
    {
        httpUtil.HttpServer(this, data, 92, true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                bean=gson.fromJson(data1,ConfirmBean.class);
                if(bean!=null) {
                    Intent intent = new Intent(ConfirmAddActivity.this, OrderPayActivity.class);
                    intent.putExtra("deskName", bean.getDeskName());
                    intent.putExtra("orderNo", bean.getOrderNO());
                    startActivity(intent);
                }else{
                    showMessage("没有数据");
                }
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage(Message);
            }
        });
    }
}
