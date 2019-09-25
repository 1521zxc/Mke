package com.example.epay.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.MealListBean;
import com.example.epay.bean.OrderMealAttrBean;
import com.example.epay.bean.RemarkBean;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by liujin on 2018/6/13.
 */

public class SearchMealAdapter extends TBaseAdapter<MealListBean.MealRight> {
    ArrayList<MealListBean.MealRight> lists = new ArrayList<>();
    OnNtClickListener onNtClickListener;
    ArrayList<OrderMealAttrBean> Attrlist1 = new ArrayList<>();
    ArrayList<OrderMealAttrBean> Attrlist2 = new ArrayList<>();
    ArrayList<RemarkBean> Attrlist3 = new ArrayList<>();
    ArrayList<RemarkBean> Attrlist5 = new ArrayList<>();
    static double price = 0;
    static int num = 0;

    public SearchMealAdapter(Activity context, ArrayList<MealListBean.MealRight> list, ArrayList<RemarkBean> list2, double price, ArrayList<MealListBean.MealRight> lists, int num) {
        super(context, list);
        this.price = price;
        this.lists = lists;
        this.num = num;
        this.Attrlist3 = list2;
        this.Attrlist5 = list2;

    }


    public void setData(ArrayList<MealListBean.MealRight> lists, ArrayList<RemarkBean> list2, double price1, int num1) {
        this.lists = lists;
        this.price = price1;
        this.num = num1;
        this.Attrlist3 = list2;
        this.Attrlist5 = list2;
        notifyDataSetChanged();
    }


    public interface OnNtClickListener {
        void onNtClick(ArrayList<MealListBean.MealRight> list, int num, double price);
    }

    public void setOnNtClickListener(OnNtClickListener listener) {
        this.onNtClickListener = listener;
    }


    @Override
    public int getItemResourceId() {
        return R.layout.item_right_list;
    }

    @Override
    public void initItemView(final PxViewHolder view1, final ArrayList<MealListBean.MealRight> list, final int position) {
        final MealListBean.MealRight right = list.get(position);
        final MealListBean.MealRight mealRight = new MealListBean.MealRight();
        boolean isNum = true;
        mealRight.setSoldCount(right.getSoldCount());
        mealRight.setPrice(right.getPrice());
        mealRight.setVipPrice(right.getVipPrice());
        mealRight.setID(right.getID());
        mealRight.setSellStatus(right.getSellStatus());
        mealRight.setSetMeal(right.getSetMeal());
        mealRight.setCataID(right.getCataID());
        mealRight.setName(right.getName());
        mealRight.setIconURL(right.getIconURL());
        mealRight.setAttrs(right.getAttrs());
        if (lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                if (right.getID() == lists.get(i).getID()) {
                    if (lists.get(i).getAttrs() == null || lists.get(i).getAttrs().size() < 1) {
                        isNum = false;
                        right.setNumber(lists.get(i).getNumber());
                    }
                }
            }
        }
        if (isNum) {
            right.setNumber(0);

        }
        mealRight.setNumber(right.getNumber());
        load(mealRight.getIconURL(), view1.<ImageView>find(R.id.item_right_img), 0);
        view1.<TextView>find(R.id.item_right_name).setText(mealRight.getName());
        if (mealRight.getPrice() == 0) {
            view1.<TextView>find(R.id.item_right_vip).setVisibility(View.INVISIBLE);
            view1.<TextView>find(R.id.item_right_price).setVisibility(View.INVISIBLE);
        }
        view1.<TextView>find(R.id.item_right_vip).setText("会员价:￥" + mealRight.getVipPrice());
        view1.<TextView>find(R.id.item_right_price).setText("￥" + mealRight.getPrice());
        view1.<TextView>find(R.id.item_right_sell).setText("已售" + mealRight.getSoldCount() + "份");
        view1.<TextView>find(R.id.item_right_num).setText(mealRight.getNumber() + "");

