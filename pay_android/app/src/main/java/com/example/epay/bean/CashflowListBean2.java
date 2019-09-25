package com.example.epay.bean;

import com.example.epay.util.DateUtil;

/**
 * Created by liujin on 2018/3/9.
 */

public class CashflowListBean2 {
    private String time="";
    private String all="";
    private double money=0;
    private int ID=0;
    private int type=1;
    private String typeName="";
    private double sum=0;
    private String dateTime="";
    private String nickName="";
    private String iconURL="";

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public double getMoney() {
        return DateUtil.doubleValue(money);
    }

    public void setMoney(double money) {
        this.money = money;
    }

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

    public double getSum() {
        return DateUtil.doubleValue(sum);
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
