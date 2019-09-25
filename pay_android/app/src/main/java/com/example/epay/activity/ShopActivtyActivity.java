package com.example.epay.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.epay.R;
import com.example.epay.adapter.ShopActivityListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.MembersGiftListBean;
import com.example.epay.bean.MessageBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopActivtyActivity extends BaseActivity {


    @Bind(R.id.shop_activity_listView)
    ListView shopActivityListView;

    ShopActivityListAdapter activityListAdapter;
    ArrayList<MembersGiftListBean.GiftBean> list=new ArrayList();

    private int ID=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_activty);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        MembersGiftListBean listBean=gson.fromJson(CacheData.getMemberGiftList(this),MembersGiftListBean.class);
        list=listBean.getGifts();
        activityListAdapter=new ShopActivityListAdapter(this,list);
        shopActivityListView.setAdapter(activityListAdapter);

        shopActivityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ID=list.get(i).getID();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ShopActivtyActivity.this);
                builder.setMessage("确定选择此活动");
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        httpUtil.HttpServer(ShopActivtyActivity.this, "{\"ID\":"+ID+",\"orderNO\":\""+getIntent().getStringExtra("orderNO")+"\",\"phone\":\""+getIntent().getStringExtra("vipCode")+"\"}", 63, false, new HttpCallBack() {
                            @Override
                            public void back(String data) {
                                startActivity(ShopActivtyActivity.this, DeskManageActivity.class);
                                finish();
                            }

                            @Override
                            public void fail(String Message, int code, String data) {
                                showMessage(Message);
                            }
                        });
                    }
                });
                builder.show();
            }
        });
    }

}
