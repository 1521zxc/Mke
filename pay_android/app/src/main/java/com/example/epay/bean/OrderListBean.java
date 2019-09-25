package com.example.epay.bean;


import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/7.
 */

public class OrderListBean {
    private int count=0;
    private double todaySum=0;
    private double totalSum=0;
    private int totalCount=0;
    private ArrayList<OrderInfoBean> items= new ArrayList<OrderInfoBean>();
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<OrderInfoBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderInfoBean> items) {
        this.items = items;
    }



    public double getTodaySum() {
        return todaySum;
    }

    public void setTodaySum(double todaySum) {
        this.todaySum = todaySum;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}