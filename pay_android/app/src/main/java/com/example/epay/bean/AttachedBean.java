package com.example.epay.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/14.
 */

public class AttachedBean implements Serializable {
    private int ID=0;
    private int count=0;
    private ArrayList<SubItemBean> subItems=new ArrayList<>();
    private ArrayList<AttrBean> attrs=new ArrayList<>();
    private String remark="";

    @JSONField(name="ID")
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<AttrBean> getAttrs() {
        return attrs;
    }

    public void setAttrs(ArrayList<AttrBean> attrs) {
        this.attrs = attrs;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ArrayList<SubItemBean> getSubItems() {
        return subItems;
    }

    public void setSubItems(ArrayList<SubItemBean> subItems) {
        this.subItems = subItems;
    }

    public static class AttrBean{
        private int ID=0;

        @JSONField(name="ID")
        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }
    }

    public static class SubItemBean {
        private int ID=0;
        private double count=0;

        @JSONField(name="ID")
        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }
    }
}
