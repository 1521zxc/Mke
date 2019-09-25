package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/15.
 */

public class ConfirmBean implements Serializable {
    private int ID=0;
    private int orderID=0;

    private String orderNO="";
    private long  createTime=0;
    private int payStatus=0;
    private double primeMoney=0;
    private double saleMoney=0;
    private double paidMoney=0;
    private String deskNO="";
    private String deskName="";
    private int totalNUm=0;
    private String brandName="";
    private int payType=0;
    private int serviceStatus=0;
    private int ordinal=0;
    private double vipMoney=0;
    private String   payURL="";
    private ArrayList<OrderInfoBean.ProductSimple> attached=new ArrayList<>();

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public double getPrimeMoney() {
        return DateUtil.doubleValue(primeMoney);
    }

    public void setPrimeMoney(double primeMoney) {
        this.primeMoney = primeMoney;
    }

    public double getSaleMoney() {
        return DateUtil.doubleValue(saleMoney);
    }

    public void setSaleMoney(double saleMoney) {
        this.saleMoney = saleMoney;
    }

    public double getPaidMoney() {
        return DateUtil.doubleValue(paidMoney);
    }

    public void setPaidMoney(double paidMoney) {
        this.paidMoney = paidMoney;
    }

    public String getDeskNO() {
        return deskNO;
    }

    public void setDeskNO(String deskNO) {
        this.deskNO = deskNO;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public int getTotalNUm() {
        return totalNUm;
    }

    public void setTotalNUm(int totalNUm) {
        this.totalNUm = totalNUm;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public double getVipMoney() {
        return DateUtil.doubleValue(vipMoney);
    }

    public void setVipMoney(double vipMoney) {
        this.vipMoney = vipMoney;
    }

    public String getPayURL() {
        return payURL;
    }

    public void setPayURL(String payURL) {
        this.payURL = payURL;
    }

    public ArrayList<OrderInfoBean.ProductSimple> getAttached() {
        return attached;
    }

    public void setAttached(ArrayList<OrderInfoBean.ProductSimple> attached) {
        this.attached = attached;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
}
