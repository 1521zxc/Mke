package com.example.epay.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.MealListBean;
import com.example.epay.bean.OptionBean;
import com.example.epay.util.DateUtil;
import com.example.epay.view.EPayListView;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/21.
 */

public class SetMealItemAdapter extends TBaseAdapter<MealListBean.MealRight> {
    private int num=0;
    public SetMealItemAdapter(Activity context, ArrayList<MealListBean.MealRight> list,int num1) {
        super(context, list);
        num=num1;

    }

    public void setList(ArrayList<MealListBean.MealRight> list,int num1,double price){
        setList(list);
    }
    @Override
    public int getItemResourceId() {
        return R.layout.item_set_meal_item_list;
    }

    @Override
    public void initItemView(PxViewHolder view, ArrayList<MealListBean.MealRight> list, int position) {
        view.<TextView>find(R.id.item_set_meal_name).setText(list.get(position).getName());
        view.<CheckBox>find(R.id.item_set_meal_checkbox).setChecked(list.get(position).isChoose());

        view.<CheckBox>find(R.id.item_set_meal_checkbox).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if (num < 1) {
                        view.<CheckBox>find(R.id.item_set_meal_checkbox).setChecked(false);
                        list.get(position).setChoose(false);
                    }else{
                        num--;
                        list.get(position).setChoose(b);
                    }
                }else{
                    num++;
                    list.get(position).setChoose(b);
                }

            }
        });
    }
}
