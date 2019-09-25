package com.example.epay.adapter;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.SpecialBean;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class SpecialListAdapter extends TBaseAdapter<SpecialBean> {
    public SpecialListAdapter(Activity context, ArrayList<SpecialBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_special_list;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<SpecialBean> list, final int position) {
        view.<TextView>find(R.id.special_title).setText(list.get(position).getTitle());
        if(list.get(position).getCreateTime()>DateUtil.getTimesmorning())
        {
            view.<TextView>find(R.id.special_time).setText("今天 "+DateUtil.format2(list.get(position).getCreateTime(),"HH:mm"));
        }else if(list.get(position).getCreateTime()>(DateUtil.getTimesmorning()-24*60*60*1000)){
            view.<TextView>find(R.id.special_time).setText("昨天 "+DateUtil.format2(list.get(position).getCreateTime(),"HH:mm"));
        }else{
            view.<TextView>find(R.id.special_time).setText(DateUtil.format2(list.get(position).getCreateTime(),"yyyy.MM.dd HH:mm"));
        }
        loadSp(list.get(position).getIconURL(), view.<ImageView>find(R.id.special_img));
    }
}