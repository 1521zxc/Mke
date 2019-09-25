package com.example.epay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.example.epay.R;


/**
 * Created by tpp01 on 2015/3/27.
 */
public class FenXiangGridAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private String[] titles;
    private int[] list;
    GridView mGv;
    private Context context;

    public FenXiangGridAdapter(GridView gv, Context context, int[] list, String[] titles) {
        inflater = LayoutInflater.from(context);
        this.titles=titles;
        this.list=list;
        this.mGv=gv;
        this.context=context;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.partakeitem, parent,false);
        ImageView img=(ImageView)convertView.findViewById(R.id.grid_img);
        LayoutParams para = img.getLayoutParams();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		width = (int) ((int) width / 10.0 - 10);
		para.width = width;
		para.height = width;
		img.setLayoutParams(para);
		img.setScaleType(ScaleType.FIT_XY);
        TextView tv=(TextView)convertView.findViewById(R.id.grid_tv);
        img.setImageResource(list[position]);
        tv.setText(titles[position]);
        return convertView;
    }
}

