package com.example.epay.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.AddressBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/21.
 */

public class AddressAdapter  extends TBaseAdapter<AddressBean> {
    public AddressAdapter(Activity context, ArrayList<AddressBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_address_list;
    }

    @Override
    public void initItemView(PxViewHolder view, ArrayList<AddressBean> list, int position) {
        view.<TextView>find(R.id.address_name).setText(list.get(position).getName());
        view.<TextView>find(R.id.address_address).setText(list.get(position).getAddress());
    }
}
