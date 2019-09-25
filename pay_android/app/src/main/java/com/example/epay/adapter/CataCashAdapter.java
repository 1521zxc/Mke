package com.example.epay.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.CataItemBean;
import com.example.epay.bean.MessageBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/10/20.
 */

public class CataCashAdapter extends TBaseAdapter<CataItemBean> {
    int value=100;
    public CataCashAdapter(Activity context, ArrayList<CataItemBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_cata_cash_layout;
    }

    @Override
    public int getCount() {
        return super.getCount()+1;
    }

    public void setList(ArrayList<CataItemBean> list,int value) {
        this.value=value;
        super.setList(list);
    }

    @Override
    public void initItemView(PxViewHolder view, ArrayList<CataItemBean> list, int position) {
        if(position!=0) {
            view.<TextView>find(R.id.item_cata_cash_name).setText(list.get(position-1).getName());
            view.<TextView>find(R.id.item_cata_cash_num).setText(list.get(position-1).getNum()+"");
            view.<TextView>find(R.id.item_cata_cash_noney).setText(list.get(position-1).getSum()+"");
            view.<TextView>find(R.id.item_cata_cash_bai).setText(list.get(position-1).getBai()+"%");
        }
    }
}
