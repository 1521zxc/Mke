package com.example.epay;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.Toast;

import androidx.multidex.MultiDex;
import com.example.epay.getui.AppStatusTracker;
import com.example.epay.getui.CustomerApp;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import java.util.Map;

/**
 * Created by liujin on 2018/1/17.
 */
public class BaseApplication extends Application {

    //设置项目是否是测试
    private static boolean isTest = true; //true是测试，false线上
    public static Map<String, Long> map;
    public static boolean isTest() {
        return isTest;
    }
    public static void setIsTest(boolean Test)
    {BaseApplication.isTest=Test;}
    public static BaseApplication mInstance = null;
    public static DisplayImageOptions options;
    private static CustomerApp _instance;
    private AppStatusTracker appStatusTracker;

    private static Context context;

    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mInstance = this;
        SpeechUtility.createUtility(context, SpeechConstant.APPID +"=5d4a34af");
        initBitmapUtils();
        share();
    }
    public static void show(String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
    private void share() {
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
//        UMConfigure.setLogEnabled(true);
//        UMConfigure.setEncryptEnabled(true);
        PlatformConfig.setWeixin("wxb23a820537c2a2d3", "9ef81bbc7a47a7ee6726e6c1313ed2c3");
        PlatformConfig.setQQZone("1106786334", "82hXpwKPffRryCwE");
        PlatformConfig.setSinaWeibo("758702315", "db1914e5ccdf4f424de8ac98e6e366ef",  "https://mobile.baidu.com/item?pid=3331881");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 获得当前进程的名字
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static BaseApplication getInstance(){
        return  mInstance;
    }

    public int getUserId(){
        SharedPreferences user_pre = getSharedPreferences("user_pre", 0);
        return user_pre.getInt("user_id", 0);
    }

    public void setUserId(int userId){
        SharedPreferences user_pre = getSharedPreferences("user_pre", 0);
        SharedPreferences.Editor editor = user_pre.edit();
        editor.putInt("user_id", userId);
        editor.commit();

    }

    //初始化图片加载
    private void initBitmapUtils(){
         options=new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon) //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//缓存到内存中
                .cacheOnDisk(true)//缓存到sd卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(new RoundedBitmapDisplayer(2))//是否设置为圆角，弧度为多少
//                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .bitmapConfig(Bitmap.Config.RGB_565 )//设置图片的解码类型//</span>
                .build();//构建完成

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(options)
                .imageDownloader(
                        new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    //设置字体不随着系统设置变化而变化（一）
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)
            getResources();
        super.onConfigurationChanged(newConfig);
    }
    //设置字体不随着系统设置变化而变化（二）
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();

            res.updateConfiguration(newConfig, res.getDisplayMetrics());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                createConfigurationContext(newConfig);
            } else {
                res.updateConfiguration(newConfig, res.getDisplayMetrics());
            }
        }
        return res;
    }



}
