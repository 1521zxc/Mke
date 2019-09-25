package com.example.epay.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.example.epay.R;

/**
 * Created by liujin on 2018/3/5.
 */

public class EPayDialog {
    Dialog dialog;
    View dialogView;

    public EPayDialog(Context con,int layoutId) {
        dialogView = View.inflate(con,layoutId, null);
        dialog = new Dialog(con, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);

    }

    public View getView()
    {
        return dialogView;
    }


    public void setCanceledOnTouchOutside(boolean boo){
        dialog.setCanceledOnTouchOutside(boo);

    }

    public void setBackDismiss(boolean isDismiss){
        if(isDismiss==false)
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return true;
                }
            });
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
//            showAnimator();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(dialogView, "scaleX", 0.3f, 1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(dialogView, "scaleY", 0.3f, 1f);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(dialogView, "alpha", 0.3f, 1f);
        animatorSet.playTogether(animatorX, animatorY, animatorAlpha);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }

}
