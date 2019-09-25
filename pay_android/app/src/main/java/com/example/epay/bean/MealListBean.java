package com.example.epay.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.epay.util.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/13.
 */

public class MealListBean  implements Serializable {
    private String title="";
    private ArrayList<MealRight> mealRights=new ArrayList<>();

    public ArrayList<MealRight> getMealRights() {
        return mealRights;
    }

    public void setMealRights(ArrayList<MealRight> mealRights) {
        this.mealRights = mealRights;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class MealRight implements Serializable{
        private int soldCount=0;
        private double count=0;
        private double price=0;
        private String iconURL="";
        private int cataID=0;
        private int ID=0;
        private int sellStatus=0;
        private int setMeal=0;
        private String name="";
        private double vipPrice=0;
        private int number=0;
        private String spell="";
        private int lowSell=1;
        private int choose=0;
        private boolean isChoose=false;
        private ArrayList<OptionBean> optionItems=new ArrayList();
        private ArrayList<MealRight> subItems=new ArrayList();
        private ArrayList<RemarkBean> remark=new ArrayList<>();
        private ArrayList<OrderMealAttrBean> attrs=new ArrayList<>();

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public ArrayList<OptionBean> getOptionItems() {
            return optionItems;
        }

        public void setOptionItems(ArrayList<OptionBean> optionItems) {
            this.optionItems = optionItems;
        }

        public ArrayList<MealRight> getSubItems() {
            return subItems;
        }

        public void setSubItems(ArrayList<MealRight> subItems) {
            this.subItems = subItems;
        }

        public int getSoldCount() {
            return soldCount;
        }

        public void setSoldCount(int soldCount) {
            this.soldCount = soldCount;
        }



        public String getIconURL() {
            return iconURL;
        }

        public void setIconURL(String iconURL) {
            this.iconURL = iconURL;
        }

        public int getCataID() {
            return cataID;
        }

        public void setCataID(int cataID) {
            this.cataID = cataID;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getSellStatus() {
            return sellStatus;
        }

        public void setSellStatus(int sellStatus) {
            this.sellStatus = sellStatus;
        }

        public int getSetMeal() {
            return setMeal;
        }

        public void setSetMeal(int setMeal) {
            this.setMeal = setMeal;
        }

        public int getChoose() {
            return choose;
        }

        public void setChoose(int choose) {
            this.choose = choose;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return DateUtil.doubleValue(price);
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getVipPrice() {
            return DateUtil.doubleValue(vipPrice);
        }

        public void setVipPrice(double vipPrice) {
            this.vipPrice = vipPrice;
        }

        public ArrayList<OrderMealAttrBean> getAttrs() {
            return attrs;
        }

        public void setAttrs(ArrayList<OrderMealAttrBean> attrs) {
            this.attrs = attrs;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public ArrayList<RemarkBean> getRemark() {
            return remark;
        }

        public void setRemark(ArrayList<RemarkBean> remark) {
            this.remark = remark;
        }

        public int getLowSell() {
            return lowSell;
        }

        public void setLowSell(int lowSell) {
            this.lowSell = lowSell;
        }

        public String getSpell() {
            return spell;
        }

        public void setSpell(String spell) {
            this.spell = spell;
        }
    }
}
