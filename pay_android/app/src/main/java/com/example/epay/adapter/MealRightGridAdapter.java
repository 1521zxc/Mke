package com.example.epay.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.TBaseAdapter;
import java.lang.reflect.Field;
import com.example.epay.bean.OrderMealAttrBean;

import java.util.ArrayList;

/**
 * Created by liujin on 2018/6/15.
 */

public class MealRightGridAdapter extends TBaseAdapter<OrderMealAttrBean> {
    OrderMealAttrBean attrBean=new OrderMealAttrBean();
    GridView gv;
    ArrayList<Integer> heights = new ArrayList<>(0);
    int gvWidth = 500;
    public MealRightGridAdapter(Activity context, ArrayList<OrderMealAttrBean> list, GridView gv) {
        super(context, list);
        this.gv=gv;
        gvWidth=gv.getWidth();
    }

    public OrderMealAttrBean getCh()
    {
        return attrBean;
    }

    @Override
    public int getItemResourceId() {
        return R.layout.item_meal_right_grid;
    }

    @Override
    public void initItemView(PxViewHolder view, final ArrayList<OrderMealAttrBean> list, final int position) {


        final ViewTreeObserver vo = view.<TextView>find(R.id.item_meal_right_name).getViewTreeObserver();

        vo.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override

            public boolean onPreDraw() {
                int line = ((int)(Math.ceil((getCount() * 1.0f) / gv.getNumColumns())));

                int gvHeight = 0;

                for (int h : heights){

                    gvHeight += h;

                }

                if (heights.size() >= line &&  gv.getHeight() != gvHeight){

                    ViewGroup.LayoutParams params = gv.getLayoutParams();

                    params.height = gvHeight;

                    gv.setLayoutParams(params);

                }

                return true;

            }

        });



        if (heights.size() == 0) {

            /**

             * API要求小于16的，请使用此段代码

             */

            if (Build.VERSION.SDK_INT < 16) {

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.<TextView>find(R.id.item_meal_right_name).getLayoutParams();

                int columWidthNum = gv.getNumColumns() > 1 ? (gv.getNumColumns() - 1) : 0;

                int tvWidth = ((gvWidth - columWidthNum * getGvHorizontalSpacing(gv) - gv.getPaddingLeft()

                        - gv.getPaddingRight()) / gv.getNumColumns() - params.leftMargin - params.rightMargin);

                view.<TextView>find(R.id.item_meal_right_name).setWidth(tvWidth);

            }else {

                /**

                 * API要求大于等于16的，请使用此段代码

                 */

                view.<TextView>find(R.id.item_meal_right_name).setWidth(gv.getColumnWidth());


            }



            int line = ((int)(Math.ceil((getCount() * 1.0f) / gv.getNumColumns())));

            for (int i = 0; i < line; i++) {

                int tmpHeight = 0;

                String str = "";

                //获取一行中数据最长一个字符串

                for (int j = 0; j < gv.getNumColumns() && (i * gv.getNumColumns() + j) < getCount(); j++) {

                    String tmpStr = list.get(i*gv.getNumColumns() + j).getName();

                    if (tmpStr.length() > str.length()){

                        str = tmpStr;

                    }

                }

                //手动获取计算后的高度

                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

                view.<TextView>find(R.id.item_meal_right_name).setText(str);

                view.<TextView>find(R.id.item_meal_right_name).measure(w, h);

                int heightxx = view.<TextView>find(R.id.item_meal_right_name).getMeasuredHeight();

                if (heightxx > tmpHeight) {

                    tmpHeight = heightxx;

                }



                if (heights.size() <= i){

                    heights.add(tmpHeight);

                }else {

                    heights.set(i, tmpHeight);

                }

            }

        }



        if (gv.getHeight() != 0){

            view.<TextView>find(R.id.item_meal_right_name).setHeight(heights.get(position/gv.getNumColumns()));

        }

        view.<TextView>find(R.id.item_meal_right_name).setText(list.get(position).getName());











        if(list.get(position).isIsch())
        {
            attrBean=list.get(position);
            view.<TextView>find(R.id.item_meal_right_name).setBackground(context.getResources().getDrawable(R.drawable.corner_red_small));
            view.<TextView>find(R.id.item_meal_right_name).setTextColor(context.getResources().getColor(R.color.textColor_white));
        }else{
            view.<TextView>find(R.id.item_meal_right_name).setBackground(context.getResources().getDrawable(R.drawable.corner_white_small));
            view.<TextView>find(R.id.item_meal_right_name).setTextColor(context.getResources().getColor(R.color.textColor_grey));
        }


























        view.<TextView>find(R.id.item_meal_right_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               for(int i=0;i<list.size();i++)
               {
                   if(i==position)
                   {
                       list.get(i).setIsch(true);
                   }else{
                       list.get(i).setIsch(false);
                   }
               }
              setList(list);
            }
        });
    }


    private int getGvHorizontalSpacing(GridView gv){

        int horizontalSpace = 0;



        if (gv != null){

            //得到类对象

            Class gvClass = (Class) gv.getClass();

            /**

             * 得到类中的所有属性集合

             */

            Field[] fs = gvClass.getDeclaredFields();

            for (int i = 0; i < fs.length; i++){

                Field f = fs[i];

                f.setAccessible(true); //设置属性可访问

                try {

                    Object val = f.get(gv);

                    if (f.getName().equals("mHorizontalSpacing")){                       //切记切记，该属性名不能被混淆，否则是取不到该属性的

                        horizontalSpace = (int)val;

                        break;

                    }

                }catch (Exception e){

                }

            }

        }



        return horizontalSpace;

    }

}
