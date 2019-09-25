package com.example.epay.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.OrderInfoBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/15.
 */

public class CataDeskHAdapter extends TBaseAdapter<OrderInfoBean.ProductSimple> {
    OnNtClickListener onNtClickListener;
    public CataDeskHAdapter(Activity context, ArrayList<OrderInfoBean.ProductSimple> list) {
        super(context, list);
    }

    public interface OnNtClickListener {
        void onNtClick(ArrayList<OrderInfoBean.ProductSimple> list);
    }

    public void setOnNtClickListener(OnNtClickListener listener) {
        this.onNtClickListener = listener;
    }

    public void setData(ArrayList<OrderInfoBean.ProductSimple> lists)
    {
        this.list=lists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_order_popup;
    }

    @Override
    public void initItemView(final PxViewHolder view1, final ArrayList<OrderInfoBean.ProductSimple> list, final int position) {

//        if(list.get(position).getAttrs()==null||list.get(position).getAttrs().size()==0)
//        {
//            view1.<TextView>find(R.id.item_order_popup_name).setText(list.get(position).getName());
//        }else {
//            String name=list.get(position).getName();
//            String name1="";
//            for(int i=0;i<list.get(position).getAttrs().size();i++)
//            {
//                if(i==0) {
//                    name1 = name1 + list.get(position).getAttrs().get(i).getText();
//                }else{
//                    name1 = name1 +"、"+ list.get(position).getAttrs().get(i).getText();
//                }
//            }
//            name=name+"("+name1+")";
//            view1.<TextView>find(R.id.item_order_popup_name).setText(name);
//        }
        view1.<TextView>find(R.id.item_order_popup_name).setText(list.get(position).getName());
        view1.<TextView>find(R.id.item_order_popup_num).setText(list.get(position).getCount()+"");
//        view1.<ImageView>find(R.id.item_order_popup_reduct).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                list.get(position).setCount(list.get(position).getCount()-1);
//                view1.<TextView>find(R.id.item_order_popup_num).setText(list.get(position).getCount()+"");
//
//                if(list.get(position).getCount()==0)
//                {
//                    list.remove(list.get(position));
//                }
//                if(onNtClickListener!=null) {
//                    onNtClickListener.onNtClick(list);
//                }
//            }
//        });
//        view1.<ImageView>find(R.id.item_order_popup_add).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                list.get(position).setCount(list.get(position).getCount()+1);
//                view1.<TextView>find(R.id.item_order_popup_num).setText(list.get(position).getCount()+"");
//                if(onNtClickListener!=null) {
//                    onNtClickListener.onNtClick(list);
//                }
//            }
//        });
    }
}
