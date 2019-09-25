package com.example.epay.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.bean.OrderPayZheBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/10/8.
 */

public class OrderPaySelectorView extends Dialog {

    /**
     * 背景布局
     */
    private LinearLayout backView;

    private Context context;
    final int[] rdued = {0};
    /**
     * 根布局
     */
    private View parent;
    /**
     * 监听事件
     */
    private StringListener listener;


    private ArrayList<OrderPayZheBean.OrderPayZhe>  rfudes=new ArrayList<>();




    /**
     * DistrictSelectorView 初始化方法
     * @param context 上下文
     */
    public OrderPaySelectorView(Context context, ArrayList<OrderPayZheBean.OrderPayZhe> list) {
        super(context, R.style.Dialog);
       // super(context);
        this.context=context;
        this.rfudes=list;
        init();
    }


    /**
     * DistrictSelectorView 初始化方法 可自定义Style
     * @param context       上下文
     * @param themeResId    styleId
     */
    public OrderPaySelectorView(Context context, int themeResId) {
        super(context, themeResId);
        this.context=context;
        init();
    }

    public interface StringListener {
        void StringClick(int index);
    }

    public void setStringListener(StringListener listener) {
        this.listener = listener;
    }

    /**
     * 初始化布局
     */
    private void init(){

        /**初始化根布局*/
        parent =  getLayoutInflater().inflate(R.layout.order_pay_type_dialog,null);

        /**初始化外层布局*/
        backView = (LinearLayout) parent.findViewById(R.id.link_dialog_layout);
        backView.setBackgroundColor(context.getResources().getColor(R.color.textColor_white));
        /**填充布局到Dialog*/
        this.setContentView(parent);

        TextView localButton1 = (TextView)parent.findViewById(R.id.ofcuess);
        TextView localButton2 = (TextView)parent.findViewById(R.id.dismiss);
        ScrollerOrderPayPicker picker = (ScrollerOrderPayPicker)parent.findViewById(R.id.scroll_number);
        if(rfudes.size()>1)
            rdued[0] =1;
        picker.setData(rfudes);

        picker.setOnSelectListener(new ScrollerOrderPayPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                rdued[0] =id;
            }

            @Override
            public void selecting(int id, String text) {
                rdued[0] =id;
            }
        });
        localButton2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                dismiss();
            }
        });
        localButton1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                dismiss();
               listener.StringClick(rdued[0]);
            }
        });
        /**设置点击Dialog外的界面无法取消*/
        setCanceledOnTouchOutside(true);

        /**点击返回键可以退出*/
        setCancelable(true);
    }



    /**
     * 返回
     */
    private void back(){

    }

    /**
     * 销毁
     */
    @Override
    public void dismiss() {
        ScaleAnimation sAnima = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );//横向放大5倍，纵向放大5倍
        sAnima.setDuration(500);
        sAnima.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                OrderPaySelectorView.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        parent.startAnimation(sAnima);
    }
}