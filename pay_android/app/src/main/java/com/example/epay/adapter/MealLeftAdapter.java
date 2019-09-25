package com.example.epay.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.OrderMealLeftBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/13.
 */

public class MealLeftAdapter extends TBaseAdapter<OrderMealLeftBean> {
    private int selectItem = 0;
    public MealLeftAdapter(Activity context, ArrayList<OrderMealLeftBean> list) {
        super(context, list);
    }
    public int getSelectItem() {
        return selectItem;
    }



    public void setSelectItem(int selectItem) {

        this.selectItem = selectItem;
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_order_meal_left;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<OrderMealLeftBean> list, final int position) {
        if (position == selectItem) {
            view.<TextView>find(R.id.item_order_meal_name).setBackgroundColor(context.getResources().getColor(R.color.textColor_white));
        } else {
            view.<TextView>find(R.id.item_order_meal_name).setBackgroundColor(context.getResources().getColor(R.color.color7));
        }
        view.<TextView>find(R.id.item_order_meal_name).setText(list.get(position).getName());
    }
}

