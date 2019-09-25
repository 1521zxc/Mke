package com.example.epay.adapter;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.AddressBean;
import com.example.epay.bean.CataItemBean;
import com.example.epay.view.HorizontalProgressBarWithNumber;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/21.
 */

public class CacaCashAdapter extends TBaseAdapter<CataItemBean> {
    public CacaCashAdapter(Activity context, ArrayList<CataItemBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_caca_cash_layout;
    }

    @Override
    public void initItemView(PxViewHolder view, ArrayList<CataItemBean> list, int position) {
        view.<TextView>find(R.id.item_cata_cash_name).setText(list.get(position).getName());
        view.<HorizontalProgressBarWithNumber>find(R.id.item_cata_cash_ba).setProgress((int) (list.get(position).getNumbai()));
    }
}
