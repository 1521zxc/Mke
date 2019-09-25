package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.io.Serializable;

/**
 * Created by liujin on 2019/4/21.
 */

public class conponeBean implements Serializable {
   private int ID=0;
   private String name="";
   private long startTime=0;
   private long endTime=0;
   private String image="";
    private int received=0;
    private int outDated=0;
    private double fullPrice=0;
    private double price=0;

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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public int getOutDated() {
        return outDated;
    }

    public void setOutDated(int outDated) {
        this.outDated = outDated;
    }

    public double getFullPrice() {
        return DateUtil.doubleValue(fullPrice);
    }

    public void setFullPrice(double fullPrice) {
        this.fullPrice = fullPrice;
    }

    public double getPrice() {
        return DateUtil.doubleValue(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
