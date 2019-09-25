package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/11/6.
 */

public class storesBean {
    private int ID=0;
    private String brandName="";
    private double sum=0;
    private ArrayList items=new ArrayList();

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public double getSum() {
        return DateUtil.doubleValue(sum);
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
