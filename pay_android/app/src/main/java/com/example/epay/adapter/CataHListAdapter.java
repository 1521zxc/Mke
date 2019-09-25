package com.example.epay.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import com.example.epay.bean.CataBean;
import com.example.epay.bean.CatatypeBean;
import com.example.epay.bean.OrderMealAttrBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/1/20.
 */

public class CataHListAdapter extends TBaseAdapter<CataBean.cataItem> {

    ArrayList<CataBean.cataItem> lists=new ArrayList<>();
    OnNtClickListener onNtClickListener;

    ArrayList<OrderMealAttrBean> Attrlist1=new ArrayList<>();
    ArrayList<OrderMealAttrBean> Attrlist2=new ArrayList<>();

    public CataHListAdapter(Activity context, ArrayList<CataBean.cataItem> list,ArrayList<CataBean.cataItem> lists) {
        super(context, list);
        this.lists=lists;
    }

    public void setData(ArrayList<CataBean.cataItem> lists)
    {
        this.lists=lists;
        notifyDataSetChanged();
    }


    public interface OnNtClickListener {
        void onNtClick( ArrayList<CataBean.cataItem> list);
    }

    public void setOnNtClickListener(OnNtClickListener listener) {
        this.onNtClickListener = listener;
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_cata_h_list;
    }

