package com.example.epay.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liujin on 2019/9/4.
 */

public class OptionBean implements Serializable {
    private ArrayList<MealListBean.MealRight> items=new ArrayList();
    private int max=0;
    private int count=0;
    private String name="";
    public ArrayList<MealListBean.MealRight> getItems() {
        return items;
    }

    public void setItems(ArrayList<MealListBean.MealRight> items) {
        this.items = items;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
