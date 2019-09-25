package com.example.epay.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.adapter.MembershipDetailAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.MemberBean;
import com.example.epay.bean.MembershipListBean;
import com.example.epay.doHttp.HttpCallBack;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MembershipDetailActivity extends BaseActivity {
    @Bind(R.id.membership_listView)
    ListView listView;
    @Bind(R.id.membership_img)
    ImageView imageView;
    @Bind(R.id.membership_name)
    TextView nameText;
    @Bind(R.id.membership_phone)
    TextView phoneText;
    @Bind(R.id.member_ship_detail_type1)
    LinearLayout detailType1;
    @Bind(R.id.member_ship_detail_type2)
    LinearLayout detailType2;
    MembershipDetailAdapter adapter;
    ArrayList<MembershipListBean.Membershipbean> list = new ArrayList<>();
    ArrayList<MembershipListBean.Membershipbean> list2 = new ArrayList<>();
    MemberBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_detail);
        ButterKnife.bind(this);
        initView();
    }
    @Override
    public void initView() {
        super.initView();
        bean = (MemberBean) getIntent().getSerializableExtra("bean");
        load(bean.getIconURL(), imageView, 0);
        nameText.setText(bean.getMemberName());
        phoneText.setText(bean.getPhone());
        adapter=new MembershipDetailAdapter(this,list);
        listView.setAdapter(adapter);
        doHttp();
    }

    public void doHttp() {
        httpUtil.HttpServer(MembershipDetailActivity.this, "{\"phone\":\"" + bean.getPhone() + "\",\"pageNO\":0,\"pageSize\":100,\"flowType\":1}", 68, false, new HttpCallBack() {
            @Override
            public void back(String data) {
                MembershipListBean listBean=gson.fromJson(data,MembershipListBean.class);
                list=listBean.getItems();
                adapter.setList(list,0);
                httpUtil.HttpServer(MembershipDetailActivity.this, "{\"phone\":\"" + bean.getPhone() + "\",\"pageNO\":0,\"pageSize\":100,\"flowType\":2}", 68, false, new HttpCallBack() {
                    @Override
                    public void back(String data) {
                        MembershipListBean listBean=gson.fromJson(data,MembershipListBean.class);
                        list2=listBean.getItems();
                    }

                    @Override
                    public void fail(String Message, int code, String data) {
                        showMessage(Message);
                    }
                });
            }
            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });


    }

    @OnClick(R.id.member_ship_detail_type1)
    public void type1(){
        adapter.setList(list,0);
        detailType1.setBackgroundColor(getResources().getColor(R.color.type1));
        detailType2.setBackgroundColor(getResources().getColor(R.color.type2));
    }

    @OnClick(R.id.member_ship_detail_type2)
    public void type2(){
        adapter.setList(list2,1);
        detailType1.setBackgroundColor(getResources().getColor(R.color.type2));
        detailType2.setBackgroundColor(getResources().getColor(R.color.type1));
    }
}
