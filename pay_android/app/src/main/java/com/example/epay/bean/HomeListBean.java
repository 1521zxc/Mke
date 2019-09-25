package com.example.epay.bean;


import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/29.
 */

public class HomeListBean {
    private String orderUrl="";
    private String payCodeURL="";
    private ArrayList<Deta>  actionCata=new ArrayList<Deta>();


    public String getOrderUrl() {
        return orderUrl;
    }

    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }

    public ArrayList<Deta> getActionCata() {
        return actionCata;
    }

    public void setActionCata(ArrayList<Deta> actionCata) {
        this.actionCata = actionCata;
    }

    public String getPayCodeURL() {
        return payCodeURL;
    }

    public void setPayCodeURL(String payCodeURL) {
        this.payCodeURL = payCodeURL;
    }

    public static class Deta{
        private String ID="";
        private String text="";
        private String iconURL="";
        private String link="";
        private String type="";
        private String action="";

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getIconURL() {
            return iconURL;
        }

        public void setIconURL(String iconURL) {
            this.iconURL = iconURL;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }


    }
}