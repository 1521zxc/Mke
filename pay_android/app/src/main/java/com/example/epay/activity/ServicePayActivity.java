package com.example.epay.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.OrderBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.CuncResponse;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.doHttp.ReturnResquest;
import com.example.epay.doHttp.Server;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class ServicePayActivity extends BaseActivity {
    @Bind(R.id.s0)
    RadioButton s;
    @Bind(R.id.s1)
    RadioButton s2;
    @Bind(R.id.t0)
    TextView t;
    @Bind(R.id.t1)
    TextView t2;

    String orderNO="",saleMoney="";
    int  statue=0;

    IWXAPI msgApi;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
                String obj=(String)msg.obj;
                showMessage(obj);
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_pay);
        ButterKnife.bind(this);
        initView();
        msgApi = WXAPIFactory.createWXAPI(this, null);
        msgApi.registerApp("wxb23a820537c2a2d3");
    }

    @Override
    public void initView() {
        super.initView();
        Drawable drawable2=getResources().getDrawable(R.drawable.pay_selector);
        drawable2.setBounds(0,0,(int)(0.05*width),(int)(0.05*width));
        s.setCompoundDrawables(null,null,drawable2,null);
        Drawable drawable3=getResources().getDrawable(R.drawable.pay_selector);
        drawable3.setBounds(0,0,(int)(0.05*width),(int)(0.05*width));
        s2.setCompoundDrawables(null,null,drawable3,null);
        s.setChecked(true);
        Drawable drawable=getResources().getDrawable(R.drawable.icon_alipay_pay);
        drawable.setBounds(0,0,(int)(0.07*width),(int)(0.07*width));
        t.setCompoundDrawables(null,null,drawable,null);
        Drawable drawable4=getResources().getDrawable(R.drawable.icon_weixin_pay);
        drawable4.setBounds(0,0,(int)(0.07*width),(int)(0.07*width));
        t2.setCompoundDrawables(null,null,drawable4,null);
        s.setChecked(true);

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    s2.setChecked(false);
                }
            }
        });
        s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    s.setChecked(false);
                }
            }
        });

    }

    @OnClick(R.id.up_service_pay)
    public void pay()
    {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("remark","");
        jsonObject.put("money","0.01");
        jsonObject.put("muuid","2212408da5cb48bf9ac70060a6987043");
        doDetail(JSON.toJSONString(jsonObject));
    }




    //下单
    private void doDetail(final String data) {
        httpUtil.HttpServer(this, data, 80, true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                OrderBean orderBean=gson.fromJson(data1,OrderBean.class);
                orderNO=orderBean.getOrderNO();
                saleMoney=orderBean.getSaleMoney();
                doPay();
            }

            @Override
            public void fail(String Message, int code,String data) {
                showMessage(Message);
            }
        });
    }
    public void doPay()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("orderNO=").append(orderNO);
        sb.append("&");
        sb.append("muuid=").append("2212408da5cb48bf9ac70060a6987043");
        sb.append("&");
        sb.append("money=").append(saleMoney);
        if(s.isChecked())
        {
            doCodeUrl(sb.toString(),"ali/app/create",1);
        }else{
            doCodeUrl(sb.toString(),"weixin/app/create",2);
        }

    }


    //CodeUrl
    private void doCodeUrl(final String data, final String url, final int action) {
        httpUtil.HttpServer(this, url, data,true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                if(action==1)
                {
                    final String orderInfo = data1;   // 订单信息

                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(ServicePayActivity.this);
                            String result = String.valueOf(alipay.payV2(orderInfo,true));
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }else{
                    PayReq request = new PayReq();
                    request.appId = "wxb23a820537c2a2d3";
                    request.partnerId = "1900000109";
                    request.prepayId= "1101000000140415649af9fc314aa427";
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr= "1101000000140429eb40476f8896f4c9";
                    request.timeStamp= "1398746574";
                    request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
                    msgApi.sendReq(request);
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
        MobclickAgent.onPageStart("高级服务支付"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("高级服务支付"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
