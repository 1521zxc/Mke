package com.example.epay.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.epay.R;
import com.example.epay.adapter.FenXiangGridAdapter;
import com.example.epay.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class RecomActivity  extends BaseActivity{

    private ImageView quxiao_iv;
    private GridView gridView;
    FenXiangGridAdapter adapter;
    String[] titles = {"微信", "朋友圈", "QQ空间", "QQ", "新浪微博","复制链接"};
    int[] list = {R.drawable.circle_weixin, R.drawable.weixin_circle, R.drawable.qq_zone, R.drawable.qq, R.drawable.sina, R.drawable.icon_cop};
    Context context = this;
    RelativeLayout ll;
    String url="",title1="",message1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recom);
        ll = (RelativeLayout) findViewById(R.id.alert_ll);
        url=getIntent().getStringExtra("url");
        title1=getIntent().getStringExtra("title");
        message1=getIntent().getStringExtra("message");
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(params);
        gridView = (GridView) findViewById(R.id.fenxiang_gridView);
        init();

    }

    private void init() {
        adapter = new FenXiangGridAdapter(gridView, this, list, titles);
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UMWeb  web = new UMWeb(url);
                web.setTitle(title1);//标题
                web.setDescription(message1);//描述
                web.setThumb(new UMImage(RecomActivity.this, R.drawable.icon));
                switch (i)
                {
                    case 0:
                        new ShareAction(RecomActivity.this)
                                .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                                .withMedia(web)//分享内容
                                .setCallback(shareListener)//回调监听器
                                .share();
                        break;
                    case 1:
                        new ShareAction(RecomActivity.this)
                                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                                .withMedia(web)//分享内容
                                .setCallback(shareListener)//回调监听器
                                .share();
                        break;
                    case 2:
                        new ShareAction(RecomActivity.this)
                                .setPlatform(SHARE_MEDIA.QZONE)//传入平台
                                .withMedia(web)//分享内容
                                .setCallback(shareListener)//回调监听器
                                .share();
                        break;
                    case 3:
                        new ShareAction(RecomActivity.this)
                                .setPlatform(SHARE_MEDIA.QQ)//传入平台
                                .withMedia(web)//分享内容
                                .setCallback(shareListener)//回调监听器
                                .share();
                        break;
                    case 4:
                        new ShareAction(RecomActivity.this)
                                .setPlatform(SHARE_MEDIA.SINA)//传入平台
                                .withMedia(web)//分享内容
                                .setCallback(shareListener)//回调监听器
                                .share();
                        break;
                    case 5:
                        ClipboardManager clipboard = (ClipboardManager)
                                getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("",url);
                        clipboard.setPrimaryClip(clip);
                        break;
                }
            }
        });
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
          //  Toast.makeText(RecomActivity.this,"成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(RecomActivity.this,"失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
           // Toast.makeText(RecomActivity.this,"取消了",Toast.LENGTH_LONG).show();

        }
    };



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }



    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("推荐给好友"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("推荐给好友"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
