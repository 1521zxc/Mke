package com.example.epay.bean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/11/6.
 */

public class StatisticListBean {
    private ArrayList<InfosBean> infos=new ArrayList<>();
    private ArrayList<storesBean> stores=new ArrayList<>();

    public ArrayList<InfosBean> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<InfosBean> infos) {
        this.infos = infos;
    }

    public ArrayList<storesBean> getStores() {
        return stores;
    }

    public void setStores(ArrayList<storesBean> stores) {
        this.stores = stores;
    }
}
