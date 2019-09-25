package com.example.epay.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.adapter.StoredListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.StoredBean;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;

public class StoredMemberActivity extends BaseActivity {
    @Bind(R.id.stored_listView)
    ListView listView;
    @Bind(R.id.stored_title)
    TextView title;
    @Bind(R.id.stored_none)
    TextView none;
    ArrayList<StoredBean> list;
    StoredListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stored_member);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        list=new ArrayList<StoredBean>();
        adapter=new StoredListAdapter(this,list);
        listView.setAdapter(adapter);
        if(getIntent().getStringExtra("type").equals("all")) {
            StoredBean storedBean = new StoredBean();
            storedBean.setName("会员名字");
            storedBean.setIconURL("http://www.qqzhi.com/uploadpic/2014-10-01/181605187.jpg");
            storedBean.setMoney("22.00");
            storedBean.setTell("18329347282");
            storedBean.setSum("10");
            list.add(storedBean);
            list.add(storedBean);
            adapter.setList(list);
            title.setText("储值会员");
            hideView(R.id.stored_none);
        }else{
            title.setText("本次活动储值会员");
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(title.getText().toString()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(title.getText().toString()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
