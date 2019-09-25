package com.example.epay.bean;


import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/7.
 */

public class TransferListBean {
    private int count=0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<TransferBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<TransferBean> items) {
        this.items = items;
    }

    private ArrayList<TransferBean> items= new ArrayList<TransferBean>();
}