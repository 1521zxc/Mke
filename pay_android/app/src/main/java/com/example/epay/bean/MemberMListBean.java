package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.io.Serializable;

/**
 * Created by liujin on 2018/3/27.
 */

public class MemberMListBean implements Serializable{
    private String ID="";
    private String type="";
    private String nickName="";
    private String iconURL="";
    private double totalNum=0;
    private double  totalSum=0;
    private long latestBillTime=0;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public double getTotalSum() {
        return DateUtil.doubleValue(totalSum);
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
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

    public double getTotalNum() {
        return DateUtil.doubleValue(totalNum);
    }

    public void setTotalNum(double totalNum) {
        this.totalNum = totalNum;
    }

    public long getLatestBillTime() {
        return latestBillTime;
    }

    public void setLatestBillTime(long latestBillTime) {
        this.latestBillTime = latestBillTime;
    }
}
