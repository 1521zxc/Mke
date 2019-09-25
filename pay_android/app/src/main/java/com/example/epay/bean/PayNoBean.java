package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.io.Serializable;

/**
 * Created by liujin on 2018/12/4.
 */

public class PayNoBean implements Serializable{
    private String payNO="";
    private double payMoney=0;
    private long payTime=0;


    public String getPayNO() {
        return payNO;
    }

    public void setPayNO(String payNO) {
        this.payNO = payNO;
    }

    public double getPayMoney() {
        return DateUtil.doubleValue(payMoney);
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }
}
