package com.example.epay.adapter;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.MemberMListBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class MemberMListAdapter extends TBaseAdapter<MemberMListBean> {
    public MemberMListAdapter(Activity context, ArrayList<MemberMListBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_memberm_list;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<MemberMListBean> list, final int position) {
        loadCircle(list.get(position).getIconURL(),view.<ImageView>find(R.id.item_member_icon),0);
        view.<TextView>find(R.id.item_member_name).setText(list.get(position).getNickName());
        view.<TextView>find(R.id.item_member_talmoney).setText("共消费"+list.get(position).getTotalSum()+"元");
        view.<TextView>find(R.id.item_member_talsum).setText("交易"+list.get(position).getTotalNum()+"笔");
    }
}
