package com.example.epay.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.QRCode.zxing.encode.EncodingHandler;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.CodeBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.SDCardUtil;
import com.google.zxing.WriterException;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class OrderCodeActivity extends BaseActivity {
    @Bind(R.id.order_code_img)
    ImageView img;
    @Bind(R.id.order_code_type0)
    TextView type0;
    @Bind(R.id.order_code_type1)
    TextView type1;
    @Bind(R.id.order_code_layout)
    LinearLayout layout;
    @Bind(R.id.order_code_desk)
    EditText desk;
    @Bind(R.id.order_code_deskNO)
    TextView deskNo;

    CodeBean codeBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_code);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        Drawable drawable=getResources().getDrawable(R.drawable.alipay);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable.setBounds(0,0,(int)(0.07*width),(int)(0.07*width));
        //drawable   第一个是文字TOP
        type0.setCompoundDrawables(null,drawable,null,null);
        Drawable drawable1=getResources().getDrawable(R.drawable.weixin);
        drawable1.setBounds(0,0,(int)(0.07*width),(int)(0.07*width));
        type1.setCompoundDrawables(null,drawable1,null,null);
        doLogin("{\"token\":\""+CacheData.getToken(this)+"\",\"deskNO\":\"1号桌\"}");
    }

    //退出登录
    private void doLogin(String data1) {
        httpUtil.HttpServer(this, data1, 45, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                codeBean=gson.fromJson(data,CodeBean.class);
                Bitmap bitmap = null;
                try {
                    bitmap = EncodingHandler.create2Code(codeBean.getCodeURL(),(int)(0.5*width));
                } catch (WriterException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                img.setImageBitmap(bitmap);
                deskNo.setText(codeBean.getDeskNO());
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage(Message);
            }
        });
    }





    @OnClick(R.id.order_cun)
    public void cun(View view)
    {
        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(layout.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        layout.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        if(!SDCardUtil.saveBitmap(OrderCodeActivity.this,bitmap).isEmpty())
        {
            showMessage("保存成功");
        }else{
            showMessage("没保存成功");
        }
    }
    @OnClick(R.id.order_code_su)
    public void su()
    {
        doLogin("{\"token\":\""+CacheData.getToken(this)+"\",\"deskNO\":\""+desk.getText().toString()+"\"}");
    }



    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("点餐码"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("点餐码"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
