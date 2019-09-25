package com.example.epay.bean;

import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/11/6.
 */

public class InfosBean {


    private double today=0;
    private double paid=0;
    private double unpaid=0;
    private int attendNum=0;
    private int reserveNum=0;
    private double lastDay=0;
    private double thisWeek=0;
    private double thisMoneth=0;
    private double theDayBeforeWeek=0;
    private double lastWeak=0;
    private double lastMonth=0;
    private int ID=0;
    private ArrayList<sample> samples=new ArrayList<>();
    private double discount=0;
    public double getToday() {
        return DateUtil.doubleValue(today);
    }

    public void setToday(double today) {
        this.today = today;
    }

    public double getPaid() {
        return DateUtil.doubleValue(paid);
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getUnpaid() {
        return DateUtil.doubleValue(unpaid);
    }

    public void setUnpaid(double unpaid) {
        this.unpaid = unpaid;
    }

    public double getAttendNum() {
        return DateUtil.doubleValue(attendNum);
    }

    public void setAttendNum(int attendNum) {
        this.attendNum = attendNum;
    }

    public double getReserveNum() {
        return DateUtil.doubleValue(reserveNum);
    }

    public void setReserveNum(int reserveNum) {
        this.reserveNum = reserveNum;
    }

    public double getLastDay() {
        return DateUtil.doubleValue(lastDay);
    }

    public void setLastDay(double lastDay) {
        this.lastDay = lastDay;
    }

    public double getThisWeek() {
        return DateUtil.doubleValue(thisWeek);
    }

    public void setThisWeek(double thisWeek) {
        this.thisWeek = thisWeek;
    }

    public double getThisMoneth() {
        return DateUtil.doubleValue(thisMoneth);
    }

    public void setThisMoneth(double thisMoneth) {
        this.thisMoneth = thisMoneth;
    }

    public double getTheDayBeforeWeek() {
        return DateUtil.doubleValue(theDayBeforeWeek);
    }

    public void setTheDayBeforeWeek(double theDayBeforeWeek) {
        this.theDayBeforeWeek = theDayBeforeWeek;
    }

    public double getLastWeak() {
        return DateUtil.doubleValue(lastWeak);
    }

    public void setLastWeak(double lastWeak) {
        this.lastWeak = lastWeak;
    }

    public double getLastMonth() {
        return DateUtil.doubleValue(lastMonth);
    }

    public void setLastMonth(double lastMonth) {
        this.lastMonth = lastMonth;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getDiscount() {
        return DateUtil.doubleValue(discount);
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public ArrayList<sample> getSamples() {
        return samples;
    }


    public void setSamples(ArrayList<sample> samples) {
        this.samples = samples;
    }

    public class sample{
        private long datetime=0;
        private double value=0;


        public long getDatetime() {
            return datetime;
        }

        public void setDatetime(long datetime) {
            this.datetime = datetime;
        }

        public double getValue() {
            return DateUtil.doubleValue(value);
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}
