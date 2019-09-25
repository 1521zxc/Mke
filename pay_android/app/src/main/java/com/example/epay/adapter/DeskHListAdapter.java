package com.example.epay.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.DeskBean;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2015/8/6.
 */
public class DeskHListAdapter extends TBaseAdapter<DeskBean> {
    OnNtClickListener onNtClickListener;
    int isIndex=-1;


    public DeskHListAdapter(Activity context, ArrayList<DeskBean> list) {
        super(context, list);
    }



    public interface OnNtClickListener {
        void onNtClick(int position);
    }

    @Override
    public void setList(ArrayList<DeskBean> list) {
        super.setList(list);

    }

    public void setOnNtClickListener(OnNtClickListener listener) {
        this.onNtClickListener = listener;
    }
    @Override
    public int getItemResourceId() {
        return R.layout.item_desk_list;
    }

    @Override
    public void initItemView(final PxViewHolder view1, final ArrayList<DeskBean> list, final int position) {

        view1.<TextView>find(R.id.item_desk_name).setText(list.get(position).getDeskName());
        if(list.get(position).getDeskStatus()==0) {
            view1.<ImageView>find(R.id.item_desk_img).setBackground(context.getResources().getDrawable(R.drawable.desk_statue_null));
            view1.<LinearLayout>find(R.id.item_desk_layout1).setVisibility(View.GONE);
            view1.<LinearLayout>find(R.id.item_desk_layout2).setVisibility(View.GONE);
            view1.<LinearLayout>find(R.id.item_desk_null).setVisibility(View.VISIBLE);
            view1.<TextView>find(R.id.item_desk_status).setText("");
        }else if(list.get(position).getDeskStatus()==1){
            if(list.get(position).getPayStatus()==1) {
                if (list.get(position).getServiceStatus() == -1) {
                    view1.<ImageView>find(R.id.item_desk_img).setBackground(context.getResources().getDrawable(R.drawable.weidayin2));
                    view1.<TextView>find(R.id.item_desk_status).setText("未打印");
                }else {
                    view1.<ImageView>find(R.id.item_desk_img).setBackground(context.getResources().getDrawable(R.drawable.statue_desk));

                }
            }else{
                if (list.get(position).getServiceStatus() == -1) {
                    view1.<ImageView>find(R.id.item_desk_img).setBackground(context.getResources().getDrawable(R.drawable.weidayindesk));
                    view1.<TextView>find(R.id.item_desk_status).setText("未打印");
                } else {
                    view1.<ImageView>find(R.id.item_desk_img).setBackground(context.getResources().getDrawable(R.drawable.desk_statue_ing));
                    view1.<TextView>find(R.id.item_desk_status).setText("用餐中");
                }
            }

            view1.<LinearLayout>find(R.id.item_desk_layout1).setVisibility(View.VISIBLE);
            view1.<LinearLayout>find(R.id.item_desk_layout2).setVisibility(View.VISIBLE);
            view1.<LinearLayout>find(R.id.item_desk_null).setVisibility(View.GONE);
            view1.<TextView>find(R.id.item_desk_nb).setText("人数："+list.get(position).getTotalNum());
            view1.<TextView>find(R.id.item_desk_money).setText("￥"+list.get(position).getSaleMoney());
            view1.<TextView>find(R.id.item_desk_time).setText(DateUtil.format2(list.get(position).getCreateTime(),"yyyy-MM-dd HH:mm"));

        }else if(list.get(position).getDeskStatus()==2){
            view1.<ImageView>find(R.id.item_desk_img).setBackground(context.getResources().getDrawable(R.drawable.desk_statue_re));
            view1.<TextView>find(R.id.item_desk_status).setText("预定");
            view1.<LinearLayout>find(R.id.item_desk_layout1).setVisibility(View.VISIBLE);
            view1.<LinearLayout>find(R.id.item_desk_layout2).setVisibility(View.VISIBLE);
            view1.<LinearLayout>find(R.id.item_desk_null).setVisibility(View.GONE);
            view1.<TextView>find(R.id.item_desk_nb).setText("人数："+list.get(position).getTotalNum());
            view1.<TextView>find(R.id.item_desk_money).setText("￥"+list.get(position).getSaleMoney());
            view1.<TextView>find(R.id.item_desk_time).setText(DateUtil.format2(list.get(position).getCreateTime(),"yyyy-MM-dd HH:mm"));
        }else{
            view1.<ImageView>find(R.id.item_desk_img).setBackground(context.getResources().getDrawable(R.drawable.desk_statue_re));
        }
        view1.<ImageView>find(R.id.item_desk_check).setVisibility(View.VISIBLE);
        view1.<LinearLayout>find(R.id.item_desk_layout2).setVisibility(View.VISIBLE);
        if(position==isIndex) {
            view1.<ImageView>find(R.id.item_desk_check).setImageDrawable(context.getResources().getDrawable(R.drawable.checkbox_pressed));
        }else{
            view1.<ImageView>find(R.id.item_desk_check).setImageDrawable(context.getResources().getDrawable(R.drawable.checkbox_normal));
        }
        view1.<ImageView>find(R.id.item_desk_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==isIndex)
                {
                    onNtClickListener.onNtClick(-1);
                    view1.<ImageView>find(R.id.item_desk_check).setImageDrawable(context.getResources().getDrawable(R.drawable.checkbox_normal));
                }else{
                    isIndex=position;
                    view1.<ImageView>find(R.id.item_desk_check).setImageDrawable(context.getResources().getDrawable(R.drawable.checkbox_pressed));
                    onNtClickListener.onNtClick(position);
                }
            }
        });
    }
}
