package com.example.epay.bean;

import com.example.epay.util.DateUtil;

/**
 * Created by liujin on 2018/4/8.
 */

public class MemberLBean {
        private String ID="";
        private String nickName="";
        private String iconURL="";
        private String gender="";
        private String level="";
        private int totalNum=0;
        private double totalSum=0;
        private long latestBillTime=0;
        private String type="";

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getIconURL() {
            return iconURL;
        }

        public void setIconURL(String iconURL) {
            this.iconURL = iconURL;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public double getTotalSum() {
        return DateUtil.doubleValue(totalSum);
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }

    public long getLatestBillTime() {
            return latestBillTime;
        }

        public void setLatestBillTime(long latestBillTime) {
            this.latestBillTime = latestBillTime;
        }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
