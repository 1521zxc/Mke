package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/11/11.
 */

public class BusinessBean {
    private double earnings=0;
    private double gift=0;
    private double discount=0;
    private double duesum=0;
    private double normal=0;
    private double reserved=0;
    private int ordernum=0;
    private int paynum=0;
    private int ID=0;
    private long datetime=0;
    private ArrayList<storesBean> stores=new ArrayList<>();
    private ArrayList<CataItemBean> items=new ArrayList<>();
    private ArrayList<PayTypeBean> details=new ArrayList();
    private ArrayList<OrderPayTypeBean> payTypes=new ArrayList<>();

    public double getEarnings() {
        return DateUtil.doubleValue(earnings);
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public double getDiscount() {
        return DateUtil.doubleValue(discount);
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDuesum() {
        return DateUtil.doubleValue(duesum);
    }

    public void setDuesum(double duesum) {
        this.duesum = duesum;
    }

    public double getNormal() {
        return DateUtil.doubleValue(normal);
    }

    public void setNormal(double normal) {
        this.normal = normal;
    }

    public double getReserved() {
         return DateUtil.doubleValue(reserved);
    }

    public void setReserved(double reserved) {
        this.reserved = reserved;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public ArrayList<storesBean> getStores() {
        return stores;
    }

    public void setStores(ArrayList<storesBean> stores) {
        this.stores = stores;
    }


    public ArrayList<CataItemBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<CataItemBean> items) {
        this.items = items;
    }

    public ArrayList<PayTypeBean> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<PayTypeBean> details) {
        this.details = details;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public int getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(int ordernum) {
        this.ordernum = ordernum;
    }

    public int getPaynum() {
        return paynum;
    }

    public void setPaynum(int paynum) {
        this.paynum = paynum;
    }

    public double getGift() {
        return gift;
    }

    public void setGift(double gift) {
        this.gift = gift;
    }



    public ArrayList<OrderPayTypeBean> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(ArrayList<OrderPayTypeBean> payTypes) {
        this.payTypes = payTypes;
    }
}
