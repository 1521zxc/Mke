package com.example.epay.bean;

import com.example.epay.util.DateUtil;

/**
 * Created by liujin on 2018/11/13.
 */

public class CataItemBean {
    private int ID=0;
    private String name="";
    private double num=0;
    private double sum=0;
    private int value=0;
    private float bai=0;
    private float numbai=0;
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNum() {
        return DateUtil.doubleValue(num);
    }

    public void setNum(double num) {
        this.num = num;
    }

    public double getSum() {
        return DateUtil.doubleValue(sum);
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public float getBai() {
        return bai;
    }

    public void setBai(float bai) {
        this.bai = bai;
    }

    public float getNumbai() {
        return numbai;
    }

    public void setNumbai(float numbai) {
        this.numbai = numbai;
    }
}
