package com.example.epay.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/12.
 */

public class DeskListBean implements Serializable {
    private int count=0;
    private ArrayList<DeskBean> items=new ArrayList<>();
    private int isClose=0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<DeskBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<DeskBean> items) {
        this.items = items;
    }

    public int getIsClose() {
        return isClose;
    }

    public void setIsClose(int isClose) {
        this.isClose = isClose;
    }
}
