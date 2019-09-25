package com.example.epay.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


import com.example.epay.BaseApplication;
import com.example.epay.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 验证码读秒
 * Created by liujin on 2015/5/22.
 */
@SuppressLint("AppCompatCustomView")
public class TimeButton extends TextView implements View.OnClickListener {
    //倒计时时间，这里给了默认60秒
    private long lenght = 60 * 1000;
    //默认的文字
    private String textafter = "获取验证码";
    private int colorafter = getResources().getColor(R.color.appHeaderColor);
    //点击后的文字
    private String textafter2 = "发送中";
    //倒计时的文字
    private String text = ")秒后获取";
    private int colorText = getResources().getColor(R.color.textColor_true);
    //倒计时之后的文字
    private String textbefore = "获取验证码";

    private boolean isStart = false;

    private final String TIME = "time";
    private final String CTIME = "ctime";
    private OnClickListener mOnclickListener;

    private Timer t;
    private TimerTask tt;
    private long time;
    Map<String, Long> map = new HashMap<String, Long>();

    public TimeButton(Context context) {
        super(context);
        this.setText(textafter);
        TimeButton.this.setTextColor(colorafter);
        setOnClickListener(this);
    }

    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setText(textafter);
        TimeButton.this.setTextColor(colorafter);
        setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (isStart) {
                if (colorText != 0) {
                    TimeButton.this.setTextColor(colorText);
                }
                TimeButton.this.setText("(" + time / 1000 + text);
                ;
                time -= 1000;
                if (time < 0) {
                    TimeButton.this.setEnabled(true);
                    TimeButton.this.setTextColor(colorafter);
                    TimeButton.this.setText(textbefore);
                    clearTimer();
                }
            }
        }
    };

    private void initTimer() {
        time = lenght;
        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
                han.sendEmptyMessage(0x01);
            }
        };
    }

    private void clearTimer() {
        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l instanceof TimeButton) {
            super.setOnClickListener(l);
        } else
            this.mOnclickListener = l;
    }

    @Override
    public void onClick(View v) {
        if (mOnclickListener != null)
            mOnclickListener.onClick(v);
        //       initTimer();
//        this.setText(textafter2);
//        this.setEnabled(false);
//        t.schedule(tt, 0, 1000);
    }

    public TimeButton setOnCli(boolean f) {
        isStart = f;
        if (f) {
            initTimer();
            this.setText("(" + time / 1000 + text);
            if (colorText != 0) {
                TimeButton.this.setTextColor(colorText);
            }
            this.setEnabled(false);
            t.schedule(tt, 0, 1000);
        } else {
            this.setText(textafter);
            TimeButton.this.setTextColor(colorafter);
            this.setEnabled(true);
        }
        return this;
    }

    /**
     * 和activity的onDestroy()方法同步
     */
    public void onDestroy() {
        if (BaseApplication.map == null)
            BaseApplication.map = new HashMap<String, Long>();
        BaseApplication.map.put(TIME, time);
        BaseApplication.map.put(CTIME, System.currentTimeMillis());
        clearTimer();
    }

    /**
     * 和activity的onCreate()方法同步
     */
    public void onCreate(Bundle bundle) {
        if (BaseApplication.map == null)
            return;
        if (BaseApplication.map.size() <= 0)// 这里表示没有上次未完成的计时
            return;
        long time = System.currentTimeMillis() - BaseApplication.map.get(CTIME)
                - BaseApplication.map.get(TIME);
        BaseApplication.map.clear();
        if (time > 0)
            return;
        else {
            initTimer();
            this.time = Math.abs(time);
            t.schedule(tt, 0, 1000);
            if (colorText != 0) {
                TimeButton.this.setTextColor(colorText);
            }
            this.setText(textafter);
            TimeButton.this.setTextColor(colorafter);
            this.setEnabled(false);
        }
    }

    /**
     * 设置到计时长度
     *
     * @param lenght 时间 默认毫秒
     * @return
     */
    public TimeButton setLenght(long lenght) {
        this.lenght = lenght;
        return this;
    }
}