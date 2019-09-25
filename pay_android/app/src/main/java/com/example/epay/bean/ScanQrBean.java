package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.io.Serializable;

/**
 * Created by liujin on 2018/6/17.
 */

public class ScanQrBean implements Serializable{

    private double saleMoney=0;
    private double paidMoney=0;
    private String authCode="";
    private String payNO="";
    private int payType=0;
    private int payStatus=0;
    private int totalCount=0;
    private double totalMonry=0;
    private String iconURL="";

    public double getSaleMoney() {
        return DateUtil.doubleValue(saleMoney);
    }

    public void setSaleMoney(double saleMoney) {
        this.saleMoney = saleMoney;
    }

    public double getPaidMoney() {
        return DateUtil.doubleValue(paidMoney);
    }

    public void setPaidMoney(double paidMoney) {
        this.paidMoney = paidMoney;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
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

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public double getTotalMonry() {
        return DateUtil.doubleValue(totalMonry);
    }

    public void setTotalMonry(double totalMonry) {
        this.totalMonry = totalMonry;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    @Override
    public String toString() {
        return "ScanQrBean{" +
                "saleMoney=" + saleMoney +
                ", paidMoney=" + paidMoney +
                ", authCode='" + authCode + '\'' +
                ", payNO='" + payNO + '\'' +
                ", payType=" + payType +
                ", payStatus=" + payStatus +
                ", totalCount=" + totalCount +
                ", totalMonry=" + totalMonry +
                ", iconURL='" + iconURL + '\'' +
                '}';
    }
}
