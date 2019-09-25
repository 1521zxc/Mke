package com.example.epay.bean;

import java.io.Serializable;

/**
 * Created by liujin on 2018/6/13.
 */

public class OrderMealLeftBean implements Serializable {
    private int ID=0;
    private String name="";

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
}
