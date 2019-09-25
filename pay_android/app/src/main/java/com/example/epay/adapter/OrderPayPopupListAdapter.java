package com.example.epay.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.DeskBean;
import com.example.epay.bean.OrderInfoBean;
import com.example.epay.bean.TransferBean;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class OrderPayPopupListAdapter extends TBaseAdapter<DeskBean> {
    public OrderPayPopupListAdapter(Activity context, ArrayList<DeskBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_order_pay_desk_h;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<DeskBean> list, final int position) {
        view.<TextView>find(R.id.item_order_pay_desk_h_text).setText(list.get(position).getDeskName());
    }
}