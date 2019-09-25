package com.example.epay.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.adapter.DeskHListAdapter;
import com.example.epay.adapter.DeskListAdapter;
import com.example.epay.adapter.OrderInfoListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.DeskBean;
import com.example.epay.bean.DeskListBean;
import com.example.epay.bean.OrderInfoBean;
import com.example.epay.doHttp.CuncResponse;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.doHttp.ReturnResquest;
import com.example.epay.doHttp.Server;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class OrderDeskChangeActivity extends BaseActivity {

    @Bind(R.id.order_desk_gridView)
    GridView gridView;


    DeskHListAdapter adapter;
    ArrayList<OrderInfoBean.ProductSimple> list;
    DeskListBean listBean;
    OrderInfoBean infoBean;
    String data = "";
    int type=0;
    ArrayList<DeskBean> getDesklist;

    String deskNO="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_desk_change);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        list=new ArrayList<>();
        getDesklist=new ArrayList<>();
        adapter=new DeskHListAdapter(this,getDesklist);
        gridView.setAdapter(adapter);
        JSONObject object=new JSONObject();
        object.put("pageSize",100);
        object.put("deskStatus",0);
        data=object.toString();
        doNullDesk();


        adapter.setOnNtClickListener(new DeskHListAdapter.OnNtClickListener() {
            @Override
            public void onNtClick(int position) {
                if(position!=-1)
                {
                    deskNO=getDesklist.get(position).getDeskNO();
                }else{
                    deskNO="";
                }
                adapter.notifyDataSetChanged();
            }
        });
    }



        //活动
    private void doNullDesk() {
        httpUtil.HttpServer(this, data, 48, true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                listBean = gson.fromJson(data1, DeskListBean.class);
                getDesklist=listBean.getItems();
                if(getDesklist!=null) {
                    adapter.setList(getDesklist);
                    adapter.notifyDataSetChanged();
                }else{
                    showMessage( "没有数据");
                }
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage(Message);
            }
        });
    }


    @OnClick(R.id.order_desk_que)
    public void que()
    {
        if(!deskNO.equals("")) {
            JSONObject object = new JSONObject();
            object.put("toDeskNO", deskNO);
            object.put("ID", getIntent().getIntExtra("orderID", 0));

            if (getIntent().getIntExtra("reserve", 1) == 2) {
                object.put("reserve", 0);
            }
            data = object.toString();
            doDeskH();
        }else{
            showMessage("请选择桌子");
        }
    }


        //活动
    private void doDeskH() {
        httpUtil.HttpServer(this, data, 93, true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                infoBean = gson.fromJson(data1, OrderInfoBean.class);
                if(infoBean!=null) {
                    OrderDeskChangeActivity.this.setResult(12);
                    finish();
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


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("换桌"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("换桌"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
