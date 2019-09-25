package com.example.epay.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.MembershipListBean;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/3/21.
 */

public class MembershipDetailAdapter extends TBaseAdapter<MembershipListBean.Membershipbean> {
    int type=0;
    public MembershipDetailAdapter(Activity context, ArrayList<MembershipListBean.Membershipbean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_member_ship_detail_list;
    }


    public void setList(ArrayList<MembershipListBean.Membershipbean> list,int type) {
        super.setList(list);
        this.type=type;
    }

    @Override
    public void initItemView(PxViewHolder view, ArrayList<MembershipListBean.Membershipbean> list, int position) {
        view.<TextView>find(R.id.item_member_ship_detail_type2).setVisibility(View.GONE);
        view.<TextView>find(R.id.item_member_ship_detail_price2).setVisibility(View.GONE);
        if(position%2==0){
            view.<LinearLayout>find(R.id.item_member_ship_detail_layout).setBackgroundColor(context.getResources().getColor(R.color.textColor_grey2));
        }else{
            view.<LinearLayout>find(R.id.item_member_ship_detail_layout).setBackgroundColor(context.getResources().getColor(R.color.textColor_white));
        }
        if(list.get(position).getMoney()>0) {
            view.<TextView>find(R.id.item_member_ship_detail_price).setText(list.get(position).getMoney() + "");
            view.<TextView>find(R.id.item_member_ship_detail_type).setText("支付");

        }else{
            view.<TextView>find(R.id.item_member_ship_detail_price).setText(Math.abs(list.get(position).getMoney()) + "");
            view.<TextView>find(R.id.item_member_ship_detail_type).setText("退款");
        }
        if(type==1){
            view.<TextView>find(R.id.item_member_ship_detail_type).setText("充值");
            if(list.get(position).getMoney()!=list.get(position).getRealMoney()){
                view.<TextView>find(R.id.item_member_ship_detail_type2).setVisibility(View.VISIBLE);
                view.<TextView>find(R.id.item_member_ship_detail_price2).setVisibility(View.VISIBLE);
                view.<TextView>find(R.id.item_member_ship_detail_type2).setText("L 赠送");
                view.<TextView>find(R.id.item_member_ship_detail_price).setText(list.get(position).getRealMoney()+"");
                view.<TextView>find(R.id.item_member_ship_detail_price2).setText((list.get(position).getMoney()-list.get(position).getRealMoney())+"");
            }else{
                view.<TextView>find(R.id.item_member_ship_detail_type2).setVisibility(View.GONE);
                view.<TextView>find(R.id.item_member_ship_detail_price2).setVisibility(View.GONE);
                view.<TextView>find(R.id.item_member_ship_detail_price).setText(list.get(position).getMoney()+"");
            }

        }
      //  view.<TextView>find(R.id.tv_total_consumption).setText(list.get(position).getFlowType()+"");
        view.<TextView>find(R.id.item_member_ship_detail_time).setText(DateUtil.format2(list.get(position).getCreateTime(),"yyyy.MM.dd"));
    }
}
