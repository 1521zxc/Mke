package com.example.epay.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.QRCode.zxing.encode.EncodingHandler;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.MembersGiftListBean;
import com.example.epay.bean.ScanQrBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.EPayQRCode;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.WriterException;
import com.umeng.analytics.MobclickAgent;


import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class CollectCodeActivity extends BaseActivity {

    @Bind(R.id.code_title)
    TextView title;
    @Bind(R.id.collect_code)
    TextView collectCode;
    @Bind(R.id.or_code)
    ImageView QRImage;
    @Bind(R.id.code_type)
    TextView type;
    @Bind(R.id.code_sum)
    TextView sumText;
    @Bind(R.id.collect_code_layout)
    LinearLayout codeLayout;
    @Bind(R.id.qie)
    TextView qie;


    String s;
    Map<String, String> retMap;
    int sumType = 0;
    String orderNO = "";
    java.util.Timer timer;
    int sum = 0;
    String dis = "";
    ScanQrBean bean;
    Dialog bottomDialog;
    String payNO = "";
    String discountInfo = "", vipCode = "";
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }
            if (getIntent().getBooleanExtra("isisis", false)) {
                startActivity(CollectCodeActivity.this, MainActivity.class);
            } else {
                if (getIntent().getIntExtra("payType", 0) == 1) {
                    getGift();
                } else {
                    setResult(1212);
                }
            }
            finish();
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_code);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        s = getIntent().getStringExtra("sum");
        orderNO = getIntent().getStringExtra("orderID");
        dis = getIntent().getStringExtra("dis");
        sumType = getIntent().getIntExtra("sumType", 0);
        discountInfo = getIntent().getStringExtra("discountInfo");
        vipCode = getIntent().getStringExtra("vipCode");
        if (vipCode == null) {
            vipCode = "";
        }
        sumText.setText("￥" + s + "元");
        Drawable drawable = getResources().getDrawable(R.drawable.ic_gathering);
        drawable.setBounds(0, 0, (int) (0.067 * width), (int) (0.067 * width));
        collectCode.setCompoundDrawables(drawable, null, null, null);
        if (getIntent().getBooleanExtra("isisis", false)) {
            show1();
            codeLayout.setVisibility(View.VISIBLE);
            qie.setVisibility(View.VISIBLE);
        } else {
            codeLayout.setVisibility(View.GONE);
            qie.setVisibility(View.INVISIBLE);
            if (sumType == 0) {
                title.setText("支付宝收款");
                type.setText("请使用支付宝扫一扫付款");
                StringBuilder sb = new StringBuilder();
                sb.append("orderNO=").append(orderNO);
                sb.append("&");
                sb.append("muuid=").append(CacheData.getUser(CollectCodeActivity.this, String.valueOf(CacheData.getId(CollectCodeActivity.this))).getMuuid());
                sb.append("&");
                sb.append("money=").append(s);
                sb.append("&");

                if (dis.equals("0.")) {
                    sb.append("discount=").append("0");
                } else {
                    sb.append("discount=").append(dis);
                    if (discountInfo != null && !discountInfo.equals("")) {
                        sb.append("&discountInfo=").append(discountInfo);
                    }
                    if (!vipCode.equals("")) {
                        sb.append("&phone=").append(vipCode);
                    }
                }
                doCodeUrl(sb.toString(), "ali/code/create", 1);
            } else {
                title.setText("微信收款");
                type.setText("请使用微信扫一扫付款");
                StringBuilder sb = new StringBuilder();
                sb.append("orderNO=").append(orderNO);
                sb.append("&");
                sb.append("muuid=").append(CacheData.getUser(CollectCodeActivity.this, String.valueOf(CacheData.getId(CollectCodeActivity.this))).getMuuid());
                sb.append("&");
                sb.append("money=").append(s);
                sb.append("&");
                if (dis.equals("0.")) {
                    sb.append("discount=").append("0");
                } else {
                    sb.append("discount=").append(dis);
                    if (discountInfo != null && !discountInfo.equals("")) {
                        sb.append("&discountInfo=").append(discountInfo);
                    }
                    if (!vipCode.equals("")) {
                        sb.append("&phone=").append(vipCode);
                    }
                }
                doCodeUrl(sb.toString(), "weixin/code/create", 2);
            }
        }
    }

    @OnClick(R.id.qie)
    public void qie() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        show1();
    }

    @OnClick(R.id.collect_code)
    public void code(View view) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        Intent intent = new Intent(CollectCodeActivity.this, ScanQrCodeActivity.class);
        intent.putExtra("type", sumType);
        intent.putExtra("money", s);
        intent.putExtra("dis", dis);
        intent.putExtra("orderID", orderNO);
        intent.putExtra("isisis", getIntent().getBooleanExtra("isisis", false));
        intent.putExtra("discountInfo", discountInfo);
        intent.putExtra("vipCode", vipCode);
        startActivity(intent);
    }

    //CodeUrl
    private void doCodeUrl(final String data, final String url, final int action) {

        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        httpUtil.HttpServer(this, url, data, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                retMap = gson.fromJson(data,
                        new TypeToken<Map<String, String>>() {
                        }.getType());
                if (retMap != null) {
                    Bitmap bitmap = null;
                    try {
                        payNO = retMap.get("payNO");
                        bitmap = EncodingHandler.create2Code(retMap.get("codeURL"), (int) (0.67 * width));
                    } catch (WriterException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Bitmap logoBitmap = null;
                    if (action == 1) {
                        logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.alipay);
                    } else {
                        logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.weixin);
                    }
                    Bitmap barCode = EPayQRCode.addLogo(bitmap, logoBitmap);
                    QRImage.setImageBitmap(barCode);
                    time();
                }
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    public void time() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        timer = new java.util.Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                //每次需要执行的代码放到这里面。
                sum++;
                doCodeUrl();
            }
        };
        //以下是几种调度task的方法：
        // time为Date类型：在指定时间执行一次。
        //较为常用 ,上面的示例就是这个方法 //delay 为long类型：从现在起过delay毫秒执行一次。
        timer.schedule(task, 5000, 5000);
    }

    @Override
    public void onBackPressed() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        super.onBackPressed();
    }

    private void show1() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.buttom_dialog, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        TextView zfb = (TextView) contentView.findViewById(R.id.zfb);
        TextView wx = (TextView) contentView.findViewById(R.id.wx);
        Drawable drawable = getResources().getDrawable(R.drawable.icon_alipay_pay);
        drawable.setBounds(0, 0, (int) (0.067 * width), (int) (0.067 * width));
        zfb.setCompoundDrawables(drawable, null, null, null);
        Drawable drawable1 = getResources().getDrawable(R.drawable.icon_weixin_pay);
        drawable1.setBounds(0, 0, (int) (0.067 * width), (int) (0.067 * width));
        wx.setCompoundDrawables(drawable1, null, null, null);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        WindowManager.LayoutParams lp = bottomDialog.getWindow().getAttributes();
        lp.alpha = 1.0f;
        bottomDialog.getWindow().setAttributes(lp);
        bottomDialog.show();


        sumType = 0;
        title.setText("支付宝收款");
        type.setText("请使用支付宝扫一扫付款");
        StringBuilder sb = new StringBuilder();
        sb.append("orderNO=").append(orderNO);
        sb.append("&");
        sb.append("muuid=").append(CacheData.getUser(CollectCodeActivity.this, String.valueOf(CacheData.getId(CollectCodeActivity.this))).getMuuid());
        sb.append("&");
        sb.append("money=").append(s);
        sb.append("&");
        if (dis.equals("0.")) {
            sb.append("discount=").append("0");
        } else {
            sb.append("discount=").append(dis);
        }
        doCodeUrl(sb.toString(), "ali/code/create", 1);


        zfb.setOnClickListener(view -> {
            bottomDialog.dismiss();
            sumType = 0;
            title.setText("支付宝收款");
            type.setText("请使用支付宝扫一扫付款");
            StringBuilder sb1 = new StringBuilder();
            sb1.append("orderNO=").append(orderNO);
            sb1.append("&");
            sb1.append("muuid=").append(CacheData.getUser(CollectCodeActivity.this, String.valueOf(CacheData.getId(CollectCodeActivity.this))).getMuuid());
            sb1.append("&");
            sb1.append("money=").append(s);
            sb1.append("&");
            if (dis.equals("0.")) {
                sb1.append("discount=").append("0");
            } else {
                sb1.append("discount=").append(dis);
            }
            doCodeUrl(sb1.toString(), "ali/code/create", 1);
        });
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
                sumType = 1;
                title.setText("微信收款");
                type.setText("请使用微信扫一扫付款");
                StringBuilder sb = new StringBuilder();
                sb.append("orderNO=").append(orderNO);
                sb.append("&");
                sb.append("muuid=").append(CacheData.getUser(CollectCodeActivity.this, String.valueOf(CacheData.getId(CollectCodeActivity.this))).getMuuid());
                sb.append("&");
                sb.append("money=").append(s);
                sb.append("&");
                if (dis.equals("0.")) {
                    sb.append("discount=").append("0");
                } else {
                    sb.append("discount=").append(dis);
                }
                doCodeUrl(sb.toString(), "weixin/code/create", 2);
            }
        });
    }

    //CodeUrl
    private void doCodeUrl() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        httpUtil.HttpServer(this, "{\"payNO\":\"" + payNO + "\"}", 98, false, new HttpCallBack() {
            @Override
            public void back(String data) {
                if (timer != null) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }
                bean = gson.fromJson(data, ScanQrBean.class);
                if (bean.getPayStatus() == 1) {
                    handler.sendEmptyMessage(0);
                } else {
                    if (sum == 9) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CollectCodeActivity.this);
                        builder.setMessage("继续等待吗？？？");
                        builder.setPositiveButton("继续等待", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                sum = 0;
                                time();
                            }
                        });
                        builder.setNegativeButton("不等了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                // startActivity(CollectCodeActivity.this, OrderPayActivity2.class);
                                if (getIntent().getBooleanExtra("isisis", false)) {
                                    startActivity(CollectCodeActivity.this, MainActivity.class);
                                } else {
                                    if (getIntent().getIntExtra("payType", 0) == 1) {
                                        getGift();
                                    } else {
                                        setResult(1111);
                                    }
                                }
                                finish();
                            }
                        });
                        builder.show();
                    } else {
                        if (bean.getPayStatus() == 5) {
                            showMessage("收款失败");
                        } else {
                            time();
                        }
                    }
                }
            }

            @Override
            public void fail(String Message, int code, String data) {
                if (timer != null) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }
                showMessage(Message);

            }
        });
    }

    public void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        MobclickAgent.onPageStart("收款码"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        MobclickAgent.onPageEnd("收款码"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    private void getGift() {
        httpUtil.HttpServer(this, "{\"orderNO\":\"" + orderNO + "\"}", 62, false, new HttpCallBack() {
            @Override
            public void back(String data) {
                MembersGiftListBean membersGiftListBean = gson.fromJson(data, MembersGiftListBean.class);
                if (membersGiftListBean.getCount() > 0) {
                    CacheData.cacheMemberGiftList(CollectCodeActivity.this, data);
                    if (vipCode.equals("")) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CollectCodeActivity.this);
                        builder.setMessage("顾客是否是会员");
                        builder.setPositiveButton("不是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent=new Intent(CollectCodeActivity.this, AddMembershipActivity.class);
                                intent.putExtra("orderID",orderNO);
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent=new Intent(CollectCodeActivity.this, MembershipCertificationActivity.class);
                                intent.putExtra("orderID",orderNO);
                                intent.putExtra("gift",false);
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    } else {
                        Intent intent=new Intent(CollectCodeActivity.this, ShopActivtyActivity.class);
                        intent.putExtra("orderNO",orderNO);
                        intent.putExtra("vipCode",vipCode);
                        startActivity(intent);

                    }
                } else {
                    startActivity(CollectCodeActivity.this, DeskManageActivity.class);

                }
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }
}
