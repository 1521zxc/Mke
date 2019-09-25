package com.example.epay.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.activity.EnvelopeDataActivity;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.EnvelopesBean;
import com.example.epay.bean.SpecialBean;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class EnvelopeListAdapter extends TBaseAdapter<EnvelopesBean> {
    public EnvelopeListAdapter(Activity context, ArrayList<EnvelopesBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_envelope_list;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<EnvelopesBean> list, final int position) {
        view.<TextView>find(R.id.item_envelope_name).setText(list.get(position).getName());
        view.<TextView>find(R.id.item_envelope_sum).setText(context.getString(R.string.envelope_sum,list.get(position).getLingquSum(),list.get(position).getShiSum()));
        view.<TextView>find(R.id.item_envelope_allsum).setText(context.getString(R.string.envelope_allsum,list.get(position).getAllSum()));
        view.<TextView>find(R.id.item_envelope_time).setText(context.getString(R.string.envelope_time,list.get(position).getStartTime(),list.get(position).getEndTime()));
        view.<TextView>find(R.id.item_envelope_small).setText(list.get(position).getSnall()+"元");
        view.<TextView>find(R.id.item_envelope_old).setText(list.get(position).getOld()+"元");
        view.<TextView>find(R.id.xiazai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        view.<TextView>find(R.id.envelope_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(EnvelopeDataActivity.class,"enve",list,position);
            }
        });

    }
}
