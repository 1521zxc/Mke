package com.example.epay.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.OrderInfoBean;
import com.example.epay.bean.PayNoBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.view.PayPwdEditText;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class OrderRefundActivity extends BaseActivity {
    private String datas;
    OrderInfoBean cashDetailBean;

    @Bind(R.id.refund_money)
    EditText moneyText;
    @Bind(R.id.refund_yy)
    EditText yyText;
    @Bind(R.id.refund_money_all)
    TextView AllmoneyText;





    @Bind(R.id.refund_type1)
    TextView type1;
    @Bind(R.id.refund_type2)
    TextView type2;
    @Bind(R.id.refund_layout_type1)
    LinearLayout typeLayout1;
    @Bind(R.id.refund_layout_type2)
    LinearLayout typeLayout2;
    @Bind(R.id.refund_tui1)
    TextView typetui1;
    @Bind(R.id.refund_tui)
    TextView typetui2;



    Dialog walletDialog;
    ArrayList<PayNoBean> trueList=new ArrayList<>();
    int trueIndex=0;
    double money=0;

    boolean isJie=false;
    DecimalFormat df = new DecimalFormat("###0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_refund);
        ButterKnife.bind(this);
        initView();
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //1、获取main在窗体的可视区域
                decorView.getWindowVisibleDisplayFrame(rect);
                //2、获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mainInvisibleHeight = decorView.getRootView().getHeight() - rect.bottom;
                int screenHeight = decorView.getRootView().getHeight();//屏幕高度
                //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
                if (mainInvisibleHeight > screenHeight / 4) {   //软键盘显示

                } else {                      //软键盘隐藏
                    if (walletDialog != null) {        //在软键盘隐藏时，关闭Dialog。
                        walletDialog.dismiss();
                    }
                }
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        cashDetailBean = (OrderInfoBean) getIntent().getSerializableExtra("cash");
        trueList=(ArrayList<PayNoBean>) getIntent().getSerializableExtra("payNo");
        moneyText.setText(cashDetailBean.getPaidMoney() + "");
        AllmoneyText.setText("最大可退款金额" + cashDetailBean.getPaidMoney());
    }

    //将此方法放在按钮的点击事件中即可弹出输入支付密码页面
    private void showEditPayPwdDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_et_paypwd, null);
        walletDialog = new Dialog(this, R.style.walletFrameWindowStyle);
        walletDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        final Window window = walletDialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        //紧贴软键盘弹出
        wl.gravity = Gravity.BOTTOM;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        walletDialog.onWindowAttributesChanged(wl);
        walletDialog.setCanceledOnTouchOutside(false);
        walletDialog.show();
        final PayPwdEditText ppet = (PayPwdEditText) view.findViewById(R.id.dialog_ppet);
        //调用initStyle方法创建你需要设置的样式
        ppet.initStyle(R.drawable.edit_num_bg, 6, 0.33f, R.color.textColor_grey2, R.color.textColor_grey2, 30);
        ppet.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {//密码输入完后的回调
                //手动收起软键盘
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ppet.getWindowToken(), 0);
                //支付密码输入框消失
                walletDialog.dismiss();
                JSONObject object = new JSONObject();
                object.put("refundPwd", str);
                if(isJie)
                {
                    object.put("reason", "退款反结账");
                }else {
                    if (yyText.getText().toString() == null || yyText.getText().toString().equals("")) {
                        object.put("reason", "退菜");
                    } else {
                        object.put("reason", yyText.getText().toString());
                    }
                }
                object.put("orderNO", cashDetailBean.getPayNO());
                doPass(object.toString());

            }
        });
        //延迟弹起软键盘，使PayPwdEditText获取焦点
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ppet.setFocus();
            }
        }, 100);
    }
    @OnClick(R.id.order_refund_layout)
    public void refundLayout()
    {

        //关闭输入框
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
    @OnClick(R.id.refund_tui)
    public void tui() {
        if(typetui2.getText().toString().equals("确定")) {
            if(cashDetailBean.getPaidMoney()>=Double.parseDouble(moneyText.getText().toString())) {
                isJie = false;
                showEditPayPwdDialog();
            }else{
                showMessage("退款金额大于所支付金额，不能退款");
            }
        }else{
            android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
            builder.setMessage("确定退款反结算吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    moneyText.setText(cashDetailBean.getPaidMoney() + "");
                    isJie=true;
                    showEditPayPwdDialog();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }

    }


    @OnClick(R.id.refund_tui1)
    public void tui1() {
        if(typetui1.getText().toString().equals("")) {

        }else{
            android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
            builder.setMessage("确定强制反结算吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    JSONObject object=new JSONObject();
                    object.put("orderNO",cashDetailBean.getOrderNO());
                    object.put("force",1);
                    doFjie(object.toString());
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }

    }

    //登录
    private void dotui(final String data,final boolean isagin) {
        httpUtil.HttpServer(this, "refund/apply", data,true, new HttpCallBack() {
            @Override
            public void back(String data) {
                if(isagin){
                    money= money-trueList.get(trueIndex).getPayMoney();
                    if(trueIndex+1<trueList.size())
                    {
                        trueIndex++;
                        if(money>trueList.get(trueIndex).getPayMoney())
                        {
                            StringBuilder sb = new StringBuilder();
                            sb.append("payNO=").append(trueList.get(trueIndex).getPayNO());
                            sb.append("&");
                            sb.append("muuid=").append(CacheData.getUser(OrderRefundActivity.this, String.valueOf(CacheData.getId(OrderRefundActivity.this))).getMuuid());
                            sb.append("&");
                            sb.append("refundAmt=").append(trueList.get(trueIndex).getPayMoney());
                            sb.append("&");
                            if(isJie)
                            {
                                sb.append("reason=").append("退款反结账");
                            }else {
                                if (yyText.getText().toString() == null || yyText.getText().toString().equals("")) {
                                    sb.append("reason=").append("退菜");
                                } else {
                                    sb.append("reason=").append(yyText.getText().toString());
                                }
                            }

                            dotui(sb.toString(),true);
                        }else{
                            StringBuilder sb = new StringBuilder();
                            sb.append("payNO=").append(trueList.get(trueIndex).getPayNO());
                            sb.append("&");
                            sb.append("muuid=").append(CacheData.getUser(OrderRefundActivity.this, String.valueOf(CacheData.getId(OrderRefundActivity.this))).getMuuid());
                            sb.append("&");
                            sb.append("refundAmt=").append(money);
                            sb.append("&");
                            if(isJie)
                            {
                                sb.append("reason=").append("退款反结账");
                            }else {
                                if (yyText.getText().toString() == null || yyText.getText().toString().equals("")) {
                                    sb.append("reason=").append("退菜");
                                } else {
                                    sb.append("reason=").append(yyText.getText().toString());
                                }
                                sb.append("&");
                                sb.append("discount=").append(getIntent().getDoubleExtra("discount",0));
                            }
                            dotui(sb.toString(),false);
                        }
                    }
                }else {
                    if(isJie)
                    {
                        JSONObject object=new JSONObject();
                        object.put("orderNO",cashDetailBean.getOrderNO());
                        object.put("force",0);
                        doFjie(object.toString());
                    }else {
                        startActivity(OrderRefundActivity.this, DeskManageActivity.class);
                        finish();
                    }
                }
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage(Message);
            }
        });
    }


    //登录
    private void doPass(final String data) {
        httpUtil.HttpServer(this, data, 29, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                money=Double.parseDouble(moneyText.getText().toString());

                if(money>trueList.get(trueIndex).getPayMoney())
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append("payNO=").append(trueList.get(trueIndex).getPayNO());
                    sb.append("&");
                    sb.append("muuid=").append(CacheData.getUser(OrderRefundActivity.this, String.valueOf(CacheData.getId(OrderRefundActivity.this))).getMuuid());
                    sb.append("&");
                    sb.append("refundAmt=").append(trueList.get(trueIndex).getPayMoney());
                    sb.append("&");
                    if(isJie)
                    {
                        sb.append("reason=").append("退款反结账");
                    }else {
                        if (yyText.getText().toString() == null || yyText.getText().toString().equals("")) {
                            sb.append("reason=").append("退菜");
                        } else {
                            sb.append("reason=").append(yyText.getText().toString());
                        }
                    }
                    dotui(sb.toString(),true);
                }else{
                    StringBuilder sb = new StringBuilder();
                    sb.append("payNO=").append(trueList.get(trueIndex).getPayNO());
                    sb.append("&");
                    sb.append("muuid=").append(CacheData.getUser(OrderRefundActivity.this, String.valueOf(CacheData.getId(OrderRefundActivity.this))).getMuuid());
                    sb.append("&");
                    sb.append("refundAmt=").append(money);
                    sb.append("&");
                    if(isJie)
                    {
                        sb.append("reason=").append("退款反结账");
                    }else {
                        if (yyText.getText().toString() == null || yyText.getText().toString().equals("")) {
                            sb.append("reason=").append("退菜");
                        } else {
                            sb.append("reason=").append(yyText.getText().toString());
                        }
                        sb.append("&");
                        sb.append("discount=").append(getIntent().getDoubleExtra("discount",0));
                    }
                    dotui(sb.toString(),false);
                }
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage(Message);
            }
        });
    }





    //登录
    private void doFjie(final String data) {
        httpUtil.HttpServer(this, data, 73, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                isJie=false;
                startActivity(OrderRefundActivity.this, DeskManageActivity.class);
                finish();
            }

            @Override
            public void fail(String Message,int code,String data) {
                isJie=false;
                showMessage(Message);
            }
        });
    }


    @OnClick(R.id.refund_type1)
    public void type1()
    {
        if(type2.getVisibility()==View.GONE)
        {
            type2.setVisibility(View.VISIBLE);
        }else{
            type2.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.refund_type2)
    public void type2()
    {
        if(type2.getText().toString().equals("反结算"))
        {
            type2.setVisibility(View.GONE);
            type2.setText("退款");
            type1.setText("反结算");
            typetui1.setText("强制反结账");
            typetui2.setText("退款反结账");
            typeLayout1.setVisibility(View.GONE);
        }else{
            type2.setVisibility(View.GONE);
            type2.setText("反结算");
            type1.setText("退款");
            typetui1.setText("");
            typetui2.setText("确定");
            typeLayout1.setVisibility(View.VISIBLE);
        }
    }








    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("订单退款"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("订单退款"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}