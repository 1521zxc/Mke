package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/11/19.
 */

public class OrderPayZheBean {
    private int count=0;
    private ArrayList<OrderPayZhe> items=new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<OrderPayZhe> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderPayZhe> items) {
        this.items = items;
    }

    public static class OrderPayZhe{
        private int ID=0;
        private String name="";
        private double discount=0;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getDiscount() {
            return DateUtil.doubleValue(discount);
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }
    }
}
