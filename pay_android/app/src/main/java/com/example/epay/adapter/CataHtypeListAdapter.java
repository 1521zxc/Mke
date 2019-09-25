package com.example.epay.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.CatatypeBean;
import com.example.epay.bean.DeskBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class CataHtypeListAdapter extends TBaseAdapter<CatatypeBean.catatype> {
    public CataHtypeListAdapter(Activity context, ArrayList<CatatypeBean.catatype> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_order_pay_cata_h;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<CatatypeBean.catatype> list, final int position) {
        view.<TextView>find(R.id.item_order_pay_cata_h_text).setText(list.get(position).getName());
    }
}