package com.example.epay.adapter;

import android.app.Activity;
import android.widget.TextView;


import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.TransferBean;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class TransferListAdapter extends TBaseAdapter<TransferBean> {
    public TransferListAdapter(Activity context, ArrayList<TransferBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_transfer_list;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<TransferBean> list, final int position) {

        String time=DateUtil.getday(list.get(position).getGainedDate(),"yyyy-MM-dd hh:mm:ss");
        view.<TextView>find(R.id.transfer_date).setText(time.substring(0,19));
        view.<TextView>find(R.id.transfer_money).setText("￥"+list.get(position).getSum());
        view.<TextView>find(R.id.transfer_week).setText(time.substring(20,time.length()));
        if(list.get(position).getStatus().equals("20")) {
            view.<TextView>find(R.id.transfer_state).setText("已付款");
        }
    }
}