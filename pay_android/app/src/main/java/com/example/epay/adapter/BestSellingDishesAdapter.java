package com.example.epay.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.OrderMealLeftBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/21.
 */

public class BestSellingDishesAdapter extends TBaseAdapter<OrderMealLeftBean> {
    private int index=0;
    public BestSellingDishesAdapter(Activity context, ArrayList<OrderMealLeftBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_best_selling_dishes_list;
    }

    public void setIndex(int position){
        index=position;
        setList(list);
    }

    @Override
    public void initItemView(PxViewHolder view, ArrayList<OrderMealLeftBean> list, int position) {
        view.<TextView>find(R.id.item_best_selling_dishes_name).setText(list.get(position).getName());
        if(position!=index) {
            view.<TextView>find(R.id.item_best_selling_dishes_line).setVisibility(View.INVISIBLE);
        }else{
            view.<TextView>find(R.id.item_best_selling_dishes_line).setVisibility(View.VISIBLE);
        }
    }
}
