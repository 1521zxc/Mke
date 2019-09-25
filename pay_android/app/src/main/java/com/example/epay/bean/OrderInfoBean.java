package com.example.epay.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.epay.util.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/14.
 */

public class OrderInfoBean implements Serializable {


    private int ID = 0;
    private String brandName = "";
    private long createTime = 0;
    private int payType = 0;
    private String orderNO = "";
    private String payNO = "";
    private double primeMoney = 0;//消费价格
    private double saleMoney = 0;//应收价格
    private double paidMoney = 0;//已收
    private double discountMoney = 0;//折扣
    private double refundMoney = 0;
    private double canDiscountMoney = 0;
    private double cantDiscountMoney = 0;
    private double canVipDiscountMoney = 0;
    //primeMoney   刚下单其余为0
    //刚支付    primeMoney-discountMoney=saleMoney=paidMoney;
    //primeMoney-saleMoney-discountMoney=primeMoney-paidMoney-discountMoney=  需要退需要补齐的   加菜或者退菜
    private double memberMoney = 0;
    private int serviceStatus = 0;
    private int payStatus = 0;
    private int ordinal = 0;
    private int customID = 0;
    private long payTime = 0;
    private int totalNum = 0;
    private String payURL = "";
    private String remark = "";
    private String deskNO = "";
    private String deskName = "";
    private double vipMoney = 0;
    private ContactInfo contactInfo=new ContactInfo();
    private ArrayList<ProductSimple> attached = new ArrayList<>();
    private ArrayList<printer> printers = new ArrayList<>();

    private ArrayList<PayNoBean> payments = new ArrayList();
    private ArrayList<OrderPayTypeBean> payTypes = new ArrayList();



    private long  updateTime=0;

    //预约字段
    private String userName="";
    private int sex=0;
    private double sum=0;
    private int state=0;
    private int orderID=0;
    private String phone="";
    private long attendTime=0;


    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getAttendTime() {
        return attendTime;
    }

    public void setAttendTime(long attendTime) {
        this.attendTime = attendTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public String getPayNO() {
        return payNO;
    }

    public void setPayNO(String payNO) {
        this.payNO = payNO;
    }

    public double getPrimeMoney() {
        return DateUtil.doubleValue(primeMoney);
    }

    public void setPrimeMoney(double primeMoney) {
        this.primeMoney = primeMoney;
    }

    public double getSaleMoney() {
        return DateUtil.doubleValue(saleMoney);
    }

    public void setSaleMoney(double saleMoney) {
        this.saleMoney = saleMoney;
    }

    public double getPaidMoney() {
        return DateUtil.doubleValue(paidMoney);
    }

    public void setPaidMoney(double paidMoney) {
        this.paidMoney = paidMoney;
    }

    public double getMemberMoney() {
        return DateUtil.doubleValue(memberMoney);
    }

    public void setMemberMoney(double memberMoney) {
        this.memberMoney = memberMoney;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public int getCustomID() {
        return customID;
    }

    public void setCustomID(int customID) {
        this.customID = customID;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getPayURL() {
        return payURL;
    }

    public void setPayURL(String payURL) {
        this.payURL = payURL;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeskNO() {
        return deskNO;
    }

    public void setDeskNO(String deskNO) {
        this.deskNO = deskNO;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public double getVipMoney() {
        return DateUtil.doubleValue(vipMoney);
    }

    public void setVipMoney(double vipMoney) {
        this.vipMoney = vipMoney;
    }

    public ArrayList<ProductSimple> getAttached() {
        return attached;
    }

    public void setAttached(ArrayList<ProductSimple> attached) {
        this.attached = attached;
    }

    public ArrayList<printer> getPrinters() {
        return printers;
    }

    public void setPrinters(ArrayList<printer> printers) {
        this.printers = printers;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public double getDiscountMoney() {
        return DateUtil.doubleValue(discountMoney);
    }

    public void setDiscountMoney(double discountMoney) {
        this.discountMoney = discountMoney;
    }

    public ArrayList<PayNoBean> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<PayNoBean> payments) {
        this.payments = payments;
    }

    public double getRefundMoney() {
        return DateUtil.doubleValue(refundMoney);
    }

    public void setRefundMoney(double refundMoney) {
        this.refundMoney = refundMoney;
    }


    public double getCanDiscountMoney() {
        return DateUtil.doubleValue(canDiscountMoney);
    }

    public void setCanDiscountMoney(double canDiscountMoney) {
        this.canDiscountMoney = canDiscountMoney;
    }


    public ArrayList<OrderPayTypeBean> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(ArrayList<OrderPayTypeBean> payTypes) {
        this.payTypes = payTypes;
    }

    public double getCantDiscountMoney() {
        return DateUtil.doubleValue(cantDiscountMoney);
    }

    public void setCantDiscountMoney(double cantDiscountMoney) {
        this.cantDiscountMoney = cantDiscountMoney;
    }

    public double getCanVipDiscountMoney() {
        return DateUtil.doubleValue(canVipDiscountMoney);
    }

    public void setCanVipDiscountMoney(double canVipDiscountMoney) {
        this.canVipDiscountMoney = canVipDiscountMoney;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public static class ProductSimple implements Serializable {
        private int ODID = 0;
        private int ID = 0;
        private String name = "";
        private double price = 0;
        private double count = 0;
        private ArrayList<ProdAttrItem> attrs = new ArrayList<>();
        private int memberPrice = 0;
        private int delFlag = 0;
        private String remark = "";
        private int isSub = 0;
        private int index = -1;

        public int getODID() {
            return ODID;
        }

        public void setODID(int ODID) {
            this.ODID = ODID;
        }

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

        public double getPrice() {
            return DateUtil.doubleValue(price);
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getCount() {
            return DateUtil.doubleValue(count);
        }

        public void setCount(double count) {
            this.count = count;
        }

        public ArrayList<ProdAttrItem> getAttrs() {
            return attrs;
        }

        public void setAttrs(ArrayList<ProdAttrItem> attrs) {
            this.attrs = attrs;
        }

        public int getMemberPrice() {
            return memberPrice;
        }

        public void setMemberPrice(int memberPrice) {
            this.memberPrice = memberPrice;
        }

        public int getIsSub() {
            return isSub;
        }

        public void setIsSub(int isSub) {
            this.isSub = isSub;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(int delFlag) {
            this.delFlag = delFlag;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public class ProdAttrItem implements Serializable {
            private int ID = 0;
            private String text = "";

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }

    public static class ProdAttrItem implements Serializable {

        private int ODID = 0;
        private double count = 0;

        @JSONField(name = "ODID")
        public int getODID() {
            return ODID;
        }

        public void setODID(int ODID) {
            this.ODID = ODID;
        }

        public double getCount() {
            return DateUtil.doubleValue(count);
        }

        public void setCount(double count) {
            this.count = count;
        }
    }


    public static class printer implements Serializable {
        private int ID = 0;
        private int type = 0;
        private String name = "";
        private String port = "";
        private int flag = 0;
        private int isPay = 0;
        private int isFull = 0;
        private int autoPrint = 0;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public int getIsPay() {
            return isPay;
        }

        public void setIsPay(int isPay) {
            this.isPay = isPay;
        }

        public int getIsFull() {
            return isFull;
        }

        public void setIsFull(int isFull) {
            this.isFull = isFull;
        }

        public int getAutoPrint() {
            return autoPrint;
        }

        public void setAutoPrint(int autoPrint) {
            this.autoPrint = autoPrint;
        }


    }


    public static class ContactInfo implements Serializable{
        private String fullName="";
        private String telephone="";
        private String region="";
        private String detail="";

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }


    }


}
