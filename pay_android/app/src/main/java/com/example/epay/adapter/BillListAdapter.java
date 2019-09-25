package com.example.epay.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.BillBean;
import com.example.epay.bean.MessageBean;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class BillListAdapter extends TBaseAdapter<BillBean> {
    public BillListAdapter(Activity context, ArrayList<BillBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_bill_list;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<BillBean> list, final int position) {
        view.<TextView>find(R.id.item_bill_status).setText(list.get(position).getStatus());
        view.<TextView>find(R.id.item_bill_name).setText(list.get(position).getName());
        view.<TextView>find(R.id.item_bill_time).setText(list.get(position).getStartTime()+"-"+list.get(position).getEndTime());
        view.<TextView>find(R.id.item_bill_money).setText("+"+list.get(position).getMoney()+"元");
    }
}
