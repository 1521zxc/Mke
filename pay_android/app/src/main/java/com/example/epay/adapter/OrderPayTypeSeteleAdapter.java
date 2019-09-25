package com.example.epay.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.OrderPayTypeBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/12/20.
 */

public class OrderPayTypeSeteleAdapter extends TBaseAdapter<OrderPayTypeBean>{
    public OrderPayTypeSeteleAdapter(Activity context, ArrayList<OrderPayTypeBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_order_pay_type_setele;
    }

    @Override
    public void initItemView(PxViewHolder view, ArrayList<OrderPayTypeBean> list, int position) {
            view.<TextView>find(R.id.name).setText(list.get(position).getName());
    }
}
