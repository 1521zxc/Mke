package com.example.epay.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.BestSellBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/21.
 */

public class BestSellAdapter extends TBaseAdapter<BestSellBean> {
    public BestSellAdapter(Activity context, ArrayList<BestSellBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_best_sell_list;
    }

    @Override
    public void initItemView(PxViewHolder view, ArrayList<BestSellBean> list, int position) {
        view.<TextView>find(R.id.item_best_sell_color).setBackgroundColor(list.get(position).getColor());
        view.<TextView>find(R.id.item_best_sell_name).setText(list.get(position).getName());
    }
}
