package com.example.epay.bean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/27.
 */

public class MemberMBean {
    private String todayCount="0";
    private String  totalCount="0";
    private String count="0";
    private ArrayList<MemberMListBean> items=new ArrayList<MemberMListBean>();


    public String getTodayCount() {
        return todayCount;
    }

    public void setTodayCount(String todayCount) {
        this.todayCount = todayCount;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<MemberMListBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<MemberMListBean> items) {
        this.items = items;
    }
}
