package com.example.epay.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.epay.R;
import com.example.epay.adapter.SpecialListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.SpecialBean;
import com.example.epay.bean.SpecialListBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.CuncResponse;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.doHttp.ReturnResquest;
import com.example.epay.doHttp.Server;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;

public class SpecialActivity extends BaseActivity {

    @Bind(R.id.special_listView) ListView listView;

    SpecialListAdapter adapter;
    ArrayList<SpecialBean> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        arrayList=new ArrayList<SpecialBean>();
        adapter=new SpecialListAdapter(this,arrayList);
        listView.setAdapter(adapter);
        doSpecial();
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent=new Intent(SpecialActivity.this, WebActivity.class);
            intent.putExtra("titlelayout","活动详情");
            intent.putExtra("title",arrayList.get(i).getTitle());
            intent.putExtra("url",arrayList.get(i).getLink());
            intent.putExtra("editUrl",arrayList.get(i).getEditLink());
            intent.putExtra("isShare",1);
            startActivity(intent);
        });
    }

    //活动
    private void doSpecial() {
        httpUtil.HttpServer(this, "", 61, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                SpecialListBean listBean = gson.fromJson(data, SpecialListBean.class);
                arrayList=listBean.getItems();
                CacheData.cacheSpecialBeans(SpecialActivity.this,arrayList);
                if(arrayList!=null) {
                    adapter.setList(arrayList);
                }else{
                    showMessage("没有数据");
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
        MobclickAgent.onPageStart("官方活动"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("官方活动"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        doSpecial();
    }
}
