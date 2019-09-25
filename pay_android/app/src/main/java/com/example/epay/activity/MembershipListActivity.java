package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.epay.R;
import com.example.epay.adapter.MembershipAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.MemberBean;
import com.example.epay.bean.MemberListBean;
import com.example.epay.doHttp.HttpCallBack;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MembershipListActivity extends BaseActivity {

    @Bind(R.id.ry_membership_list)
    ListView ryMembershipList;
    MembershipAdapter adapter;
    ArrayList<MemberBean> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_list);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        adapter=new MembershipAdapter(this,list);
        ryMembershipList.setAdapter(adapter);
        doMemberList();
        ryMembershipList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MembershipListActivity.this,MembershipDetailActivity.class);
                intent.putExtra("bean",list.get(i));
                startActivity(intent);


            }
        });
    }

    private void doMemberList() {
        httpUtil.HttpServer(this, "{\"pageNO\":0,\"pageSize\":10000,\"keyword\":\"\"}", 18, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                MemberListBean memberListBean = gson.fromJson(data, MemberListBean.class);
                if (memberListBean.getCount() >0) {
                    list=memberListBean.getItems();
                    adapter.setList(list);
                } else {
                    showMessage("没有数据");
                }
            }
            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });

    }
}
