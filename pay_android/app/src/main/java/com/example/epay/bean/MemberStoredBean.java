package com.example.epay.bean;

import java.util.List;

public class MemberStoredBean {

    /**
     * count : 2
     * items : [{"ID":7,"name":"充值返利","price":200,"flag":1,"createTime":1548225303000,"fullMoney":0.02},
                 {"ID":9,"name":"买一送一","price":50,"flag":1,"createTime":1552544137000,"fullMoney":50}]
     */

    private int count;
    private List<ItemsBean> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * ID : 7
         * name : 充值返利
         * price : 200
         * flag : 1
         * createTime : 1548225303000
         * fullMoney : 0.02
         */

        private int ID;
        private String name;
        private int price;
        private int flag;
        private long createTime;
        private double fullMoney;
        private boolean isCheck=false;
        private int type=0;

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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public double getFullMoney() {
            return fullMoney;
        }

        public void setFullMoney(double fullMoney) {
            this.fullMoney = fullMoney;
        }

        public boolean getIsCheck() {
            return isCheck;
        }

        public void setIsCheck(boolean isCheck) {
            this.isCheck = isCheck;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
