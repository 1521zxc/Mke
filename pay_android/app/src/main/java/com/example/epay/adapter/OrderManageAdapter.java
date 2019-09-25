package com.example.epay.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.epay.R;
import com.example.epay.base.BaseRecyclerAdapter;
import com.example.epay.base.MyViewHolder;
import com.example.epay.bean.OrderInfoBean;
import com.example.epay.util.DateUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/13.
 */

public class OrderManageAdapter extends BaseRecyclerAdapter<OrderInfoBean> {
    ArrayList<OrderInfoBean> list;
    public OrderManageAdapter(ArrayList<OrderInfoBean> list, Activity context, int mLayoutId) {
        super(list, context, mLayoutId);
        this.list=list;
    }

    @Override
    public void coner(MyViewHolder holder, OrderInfoBean bean, int position) {
        if(bean.getServiceStatus()==0||bean.getServiceStatus()==-1||bean.getServiceStatus()==1) {
            if (bean.getPayStatus() == 0) {
                loadGuide(R.drawable.not_pay, holder.<ImageView>find(R.id.item_order_manage_status));
            } else if (bean.getPayStatus() == 1) {
                loadGuide(R.drawable.pay_ok, holder.<ImageView>find(R.id.item_order_manage_status));
            } else if (bean.getPayStatus() == 2) {
                loadGuide(R.drawable.pay_ok, holder.<ImageView>find(R.id.item_order_manage_status));
            } else if (bean.getPayStatus() == 3) {
                loadGuide(R.drawable.pay_ok, holder.<ImageView>find(R.id.item_order_manage_status));
            } else if (bean.getPayStatus() == 4) {
                loadGuide(R.drawable.pay_ok, holder.<ImageView>find(R.id.item_order_manage_status));
            } else if (bean.getPayStatus() == 5) {
                loadGuide(R.drawable.not_pay, holder.<ImageView>find(R.id.item_order_manage_status));
            } else if (bean.getPayStatus() == 6) {
                loadGuide(R.drawable.not_pay, holder.<ImageView>find(R.id.item_order_manage_status));
            } else if (bean.getPayStatus() == 7) {
                loadGuide(R.drawable.pay_ok, holder.<ImageView>find(R.id.item_order_manage_status));
            } else if (bean.getPayStatus() == 8) {
                loadGuide(R.drawable.refund_fail, holder.<ImageView>find(R.id.item_order_manage_status));
            } else if (bean.getPayStatus() == 9) {
                loadGuide(R.drawable.refund_ok, holder.<ImageView>find(R.id.item_order_manage_status));
            } else if (bean.getPayStatus() == 10) {
                loadGuide(R.drawable.order_statuschange_icon, holder.<ImageView>find(R.id.item_order_manage_status));
            } else {
                loadGuide(R.drawable.not_pay, holder.<ImageView>find(R.id.item_order_manage_status));
            }
        }else{
            loadGuide(R.drawable.order_calcen_icon, holder.<ImageView>find(R.id.item_order_manage_status));
        }
        if(bean.getDeskName().equals("")){
            holder.<TextView>find(R.id.item_order_manage_desk).setText(String.valueOf(bean.getOrdinal()));
            holder.<TextView>find(R.id.item_order_manage_desk1).setText("订单序号：");
        }else{
            holder.<TextView>find(R.id.item_order_manage_desk).setText(bean.getDeskName());
            holder.<TextView>find(R.id.item_order_manage_desk1).setText("桌台号：");
        }

        holder.<TextView>find(R.id.item_order_manage_time).setText("时间："+ DateUtil.format2(bean.getUpdateTime(),"HH:mm:ss"));
        holder.<TextView>find(R.id.item_order_manage_money).setText("金额："+bean.getPrimeMoney());
        holder.<TextView>find(R.id.item_order_manage_order_num).setText("订单号："+bean.getOrderNO());
        if(position==0) {
            holder.<TextView>find(R.id.item_order_manage_data).setVisibility(View.VISIBLE);
            holder.<TextView>find(R.id.item_order_manage_data).setText(DateUtil.format2(bean.getUpdateTime(), "yyyy-MM-dd"));
        }else{
            if(DateUtil.format2(bean.getUpdateTime(), "yyyy-MM-dd").equals(DateUtil.format2(list.get(position-1).getUpdateTime(), "yyyy-MM-dd"))){
                holder.<TextView>find(R.id.item_order_manage_data).setVisibility(View.GONE);
            }else{
                holder.<TextView>find(R.id.item_order_manage_data).setVisibility(View.VISIBLE);
                holder.<TextView>find(R.id.item_order_manage_data).setText(DateUtil.format2(bean.getUpdateTime(), "yyyy-MM-dd"));
            }
        }
    }
}

