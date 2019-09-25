package com.example.epay.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;

/**
 * Created by liujin on 2018/3/29.
 */

public class TypeUtil {
    private final static int WEIXIN=1;
    private final static int ZFB=2;
    private final static int QQ=3;
    private final static int JD=4;
    private final static int BAIDU=5;
    private final static int merberPay=10;
    private final static int merberChong=11;
    private final static int meiChong=12;
    private final static int eeChong=13;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void load(Context context, int type, TextView view, String text)
    {
        if(type==WEIXIN){
            view.setText(String.format(text,"微信"));
        }
        if(type==ZFB){
            view.setText(String.format(text,"支付宝"));
        }
        if(type==QQ){
            view.setText(String.format(text,"现金"));
        }
        if(type==JD){
            view.setText(String.format(text,"刷卡"));
        }
        if(type==BAIDU){
            view.setText(String.format(text,"其他"));
        }
        if(type==merberPay){
            view.setText(String.format(text,"会员卡"));
        }
        if(type==merberChong){
            view.setText(String.format(text,"会员卡充值"));
        }
        if(type==meiChong){
            view.setText(String.format(text,"美团"));
        }
        if(type==eeChong){
            view.setText(String.format(text,"饿了么"));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void load(Context context, int type, ImageView imageView)
    {
        if(type==WEIXIN){
            imageView.setBackground(context.getResources().getDrawable(R.drawable.weixin));
        }
        if(type==ZFB){
            imageView.setBackground(context.getResources().getDrawable(R.drawable.alipay));
        }
        if(type==QQ){
            imageView.setBackground(context.getResources().getDrawable(R.drawable.qq));
        }
        if(type==JD){
            imageView.setBackground(context.getResources().getDrawable(R.drawable.jd));
        }
        if(type==BAIDU){
            imageView.setBackground(context.getResources().getDrawable(R.drawable.icon));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void load(Context context, int type, TextView view, ImageView imageView, String text)
    {
        if(type==WEIXIN){
           view.setText("微信收款");
           imageView.setBackground(context.getResources().getDrawable(R.drawable.weixin));
        }
        if(type==ZFB){
            view.setText("支付宝收款");
            imageView.setBackground(context.getResources().getDrawable(R.drawable.alipay));
        }
        if(type==QQ){
            view.setText("现金收款");
            imageView.setBackground(context.getResources().getDrawable(R.drawable.qq));
        }
        if(type==JD){
            view.setText("刷卡");
            imageView.setBackground(context.getResources().getDrawable(R.drawable.union));
        }
        if(type==BAIDU){
            view.setText("其他");
            imageView.setBackground(context.getResources().getDrawable(R.drawable.icon));
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void loadRound(Context context, int type, TextView view, ImageView imageView, String text)
    {
        if(type==WEIXIN){
            view.setText(String.format(text,"微信"));
            imageView.setBackground(context.getResources().getDrawable(R.drawable.icon_weixin_pay));
        }
        if(type==ZFB){
            view.setText(String.format(text,"支付宝"));
            imageView.setBackground(context.getResources().getDrawable(R.drawable.icon_alipay_pay));
        }
        if(type==QQ){
            view.setText(String.format(text,"现金收款"));
            imageView.setBackground(context.getResources().getDrawable(R.drawable.qq));
        }
        if(type==JD){
            view.setText(String.format(text,"刷卡"));
            imageView.setBackground(context.getResources().getDrawable(R.drawable.union));
        }
        if(type==BAIDU){
            view.setText(String.format(text,"其他"));
            imageView.setBackground(context.getResources().getDrawable(R.drawable.icon));
        }
    }








    public static String name( int type)
    {
        if(type==WEIXIN){
            return "微信";
        }
        if(type==ZFB){
            return "支付宝";
        }
        if(type==QQ){
            return "现金";
        }
        if(type==JD){
            return "刷卡";
        }
        if(type==BAIDU){
            return "其他";
        }
        if(type==merberPay){
            return "会员卡";
        }
        if(type==merberChong){
            return "会员卡充值";
        }
        if(type==meiChong){
            return "美团";
        }
        if(type==eeChong){
            return "饿了么";
        }
        return "未知";
    }
}
