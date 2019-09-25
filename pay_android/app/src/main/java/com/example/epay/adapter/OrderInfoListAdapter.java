package com.example.epay.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.OrderInfoBean;
import com.example.epay.bean.SpecialBean;
import com.example.epay.util.DateUtil;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class OrderInfoListAdapter extends TBaseAdapter<OrderInfoBean.ProductSimple> {

    ArrayList<String> prodAttrItems = new ArrayList<>();
    ArrayList<OrderInfoBean.ProductSimple> proItems = new ArrayList<>();

    boolean isCheck = true;

    public OrderInfoListAdapter(Activity context, ArrayList<OrderInfoBean.ProductSimple> list) {
        super(context, list);
    }

    public ArrayList<String> getProdAttrItems() {
        return prodAttrItems;
    }

    public void setProdAttrItems(ArrayList<String> prodAttrItems) {
        this.prodAttrItems = prodAttrItems;
    }

    public ArrayList<OrderInfoBean.ProductSimple> getProItems() {
        return proItems;
    }

    @Override
    public void setList(ArrayList<OrderInfoBean.ProductSimple> list) {
        isCheck = true;
        proItems = new ArrayList<>();
        super.setList(list);
    }

    public void setCheck(boolean t) {
        isCheck = t;
        proItems = new ArrayList<>();
        prodAttrItems = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_order_info_list;
    }

    @Override
    public void initItemView(final PxViewHolder view, final ArrayList<OrderInfoBean.ProductSimple> list, final int position) {

        view.<TextView>find(R.id.item_order_info_name).setText(list.get(position).getName());
        String name1 = "";
        if (list.get(position).getAttrs() == null || list.get(position).getAttrs().size() == 0) {
            if (!list.get(position).getRemark().equals("")) {
                view.<TextView>find(R.id.item_order_info_remark).setVisibility(View.VISIBLE);
                view.<TextView>find(R.id.item_order_info_remark_line).setVisibility(View.VISIBLE);
                view.<TextView>find(R.id.item_order_info_remark).setText("忌口：" + list.get(position).getRemark());
            } else {
                view.<TextView>find(R.id.item_order_info_remark).setVisibility(View.GONE);
                view.<TextView>find(R.id.item_order_info_remark_line).setVisibility(View.GONE);
            }
        } else {

            for (int i = 0; i < list.get(position).getAttrs().size(); i++) {
                if (i == 0) {
                    name1 = name1 + list.get(position).getAttrs().get(i).getText();
                } else {
                    name1 = name1 + "、" + list.get(position).getAttrs().get(i).getText();
                }
            }
            name1 = "(" + name1 + ")";
            view.<TextView>find(R.id.item_order_info_remark).setVisibility(View.VISIBLE);
            view.<TextView>find(R.id.item_order_info_remark_line).setVisibility(View.VISIBLE);
            view.<TextView>find(R.id.item_order_info_remark).setText("规格：" + name1 + ";忌口：" + list.get(position).getRemark());
        }
        view.<TextView>find(R.id.item_order_info_num).setText(list.get(position).getCount() + "");
        view.<TextView>find(R.id.item_order_info_price).setText(list.get(position).getPrice() + "");
        if (list.get(position).getDelFlag() == -1) {
            view.<CheckBox>find(R.id.item_order_info_check).setVisibility(View.INVISIBLE);
            view.<TextView>find(R.id.item_order_info_line).setVisibility(View.VISIBLE);
            view.<ImageView>find(R.id.item_order_info_reduct).setVisibility(View.INVISIBLE);
            view.<ImageView>find(R.id.item_order_info_add).setVisibility(View.INVISIBLE);
//            if(list.get(position).getCount()<0)
//            {
            view.<TextView>find(R.id.item_order_info_num).setText("0");
            //           }
        } else {
            view.<CheckBox>find(R.id.item_order_info_check).setVisibility(View.VISIBLE);
            view.<TextView>find(R.id.item_order_info_line).setVisibility(View.GONE);
            view.<ImageView>find(R.id.item_order_info_reduct).setVisibility(View.VISIBLE);
            view.<ImageView>find(R.id.item_order_info_add).setVisibility(View.VISIBLE);
        }

        if (isCheck) {
            view.<CheckBox>find(R.id.item_order_info_check).setVisibility(View.INVISIBLE);
            view.<ImageView>find(R.id.item_order_info_reduct).setVisibility(View.INVISIBLE);
            view.<ImageView>find(R.id.item_order_info_add).setVisibility(View.INVISIBLE);
            view.<CheckBox>find(R.id.item_order_info_check).setChecked(false);
        }


        view.<ImageView>find(R.id.item_order_info_reduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                boolean isHave = false;
                if (proItems.size() > 0) {
                    for (int i = 0; i < proItems.size(); i++) {
                        if (proItems.get(i).getODID() == list.get(position).getODID()) {
                            isHave = true;
                            if (proItems.get(i).getCount() == 1) {
                                view.<ImageView>find(R.id.item_order_info_reduct).setVisibility(View.INVISIBLE);
                            }
                            proItems.get(i).setCount(proItems.get(i).getCount() - 1);
                            view.<TextView>find(R.id.item_order_info_num).setText(proItems.get(i).getCount() + "");
                        }
                    }
                    if (!isHave) {
                        OrderInfoBean.ProductSimple productSimple = list.get(position);
                        OrderInfoBean.ProductSimple productSimple1 = new OrderInfoBean.ProductSimple();
                        if (list.get(position).getCount() == 1) {
                            view.<ImageView>find(R.id.item_order_info_reduct).setVisibility(View.INVISIBLE);
                        }
                        productSimple1.setCount(productSimple.getCount() - 1);
                        productSimple1.setAttrs(productSimple.getAttrs());
                        productSimple1.setDelFlag(productSimple.getDelFlag());
                        productSimple1.setPrice(productSimple.getPrice());
                        productSimple1.setODID(productSimple.getODID());
                        productSimple1.setName(productSimple.getName());
                        productSimple1.setMemberPrice(productSimple.getMemberPrice());
                        productSimple1.setID(productSimple.getID());
                        productSimple1.setIndex(position);
                        proItems.add(productSimple1);
                        view.<TextView>find(R.id.item_order_info_num).setText(productSimple1.getCount() + "");
                    }
                } else {
                    if (!isHave) {
                        OrderInfoBean.ProductSimple productSimple = list.get(position);
                        OrderInfoBean.ProductSimple productSimple1 = new OrderInfoBean.ProductSimple();
                        if (list.get(position).getCount() == 1) {
                            view.<ImageView>find(R.id.item_order_info_reduct).setVisibility(View.INVISIBLE);
                        }
                        productSimple1.setCount(productSimple.getCount() - 1);
                        productSimple1.setAttrs(productSimple.getAttrs());
                        productSimple1.setDelFlag(productSimple.getDelFlag());
                        productSimple1.setPrice(productSimple.getPrice());
                        productSimple1.setODID(productSimple.getODID());
                        productSimple1.setName(productSimple.getName());
                        productSimple1.setMemberPrice(productSimple.getMemberPrice());
                        productSimple1.setID(productSimple.getID());
                        productSimple1.setIndex(position);
                        proItems.add(productSimple1);
                        view.<TextView>find(R.id.item_order_info_num).setText(productSimple1.getCount() + "");
                    }
                }

            }
        });
        view.<ImageView>find(R.id.item_order_info_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                boolean isHave = false;
                if (proItems.size() > 0) {
                    for (int i = 0; i < proItems.size(); i++) {
                        if (proItems.get(i).getODID() == list.get(position).getODID()) {
                            isHave = true;
                            proItems.get(i).setCount(proItems.get(i).getCount() + 1);
                            view.<TextView>find(R.id.item_order_info_num).setText(proItems.get(i).getCount() + "");
                        }
                    }
                    if (!isHave) {
                        OrderInfoBean.ProductSimple productSimple = list.get(position);
                        OrderInfoBean.ProductSimple productSimple1 = new OrderInfoBean.ProductSimple();
                        productSimple1.setCount(productSimple.getCount() + 1);
                        productSimple1.setAttrs(productSimple.getAttrs());
                        productSimple1.setDelFlag(productSimple.getDelFlag());
                        productSimple1.setPrice(productSimple.getPrice());
                        productSimple1.setODID(productSimple.getODID());
                        productSimple1.setName(productSimple.getName());
                        productSimple1.setMemberPrice(productSimple.getMemberPrice());
                        productSimple1.setID(productSimple.getID());
                        productSimple1.setIndex(position);
                        proItems.add(productSimple1);
                        view.<TextView>find(R.id.item_order_info_num).setText(productSimple1.getCount() + "");
                    }
                } else {
                    if (!isHave) {

                        OrderInfoBean.ProductSimple productSimple = list.get(position);
                        OrderInfoBean.ProductSimple productSimple1 = new OrderInfoBean.ProductSimple();
                        productSimple1.setCount(productSimple.getCount() + 1);
                        productSimple1.setAttrs(productSimple.getAttrs());
                        productSimple1.setDelFlag(productSimple.getDelFlag());
                        productSimple1.setPrice(productSimple.getPrice());
                        productSimple1.setODID(productSimple.getODID());
                        productSimple1.setName(productSimple.getName());
                        productSimple1.setMemberPrice(productSimple.getMemberPrice());
                        productSimple1.setID(productSimple.getID());
                        productSimple1.setIndex(position);
                        proItems.add(productSimple1);
                        view.<TextView>find(R.id.item_order_info_num).setText(productSimple1.getCount() + "");
                    }
                }
                view.<ImageView>find(R.id.item_order_info_reduct).setVisibility(View.VISIBLE);
            }
        });


        view.<CheckBox>find(R.id.item_order_info_check).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    prodAttrItems.add(position + "");
                } else {
                    prodAttrItems.remove(position + "");
                }
            }
        });

    }
}
