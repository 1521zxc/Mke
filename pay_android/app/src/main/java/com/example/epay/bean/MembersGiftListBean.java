package com.example.epay.bean;

import java.util.ArrayList;

/**
 * Created by liujin on 2019/7/17.
 */

public class MembersGiftListBean {
    private int count=0;
    private ArrayList<GiftBean> gifts=new ArrayList();
    private String orderNO="";

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<GiftBean> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<GiftBean> gifts) {
        this.gifts = gifts;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public static class GiftBean{
        private int ID=0;
        private String name="";
        private double fullPrice=0;
        private int CPID=0;
        private int count=0;

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

        public double getFullPrice() {
            return fullPrice;
        }

        public void setFullPrice(double fullPrice) {
            this.fullPrice = fullPrice;
        }

        public int getCPID() {
            return CPID;
        }

        public void setCPID(int CPID) {
            this.CPID = CPID;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
