package com.example.epay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.activity.SetMealActivity;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.MealListBean;
import com.example.epay.bean.RemarkBean;
import com.example.epay.view.EPayListView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/13.
 */

public class MealRightAdapter extends TBaseAdapter<MealListBean> {
    MealRightListAdapter adapter;
    double price1=0;
    int num1=0;
    ArrayList<MealListBean.MealRight> lists = new ArrayList<>();
    ArrayList<RemarkBean> list2 = new ArrayList<>();
    OnNtClickListener onNtClickListener;
    public MealRightAdapter(Activity context, ArrayList<MealListBean> list, ArrayList<RemarkBean> list2, ArrayList<MealListBean.MealRight> lists, double price1, int num1) {
        super(context, list);
        this.lists=lists;
        this.price1=price1;
        this.num1=num1;
        this.list2=list2;
    }
    public void setData(ArrayList<MealListBean.MealRight> lists,ArrayList<RemarkBean> list2,double price1,int num1)
    {
        this.lists=lists;
        this.price1=price1;
        this.num1=num1;
        this.list2=list2;
        notifyDataSetChanged();
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_order_meal_right;
    }

    public interface OnNtClickListener {
        void onNtClick( ArrayList<MealListBean.MealRight> list,int num,double price);
    }

    public void setOnNtClickListener(OnNtClickListener listener) {
        this.onNtClickListener = listener;
    }


    @Override
    public void initItemView(PxViewHolder view, final ArrayList<MealListBean> list, final int position) {
        view.<TextView>find(R.id.item_order_meal_title).setText(list.get(position).getTitle());
        adapter = new MealRightListAdapter(context, list.get(position).getMealRights(),list2,price1,lists,num1);
        view.<EPayListView>find(R.id.item_order_meal_listView).setAdapter(adapter);
        adapter.setOnNtClickListener(new MealRightListAdapter.OnNtClickListener() {
           @Override
           public void onNtClick(ArrayList<MealListBean.MealRight> list1, int num, double price) {
               if(onNtClickListener!=null) {
                   onNtClickListener.onNtClick(list1,num,price);
                   lists=list1;
                   num1=num;
                   price1=price;
               }
           }
       });

        view.<EPayListView>find(R.id.item_order_meal_listView).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(list.get(position).getMealRights().get(i).getChoose()==1) {
                    Intent intent = new Intent(context, SetMealActivity.class);
                    intent.putExtra("id", list.get(position).getMealRights().get(i).getID());
                    intent.putExtra("lists", (Serializable) lists);
                    intent.putExtra("num1", num1);
                    intent.putExtra("price1", price1);
                    context.startActivityForResult(intent,1001);
                }
            }
        });
    }
}

