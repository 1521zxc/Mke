package com.example.epay.bean;

import com.example.epay.util.DateUtil;

/**
 * Created by liujin on 2018/12/6.
 */

public class PayTypeBean {
    private int payType=0;
    private double sum=0;

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public double getSum() {
        return DateUtil.doubleValue(sum);
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
