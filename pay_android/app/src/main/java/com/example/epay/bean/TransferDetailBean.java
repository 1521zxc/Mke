package com.example.epay.bean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/7.
 */

public class TransferDetailBean {
    private String totalSum="";
    private String totalTransfer="";
    private String serviceFee="";
    private String weixinRatio="";
    private String aliRatio="";
    private String qqRatio=":0.35,";
    private long gainedDate=0;
    private ArrayList<TransferList> details=new ArrayList<TransferList>();

    public String getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(String totalSum) {
        this.totalSum = totalSum;
    }

    public String getTotalTransfer() {
        return totalTransfer;
    }

    public void setTotalTransfer(String totalTransfer) {
        this.totalTransfer = totalTransfer;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getWeixinRatio() {
        return weixinRatio;
    }

    public void setWeixinRatio(String weixinRatio) {
        this.weixinRatio = weixinRatio;
    }

    public String getAliRatio() {
        return aliRatio;
    }

    public void setAliRatio(String aliRatio) {
        this.aliRatio = aliRatio;
    }

    public String getQqRatio() {
        return qqRatio;
    }

    public void setQqRatio(String qqRatio) {
        this.qqRatio = qqRatio;
    }

    public long getGainedDate() {
        return gainedDate;
    }

    public void setGainedDate(long gainedDate) {
        this.gainedDate = gainedDate;
    }

    public ArrayList<TransferList> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<TransferList> details) {
        this.details = details;
    }
}
