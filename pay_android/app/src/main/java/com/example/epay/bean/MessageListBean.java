package com.example.epay.bean;


import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/7.
 */

public class MessageListBean {
    private int count=0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<MessageBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<MessageBean> items) {
        this.items = items;
    }

    private ArrayList<MessageBean> items= new ArrayList<MessageBean>();
}