package com.example.epay.bean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/7/14.
 */

public class SearchMealBean {
    private int count=0;
    private ArrayList<MealListBean.MealRight> items=new ArrayList<>();
    private ArrayList<OrderMealAttrBean> productAttrs=new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<MealListBean.MealRight> getItems() {
        return items;
    }

    public void setItems(ArrayList<MealListBean.MealRight> items) {
        this.items = items;
    }

    public ArrayList<OrderMealAttrBean> getProductAttrs() {
        return productAttrs;
    }

    public void setProductAttrs(ArrayList<OrderMealAttrBean> productAttrs) {
        this.productAttrs = productAttrs;
    }
}
