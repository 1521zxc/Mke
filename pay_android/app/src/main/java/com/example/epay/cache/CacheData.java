package com.example.epay.cache;

import android.content.Context;

import com.example.epay.activity.SpecialActivity;
import com.example.epay.bean.BeanNoti;
import com.example.epay.bean.CashflowListBean2;
import com.example.epay.bean.HomeListBean;
import com.example.epay.bean.MealListBean;
import com.example.epay.bean.MessageBean;
import com.example.epay.bean.SpecialBean;
import com.example.epay.bean.TransferBean;
import com.example.epay.bean.User;
import com.example.epay.bean.UserBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * 缓存app数据
 *
 * Created by liujin on 2018/1/17.
 */
public class CacheData extends Cache {
    static Gson gson = new Gson();
    /**
     * 是否首次使用
     * @param context
     * @param boo
     */
    public static void setGuide(Context context,Boolean boo) {
        putBoolean(context, "setGuide", boo);
    }

    public static boolean isGuide(Context context) {
        return getBoolean(context,"setGuide");
    }

    /**
     * 是否屏幕常亮
     * @param context
     * @param boo
     */
    public static void setOpen(Context context,Boolean boo) {
        putBoolean(context, "setOpen", boo);
    }

    public static boolean isOpen(Context context) {
        return getBoolean(context,"setOpen");
    }

    /**
     * 缓存登录状态
     * @param context
     * @return
     */
    public static void setLoginstate(Context context,Boolean loginstate) {
        putBoolean(context, "loginstate", loginstate);
    }

    /**
     * 获取是否登录
     * @param context
     * @return
     */
    public static Boolean getLoginstate(Context context ) {
        Boolean str = getBoolean(context, "loginstate");
        if (str==null)
        {
            str=false;
        }
        return str;
    }

    /**
     * 缓存登录Token
     * @param context
     * @return
     */
    public static void setToken(Context context,String token) {
        putString(context, "token", token);
    }

    /**
     * 获取登录Token
     * @param context
     * @return
     */
    public static String getToken(Context context ) {
        String str = getString(context, "token");
        if (str==null)
        {
            str="";
        }
        return str;
    }

    /**
     * 缓存登录id
     * @param context
     * @return
     */
    public static void setId(Context context,int id) {
        putInt(context, "id", id);
    }

    /**
     * 获取登录id
     * @param context
     * @return
     */
    public static int getId(Context context ) {
        int str = getInt(context, "id");
        return str;
    }

    /**
     * 缓存登陆用户对象
     *  @param context
     * @param user
     * @param userId
     */
    public static void cacheUser(Context context, User user, int userId) {
        putString(context, "cacheUser"+userId, gson.toJson(user));
    }

    /**
     * 获取登陆用户对象
     * @param context
     * @return
     */
    public static User getUser(Context context,String userId) {
            String str = getString(context, "cacheUser"+userId);
            User user = gson.fromJson(str, User.class);
            if (user == null)
                user = new User();
            return user;
    }

    /**
     * 缓存手机clientId
     * @param context
     * @return
     */
    public static void setClientId(Context context,String clientId) {
        putString(context, "clientId", clientId);
    }

    /**
     * 获取登录Token
     * @param context
     * @return
     */
    public static String getClientId(Context context ) {
        String str = getString(context, "clientId");
        if (str==null)
        {
            str="";
        }
        return str;
    }

    /**
     * 保存流水记录
     * @param context
     * @param bases
     */
    public static void cacheCashflowBeans(Context context,
                                          ArrayList<CashflowListBean2> bases, String id) {
        Gson gson = new Gson();
        putString(context, "cacheCashflowBeans"+id, gson.toJson(bases));
    }

    public static ArrayList<CashflowListBean2> getCashflowBeans(Context context, String id) {
        Gson gson = new Gson();
        String str = getString(context, "cacheCashflowBeans"+id);
        Type type = new TypeToken<ArrayList<CashflowListBean2>>() {
        }.getType();
        ArrayList<CashflowListBean2> bases = gson.fromJson(str, type);
        if (bases == null)
            bases = new ArrayList<CashflowListBean2>();
        return bases;
    }

    /**
     * 保存划款记录
     * @param context
     * @param bases
     */
    public static void cacheTransferBeans(Context context,
                                      ArrayList<TransferBean> bases) {
        Gson gson = new Gson();
        putString(context, "cacheTransferBeans", gson.toJson(bases));
    }

    public static ArrayList<TransferBean> getTransferBeans(Context context) {
        Gson gson = new Gson();
        String str = getString(context, "cacheTransferBeans");
        Type type = new TypeToken<ArrayList<TransferBean>>() {
        }.getType();
        ArrayList<TransferBean> bases = gson.fromJson(str, type);
        if (bases == null)
            bases = new ArrayList<TransferBean>();
        return bases;
    }

