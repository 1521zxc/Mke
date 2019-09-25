package com.example.epay.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.epay.QRCode.defineview.MyImageView;
import com.example.epay.QRCode.zxing.ScanListener;
import com.example.epay.QRCode.zxing.ScanManager;
import com.example.epay.QRCode.zxing.decode.DecodeThread;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.MembersGiftListBean;
import com.example.epay.bean.ScanQrBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class ScanQrCodeActivity extends BaseActivity {
    @Bind(R.id.rootView)
    RelativeLayout rootView;
    @Bind(R.id.surfaceView)
    SurfaceView surfaceView;
    @Bind(R.id.capture_scan_line)
    ImageView scanLine;
    @Bind(R.id.scan_image)
    MyImageView rlCropView;
    @Bind(R.id.scan_hint)
    TextView textMoney;
    @Bind(R.id.iv_light)
    TextView tv;
    ScanManager scanManager;
    Context context;
    int type = 0;
    String money = "";
    String orderID = "";
    String dis = "";
    ScanQrBean bean;
    java.util.Timer timer;
    String payNO = "";
    int sum = 0;
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
                startActivity(ScanQrCodeActivity.this, MainActivity.class);
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


    String chrageData = "";
    private int perIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    @OnCheckedChanged(R.id.scan_light)
    public void light(CompoundButton compoundButton, boolean b) {
        scanManager.switchLight();
    }

    @OnClick(R.id.iv_light)
    public void qie(View view) {
        if (chrageData.equals("")) {
            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }
            Intent intent = new Intent(ScanQrCodeActivity.this, CollectCodeActivity.class);
            intent.putExtra("dis", dis);
            intent.putExtra("sum", money);
            intent.putExtra("orderID", orderID);
            intent.putExtra("discountInfo", discountInfo);
            intent.putExtra("vipCode", vipCode);
            intent.putExtra("isisis", getIntent().getBooleanExtra("isisis", false));
            startActivity(intent);
        } else {
            showMessage("会员充值不支持被扫");
        }
    }

    @Override
    public void initView() {
        super.initView();
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        type = getIntent().getIntExtra("type", 0);
        chrageData = getIntent().getStringExtra("data");

        if (chrageData == null) {
            money = getIntent().getStringExtra("money");
            orderID = getIntent().getStringExtra("orderID");
            dis = getIntent().getStringExtra("dis");
            discountInfo = getIntent().getStringExtra("discountInfo");
            vipCode = getIntent().getStringExtra("vipCode");
            if (vipCode == null) {
                vipCode = "";
            }
            if (getIntent().getBooleanExtra("isisis", false)) {
                tv.setVisibility(View.VISIBLE);
            } else {
                tv.setVisibility(View.GONE);
            }
        } else {
            money = String.valueOf(getIntent().getDoubleExtra("money", 0));
            tv.setVisibility(View.GONE);
        }
        textMoney.setText("￥" + money + "元");
        Drawable drawable = getResources().getDrawable(R.drawable.ic_gather);
        drawable.setBounds(0, 0, (int) (0.067 * width), (int) (0.067 * width));
        tv.setCompoundDrawables(drawable, null, null, null);
        doScan();
    }

    public void doScan() {
        scanManager = new ScanManager(this, surfaceView, rootView, rlCropView, scanLine, DecodeThread.QRCODE_MODE, new ScanListener() {
            @Override
            public void scanResult(Result rawResult, Bundle bundle) {
                if (type == 0) {
                    if (chrageData == null) {
                        doCodeUrl("ali/micro/create", rawResult.getText());
                    } else {
                        doCodeUrl("ali/vip/micro/create", rawResult.getText());
                    }
                } else {
                    if (chrageData == null) {
                        doCodeUrl("weixin/micro/create", rawResult.getText());
                    } else {
                        doCodeUrl("weixin/vip/micro/create", rawResult.getText());
                    }
                }

            }

            @Override
            public void scanError(Exception e) {
                showMessage(e.getMessage());
            }
        });
    }

    //CodeUrl
    String message1 = "";

    private void doCodeUrl(final String url, final String authCode) {
        if (chrageData == null) {

            final StringBuilder sb = new StringBuilder();
            sb.append("orderNO=").append(orderID);
            sb.append("&");
            sb.append("money=").append(money);
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
            sb.append("&");
            sb.append("muuid=").append(CacheData.getUser(context, String.valueOf(CacheData.getId(context))).getMuuid());
            if (authCode.equals("") || authCode == null) {
            } else {
                sb.append("&");
                sb.append("authCode=").append(authCode);
            }
            httpUtil.HttpServer(this, url, sb.toString(), true, new HttpCallBack() {
                @Override
                public void back(String data) {
                    if (timer != null) {
                        timer.cancel();
                        timer.purge();
                        timer = null;
                    }
                    if (getIntent().getBooleanExtra("isisis", false)) {
                        startActivity(ScanQrCodeActivity.this, MainActivity.class);
                    } else {
                        if (getIntent().getIntExtra("payType", 0) == 1) {
                            getGift();
                        } else {
                            setResult(1212);
                        }
                    }
                    finish();
                }

                @Override
                public void fail(String Message, int code, String data) {
                    if (code == 2) {
                        Map<String, String> retMap = gson.fromJson(data,
                                new TypeToken<Map<String, String>>() {
                                }.getType());
                        if (retMap != null) {
                            payNO = retMap.get("payNO");
                            time();
                        }
                    } else {
                        showMessage(message1);
                    }
                }
            });
        } else {
            chrageData = chrageData + "&authCode=" + authCode;
            httpUtil.HttpServer(this, url, chrageData, true, new HttpCallBack() {
                @Override
                public void back(String data2) {
                    startActivity(ScanQrCodeActivity.this, MainActivity.class);
                    finish();
                }

                @Override
                public void fail(String Message, int code, String data) {
                    if (code == 2) {
                        Map<String, String> retMap = gson.fromJson(data,
                                new TypeToken<Map<String, String>>() {
                                }.getType());
                        if (retMap != null) {
                            payNO = retMap.get("payNO");
                            time();
                        }
                    } else {
                        showMessage(message1);
                    }
                }
            });
        }
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


    //CodeUrl
    private void doCodeUrl() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        String payNoString = "";
        if (chrageData == null) {
            payNoString = "{\"payNO\":\"" + payNO + "\"}";
        } else {
            payNoString = "{\"payNO\":\"" + payNO + "\",\"vip\":1}";
        }
        httpUtil.HttpServer(this, payNoString, 98, false, new HttpCallBack() {
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
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ScanQrCodeActivity.this);
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
                                // startActivity(ScanQrCodeActivity.this, OrderPayActivity2.class);
                                if (getIntent().getBooleanExtra("isisis", false)) {
                                    startActivity(ScanQrCodeActivity.this, MainActivity.class);
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

    @Override
    public void onBackPressed() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        super.onBackPressed();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (scanManager != null) {
            scanManager.onResume();
        }

        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        MobclickAgent.onPageStart("扫码收款"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanManager.onPause();
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        MobclickAgent.onPageEnd("扫码收款"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    private void getGift() {
        httpUtil.HttpServer(this, "{\"orderNO\":\"" + orderID + "\"}", 62, false, new HttpCallBack() {
            @Override
            public void back(String data) {
                MembersGiftListBean membersGiftListBean = gson.fromJson(data, MembersGiftListBean.class);
                if (membersGiftListBean.getCount() > 0) {
                    CacheData.cacheMemberGiftList(ScanQrCodeActivity.this, data);
                    if (vipCode.equals("")) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ScanQrCodeActivity.this);
                        builder.setMessage("顾客是否是会员");
                        builder.setPositiveButton("不是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent=new Intent(ScanQrCodeActivity.this, AddMembershipActivity.class);
                                intent.putExtra("orderID",orderID);
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent=new Intent(ScanQrCodeActivity.this, MembershipCertificationActivity.class);
                                intent.putExtra("orderID",orderID);
                                intent.putExtra("gift",false);
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    } else {
                        Intent intent=new Intent(ScanQrCodeActivity.this, ShopActivtyActivity.class);
                        intent.putExtra("orderNO",orderID);
                        intent.putExtra("vipCode",vipCode);
                        startActivity(intent);

                    }
                } else {
                    startActivity(ScanQrCodeActivity.this, DeskManageActivity.class);

                }
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }
}
