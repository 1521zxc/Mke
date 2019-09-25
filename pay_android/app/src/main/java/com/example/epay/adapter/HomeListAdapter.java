package com.example.epay.adapter;

import android.app.Activity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.HomeListBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class HomeListAdapter extends TBaseAdapter<HomeListBean.Deta> {
    public HomeListAdapter(Activity context, ArrayList<HomeListBean.Deta> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_home_list;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<HomeListBean.Deta> list, final int position) {
        view.<TextView>find(R.id.home_type).setText(list.get(position).getText());
        loadCircle(list.get(position).getIconURL(),view.<ImageView>find(R.id.home_img),0);
    }
}
