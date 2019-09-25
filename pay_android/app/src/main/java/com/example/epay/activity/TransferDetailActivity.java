package com.example.epay.activity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.adapter.TransferDetailListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.TransferDetailBean;
import com.example.epay.bean.TransferList;
import com.example.epay.doHttp.CuncResponse;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.doHttp.ReturnResquest;
import com.example.epay.doHttp.Server;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.util.DateUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;

public class TransferDetailActivity extends BaseActivity {
    @Bind(R.id.transfer_detail_all)
    TextView all;
    @Bind(R.id.transfer_detail_money)
    TextView money;
    @Bind(R.id.transfer_detail_state)
    TextView state;
    @Bind(R.id.transfer_detail_bank_state)
    TextView bankState;
    @Bind(R.id.transfer_detail_allmoney)
    TextView allMoney;
    @Bind(R.id.transfer_detail_fee)
    TextView fee;
    @Bind(R.id.transfer_detail_rb)
    CheckBox checkBox;
    @Bind(R.id.transfer_detail_layout)
    LinearLayout layout;
    @Bind(R.id.transfer_detail_wx)
    Button wx;
    @Bind(R.id.transfer_detail_zfb)
    Button zfb;
    @Bind(R.id.transfer_detail_qq)
    Button qq;
    @Bind(R.id.transfer_detail_all_title)
    TextView title;
    @Bind(R.id.transfer_detail_list)
    ListView listView;

    private String datas;
    TransferDetailBean detailBean;
    ArrayList<TransferList> arrayList;
    TransferDetailListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_detail);
        ButterKnife.bind(this);
        initView();
    }


    @Override
    public void initView() {
        super.initView();
        detailBean=new TransferDetailBean();
        arrayList=new ArrayList<TransferList>();
        adapter=new TransferDetailListAdapter(this,arrayList);
        listView.setAdapter(adapter);
        long time= getIntent().getLongExtra("gainedDate",0);
        JSONObject data=new JSONObject();
        data.put("gainedDate",time);
        datas = data.toString();
        doDetail(datas);

        Drawable drawable=TransferDetailActivity.this.getResources().getDrawable(R.drawable.transfer_detail_btn);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable.setBounds(0,0,(int)(0.05*width),(int)(0.035*width));
        //drawable   第一个是文字TOP
        checkBox.setCompoundDrawables(null,null,drawable,null);

        Drawable drawable1=TransferDetailActivity.this.getResources().getDrawable(R.drawable.icon_weixin_pay);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable1.setBounds(0,0,(int)(0.08*width),(int)(0.08*width));
        //drawable   第一个是文字TOP
        wx.setCompoundDrawables(drawable1,null,null,null);
        Drawable drawable2=TransferDetailActivity.this.getResources().getDrawable(R.drawable.icon_alipay_pay);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable2.setBounds(0,0,(int)(0.08*width),(int)(0.08*width));
        //drawable   第一个是文字TOP
        zfb.setCompoundDrawables(drawable2,null,null,null);
        Drawable drawable3=TransferDetailActivity.this.getResources().getDrawable(R.drawable.qq);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable3.setBounds(0,0,(int)(0.08*width),(int)(0.08*width));
        //drawable   第一个是文字TOP
        qq.setCompoundDrawables(drawable3,null,null,null);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    layout.setVisibility(View.VISIBLE);
                }else{
                    layout.setVisibility(View.GONE);
                }
            }
        });

    }


    //登录
    private void doDetail(final String data) {
        httpUtil.HttpServer(this, data, 85, true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                detailBean=gson.fromJson(data1, TransferDetailBean.class);
                if(detailBean!=null) {
                    all.setText(DateUtil.format2(detailBean.getGainedDate(), "yyyy-MM-dd") + "  共" + detailBean.getDetails().size() + "笔");
                    money.setText(detailBean.getTotalSum());
                    state.setText("（已划款）");
                    bankState.setText("银行正在处理中。正常18：00之前到帐，如未到账请咨询发卡行");
                    allMoney.setText("交易总金额：￥" + detailBean.getTotalTransfer());
                    fee.setText("手续费：￥" + detailBean.getServiceFee());
                    wx.setText("￥" + detailBean.getWeixinRatio() + "%");
                    zfb.setText("￥" + detailBean.getAliRatio() + "%");
                    qq.setText("￥" + detailBean.getQqRatio() + "%");
                    title.setText(DateUtil.format2(detailBean.getGainedDate(), "yyyy-MM-dd") + "  共" + detailBean.getDetails().size() + "笔");
                    arrayList = detailBean.getDetails();
                    adapter.setList(arrayList);
                    all.setFocusable(true);
                    all.setFocusableInTouchMode(true);
                    all.requestFocus();
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
        MobclickAgent.onPageStart("划款详情"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("划款详情"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
