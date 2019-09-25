package com.example.epay.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseRecyclerAdapter;
import com.example.epay.base.MyViewHolder;
import com.example.epay.bean.CashflowListBean2;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class CashflowListAdapter extends BaseRecyclerAdapter<CashflowListBean2> {
    public CashflowListAdapter(ArrayList<CashflowListBean2> list, Activity context, int mLayoutId) {
        super(list, context, mLayoutId);
    }

    @Override
    public void coner(MyViewHolder holder, CashflowListBean2 bean, int position) {
        if (bean.getMoney()!=0)
        {
            holder.<TextView>find(R.id.cashflow_title_time).setText(bean.getTime());
            holder.<TextView>find(R.id.cashflow_title_money).setText(bean.getMoney()+"元");
            holder.<TextView>find(R.id.cashflow_title_all).setText("共"+bean.getAll()+"笔");
            holder.<LinearLayout>find(R.id.cashflow_title).setVisibility(View.VISIBLE);
        }else {
            holder.<LinearLayout>find(R.id.cashflow_title).setVisibility(View.GONE);
        }
        holder.<TextView>find(R.id.con_money).setText(bean.getSum()+"");
        loadCircle(bean.getIconURL(),holder.<ImageView>find(R.id.con_img),0);
        if(bean.getSum()>=0)
        {
            holder.<TextView>find(R.id.con_state).setText(bean.getDateTime()+" "+bean.getTypeName()+"收款");
        }else{
            holder.<TextView>find(R.id.con_state).setText(bean.getDateTime()+" "+bean.getTypeName()+"退款");
        }
        if(bean.getNickName()!=null&&!bean.equals(""))
        {
            holder.<TextView>find(R.id.con_state2).setText(bean.getNickName());
        }else{
            holder.<TextView>find(R.id.con_state2).setText(bean.getTypeName()+"用户");
        }
    }



}