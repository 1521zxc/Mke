package com.example.epay.bean;

import java.io.Serializable;

/**
 * Created by liujin on 2018/5/21.
 */

public class OrderBean implements Serializable {
    private String ID="";
    private String orderNO="";
    private String dateTime="";
    private String status="";
    private String primeMoney="";
    private String saleMoney="";
    private String paidMoney="";

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrimeMoney() {
        return primeMoney;
    }

    public void setPrimeMoney(String primeMoney) {
        this.primeMoney = primeMoney;
    }

    public String getSaleMoney() {
        return saleMoney;
    }

    public void setSaleMoney(String saleMoney) {
        this.saleMoney = saleMoney;
    }

    public String getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(String paidMoney) {
        this.paidMoney = paidMoney;
    }
}
