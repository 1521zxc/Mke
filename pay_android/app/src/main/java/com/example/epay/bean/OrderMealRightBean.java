package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.io.Serializable;

/**
 * Created by liujin on 2018/6/13.
 */

public class OrderMealRightBean implements Serializable {
    private int soldCount=0;
    private double price=0;
    private String iconURL="";
    private int cataID=0;
    private int ID=0;
    private int sellStatus=0;
    private int setMeal=0;
    private String name="";
    private double vipPrice=0;
    private int lowSell=1;
    private String spell="";
    private int choose=0;


    public int getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(int soldCount) {
        this.soldCount = soldCount;
    }

    public int getChoose() {
        return choose;
    }

    public void setChoose(int choose) {
        this.choose = choose;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public int getCataID() {
        return cataID;
    }

    public void setCataID(int cataID) {
        this.cataID = cataID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSellStatus() {
        return sellStatus;
    }

    public void setSellStatus(int sellStatus) {
        this.sellStatus = sellStatus;
    }

    public int getSetMeal() {
        return setMeal;
    }

    public void setSetMeal(int setMeal) {
        this.setMeal = setMeal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return DateUtil.doubleValue(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVipPrice() {
        return DateUtil.doubleValue(vipPrice);
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public int getLowSell() {
        return lowSell;
    }

    public void setLowSell(int lowSell) {
        this.lowSell = lowSell;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }
}
