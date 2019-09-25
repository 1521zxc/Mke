package com.example.epay.bean;

/**
 * Created by zyf on 16/5/2.
 */
public class User {

    public int userID=0;
    public String mobile="";
    public String nickName="";
    public String qiniuToken="";
    public String muuid="";
    public int onlinePay=0; // 店铺是否能支付
    public int isClose=0;//0是开    1是关   其他为开
    public int ID=0;
    public String cusPhone="";
    public String userName="";
    public String iconURL="";
    public String coverURL="";
    public String address="";
    public int userIsPay=0; // 账号是否可以结账
    public int userIsReceive=0;
    private int roleType=0;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getQiniuToken() {
        return qiniuToken;
    }

    public void setQiniuToken(String qiniuToken) {
        this.qiniuToken = qiniuToken;
    }

    public String getMuuid() {
        return muuid;
    }

    public void setMuuid(String muuid) {
        this.muuid = muuid;
    }

    public int getOnlinePay() {
        return onlinePay;
    }

    public void setOnlinePay(int onlinePay) {
        this.onlinePay = onlinePay;
    }

    public int getIsClose() {
        return isClose;
    }

    public void setIsClose(int isClose) {
        this.isClose = isClose;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUserIsPay() {
        return userIsPay;
    }

    public void setUserIsPay(int userIsPay) {
        this.userIsPay = userIsPay;
    }

    public int getUserIsReceive() {
        return userIsReceive;
    }

    public void setUserIsReceive(int userIsReceive) {
        this.userIsReceive = userIsReceive;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }
}
