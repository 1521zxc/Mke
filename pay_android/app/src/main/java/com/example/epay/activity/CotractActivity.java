package com.example.epay.activity;

import android.os.Bundle;

import com.example.epay.R;
import com.example.epay.adapter.CotractListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.CotractBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.view.EPayListView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class CotractActivity extends BaseActivity {
    @Bind(R.id.cotract_list)
    EPayListView listView;
    ArrayList<CotractBean> list;
    CotractListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotract);
        ButterKnife.bind(this);
        initView();
    }
    //费率
    private void doqian() {
        httpUtil.HttpServer(this, "", 37, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
                //再转JsonArray 加上数据头
                JsonArray jsonArray = jsonObject.getAsJsonArray("items");
                //循环遍历
                for (JsonElement user : jsonArray) {
                    //通过反射 得到UserBean.class
                    CotractBean userBean = gson.fromJson(user, new TypeToken<CotractBean>() {}.getType());
                    list.add(userBean);
                }
                if(list!=null) {
                    adapter.setList(list);
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

    @Override
    public void initView() {
        super.initView();
        list=new ArrayList<CotractBean>();
        adapter=new CotractListAdapter(this,list);
        listView.setAdapter(adapter);
        doqian();
    }

    @OnClick(R.id.xieyi)
    public void xie()
    {
        startActivity(this,AgreementActivity.class);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("店铺签约信息"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("店铺签约信息"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}
