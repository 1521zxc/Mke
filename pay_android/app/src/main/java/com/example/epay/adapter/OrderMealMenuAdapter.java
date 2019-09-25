package com.example.epay.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.MealListBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/15.
 */

public class OrderMealMenuAdapter extends TBaseAdapter<MealListBean.MealRight> {
    double price1 = 0;
    int num1 = 0;
    OnNtClickListener onNtClickListener;

    public OrderMealMenuAdapter(Activity context, ArrayList<MealListBean.MealRight> list, double price1, int num1) {
        super(context, list);
        this.price1 = price1;
        this.num1 = num1;
    }

    public interface OnNtClickListener {
        void onNtClick(ArrayList<MealListBean.MealRight> list, int num, double price);
    }

    public void setOnNtClickListener(OnNtClickListener listener) {
        this.onNtClickListener = listener;
    }

    public void setData(ArrayList<MealListBean.MealRight> lists, double price1, int num1) {
        this.list = lists;
        this.price1 = price1;
        this.num1 = num1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_order_popup;
    }

    @Override
    public void initItemView(final PxViewHolder view1, final ArrayList<MealListBean.MealRight> list, final int position) {
        double attrPrice = 0;
        if (list.get(position).getAttrs() == null || list.get(position).getAttrs().size() == 0) {
            view1.<TextView>find(R.id.item_order_popup_name).setText(list.get(position).getName());
        } else {
            String name = list.get(position).getName();
            String name1 = "";
            for (int i = 0; i < list.get(position).getAttrs().size(); i++) {
                if (i == 0) {
                    name1 = name1 + list.get(position).getAttrs().get(i).getName();
                } else {
                    name1 = name1 + "、" + list.get(position).getAttrs().get(i).getName();
                }
                attrPrice = attrPrice + list.get(position).getAttrs().get(i).getPrice();
            }
            name = name + "(" + name1 + ")";
            view1.<TextView>find(R.id.item_order_popup_name).setText(name);

        }
        view1.<TextView>find(R.id.item_order_popup_num).setText(list.get(position).getNumber() + "");
        final double finalAttrPrice = attrPrice;
        final boolean[] isEnd = {true};

        view1.<ImageView>find(R.id.item_order_popup_reduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < list.size()) {
                    if (isEnd[0]) {
                        isEnd[0] = false;
                        if (list.get(position).getNumber() == list.get(position).getLowSell()) {
                            list.get(position).setNumber(0);
                            num1 = num1 - list.get(position).getLowSell();
                            price1 = price1 - (list.get(position).getPrice() * list.get(position).getLowSell()) - finalAttrPrice;
                            list.remove(list.get(position));
                        } else {
                            list.get(position).setNumber(list.get(position).getNumber() - 1);
                            num1--;
                            price1 = price1 - list.get(position).getPrice() - finalAttrPrice;
                            view1.<TextView>find(R.id.item_order_popup_num).setText(list.get(position).getNumber() + "");
                        }
                        price1 = (double) (Math.round(price1 * 100) / 100.0);
                        if (onNtClickListener != null) {
                            isEnd[0] = true;
                            onNtClickListener.onNtClick(list, num1, price1);
                        }
                    }
                }
            }
        });

        view1.<ImageView>find(R.id.item_order_popup_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(position).setNumber(list.get(position).getNumber() + 1);
                num1++;
                price1 = price1 + list.get(position).getPrice() + finalAttrPrice;
                price1 = (double) (Math.round(price1 * 100) / 100.0);
                view1.<TextView>find(R.id.item_order_popup_num).setText(list.get(position).getNumber() + "");

                if (onNtClickListener != null) {
                    onNtClickListener.onNtClick(list, num1, price1);
                }
            }
        });
    }
}
