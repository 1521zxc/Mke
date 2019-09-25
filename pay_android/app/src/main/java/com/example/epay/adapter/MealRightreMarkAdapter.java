package com.example.epay.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.OrderMealAttrBean;
import com.example.epay.bean.RemarkBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/24.
 */

public class MealRightreMarkAdapter extends TBaseAdapter<RemarkBean> {
    ArrayList<RemarkBean> list1 = new ArrayList<>();
    private int type = 0;

    public MealRightreMarkAdapter(Activity context, ArrayList<RemarkBean> list) {
        super(context, list);
    }

    public void setList(ArrayList<RemarkBean> list, int type) {
        this.list = list;
        this.type = type;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    public ArrayList<RemarkBean> getCh() {
        return list1;
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_meal_right_grid;
    }

    @Override
    public void initItemView(final PxViewHolder view, final ArrayList<RemarkBean> list, final int position) {

        if (position == list.size()) {
            view.<TextView>find(R.id.item_meal_right_name).setVisibility(View.GONE);
        } else {
            view.<TextView>find(R.id.item_meal_right_name).setText(list.get(position).getName());
            if (type == 1) {
                if (list.get(position).isIsch()) {
                    view.<TextView>find(R.id.item_meal_right_name).setBackground(context.getResources().getDrawable(R.drawable.corner_red_small));
                    view.<TextView>find(R.id.item_meal_right_name).setTextColor(context.getResources().getColor(R.color.textColor_white));
                } else {
                    view.<TextView>find(R.id.item_meal_right_name).setBackground(context.getResources().getDrawable(R.drawable.corner_white_small));
                    view.<TextView>find(R.id.item_meal_right_name).setTextColor(context.getResources().getColor(R.color.textColor_grey));
                }
            } else {
                list.get(position).setIsch(false);
                view.<TextView>find(R.id.item_meal_right_name).setBackground(context.getResources().getDrawable(R.drawable.corner_white_small));
                view.<TextView>find(R.id.item_meal_right_name).setTextColor(context.getResources().getColor(R.color.textColor_grey));
            }
        }
        view.<TextView>find(R.id.item_meal_right_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).isIsch()) {
                    list.get(position).setIsch(false);
                    list1.remove(list.get(position));
                } else {
                    list.get(position).setIsch(true);
                    list1.add(list.get(position));
                }
                setList(list, 1);
            }
        });
    }
}