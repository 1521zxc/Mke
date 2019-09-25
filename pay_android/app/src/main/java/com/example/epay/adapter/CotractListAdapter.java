package com.example.epay.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.CotractBean;
import com.example.epay.bean.TransferList;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class CotractListAdapter extends TBaseAdapter<CotractBean> {
    public CotractListAdapter(Activity context, ArrayList<CotractBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_cotract_list;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<CotractBean> list, final int position) {
        load(list.get(position).getIconURL(),view.<ImageView>find(R.id.cotract_img),0);
        view.<TextView>find(R.id.cotract_tv).setText(list.get(position).getName()+"收款");
        view.<TextView>find(R.id.cotract_ratio_tv).setText(list.get(position).getRatio());
    }
}
