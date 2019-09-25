package com.example.epay.adapter;

import android.app.Activity;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.MemberMListBean;
import com.example.epay.bean.StoredBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class StoredListAdapter extends TBaseAdapter<StoredBean> {
    public StoredListAdapter(Activity context, ArrayList<StoredBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_stored_list;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<StoredBean> list, final int position) {
        loadCircle(list.get(position).getIconURL(),view.<ImageView>find(R.id.item_stored_img),0);
        view.<TextView>find(R.id.item_stored_name).setText(list.get(position).getName());
        String a=list.get(position).getTell()+"&nbsp;&nbsp;&nbsp;<font color='#325dc0'>储值"+list.get(position).getSum()+"次</font>";
        view.<TextView>find(R.id.item_stored_text).setText(Html.fromHtml(a));
        String a1="余额"+"<font color='#fd5c5c'>"+list.get(position).getMoney()+"</font>&nbsp;元>";
        view.<TextView>find(R.id.item_stored_money).setText(Html.fromHtml(a1));
    }
}
