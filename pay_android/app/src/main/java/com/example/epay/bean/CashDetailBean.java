package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.io.Serializable;

/**
 * Created by liujin on 2018/3/12.
 */

public class CashDetailBean implements Serializable{

    int ID=0;
    int buyerID=0;
    String nickName="";
    String iconURL="";
    String payNO="";
    int payType=1;
    double primeMoney=0;
    double paidMoney=0.01;
    String brandName="";
    long createTime=0;
    String status="";
    String desc="";






    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(int buyerID) {
        this.buyerID = buyerID;
    }

    public String getPayNO() {
        return payNO;
    }

    public void setPayNO(String payNO) {
        this.payNO = payNO;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public double getPrimeMoney() {
        return DateUtil.doubleValue(primeMoney);
    }

    public void setPrimeMoney(double primeMoney) {
        this.primeMoney = primeMoney;
    }

    public double getPaidMoney() {
        return DateUtil.doubleValue(paidMoney);
    }

    public void setPaidMoney(double paidMoney) {
        this.paidMoney = paidMoney;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
