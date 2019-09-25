package com.example.epay.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by liujin on 2018/6/24.
 */

public class RemarkBean implements Comparable<RemarkBean> , Serializable {
    private int ID=0;
    private String remarkNO="";
    private String name="";
    private boolean isch=false;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getRemarkNO() {
        return remarkNO;
    }

    public void setRemarkNO(String remarkNO) {
        this.remarkNO = remarkNO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsch() {
        return isch;
    }

    public void setIsch(boolean isch) {
        this.isch = isch;
    }

    @Override
    public int compareTo(@NonNull RemarkBean remarkBean) {
        return 0;
    }
}
