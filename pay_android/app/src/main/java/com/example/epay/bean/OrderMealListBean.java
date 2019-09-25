package com.example.epay.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/13.
 */

public class OrderMealListBean implements Serializable {
    private int productCount=0;
    private ArrayList<OrderMealRightBean> products=new ArrayList<>();
    private int count=0;
    private ArrayList<OrderMealLeftBean> items=new ArrayList<>();
    private ArrayList<OrderMealAttrBean> productAttrs=new ArrayList<>();
    private ArrayList<RemarkBean> productRemarks=new ArrayList<>();

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public ArrayList<OrderMealRightBean> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<OrderMealRightBean> products) {
        this.products = products;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<OrderMealLeftBean> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderMealLeftBean> items) {
        this.items = items;
    }

    public ArrayList<OrderMealAttrBean> getProductAttrs() {
        return productAttrs;
    }

    public void setProductAttrs(ArrayList<OrderMealAttrBean> productAttrs) {
        this.productAttrs = productAttrs;
    }

    public ArrayList<RemarkBean> getProductRemarks() {
        return productRemarks;
    }

    public void setProductRemarks(ArrayList<RemarkBean> productRemarks) {
        this.productRemarks = productRemarks;
    }
}
