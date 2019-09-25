package com.example.epay.bean;


import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/7.
 */

public class SpecialListBean {

    private int count=0;
    private ArrayList<SpecialBean> items= new ArrayList<SpecialBean>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<SpecialBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<SpecialBean> items) {
        this.items = items;
    }

}