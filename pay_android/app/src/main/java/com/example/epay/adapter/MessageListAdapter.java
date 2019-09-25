package com.example.epay.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.MessageBean;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class MessageListAdapter extends TBaseAdapter<MessageBean> {
    public MessageListAdapter(Activity context, ArrayList<MessageBean> list) {
        super(context, list);
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_message_list;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<MessageBean> list, final int position) {
        view.<TextView>find(R.id.message_con).setText(list.get(position).getText());
        view.<TextView>find(R.id.message_time).setText(DateUtil.format2(list.get(position).getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
    }
}
