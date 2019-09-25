package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/16.
 */

public class CataBean {
   private int count=0;
   private ArrayList<cataItem> items=new ArrayList<>();
    private ArrayList<OrderMealAttrBean> productAttrs=new ArrayList<>();

    public ArrayList<cataItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<cataItem> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<OrderMealAttrBean> getProductAttrs() {
        return productAttrs;
    }

    public void setProductAttrs(ArrayList<OrderMealAttrBean> productAttrs) {
        this.productAttrs = productAttrs;
    }

    public static class cataItem{
             private int soldCount=0;
              private double price=0;
              private String iconURL="";
              private int cataID=0;
              private int  ID=0;
              private String  name="";
              private int  sellStatus=0;
              private int  reserve=0;
              private double reserveMoney=0;
              private double  vipPrice=0;
              private int  setMeal=0;
              private int number=0;
              private ArrayList<OrderMealAttrBean> attrs=new ArrayList<>();


              public int getSoldCount() {
                  return soldCount;
              }

              public void setSoldCount(int soldCount) {
                  this.soldCount = soldCount;
              }

              public double getPrice() {
                  return DateUtil.doubleValue(price);
              }

              public void setPrice(double price) {
                  this.price = price;
              }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getIconURL() {
                  return iconURL;
              }

              public void setIconURL(String iconURL) {
                  this.iconURL = iconURL;
              }

              public int getCataID() {
                  return cataID;
              }

              public void setCataID(int cataID) {
                  this.cataID = cataID;
              }

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

              public int getSellStatus() {
                  return sellStatus;
              }

              public void setSellStatus(int sellStatus) {
                  this.sellStatus = sellStatus;
              }

              public int getReserve() {
                  return reserve;
              }

              public void setReserve(int reserve) {
                  this.reserve = reserve;
              }

              public double getReserveMoney() {
                  return DateUtil.doubleValue(reserveMoney);
              }

              public void setReserveMoney(double reserveMoney) {
                  this.reserveMoney = reserveMoney;
              }

              public double getVipPrice() {
                  return DateUtil.doubleValue(vipPrice);
              }

              public void setVipPrice(double vipPrice) {
                  this.vipPrice = vipPrice;
              }

              public int getSetMeal() {
                  return setMeal;
              }

              public void setSetMeal(int setMeal) {
                  this.setMeal = setMeal;
              }

        public ArrayList<OrderMealAttrBean> getAttrs() {
            return attrs;
        }

        public void setAttrs(ArrayList<OrderMealAttrBean> attrs) {
            this.attrs = attrs;
        }
    }

}