    public static void cacheSpecialBeans(SpecialActivity specialActivity, ArrayList<SpecialBean> arrayList) {
    }

    /**
     * 保存官方活动
     * @param context
     * @param bases
     */
    public static void cacheSpecialBeans(Context context,
                                          ArrayList<SpecialBean> bases) {
        Gson gson = new Gson();
        putString(context, "cacheSpecialBeans", gson.toJson(bases));
    }

    public static ArrayList<SpecialBean> getSpecialBeans(Context context) {
        Gson gson = new Gson();
        String str = getString(context, "cacheSpecialBeans");
        Type type = new TypeToken<ArrayList<SpecialBean>>() {
        }.getType();
        ArrayList<SpecialBean> bases = gson.fromJson(str, type);
        if (bases == null)
            bases = new ArrayList<SpecialBean>();
        return bases;
    }




    /**
     * 保存官方活动
     * @param context
     * @param giftListString
     */
    public static void cacheMemberGiftList(Context context,
                                         String giftListString) {
        putString(context, "cacheMemberGiftList", giftListString);
    }

    public static String getMemberGiftList(Context context) {
        String str = getString(context, "cacheMemberGiftList");
        if (str==null)
        {
            str="";
        }
        return str;
    }

    /**
     * 保存消息
     * @param context
     * @param bases
     */
    public static void cacheMessageBeans(Context context,
                                         ArrayList<MessageBean> bases) {
        Gson gson = new Gson();
        putString(context, "cacheMessageBeans", gson.toJson(bases));
    }

    public static ArrayList<MessageBean> getMessageBeans(Context context) {
        Gson gson = new Gson();
        String str = getString(context, "cacheMessageBeans");
        Type type = new TypeToken<ArrayList<MessageBean>>() {
        }.getType();
        ArrayList<MessageBean> bases = gson.fromJson(str, type);
        if (bases == null)
            bases = new ArrayList<MessageBean>();
        return bases;
    }
    /**
     * 保存个人资料
     * @param context
     * @param
     */
    public static void cacheMyBeans(Context context,
                                    UserBean UserBean) {
        Gson gson = new Gson();
        putString(context, "cacheMyBean", gson.toJson(UserBean));
    }

    public static UserBean getMyBeans(Context context) {
        Gson gson = new Gson();
        String str = getString(context, "cacheMyBean");
        Type type = new TypeToken<UserBean>() {
        }.getType();
        UserBean bases = gson.fromJson(str, type);
        if (bases == null)
            bases = new UserBean();
        return bases;
    }

    /**
     * 保存首页
     * @param context
     * @param
     */
    public static void cacheHomeBeans(Context context,
                                    HomeListBean homeBean) {
        Gson gson = new Gson();
        putString(context, "cacheHomeBean", gson.toJson(homeBean));
    }

    public static HomeListBean getHomeBeans(Context context) {
        Gson gson = new Gson();
        String str = getString(context, "cacheHomeBean");
        Type type = new TypeToken<HomeListBean>() {
        }.getType();
        HomeListBean bases = gson.fromJson(str, type);
        if (bases == null)
            bases = new HomeListBean();
        return bases;
    }

    /**
     * 保存通知
     * @param context
     * @param bases
     */
    public static void cacheNotis(Context context,
                                  ArrayList<BeanNoti> bases, int notiType) {
        Gson gson = new Gson();
        UserBean user=CacheData.getMyBeans(context);
        putString(context, "cacheNotis"+user.getID()+notiType, gson.toJson(bases));
    }

    public static ArrayList<BeanNoti> getNotis(Context context,int notiType) {
        UserBean user=CacheData.getMyBeans(context);
        Gson gson = new Gson();
        String str = getString(context, "cacheNotis"+user.getID()+notiType);
        Type type = new TypeToken<ArrayList<BeanNoti>>() {
        }.getType();
        ArrayList<BeanNoti> bases = gson.fromJson(str, type);
        if (bases == null)
            bases = new ArrayList<BeanNoti>();
        return bases;
    }

    /**
     * 保存流水记录
     * @param context
     */
    public static void cacheMealBean(Context context,
                                     ArrayList<MealListBean.MealRight> mealRights) {
        Gson gson = new Gson();
        putString(context, "cacheMealRight", gson.toJson(mealRights));
    }

    public static ArrayList<MealListBean.MealRight> getMealBean(Context context) {
        Gson gson = new Gson();
        String str = getString(context, "cacheMealRight");
        Type type = new TypeToken<ArrayList<MealListBean.MealRight>>() {
        }.getType();
        ArrayList<MealListBean.MealRight> bases = gson.fromJson(str, type);
        if (bases == null)
            bases = new ArrayList<MealListBean.MealRight>();
        return bases;
    }
}
