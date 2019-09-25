package com.example.epay.bean;


import com.example.epay.util.DateUtil;

/**
 * Created by liujin on 2018/3/7.
 */

public class CashflowBean {
        private int ID=0;
        private int payType=1;
        private double sum=0;
        private long createTime=1;
        private String nickName="";
        private String iconURL="";
        private String state="";

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

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
}