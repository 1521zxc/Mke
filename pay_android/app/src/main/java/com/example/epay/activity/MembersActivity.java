package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.adapter.MemberMListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.MemberMBean;
import com.example.epay.bean.MemberMListBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.view.PxListViewForScroll;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class MembersActivity extends BaseActivity {

    @Bind(R.id.member_add)
    TextView add;
    @Bind(R.id.member_total)
    TextView total;
    @Bind(R.id.member_listView)
    PxListViewForScroll listView;

    MemberMBean memberMBean;
    ArrayList<MemberMListBean> list;
    MemberMListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        list = new ArrayList<MemberMListBean>();
        adapter = new MemberMListAdapter(this, list);
        listView.setAdapter(adapter);
        doMember("");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MembersActivity.this, MemberDetailActivity.class);
                intent.putExtra("members", list.get(i - 1).getID());
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.kt_member)
    public void mem() {
        startActivity(this, MyServiceActivity.class);
    }

    @OnClick(R.id.member_code)
    public void code() {
        Intent intent = new Intent(this, MemberCodeActivity.class);
        intent.putExtra("title", 0);
        startActivity(intent);
    }

    //登录
    private void doMember(String data1) {
        httpUtil.HttpServer(this, data1, 43, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                memberMBean = gson.fromJson(data, MemberMBean.class);
                if (memberMBean != null) {
                    total.setText(memberMBean.getTotalCount());
                    add.setText(memberMBean.getTodayCount());
                    list = memberMBean.getItems();
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("会员管理"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); // 统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("会员管理"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