        if (right.getAttrs().size() > 0) {
            view1.<TextView>find(R.id.item_right_img2).setVisibility(View.GONE);
            view1.<LinearLayout>find(R.id.item_right_add).setVisibility(View.GONE);
            view1.<LinearLayout>find(R.id.item_right_gg).setVisibility(View.VISIBLE);
        } else {
            view1.<TextView>find(R.id.item_right_img2).setVisibility(View.GONE);
            view1.<LinearLayout>find(R.id.item_right_add).setVisibility(View.VISIBLE);
            view1.<LinearLayout>find(R.id.item_right_gg).setVisibility(View.GONE);
        }
        if (mealRight.getNumber() > 0) {
            view1.<LinearLayout>find(R.id.item_right_reduct).setVisibility(View.VISIBLE);
            view1.<TextView>find(R.id.item_right_num).setVisibility(View.VISIBLE);
            view1.<TextView>find(R.id.item_right_img2).setVisibility(View.VISIBLE);
        } else {
            view1.<LinearLayout>find(R.id.item_right_reduct).setVisibility(View.INVISIBLE);
            view1.<TextView>find(R.id.item_right_num).setVisibility(View.INVISIBLE);
            view1.<TextView>find(R.id.item_right_img2).setVisibility(View.GONE);
        }

        if (list.get(position).getSellStatus() == 0) {
            //未销售
            view1.<ImageView>find(R.id.item_right_img1).setBackground(context.getResources().getDrawable(R.drawable.index_null));
            view1.<LinearLayout>find(R.id.item_right_gg).setVisibility(View.GONE);
            view1.<TextView>find(R.id.item_right_img2).setVisibility(View.INVISIBLE);
            view1.<LinearLayout>find(R.id.item_right_add).setVisibility(View.INVISIBLE);
        } else if (list.get(position).getSellStatus() == -1) {
            //已售完
            view1.<ImageView>find(R.id.item_right_img1).setBackground(context.getResources().getDrawable(R.drawable.index_soll));
            if (view1.<LinearLayout>find(R.id.item_right_add).getVisibility() == View.VISIBLE) {
                view1.<TextView>find(R.id.item_right_img2).setVisibility(View.INVISIBLE);
                view1.<LinearLayout>find(R.id.item_right_add).setVisibility(View.INVISIBLE);
            }
            if (view1.<LinearLayout>find(R.id.item_right_gg).getVisibility() == View.VISIBLE) {
                view1.<LinearLayout>find(R.id.item_right_gg).setVisibility(View.GONE);
            }
        }


        if (view1.<TextView>find(R.id.item_right_img2).getVisibility() == View.VISIBLE) {
            view1.<ImageView>find(R.id.item_right_img1).setBackground(null);
        }

