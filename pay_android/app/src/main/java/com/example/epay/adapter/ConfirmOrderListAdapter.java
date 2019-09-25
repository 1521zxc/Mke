package com.example.epay.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.MealListBean;
import com.example.epay.bean.OrderInfoBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class ConfirmOrderListAdapter extends TBaseAdapter<MealListBean.MealRight> {
    public ConfirmOrderListAdapter(Activity context, ArrayList<MealListBean.MealRight> list) {
        super(context, list);
    }
    @Override
    public int getItemResourceId() {
        return R.layout.item_order_info_list;
    }

    @Override
    public void initItemView(final PxViewHolder view, final ArrayList<MealListBean.MealRight> list, final int position) {

        view.<TextView>find(R.id.item_order_info_name).setText(list.get(position).getName());
        String name1="",remark="";
        double price=0;
        if(list.get(position).getAttrs()==null||list.get(position).getAttrs().size()==0)
        {
            if(list.get(position).getRemark().size()>0) {
                view.<TextView>find(R.id.item_order_info_remark).setVisibility(View.VISIBLE);
                view.<TextView>find(R.id.item_order_info_remark_line).setVisibility(View.VISIBLE);
                for(int i=0;i<list.get(position).getRemark().size();i++)
                {
                    if(i==0) {
                        remark = remark + list.get(position).getRemark().get(i).getName();
                    }else{
                        remark = remark +","+ list.get(position).getRemark().get(i).getName();
                    }
                }
                view.<TextView>find(R.id.item_order_info_remark).setText("忌口：" + remark);
            }else{
                view.<TextView>find(R.id.item_order_info_remark).setVisibility(View.GONE);
                view.<TextView>find(R.id.item_order_info_remark_line).setVisibility(View.GONE);
            }
        }else{

            for(int i=0;i<list.get(position).getAttrs().size();i++)
            {
                if(i==0) {
                    name1 = name1 + list.get(position).getAttrs().get(i).getName();

                }else{
                    name1 = name1 +"、"+ list.get(position).getAttrs().get(i).getName();
                }
                price=price+list.get(position).getAttrs().get(i).getPrice();
            }
            name1="("+name1+")";
            if(list.get(position).getRemark().size()>0) {
                view.<TextView>find(R.id.item_order_info_remark).setVisibility(View.VISIBLE);
                view.<TextView>find(R.id.item_order_info_remark_line).setVisibility(View.VISIBLE);
                for(int i=0;i<list.get(position).getRemark().size();i++)
                {
                    if(i==0) {
                        remark = remark + list.get(position).getRemark().get(i).getName();
                    }else{
                        remark = remark +","+ list.get(position).getRemark().get(i).getName();
                    }
                }
                if(!remark.equals("")) {
                    view.<TextView>find(R.id.item_order_info_remark).setText("忌口：" + remark);
                }
            }
            if(!remark.equals("")) {
                view.<TextView>find(R.id.item_order_info_remark).setText("规格：" + name1 + ";忌口：" + remark);
            }else {
                view.<TextView>find(R.id.item_order_info_remark).setText("规格：" + name1);
            }
        }
        view.<TextView>find(R.id.item_order_info_num).setText(list.get(position).getNumber()+"");
        view.<TextView>find(R.id.item_order_info_price).setText((list.get(position).getPrice()+price)+"");
        view.<CheckBox>find(R.id.item_order_info_check).setVisibility(View.INVISIBLE);
        view.<TextView>find(R.id.item_order_info_line).setVisibility(View.GONE);
        view.<ImageView>find(R.id.item_order_info_reduct).setVisibility(View.INVISIBLE);
        view.<ImageView>find(R.id.item_order_info_add).setVisibility(View.INVISIBLE);

    }
}
