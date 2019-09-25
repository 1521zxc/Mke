package com.example.epay.bean;

import com.example.epay.util.DateUtil;

/**
 * Created by liujin on 2019/1/9.
 */

public class CashTypeBean {
    private String name="";
    private double sum=0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSum() {
        return DateUtil.doubleValue(sum);
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
