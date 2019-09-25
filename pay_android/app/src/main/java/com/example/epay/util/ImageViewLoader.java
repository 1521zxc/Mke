package com.example.epay.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.epay.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by liujin on 2018/1/17.
 */

public class ImageViewLoader {
    static ImageLoader imageLoader = ImageLoader.getInstance();
    static RequestOptions options = new RequestOptions()
            .fitCenter()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .priority(Priority.HIGH)
            .skipMemoryCache(false)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .diskCacheStrategy(DiskCacheStrategy.NONE);

    public static void load(String url, ImageView imageView, final  ImageView.ScaleType scaleType){
        imageView.setScaleType(scaleType);
        imageLoader.displayImage(url, imageView);
    }
    public static void load(String url,ImageView imageView){
        imageLoader.displayImage(url, imageView);
    }
    public static void loadLogo(String url,ImageView imageView)
    {
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo_default) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.logo_default)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.logo_default)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .bitmapConfig(Bitmap.Config.RGB_565 )//设置图片的解码类型//</span>
                .build();//构建完成
        imageLoader.displayImage(url, imageView,options);
    }
    public static void loadCircle(String url,ImageView imageView,int round){
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_round) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_round)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_round)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(new Displayer(round))
                .bitmapConfig(Bitmap.Config.RGB_565 )//设置图片的解码类型//</span>
                .build();//构建完成
        imageLoader.displayImage(url, imageView,options);
    }
    public static void load(String url,ImageView imageView,int round){
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565 )//设置图片的解码类型//</span>
                .build();//构建完成
        imageLoader.displayImage(url, imageView,options);
    }


    public static void loadSp(String url,ImageView imageView){
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_bg_defult) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_bg_defult)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_bg_defult)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565 )//设置图片的解码类型//</span>
                .build();//构建完成
        imageLoader.displayImage(url, imageView,options);
    }

    // 加载本地引导图片
    public static void loadGuide(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .apply(options)
                .into(imageView);
    }
}
