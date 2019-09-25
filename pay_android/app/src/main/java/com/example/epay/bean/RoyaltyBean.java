package com.example.epay.bean;

import java.util.ArrayList;

/**
 * Created by liujin on 2019/6/4.
 */

public class RoyaltyBean {
    private String name="";
    private String imgUrl="";
    private String phone="";
    private double monthRoyalty=0;
    private double dayRoyalty=0;
    private ArrayList<RoyaltyItem> item=new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getMonthRoyalty() {
        return monthRoyalty;
    }

    public void setMonthRoyalty(double monthRoyalty) {
        this.monthRoyalty = monthRoyalty;
    }

    public double getDayRoyalty() {
        return dayRoyalty;
    }

    public void setDayRoyalty(double dayRoyalty) {
        this.dayRoyalty = dayRoyalty;
    }

    public ArrayList<RoyaltyItem> getItem() {
        return item;
    }

    public void setItem(ArrayList<RoyaltyItem> item) {
        this.item = item;
    }

    public static class RoyaltyItem{
        private String imgUrl="";
        private String name="";
        private double price=0;
        private double royalty=0;
        private String phone="";
        private double dayRoyalty=0;
        private ArrayList<RoyaltyItem> item=new ArrayList<>();

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getRoyalty() {
            return royalty;
        }

        public void setRoyalty(double royalty) {
            this.royalty = royalty;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }


        public double getDayRoyalty() {
            return dayRoyalty;
        }

        public void setDayRoyalty(double dayRoyalty) {
            this.dayRoyalty = dayRoyalty;
        }

        public ArrayList<RoyaltyItem> getItem() {
            return item;
        }

        public void setItem(ArrayList<RoyaltyItem> item) {
            this.item = item;
        }
    }
}
