package com.example.epay.bean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/16.
 */

public class CatatypeBean {
    private int count=0;
    private ArrayList<catatype> items=new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<catatype> getItems() {
        return items;
    }

    public void setItems(ArrayList<catatype> items) {
        this.items = items;
    }

    public static class catatype{
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

}
