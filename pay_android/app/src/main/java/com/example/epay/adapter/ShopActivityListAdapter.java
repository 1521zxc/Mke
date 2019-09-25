package com.example.epay.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.MembersGiftListBean;
import com.example.epay.bean.MessageBean;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class ShopActivityListAdapter extends TBaseAdapter<MembersGiftListBean.GiftBean> {
    public ShopActivityListAdapter(Activity context, ArrayList<MembersGiftListBean.GiftBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_member_stored_value;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<MembersGiftListBean.GiftBean> list, final int position) {
        view.<TextView>find(R.id.tv_charge).setText(list.get(position).getName());
    }
}
