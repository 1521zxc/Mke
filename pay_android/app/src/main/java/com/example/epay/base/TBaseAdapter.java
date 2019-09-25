package com.example.epay.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.example.epay.util.ImageViewLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujin on 2018/1/17.
 */
//<T>  指泛型可传任意参数
public abstract class TBaseAdapter<T> extends BaseAdapter {
    public String TAG;
    public Activity context;
    private LayoutInflater inflater;
    public ArrayList<T> list;
    public View view;
    public int w;
    public int h;
    public int index = 1;
    public List<Integer> colorData = new ArrayList<>();
    /**
     * @param context 环境
     * @param list    泛指一个bean类任何
     */
    public TBaseAdapter(Activity context, ArrayList<T> list) {
        this.context = context;
        TAG = this.getClass().getSimpleName();
        TAG ="fref";
        this.list = list;
        if (this.list == null)
            this.list = new ArrayList<T>();
        w = context.getWindowManager().getDefaultDisplay().getWidth();
        h = context.getWindowManager().getDefaultDisplay().getHeight();
        inflater = this.context.getLayoutInflater();

        colorData.add(Color.parseColor("#d200ff"));
        colorData.add(Color.parseColor("#ed98ff"));
        colorData.add(Color.parseColor("#ff00c6"));
        colorData.add(Color.parseColor("#ba0090"));
        colorData.add(Color.parseColor("#ffd9f7"));
        colorData.add(Color.parseColor("#ff006c"));
        colorData.add(Color.parseColor("#b2004b"));
        colorData.add(Color.parseColor("#ff60a3"));
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    public View initConvertView(int resource) {
        return inflater.inflate(resource, null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void log(String log) {
        Log.e(TAG, log);
    }

    public void loadBitmap(String uri, ImageView imageView) {
        ImageViewLoader.load(uri, imageView, ImageView.ScaleType.FIT_XY);

    }

    public void loadSp(String uri, ImageView imageView) {
        ImageViewLoader.loadSp(uri, imageView);

    }

    public void loadBitmap(String uri, ImageView imageView, ImageView.ScaleType scaleType) {
        ImageViewLoader.load(uri, imageView, scaleType);

    }

    public void load(String uri, ImageView imageView, int round) {
        ImageViewLoader.load(uri, imageView, round);

    }

    public void loadCircle(String uri, ImageView imageView, int round) {
        ImageViewLoader.loadCircle(uri, imageView, round);

    }

    public abstract int getItemResourceId();

    public abstract void initItemView(PxViewHolder view, ArrayList<T> list,
                                      int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PxViewHolder viewHolder = PxViewHolder.get(context, convertView,
                parent, getItemResourceId(), position);
        initItemView(viewHolder, list, position);
        return viewHolder.getConvertView();
    }

    public static class PxViewHolder {
        private final SparseArray<View> mViews;
        private int mPosition;
        private View mConvertView;

        private PxViewHolder(Context context, ViewGroup parent, int layoutId,
                             int position) {
            this.mPosition = position;
            this.mViews = new SparseArray<View>();
            mConvertView = LayoutInflater.from(context).inflate(layoutId,
                    parent, false);
            mConvertView.setTag(this);
        }

        public static PxViewHolder get(Context context, View convertView,
                                       ViewGroup parent, int layoutId, int position) {
            if (convertView == null) {
                return new PxViewHolder(context, parent, layoutId, position);
            }
            return (PxViewHolder) convertView.getTag();
        }

        public View findViewById(int viewId) {
            return mConvertView.findViewById(viewId);
        }

        public View getConvertView() {
            return mConvertView;
        }

        public <T extends View> T find(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public int getPosition() {
            return mPosition;
        }

    }

    public void startActivity(Class<?> actIntent, String name, ArrayList<T> list, int position) {
        Intent intent = new Intent(context, actIntent);
        intent.putExtra(name, (Serializable) list.get(position));
        context.startActivity(intent);
    }

}
