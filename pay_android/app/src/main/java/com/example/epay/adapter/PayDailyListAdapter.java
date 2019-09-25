package com.example.epay.adapter;

import android.app.Activity;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.PayTypeBean;
import com.example.epay.bean.StoredBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liujin on 2018/1/20.
 */

public class PayDailyListAdapter extends TBaseAdapter<PayTypeBean> {
    HashMap<Integer,String> payMap=new HashMap<>();
    public PayDailyListAdapter(Activity context, ArrayList<PayTypeBean> list) {
        super(context, list);
    }
    public void setMap(HashMap<Integer,String> Map)
    {
        payMap=Map;
    }

    @Override
    public int getCount() {
        return super.getCount()+1;
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_pay_daily_list;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<PayTypeBean> list, final int position) {
        if(position==0)
        {
            view.<TextView>find(R.id.type_name).setText("支付类型");
            view.<TextView>find(R.id.type_money).setText("收款金额");
        }else {
            if(payMap.size()>0) {
              String name=  payMap.get(list.get(position - 1).getPayType());
                if ( name== null ||name.equals("null")) {
                    view.<TextView>find(R.id.type_name).setText("未知");
                } else {
                    view.<TextView>find(R.id.type_name).setText(name);
                }
                view.<TextView>find(R.id.type_money).setText(list.get(position - 1).getSum() + "");
            }
        }
    }
}
