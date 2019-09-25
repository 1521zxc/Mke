package com.example.epay.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.example.epay.bean.BeanNoti;
import com.example.epay.cache.CacheData;

import java.util.ArrayList;

/**
 * Created by liujin on 2015/7/31.
 */

public class NotiUtils {

    private static ArrayList<BeanNoti> notis = new ArrayList<BeanNoti>();
    private static ArrayList<BeanNoti> systemNoti = new ArrayList<BeanNoti>();
    private static ArrayList<BeanNoti> orderNoti = new ArrayList<BeanNoti>();
    private static ArrayList<BeanNoti> activityNoti = new ArrayList<BeanNoti>();

    public static void put(Context con, String title, String url, Intent intent) {
        NotificationUtil.sendNotification(con, title,url,intent, false);
    }

    public static void put(Context con, BeanNoti noti, Intent intent) {
        Log.i("NotiUtils",noti.toString() );
        if (noti == null)
            return;
        noti.setTime(System.currentTimeMillis()/1000);
        switch (noti.getType()) {
            case 0:
                Log.d("系统通知",noti.toString() );
                noti.setTitle("系统通知");
                systemNoti= CacheData.getNotis(con,noti.getType());
                systemNoti.add(noti);
                CacheData.cacheNotis(con, systemNoti, 0);
               // if(EasyUtils.isAppRunningForeground(con)==false)
                    NotificationUtil.sendNotification(con, "系统通知","",intent, false);
                break;
            case 1:
                Log.d("订单通知",noti.toString() );
                noti.setTitle("订单通知");
                orderNoti= CacheData.getNotis(con,noti.getType());
                orderNoti.add(noti);
                CacheData.cacheNotis(con, orderNoti, 1);
               // if(EasyUtils.isAppRunningForeground(con)==false)
                    NotificationUtil.sendNotification(con, "订单通知","",intent, false);
                break;
            case 2:
                Log.d("活动通知",noti.toString() );
                noti.setTitle("活动通知");
                activityNoti= CacheData.getNotis(con,noti.getType());
                activityNoti.add(noti);
                CacheData.cacheNotis(con, activityNoti, 3);
               // if(EasyUtils.isAppRunningForeground(con)==false)
                    NotificationUtil.sendNotification(con, "活动通知","",intent, false);
                break;
        }
    }

    public static ArrayList<BeanNoti> getNotis(Context con, int type) {
        return CacheData.getNotis(con, type);
    }

    public static void removeNoti(Context con, int i){
        ArrayList<BeanNoti> notis= CacheData.getNotis(con, -1);
        int type = 0;
        if(i<notis.size()){
            type=notis.get(i).getType();
            notis.remove(i);
            CacheData.cacheNotis(con, notis, -1);
        }
        switch (type) {
            case 0:
                CacheData.cacheNotis(con, new ArrayList<BeanNoti>(), 0);
                break;
            case 1:
                CacheData.cacheNotis(con, new ArrayList<BeanNoti>(), 1);
                break;

            case 2:
                CacheData.cacheNotis(con, new ArrayList<BeanNoti>(), 2);
                break;
        }
    }
}
