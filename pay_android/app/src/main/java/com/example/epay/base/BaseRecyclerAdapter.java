package com.example.epay.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.epay.doHttp.HttpUtil;
import com.example.epay.util.ImageViewLoader;

import java.util.ArrayList;


/**
 * Created by liujin on 2018/5/5.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<MyViewHolder>{
    public String TAG;
    private ArrayList<T> list;
    public Activity context;
    private int mLayoutId;
    public int w;
    public int h;
//    public Typeface BoldTtf;
//    public Typeface mediumTtf;
    public HttpUtil httpUtil=new HttpUtil();
    public BaseRecyclerAdapter(ArrayList<T> list, Activity context, int mLayoutId){
        this.list=list;
        this.context=context;
        this.mLayoutId=mLayoutId;
//        BoldTtf = Typeface.createFromAsset (context.getAssets(), "font/Bold.ttf");
//        mediumTtf= Typeface.createFromAsset (context.getAssets(), "font/medium.ttf");
        w = context.getWindowManager().getDefaultDisplay().getWidth();
        h = context.getWindowManager().getDefaultDisplay().getHeight();
        TAG = this.getClass().getSimpleName();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = MyViewHolder.get(context,null,parent,mLayoutId);
        setListener(parent, holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        coner(holder,list.get(position),position);
    }
    public abstract void coner(MyViewHolder holder, T bean,int position);

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<T> list) {
         this.list=list;
         this.notifyDataSetChanged();
    }

    public void log(String log) {
        Log.i(TAG, log);
    }

    public void startActivity(Class<?> actClass) {
        Intent intent = new Intent(context, actClass);
        context.startActivity(intent);
    }

    /**
     * java回调机制，依赖于子Item View的onClickListener及onLongClickListener。
     * @param <T> //数据类
     */
    public interface OnItemClickListener<T>{
        //RecyclerView监听
        void onItemClick(ViewGroup parent, View view,  int position);
    }
    public interface OnItemLongClickListener<T>{
        //长按监听
        boolean onItemLongClick(ViewGroup parent, View view,  int position);
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    protected void setListener(final ViewGroup parent, final MyViewHolder holder){
        //判断是否设置了监听器
        if(mOnItemClickListener != null) {
            //为ItemView设置监听器
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(parent, v,  position);
                }
            });
        }
        if(mOnItemLongClickListener != null){
            holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        int position = holder.getLayoutPosition();
                        return mOnItemLongClickListener.onItemLongClick(parent, v,  position);
                        //返回true 表示消耗了事件 事件不会继续传递
                    }

                    return false;
                }
            });
        }
    }


    public static void iconSize(Drawable drawable, double Multiple, Button view, int width, int Position)
    {
        drawable.setBounds(0,0,(int)(Multiple*width),(int)(Multiple*width));
        if(Position==0)
        {
            view.setCompoundDrawables(drawable,null,null,null);
        }else if(Position==1)
        {
            view.setCompoundDrawables(null,drawable,null,null);
        }else if(Position==2)
        {
            view.setCompoundDrawables(null,null,drawable,null);
        }else{
            view.setCompoundDrawables(null,null,null,drawable);
        }
    }


    public void loadBitmap(String uri, ImageView imageView) {
        ImageViewLoader.load(uri, imageView, ImageView.ScaleType.FIT_XY);

    }
    public void loadBitmap(String uri, ImageView imageView,ImageView.ScaleType scaleType) {
        ImageViewLoader.load(uri, imageView, scaleType);

    }
    public void loadGuide(int resId, ImageView imageView){
        ImageViewLoader.loadGuide(context,resId,imageView);
    }
    public void load(String uri, ImageView imageView,int round) {
        ImageViewLoader.load(uri, imageView,round);

    }
    public void loadCircle(String uri, ImageView imageView,int round) {
        ImageViewLoader.loadCircle(uri, imageView,round);

    }
    public void showMessage(String message) {
        if (context == null || message == null||message.trim().equals(""))
            return;
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }
}
