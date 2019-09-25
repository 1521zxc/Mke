package com.example.epay.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.epay.R;
import com.example.epay.adapter.TransferListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.TransferBean;
import com.example.epay.bean.TransferListBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.CuncResponse;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.doHttp.ReturnResquest;
import com.example.epay.doHttp.Server;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;

public class TransferActivity extends BaseActivity{

    @Bind(R.id.transfer_listView)
    ListView listView;

    TransferListAdapter adapter;
    ArrayList<TransferBean> arrayList;
    TransferListBean listBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);
        initView();
    }
    @Override
    public void initView() {
        super.initView();
        arrayList=new ArrayList<TransferBean>();
        adapter=new TransferListAdapter(TransferActivity.this,arrayList);
        listView.setAdapter(adapter);
        doTransfer();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(TransferActivity.this,TransferDetailActivity.class);
                intent.putExtra("gainedDate",arrayList.get(i).getGainedDate());
                startActivity(intent);
            }
        });
    }
    //划款记录
    private void doTransfer() {
        httpUtil.HttpServer(this, "", 84, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                listBean = gson.fromJson(data, TransferListBean.class);
                if(listBean.getItems()!=null) {
                    arrayList = listBean.getItems();
                    CacheData.cacheTransferBeans(TransferActivity.this, arrayList);
                    adapter.setList(arrayList);
                }
            }

            @Override
            public void fail(String Message, int code,String data) {
                showMessage(Message);
            }
        });
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("划款记录"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("划款记录"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