    @Override
    public void initItemView(final PxViewHolder view1, final ArrayList<CataBean.cataItem> list, final int position) {
        final CataBean.cataItem cataItem1=list.get(position);
        final CataBean.cataItem cataItem2=new CataBean.cataItem();
        boolean isNum=true;
        cataItem2.setSoldCount(cataItem1.getSoldCount());
        cataItem2.setPrice(cataItem1.getPrice());
        cataItem2.setVipPrice(cataItem1.getVipPrice());
        cataItem2.setID(cataItem1.getID());
        cataItem2.setSellStatus(cataItem1.getSellStatus());
        cataItem2.setSetMeal(cataItem1.getSetMeal());
        cataItem2.setCataID(cataItem1.getCataID());
        cataItem2.setName(cataItem1.getName());
        cataItem2.setIconURL(cataItem1.getIconURL());
        cataItem2.setAttrs(cataItem1.getAttrs());
        if(lists.size()>0) {
            for (int i = 0; i < lists.size(); i++) {
                if(cataItem1.getID()==lists.get(i).getID())
                {
                    if(lists.get(i).getAttrs()==null||lists.get(i).getAttrs().size()<1) {
                        isNum=false;
                        cataItem1.setNumber(lists.get(i).getNumber());
                    }
                }
            }
        }
        if(isNum)
        {
            cataItem1.setNumber(0);

        }
        cataItem2.setNumber(cataItem1.getNumber());
        view1.<TextView>find(R.id.item_cata_h_name).setText(cataItem2.getName());
        view1.<TextView>find(R.id.item_cata_h_price).setText("￥"+cataItem2.getPrice());
        view1.<TextView>find(R.id.item_cata_h_num).setText(cataItem2.getNumber()+"");


        if(cataItem2.getAttrs()==null||cataItem2.getAttrs().size()<1){
            view1.<LinearLayout>find(R.id.item_cata_h_layout).setVisibility(View.VISIBLE);
            view1.<TextView>find(R.id.item_cata_h_gg).setVisibility(View.GONE);
        }else{
            view1.<LinearLayout>find(R.id.item_cata_h_layout).setVisibility(View.GONE);
            view1.<TextView>find(R.id.item_cata_h_gg).setVisibility(View.VISIBLE);
        }



        if(cataItem2.getNumber()>0)
        {
            view1.<TextView>find(R.id.item_cata_h_reduct).setVisibility(View.VISIBLE);
            view1.<TextView>find(R.id.item_cata_h_num).setVisibility(View.VISIBLE);
        }else{
            view1.<TextView>find(R.id.item_cata_h_reduct).setVisibility(View.INVISIBLE);
            view1.<TextView>find(R.id.item_cata_h_num).setVisibility(View.INVISIBLE);
        }


        if(cataItem1.getAttrs().size()>0)
        {
            view1.<LinearLayout>find(R.id.item_cata_h_layout).setVisibility(View.GONE);
            view1.<TextView>find(R.id.item_cata_h_gg).setVisibility(View.VISIBLE);
        }else{
            view1.<LinearLayout>find(R.id.item_cata_h_layout).setVisibility(View.VISIBLE);
            view1.<TextView>find(R.id.item_cata_h_gg).setVisibility(View.GONE);
        }




        view1.<TextView>find(R.id.item_cata_h_gg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow popupWindow=initListPopuptWindow(position);
                popupWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.CENTER,0,0);
            }
        });








        view1.<TextView>find(R.id.item_cata_h_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cataItem2.getNumber()==0)
                {
                    view1.<TextView>find(R.id.item_cata_h_reduct).setVisibility(View.VISIBLE);
                    view1.<TextView>find(R.id.item_cata_h_num).setVisibility(View.VISIBLE);
                }
                if(lists.size()>0) {
                    for (int i = 0; i < lists.size(); i++) {
                        if(cataItem1.getID()==lists.get(i).getID())
                        {
                            if(lists.get(i).getAttrs()==null||lists.get(i).getAttrs().size()<1) {
                                lists.remove(i);
                            }
                        }
                    }
                }

                cataItem2.setNumber(cataItem1.getNumber()+1);
                view1.<TextView>find(R.id.item_cata_h_num).setText(cataItem2.getNumber()+"");
                lists.add(cataItem2);
                cataItem1.setNumber(cataItem2.getNumber());
                if(onNtClickListener!=null) {
                    onNtClickListener.onNtClick(lists);
                }
            }
        });

        view1.<TextView>find(R.id.item_cata_h_reduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cataItem2.getNumber()==1)
                {
                    view1.<TextView>find(R.id.item_cata_h_reduct).setVisibility(View.INVISIBLE);
                    view1.<TextView>find(R.id.item_cata_h_num).setVisibility(View.INVISIBLE);
                }
                if(lists.size()>0) {
                    for (int i = 0; i < lists.size(); i++) {
                        if(cataItem1.getID()==lists.get(i).getID())
                        {
                            if(lists.get(i).getAttrs()==null||lists.get(i).getAttrs().size()<1) {
                                lists.remove(i);
                            }
                        }
                    }
                }
                cataItem2.setNumber(cataItem1.getNumber()-1);
                view1.<TextView>find(R.id.item_cata_h_num).setText(cataItem2.getNumber()+"");
                if(cataItem2.getNumber()!=0)
                {
                    lists.add(cataItem2);
                }

                cataItem1.setNumber(cataItem2.getNumber());
                if(onNtClickListener!=null) {
                    onNtClickListener.onNtClick(lists);
                }

            }
        });
    }









    private  PopupWindow mPopupWindow;
    MealRightGridAdapter adapter;
    MealRightGridAdapter adapter1;
    View popupWindow;
    public PopupWindow initListPopuptWindow(final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popupWindow = layoutInflater.inflate(R.layout.meal_right_list, null);
        GridView gridView = (GridView) popupWindow.findViewById(R.id.right_type_gridView);
        GridView gridView1 = (GridView) popupWindow.findViewById(R.id.right_type_gridView2);
        TextView rightG = (TextView) popupWindow.findViewById(R.id.meal_right_gg);
        TextView rightK = (TextView) popupWindow.findViewById(R.id.meal_right_kw);
        ImageView tui = (ImageView) popupWindow.findViewById(R.id.meal_right_tui);
        TextView ren = (TextView) popupWindow.findViewById(R.id.meal_right_ren);
        Attrlist1=new ArrayList<>();
        Attrlist2=new ArrayList<>();
        for(int i=0;i<list.get(position).getAttrs().size();i++)
        {
            OrderMealAttrBean bean=list.get(position).getAttrs().get(i);
            if(bean.getType()==1)
            {
                Attrlist1.add(bean);
            }else{
                Attrlist2.add(bean);
            }
        }
        if(Attrlist1.size()==0)
        {
            rightG.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
        }
        if(Attrlist2.size()==0)
        {
            rightK.setVisibility(View.GONE);
            gridView1.setVisibility(View.GONE);
        }

        adapter = new MealRightGridAdapter(context,Attrlist1,gridView);
        gridView.setAdapter(adapter);
        adapter1 = new MealRightGridAdapter(context,Attrlist2,gridView1);
        gridView1.setAdapter(adapter1);

        mPopupWindow = new PopupWindow(popupWindow, (int)(w*0.8), LinearLayout.LayoutParams.WRAP_CONTENT, false);
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
                ArrayList<OrderMealAttrBean> Attrlist3=new ArrayList<>();
                CataBean.cataItem mealRight1=new CataBean.cataItem();
                if(!adapter.getCh().getName().equals("")) {
                    Attrlist3.add(adapter.getCh());
                }
                if(!adapter1.getCh().getName().equals("")) {
                    Attrlist3.add(adapter1.getCh());
                }
                if(lists.size()>0) {
                    boolean isE=true;
                    for (int i = 0; i < lists.size(); i++) {
                        if(lists.get(i).getAttrs().equals(Attrlist3))
                        {
                            isE=false;
                            lists.get(i).setNumber(lists.get(i).getNumber()+1);
                        }
                    }
                    if(isE)
                    {
                        CataBean.cataItem  mealRight = list.get(position);
                        mealRight1.setSoldCount(mealRight.getSoldCount());
                        mealRight1.setPrice(mealRight.getPrice());
                        mealRight1.setVipPrice(mealRight.getVipPrice());
                        mealRight1.setID(mealRight.getID());
                        mealRight1.setSellStatus(mealRight.getSellStatus());
                        mealRight1.setSetMeal(mealRight.getSetMeal());
                        mealRight1.setCataID(mealRight.getCataID());
                        mealRight1.setName(mealRight.getName());
                        mealRight1.setIconURL(mealRight.getIconURL());
                        mealRight1.setAttrs(Attrlist3);
                        mealRight1.setNumber(1);
                        lists.add(mealRight1);
                    }

                }else{
                    CataBean.cataItem  mealRight = list.get(position);
                    mealRight1.setSoldCount(mealRight.getSoldCount());
                    mealRight1.setPrice(mealRight.getPrice());
                    mealRight1.setVipPrice(mealRight.getVipPrice());
                    mealRight1.setID(mealRight.getID());
                    mealRight1.setSellStatus(mealRight.getSellStatus());
                    mealRight1.setSetMeal(mealRight.getSetMeal());
                    mealRight1.setCataID(mealRight.getCataID());
                    mealRight1.setName(mealRight.getName());
                    mealRight1.setIconURL(mealRight.getIconURL());
                    mealRight1.setAttrs(Attrlist3);
                    mealRight1.setNumber(1);
                    lists.add(mealRight1);
                }
                if(onNtClickListener!=null) {
                    popupWindow=null;
                    mPopupWindow=null;
                    onNtClickListener.onNtClick(lists);
                }
            }
        });
        return mPopupWindow;
    }
}