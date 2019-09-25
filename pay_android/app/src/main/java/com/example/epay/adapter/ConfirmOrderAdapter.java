package com.example.epay.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.DeskBean;
import com.example.epay.bean.OrderInfoBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class ConfirmOrderAdapter extends TBaseAdapter<OrderInfoBean.ProductSimple> {
    public ConfirmOrderAdapter(Activity context, ArrayList<OrderInfoBean.ProductSimple> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_confirm_add_list;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<OrderInfoBean.ProductSimple> list, final int position) {
        view.<TextView>find(R.id.name).setText(list.get(position).getName());
        view.<TextView>find(R.id.count).setText("x"+list.get(position).getCount());
    }
}