package com.example.epay.bean;

/**
 * Created by liujin on 2018/3/17.
 */

public class CotractBean {
    public int type=0;
    public String  iconURL="";
    public  String name="";
    public  String ratio="";


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}