        view1.<LinearLayout>find(R.id.item_right_gg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow popupWindow = initListPopuptWindow(position);
                popupWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
        });

        view1.<TextView>find(R.id.item_right_img2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow popupWindow = initListPopuptWindow(position);
                popupWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
        });

        view1.<LinearLayout>find(R.id.item_right_reduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mealRight.getNumber() == 1) {
                    view1.<LinearLayout>find(R.id.item_right_reduct).setVisibility(View.INVISIBLE);
                    view1.<TextView>find(R.id.item_right_num).setVisibility(View.INVISIBLE);
                }
                if (lists.size() > 0) {
                    for (int i = 0; i < lists.size(); i++) {
                        if (right.getID() == lists.get(i).getID()) {
                            if (lists.get(i).getAttrs() == null || lists.get(i).getAttrs().size() < 1) {
                                lists.remove(i);
                            }
                        }
                    }
                }
                price = price - mealRight.getPrice();
                price = (double) (Math.round(price * 100) / 100.0);
                mealRight.setNumber(right.getNumber() - 1);
                view1.<TextView>find(R.id.item_right_num).setText(mealRight.getNumber() + "");
                if (mealRight.getNumber() != 0) {
                    lists.add(mealRight);
                }
                num--;
                right.setNumber(mealRight.getNumber());
                if (onNtClickListener != null) {
                    onNtClickListener.onNtClick(lists, num, price);
                }

            }
        });
        view1.<LinearLayout>find(R.id.item_right_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mealRight.getNumber() == 0) {
                    view1.<LinearLayout>find(R.id.item_right_reduct).setVisibility(View.VISIBLE);
                    view1.<TextView>find(R.id.item_right_num).setVisibility(View.VISIBLE);
                }


                if (lists.size() > 0) {
                    for (int i = 0; i < lists.size(); i++) {
                        if (right.getID() == lists.get(i).getID()) {
                            if (lists.get(i).getAttrs() == null || lists.get(i).getAttrs().size() < 1) {
                                lists.remove(i);
                            }
                        }
                    }
                }
                price = price + mealRight.getPrice();
                price = (double) (Math.round(price * 100) / 100.0);
                mealRight.setNumber(mealRight.getNumber() + 1);
                view1.<TextView>find(R.id.item_right_num).setText(mealRight.getNumber() + "");
                lists.add(mealRight);
                num++;

                right.setNumber(mealRight.getNumber());
                if (onNtClickListener != null) {
                    onNtClickListener.onNtClick(lists, num, price);
                }
            }
        });
    }


    private PopupWindow mPopupWindow;
    MealRightGridAdapter adapter;
    MealRightGridAdapter adapter1;
    MealRightreMarkAdapter adapter2;
    View popupWindow;

    public PopupWindow initListPopuptWindow(final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popupWindow = layoutInflater.inflate(R.layout.meal_right_list, null);
        GridView gridView = (GridView) popupWindow.findViewById(R.id.right_type_gridView);
        GridView gridView1 = (GridView) popupWindow.findViewById(R.id.right_type_gridView2);
        GridView gridView2 = (GridView) popupWindow.findViewById(R.id.right_type_gridView3);
        TextView rightG = (TextView) popupWindow.findViewById(R.id.meal_right_gg);
        TextView rightK = (TextView) popupWindow.findViewById(R.id.meal_right_kw);
        TextView righjk = (TextView) popupWindow.findViewById(R.id.meal_right_jk);
        ImageView tui = (ImageView) popupWindow.findViewById(R.id.meal_right_tui);
        TextView ren = (TextView) popupWindow.findViewById(R.id.meal_right_ren);
        final EditText jkedit = (EditText) popupWindow.findViewById(R.id.right_type_edit);
        jkedit.setHintTextColor(context.getResources().getColor(R.color.textColor_grey2));
        Attrlist1 = new ArrayList<>();
        Attrlist2 = new ArrayList<>();

        for (int i = 0; i < list.get(position).getAttrs().size(); i++) {
            OrderMealAttrBean bean = list.get(position).getAttrs().get(i);
            if (bean.getType() == 1) {
                Attrlist1.add(bean);
            } else {
                Attrlist2.add(bean);
            }
        }
        if (Attrlist1.size() == 0) {
            rightG.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
        }
        if (Attrlist2.size() == 0) {
            rightK.setVisibility(View.GONE);
            gridView1.setVisibility(View.GONE);
        }
        if (Attrlist3.size() == 0) {
            righjk.setVisibility(View.GONE);
            gridView2.setVisibility(View.GONE);
        }
        adapter = new MealRightGridAdapter(context, Attrlist1,gridView);
        gridView.setAdapter(adapter);
        adapter1 = new MealRightGridAdapter(context, Attrlist2,gridView1);
        gridView1.setAdapter(adapter1);
        adapter2 = new MealRightreMarkAdapter(context, Attrlist3);
        gridView2.setAdapter(adapter2);


        mPopupWindow = new PopupWindow(popupWindow, (int) (w * 0.8), LinearLayout.LayoutParams.WRAP_CONTENT, false);
        mPopupWindow.setOutsideTouchable(true);// 设置外部触摸会关闭窗口
        mPopupWindow.setFocusable(true);
        tui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });

        ren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                ArrayList<RemarkBean> remarkBeanArrayList = adapter2.getCh();
                ArrayList<OrderMealAttrBean> Attrlist4 = new ArrayList<>();
                MealListBean.MealRight mealRight1 = new MealListBean.MealRight();
                double p = 0;
                boolean isatttr = false;
                RemarkBean remarkBean = new RemarkBean();
                remarkBean.setName(jkedit.getText().toString());
                remarkBeanArrayList.add(remarkBean);

                if (!adapter.getCh().getName().equals("")) {
                    Attrlist4.add(adapter.getCh());
                    p = p + adapter.getCh().getPrice();
                    isatttr = true;
                }
                if (!adapter1.getCh().getName().equals("")) {
                    Attrlist4.add(adapter1.getCh());
                    p = p + adapter1.getCh().getPrice();
                    isatttr = true;
                }
                if (!isatttr) {
                    price = (double) (Math.round(price * 100) / 100.0);
                } else {
                    price = price + list.get(position).getPrice() + p;
                    price = (double) (Math.round(price * 100) / 100.0);
                    num++;
                }

                if (lists.size() > 0) {
                    boolean isE = true;
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).getAttrs().equals(Attrlist4) && Attrlist4.size() > 0) {

                            isE = false;
                            lists.get(i).setNumber(lists.get(i).getNumber() + 1);
                            Collections.sort(lists.get(i).getRemark());
                            Collections.sort(remarkBeanArrayList);
                            lists.get(i).setRemark(remarkBeanArrayList);
                        }
                    }
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).getID() == list.get(position).getID()) {
                            if (!isatttr) {
                                isE = false;
                                Collections.sort(lists.get(i).getRemark());
                                Collections.sort(remarkBeanArrayList);
                                lists.get(i).setRemark(remarkBeanArrayList);
                                lists.get(i).setNumber(lists.get(i).getNumber());
                            }

                        }
                    }
                    if (isE) {
                        MealListBean.MealRight mealRight = list.get(position);
                        mealRight1.setSoldCount(mealRight.getSoldCount());
                        mealRight1.setPrice(mealRight.getPrice());
                        mealRight1.setVipPrice(mealRight.getVipPrice());
                        mealRight1.setID(mealRight.getID());
                        mealRight1.setSellStatus(mealRight.getSellStatus());
                        mealRight1.setSetMeal(mealRight.getSetMeal());
                        mealRight1.setCataID(mealRight.getCataID());
                        mealRight1.setName(mealRight.getName());
                        mealRight1.setIconURL(mealRight.getIconURL());
                        mealRight1.setAttrs(Attrlist4);
                        mealRight1.setRemark(remarkBeanArrayList);
                        mealRight1.setNumber(1);
                        lists.add(mealRight1);
                    }

                } else {
                    MealListBean.MealRight mealRight = list.get(position);
                    mealRight1.setSoldCount(mealRight.getSoldCount());
                    mealRight1.setPrice(mealRight.getPrice());
                    mealRight1.setVipPrice(mealRight.getVipPrice());
                    mealRight1.setID(mealRight.getID());
                    mealRight1.setSellStatus(mealRight.getSellStatus());
                    mealRight1.setSetMeal(mealRight.getSetMeal());
                    mealRight1.setCataID(mealRight.getCataID());
                    mealRight1.setName(mealRight.getName());
                    mealRight1.setIconURL(mealRight.getIconURL());
                    mealRight1.setAttrs(Attrlist4);
                    mealRight1.setNumber(1);
                    mealRight1.setRemark(remarkBeanArrayList);
                    lists.add(mealRight1);
                }

                if (onNtClickListener != null) {
                    popupWindow = null;
                    mPopupWindow = null;
                    onNtClickListener.onNtClick(lists, num, price);
                }
            }
        });
        return mPopupWindow;
    }
}
