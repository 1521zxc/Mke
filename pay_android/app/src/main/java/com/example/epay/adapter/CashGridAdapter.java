package com.example.epay.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.CashTypeBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by liujin on 2018/12/20.
 */

public class CashGridAdapter extends TBaseAdapter<CashTypeBean>{
    Random random = new Random();
    DecimalFormat df = new DecimalFormat("######0.00");
    public CashGridAdapter(Activity context, ArrayList<CashTypeBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_cash_grid_list;
    }

    @Override
    public void initItemView(PxViewHolder view, ArrayList<CashTypeBean> list, int position) {
        int color = Color.rgb( random.nextInt(255) , random.nextInt(255) , random.nextInt(255) );
        view.<LinearLayout>find(R.id.layout).setBackgroundColor(color);
        view.<TextView>find(R.id.name).setText(list.get(position).getName()+"\n"+df.format(list.get(position).getSum()));
    }
}
