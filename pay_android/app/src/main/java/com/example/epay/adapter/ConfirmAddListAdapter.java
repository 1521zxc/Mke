package com.example.epay.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.AttachedBean;
import com.example.epay.bean.DeskBean;
import com.example.epay.bean.MealListBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class ConfirmAddListAdapter extends TBaseAdapter<MealListBean.MealRight> {
    public ConfirmAddListAdapter(Activity context, ArrayList<MealListBean.MealRight> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_confirm_add_list;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<MealListBean.MealRight> list, final int position) {
        view.<TextView>find(R.id.name).setText(list.get(position).getName());
        view.<TextView>find(R.id.count).setText("x"+list.get(position).getNumber());
    }
}