package com.example.epay.bean;

/**
 * Created by liujin on 2018/3/7.
 */

public class TransferBean {
    private String status="";
    private String sum="";
    private long gainedDate=0;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getGainedDate() {
        return gainedDate;
    }

    public void setGainedDate(long gainedDate) {
        this.gainedDate = gainedDate;
    }




    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }
}
