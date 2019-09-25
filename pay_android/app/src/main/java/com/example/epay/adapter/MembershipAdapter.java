package com.example.epay.adapter;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.AddressBean;
import com.example.epay.bean.MemberBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/21.
 */

public class MembershipAdapter extends TBaseAdapter<MemberBean> {
    public MembershipAdapter(Activity context, ArrayList<MemberBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_membership_list;
    }

    @Override
    public void initItemView(PxViewHolder view, ArrayList<MemberBean> list, int position) {
        view.<TextView>find(R.id.tv_name).setText(list.get(position).getMemberName());
        view.<TextView>find(R.id.tv_total_consumption).setText("共消费"+list.get(position).getConsumed());
        view.<TextView>find(R.id.tv_balance).setText("￥"+list.get(position).getCredit());
        load(list.get(position).getIconURL(),view.<ImageView>find(R.id.iv_portrait),0);
    }
}
