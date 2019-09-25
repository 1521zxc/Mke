package com.example.epay.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.AddressBean;
import com.example.epay.bean.MealListBean;
import com.example.epay.bean.OptionBean;
import com.example.epay.view.EPayListView;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/21.
 */

public class SetMealAdapter extends TBaseAdapter<OptionBean> {
    SetMealItemAdapter adapter;
    public SetMealAdapter(Activity context, ArrayList<OptionBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_set_meal_list;
    }
    @Override
    public void initItemView(PxViewHolder view, ArrayList<OptionBean> list, int position) {
        view.<TextView>find(R.id.item_set_meal_name).setText(list.get(position).getName());
        view.<EPayListView>find(R.id.item_set_meal_list).setVisibility(View.GONE);
        if(list.get(position).getCount()>1) {
            adapter = new SetMealItemAdapter(context, list.get(position).getItems(),list.get(position).getMax());
            view.<EPayListView>find(R.id.item_set_meal_list).setAdapter(adapter);

        }

        view.<TextView>find(R.id.item_set_meal_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if(list.get(position).getCount()>1) {
                    if (view.<EPayListView>find(R.id.item_set_meal_list).getVisibility() == View.GONE) {
                        view.<EPayListView>find(R.id.item_set_meal_list).setVisibility(View.VISIBLE);
                    } else {
                        view.<EPayListView>find(R.id.item_set_meal_list).setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}
