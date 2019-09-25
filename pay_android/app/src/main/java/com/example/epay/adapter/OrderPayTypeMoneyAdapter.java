package com.example.epay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.activity.CollectCodeActivity;
import com.example.epay.activity.ScanQrCodeActivity;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.OrderPayTypeBean;
import com.example.epay.util.DateUtil;
import com.example.epay.util.MyDialog;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/12/20.
 */

public class OrderPayTypeMoneyAdapter extends TBaseAdapter<OrderPayTypeBean> {
    ArrayList<OrderPayTypeBean> beanArrayList;
    OnNtClickListener onNtClickListener;
    private String orderNO = "";
    public OrderPayTypeMoneyAdapter(Activity context, ArrayList<OrderPayTypeBean> list,String orderNo,ArrayList<OrderPayTypeBean> beanArrayLis) {
        super(context, list);
        beanArrayList=beanArrayLis;
        orderNO=orderNo;
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_order_pay_type_money;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    public interface OnNtClickListener {
        void onNtClick(OrderPayTypeBean bean);
    }

    public void setOnNtClickListener(OnNtClickListener listener) {
        this.onNtClickListener = listener;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<OrderPayTypeBean> list, int position) {
        if (position == list.size()) {
            view.<LinearLayout>find(R.id.layout).setVisibility(View.GONE);
            view.<TextView>find(R.id.add).setVisibility(View.VISIBLE);
        } else {
            view.<LinearLayout>find(R.id.layout).setVisibility(View.VISIBLE);
            view.<TextView>find(R.id.add).setVisibility(View.GONE);
            view.<TextView>find(R.id.type).setText(list.get(position).getName());
            view.<TextView>find(R.id.money).setText(String.format("%.2f", list.get(position).getMoney()));
        }

        view.<TextView>find(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view12) {
                final OrderPayTypeBean typeBean = new OrderPayTypeBean();
                View view1 = context.getLayoutInflater().inflate(R.layout.dialog_order_pay_type_list, null);
                final MyDialog mMyDialog = new MyDialog(context, view1, R.style.DialogTheme);
                ListView listView = (ListView) view1.findViewById(R.id.listView);
                OrderPayTypeSeteleAdapter adapter = new OrderPayTypeSeteleAdapter(context, beanArrayList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener((adapterView, view22, i, l) -> {
                    mMyDialog.dismiss();
                    typeBean.setName(beanArrayList.get(i).getName());
                    typeBean.setType(beanArrayList.get(i).getType());
                    View view2 = context.getLayoutInflater().inflate(R.layout.dialog_order_pay_money, null);
                    final MyDialog mMyDialog2 = new MyDialog(context, view2, R.style.DialogTheme);
                    final EditText moneyEdit = (EditText) view2.findViewById(R.id.money);
                    TextView sure = (TextView) view2.findViewById(R.id.ok);
                    sure.setOnClickListener(view3 -> {
                        mMyDialog2.dismiss();
                        if (!moneyEdit.getText().toString().equals("")) {
                            if (DateUtil.isNumeric00(moneyEdit.getText().toString())) {
                                typeBean.setMoney(Double.valueOf(moneyEdit.getText().toString()));
                                if (onNtClickListener != null) {
                                    onNtClickListener.onNtClick(typeBean);
                                }
                            }
                        }
                    });
                    mMyDialog2.setCancelable(true);
                    mMyDialog2.show();
                });
                mMyDialog.setCancelable(true);
                mMyDialog.show();
            }
        });


    }
}
