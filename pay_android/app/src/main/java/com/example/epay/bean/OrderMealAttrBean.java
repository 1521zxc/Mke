package com.example.epay.bean;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.example.epay.util.DateUtil;

import java.io.FileDescriptor;
import java.io.Serializable;

/**
 * Created by liujin on 2018/6/13.
 */

public class OrderMealAttrBean  implements Serializable {

    private double price=0;
    private int type=0;
    private int ID=0;
    private int productID=0;
    private String name="";
    private boolean isch=false;

    public double getPrice() {
        return DateUtil.doubleValue(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsch() {
        return isch;
    }

    public void setIsch(boolean isch) {
        this.isch = isch;
    }

}
