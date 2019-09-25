package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.io.Serializable;

/**
 * Created by liujin on 2018/12/20.
 */

public class OrderPayTypeBean implements Serializable {
    private int ID=0;
    private int type=0;
    private String name="";
    private double money=0;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

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
        return DateUtil.doubleValue(money);
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
