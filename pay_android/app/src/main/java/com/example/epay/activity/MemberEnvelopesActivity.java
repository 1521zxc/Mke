package com.example.epay.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.adapter.EnvelopeListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.EnvelopesBean;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class MemberEnvelopesActivity extends BaseActivity {
    @Bind(R.id.member_envelopes_listView)
    ListView listView;

    EnvelopeListAdapter adapter;
    ArrayList<EnvelopesBean> arrayList;
    int rd=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_envelopes);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        rd=Math.random()>0.5?1:0;
        arrayList=new ArrayList<EnvelopesBean>();
        adapter=new EnvelopeListAdapter(this,arrayList);
        listView.setAdapter(adapter);
        if(rd==1)
        {
            hideView(R.id.member_envelopes_null);
            EnvelopesBean envelopesBean=new EnvelopesBean();
            envelopesBean.setName("新店开张送红包1");
            envelopesBean.setAllSum("500");
            envelopesBean.setEndTime("2018.07.12");
            envelopesBean.setLingquSum("5");
            envelopesBean.setShiSum("1");
            envelopesBean.setStartTime("2018.06.12");
            envelopesBean.setSnall("0.1");
            envelopesBean.setOld("1");
            envelopesBean.setEnvelopeSum("222");
            envelopesBean.setMoneyTime("30");
            arrayList.add(envelopesBean);
            adapter.setList(arrayList);
        }
    }
    @OnClick(R.id.member_envelopes_up)
    public void up(){
        startActivity(this,UpEnvelopeTypeActivity.class);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("会员红包"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("会员红包"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
