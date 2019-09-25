package com.example.epay.bean;


import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/7.
 */

public class CashflowListBean {
    private int count=0;
    private ArrayList<CashflowBean> items= new ArrayList<CashflowBean>();
    private ArrayList<OrderPayTypeBean> payTypes=new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<CashflowBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<CashflowBean> items) {
        this.items = items;
    }



    public ArrayList<OrderPayTypeBean> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(ArrayList<OrderPayTypeBean> payTypes) {
        this.payTypes = payTypes;
    }
}