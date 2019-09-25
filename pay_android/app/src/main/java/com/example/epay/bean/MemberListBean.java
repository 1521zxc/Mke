package com.example.epay.bean;

import java.util.ArrayList;

/**
 * Created by liujin on 2019/5/10.
 */

public class MemberListBean {
    private int count=0;
    private ArrayList<MemberBean> items=new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<MemberBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<MemberBean> items) {
        this.items = items;
    }
}

