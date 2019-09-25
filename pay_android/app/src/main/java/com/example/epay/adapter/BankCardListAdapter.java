package com.example.epay.adapter;

import android.app.Activity;


import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.BeanBankCard;

import java.util.ArrayList;

/**
 * Created by liujin on 2015/8/6.
 */
public class BankCardListAdapter extends TBaseAdapter<BeanBankCard> {

    public BankCardListAdapter(Activity context, ArrayList<BeanBankCard> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_card_list;
    }

    @Override
    public void initItemView(TBaseAdapter.PxViewHolder view, ArrayList<BeanBankCard> list, int position) {
    }
}
