package com.example.epay.base;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liujin on 2018/5/5.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;
    private int mLayoutId;

    public MyViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        //由于itemView是item的布局文件，我们需要的是里面的textView，因此利用itemView.findViewById获
        //取里面的View实例，后面通过onBindViewHolder方法能直接填充数据到每一个View了
        mContext = context;
        mConvertView = itemView;
        //运用泛型，适配所有的View，多布局，不用写多个RecyclerView.ViewHolder。
        mViews = new SparseArray<View>();
        mConvertView.setTag(this);

    }

    //缓存
    public static MyViewHolder get(Context context, View convertView,
                                   ViewGroup parent, int layoutId){

        if(null==convertView){
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            MyViewHolder holder = new MyViewHolder(context, itemView, parent);
            holder.mLayoutId = layoutId;
            return holder;
        }else {
            MyViewHolder holder = (MyViewHolder) convertView.getTag();
            return holder;
        }
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T find(int viewId)
    {
        View view = mViews.get(viewId);
        if (view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView()
    {
        return mConvertView;
    }
}
