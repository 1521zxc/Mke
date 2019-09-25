package com.example.epay.bean;

/**
 * Created by liujin on 2019/6/13.
 */

public class ComPayTypeBean {
    private int type=0;
    private String name="";
    private double money=0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
