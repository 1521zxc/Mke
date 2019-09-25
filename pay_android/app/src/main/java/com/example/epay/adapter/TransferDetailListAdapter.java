package com.example.epay.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.TransferList;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class TransferDetailListAdapter extends TBaseAdapter<TransferList> {
    public TransferDetailListAdapter(Activity context, ArrayList<TransferList> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_transfer_detail;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<TransferList> list, final int position) {
        view.<TextView>find(R.id.item_sum).setText("￥"+list.get(position).getSum());
        Drawable drawable = null;
        if(list.get(position).getType().equals("1")) {
             drawable = context.getResources().getDrawable(R.drawable.icon_weixin_pay);
        }else if(list.get(position).getType().equals("2")){
            drawable = context.getResources().getDrawable(R.drawable.icon_alipay_pay);
        }else if (list.get(position).getType().equals("3")){
            drawable = context.getResources().getDrawable(R.drawable.qq);
        }else if (list.get(position).getType().equals("4")){
            drawable = context.getResources().getDrawable(R.drawable.jd);
        }else if (list.get(position).getType().equals("5")){
            drawable = context.getResources().getDrawable(R.drawable.baidu);
        }else if (list.get(position).getType().equals("6")){
            drawable = context.getResources().getDrawable(R.drawable.union);
        }
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable.setBounds(0,0,(int)(0.07*w),(int)(0.07*w));
        //drawable   第一个是文字TOP
        view.<TextView>find(R.id.item_sum).setCompoundDrawables(drawable,null,null,null);
        view.<TextView>find(R.id.item_fee).setText("￥"+list.get(position).getServiceFee());
        view.<TextView>find(R.id.item_time).setText(DateUtil.format2(list.get(position).getTransferTime(),"yyyy-MM-dd HH:mm:ss"));

    }
}
