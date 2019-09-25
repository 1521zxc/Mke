package com.example.epay.bean;

import java.io.Serializable;

/**
 * Created by liujin on 2019/7/17.
 */

public class MemberGiftBean implements Serializable{
    private int ID=0;
    private String name="";
    private String cpName="";
    private double money=0;
    private double vipMoney=0;


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

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getVipMoney() {
        return vipMoney;
    }

    public void setVipMoney(double vipMoney) {
        this.vipMoney = vipMoney;
    }
}
