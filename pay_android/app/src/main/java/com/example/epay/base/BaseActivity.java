package com.example.epay.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.epay.doHttp.HttpUtil;
import com.example.epay.service.DemoPushService;
import com.example.epay.service.MyPushIntentService;
import com.example.epay.util.ImageViewLoader;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liujin on 2018/1/17.
 */
//继承fragmentActivity
public class BaseActivity extends FragmentActivity {

    public int width;
    public int height;
    public Gson gson = new Gson();
    public String TAG = "epay";
    public HttpUtil httpUtil = new HttpUtil();

    public void initView() {
        width = getWindowManager().getDefaultDisplay().getWidth();
        height = getWindowManager().getDefaultDisplay().getHeight();
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), MyPushIntentService.class);
        this.startService(new Intent(this, DemoPushService.class));
        this.startService(new Intent(this, MyPushIntentService.class));
         List<Integer> colorData = new ArrayList<>();
        colorData.add(Color.parseColor("#d200ff"));
        colorData.add(Color.parseColor("#ed98ff"));
        colorData.add(Color.parseColor("#ff00c6"));
        colorData.add(Color.parseColor("#ba0090"));
        colorData.add(Color.parseColor("#ffd9f7"));
        colorData.add(Color.parseColor("#ff006c"));
        colorData.add(Color.parseColor("#b2004b"));
        colorData.add(Color.parseColor("#ff60a3"));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void loadBitmap(String uri, ImageView imageView) {
        ImageViewLoader.load(uri, imageView, ImageView.ScaleType.FIT_XY);
    }

    public void loadLogo(String uri, ImageView imageView) {
        ImageViewLoader.loadLogo(uri, imageView);
    }

    public void loadCircle(String uri, ImageView imageView, int round) {
        ImageViewLoader.loadCircle(uri, imageView, round);
    }

    public void load(String uri, ImageView imageView, int round) {
        ImageViewLoader.load(uri, imageView, round);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void showMessage(String message) {
        if (this == null || message == null || message.trim().equals(""))
            return;
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void startActivity(Context context, Class<?> actIntent) {
        Intent intent = new Intent(context, actIntent);
        context.startActivity(intent);
    }

    //判断手机格式是否正确
    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((1[3-8][0-9])\\d{8}$)");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 显示view
     * @param id
     */
    protected void showView(int id) {
        this.findViewById(id).setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏view
     * @param id
     */
    protected void hideView(int id) {
        this.findViewById(id).setVisibility(View.GONE);
    }

    /**
     * 隐藏view  占空间
     * @param id
     */
    protected void InVisibleView(int id) {
        this.findViewById(id).setVisibility(View.INVISIBLE);
    }

}
