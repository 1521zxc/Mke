package com.example.epay.adapter;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.AddressBean;
import com.example.epay.bean.RoyaltyBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/21.
 */

public class RoyaltyAdapter extends TBaseAdapter<RoyaltyBean.RoyaltyItem> {
    public RoyaltyAdapter(Activity context, ArrayList<RoyaltyBean.RoyaltyItem> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_royalty_list;
    }

    @Override
    public void initItemView(PxViewHolder view, ArrayList<RoyaltyBean.RoyaltyItem> list, int position) {
       loadBitmap(list.get(position).getImgUrl(), view.<ImageView>find(R.id.item_royalty_img));
        view.<TextView>find(R.id.item_royalty_name).setText(list.get(position).getName());
        view.<TextView>find(R.id.item_royalty_price).setText(list.get(position).getPrice()+"");
        view.<TextView>find(R.id.item_royalty_num).setText(list.get(position).getRoyalty()+"");
        if(position%2==0)
        {
            view.<LinearLayout>find(R.id.item_royalty_layout).setBackgroundColor(context.getResources().getColor(R.color.color8));
        }else{
            view.<LinearLayout>find(R.id.item_royalty_layout).setBackgroundColor(context.getResources().getColor(R.color.textColor_white));
        }
    }
}
