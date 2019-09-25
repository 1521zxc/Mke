package com.example.epay.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.OrderPayTypeBean;
import com.example.epay.bean.conponeBean;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/13.
 */

public class DisCountListAdapter extends TBaseAdapter<conponeBean> {

    public DisCountListAdapter(Activity context, ArrayList<conponeBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_pay_dis_count_layout;
    }

    @Override
    public int getCount() {
        return super.getCount()+1;
    }

    @Override
    public void initItemView(PxViewHolder view, ArrayList<conponeBean> list, int position) {
        if(position==list.size())
        {
            view.<LinearLayout>find(R.id.conpone_layout).setVisibility(View.GONE);
            view.<TextView>find(R.id.no_conpone_text).setVisibility(View.VISIBLE);
        }else{
            load(list.get(position).getImage(),view.<ImageView>find(R.id.conpone_img),0);
            view.<TextView>find(R.id.conpone_name).setText(list.get(position).getName());
            view.<TextView>find(R.id.conpone_text).setText("[满"+list.get(position).getFullPrice()+"减"+list.get(position).getPrice()+"][有效期至"+ DateUtil.format2(list.get(position).getEndTime(),"yyyy-MM-dd")+"]");
            view.<LinearLayout>find(R.id.conpone_layout).setVisibility(View.VISIBLE);
            view.<TextView>find(R.id.no_conpone_text).setVisibility(View.GONE);
        }

    }
}

