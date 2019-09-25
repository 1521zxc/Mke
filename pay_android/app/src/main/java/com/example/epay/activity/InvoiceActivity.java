package com.example.epay.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.epay.QRCode.zxing.encode.EncodingHandler;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.util.SDCardUtil;
import com.google.zxing.WriterException;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class InvoiceActivity extends BaseActivity {
    @Bind(R.id.invoice_code_img)
    ImageView img;
    @Bind(R.id.invoice_code_layout)
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        Bitmap bitmap = null;
        try {
            bitmap = EncodingHandler.create2Code("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxeb6e671f5571abce&redirect_uri=https%3A%2F%2Fo2.qfpay.com%2Ftrade%2Fv1%2Fcustomer%2Fget%3Fredirect_uri%3Dhttps%253A%252F%252Fmarketing.qfpay.com%252Fpaydone%252Fbillcode-page.html%253Fuserid%253DpdbrL&response_type=code&scope=snsapi_base&state=&connect_redirect=1#wechat_redirect",(int)(0.5*width));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        img.setImageBitmap(bitmap);
    }
    @OnClick(R.id.invoice_cun)
    public void cun(View view)
    {
        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(layout.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        layout.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        if(!SDCardUtil.saveBitmap(InvoiceActivity.this,bitmap).isEmpty())
        {
            showMessage("保存成功");
        }else{
            showMessage("没保存成功");
        }
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("发票打印码"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("发票打印码"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}
