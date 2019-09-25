package com.example.epay.bean;

import java.io.Serializable;

/**
 * Created by liujin on 2018/4/16.
 */

public class EnvelopesBean implements Serializable {
    private String name="";
    private String lingquSum="";
    private String shiSum="";
    private String allSum="";
    private String startTime="";
    private String endTime="";
    private String snall="";
    private String old="";
    private String moneyTime="";
    private String envelopeSum="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLingquSum() {
        return lingquSum;
    }

    public void setLingquSum(String lingquSum) {
        this.lingquSum = lingquSum;
    }

    public String getShiSum() {
        return shiSum;
    }

    public void setShiSum(String shiSum) {
        this.shiSum = shiSum;
    }

    public String getAllSum() {
        return allSum;
    }

    public void setAllSum(String allSum) {
        this.allSum = allSum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSnall() {
        return snall;
    }

    public void setSnall(String snall) {
        this.snall = snall;
    }

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public String getMoneyTime() {
        return moneyTime;
    }

    public void setMoneyTime(String moneyTime) {
        this.moneyTime = moneyTime;
    }

    public String getEnvelopeSum() {
        return envelopeSum;
    }

    public void setEnvelopeSum(String envelopeSum) {
        this.envelopeSum = envelopeSum;
    }
}
