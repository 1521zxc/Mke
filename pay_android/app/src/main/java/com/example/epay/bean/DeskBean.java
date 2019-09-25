package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.io.Serializable;

/**
 * Created by liujin on 2018/6/12.
 */

public class DeskBean implements Serializable {
    private int ID=0;
    private String deskNO="";
    private String deskName="";
    private int deskStatus=0;
    private String orderID="";
    private String orderNO="";
    private int serviceStatus=0;
    private int totalNum=0;
    private double saleMoney=0;
    private long createTime=0;
    private int index=-1;
    private int payStatus=0;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDeskNO() {
        return deskNO;
    }

    public void setDeskNO(String deskNO) {
        this.deskNO = deskNO;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public int getDeskStatus() {
        return deskStatus;
    }

    public void setDeskStatus(int deskStatus) {
        this.deskStatus = deskStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public double getSaleMoney() {
        return DateUtil.doubleValue(saleMoney);
    }

    public void setSaleMoney(double saleMoney) {
        this.saleMoney = saleMoney;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }
}
