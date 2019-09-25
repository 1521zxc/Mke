package com.example.epay.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liujin on 2019/4/21.
 */

public class MemberBean implements Serializable{
    private int ID=0;
    private int type=0;
    private String userName="";
    private int flag=0;
    private double price=0;
    private long createTime=0;

    private long endTime=0;
    private int count=0;
    private ArrayList<conponeBean> items=new ArrayList<>();
    private ArrayList<MemberGiftBean> gifts=new ArrayList<>();

    private String cardName="";
    private String memberName="";
    private String phone="";
    private String cardNO="";
    private double money=0;
    private double credit=0;
    private double consumed=0;
    private String iconURL="";


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<conponeBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<conponeBean> items) {
        this.items = items;
    }


    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getConsumed() {
        return consumed;
    }

    public void setConsumed(double consumed) {
        this.consumed = consumed;
    }
    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }


    public ArrayList<MemberGiftBean> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<MemberGiftBean> gifts) {
        this.gifts = gifts;
    }
}
