package com.example.epay.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
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
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.CashDetailBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.view.PayPwdEditText;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class RefundActivity extends BaseActivity {

    private String datas;
    CashDetailBean cashDetailBean;

    @Bind(R.id.refund_money)
    EditText moneyText;
    @Bind(R.id.refund_money_all)
    TextView AllmoneyText;
    @Bind(R.id.refund_yy)
    EditText yyText;

    Dialog walletDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
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
        cashDetailBean = (CashDetailBean) getIntent().getSerializableExtra("cash");
        moneyText.setText(cashDetailBean.getPaidMoney()+ "");
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

    @OnClick(R.id.refund_tui)
    public void tui() {
        showEditPayPwdDialog();

    }

    @OnClick(R.id.refund_layout)
    public void refundLayout()
    {
        //关闭输入框
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    //登录
    private void dotui(final String data) {
        httpUtil.HttpServer(this, "refund/apply", data,true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                startActivity(RefundActivity.this, CashflowActivity.class);
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
                //可在此执行下一步操作
                StringBuilder sb = new StringBuilder();
                sb.append("payNO=").append(cashDetailBean.getPayNO());
                sb.append("&");
                sb.append("muuid=").append(CacheData.getUser(RefundActivity.this, String.valueOf(CacheData.getId(RefundActivity.this))).getMuuid());
                sb.append("&");
                sb.append("refundAmt=").append(moneyText.getText().toString());
                sb.append("&");
                sb.append("reason=").append(yyText.getText().toString());
                dotui(sb.toString());
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage(Message);
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("退款"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("退款"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
