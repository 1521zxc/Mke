package com.example.epay.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;

/**
 * Created by liujin on 2018/3/8.
 */

public class EPayProgressDialog {
    /**
     * 得到自定义的progressDialog
     * @param context
     * @return
     */
    public static Dialog createLoadingDialog(Activity context) {
        int width = context.getWindowManager().getDefaultDisplay().getWidth();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.epay_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.epay_dialog_layout);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.loadingIv);
        final AnimationDrawable mAnimation = (AnimationDrawable) spaceshipImage.getBackground();
        spaceshipImage.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams((int) (0.3 * width),
                (int) (0.3 * width)));// 设置布局
        return loadingDialog;
    }



}
