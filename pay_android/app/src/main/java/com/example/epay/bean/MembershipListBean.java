package com.example.epay.bean;

import java.util.ArrayList;

/**
 * Created by liujin on 2019/5/15.
 */

public class MembershipListBean {
    private int count=0;
    private ArrayList<Membershipbean> items=new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Membershipbean> getItems() {
        return items;
    }

    public void setItems(ArrayList<Membershipbean> items) {
        this.items = items;
    }

    public class Membershipbean{
        private int payType=0;
        private int flowType=0;
        private double money=0;
        private double realMoney=0;
        private long createTime=0;
        private int ID=0;
        private String payNO="";
        private int state=0;

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public int getFlowType() {
            return flowType;
        }

        public void setFlowType(int flowType) {
            this.flowType = flowType;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public double getRealMoney() {
            return realMoney;
        }

        public void setRealMoney(double realMoney) {
            this.realMoney = realMoney;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getPayNO() {
            return payNO;
        }

        public void setPayNO(String payNO) {
            this.payNO = payNO;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
