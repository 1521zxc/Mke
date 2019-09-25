package com.example.epay.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.adapter.ConfirmOrderListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.AttachedBean;
import com.example.epay.bean.ConfirmBean;
import com.example.epay.bean.DeskBean;
import com.example.epay.bean.MealListBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.view.EPayListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class ConfirmOrderActivity extends BaseActivity {
    @Bind(R.id.confirm_order_listView)
    EPayListView listView;

    @Bind(R.id.confirm_order_price1)
    TextView price1;
    @Bind(R.id.confirm_order_price2)
    TextView price2;
    @Bind(R.id.confirm_order_beizhu)
    TextView bz;
    @Bind(R.id.confirm_order_num)
    TextView numEdit;
    @Bind(R.id.confirm_order_desk)
    TextView desk;
    ArrayList<MealListBean.MealRight> list;
    ArrayList<AttachedBean> lists;
    double price=0;
    int num=0;
    ConfirmOrderListAdapter adapter;

    String data="";
    ConfirmBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        list= CacheData.getMealBean(this);
        lists=new ArrayList<>();
        price=getIntent().getDoubleExtra("price",0);
        num=getIntent().getIntExtra("num",0);
        price1.setText("￥"+price);
        price2.setText(num+"份￥"+price);
        desk.setText("桌号："+getIntent().getStringExtra("deskName"));
        adapter=new ConfirmOrderListAdapter(this,list);
        listView.setAdapter(adapter);
        numEdit.setHintTextColor(getResources().getColor(R.color.textColor_grey2));
        if(!getIntent().getBooleanExtra("meal",true)){
            bz.setText(getIntent().getStringExtra("remark"));
            numEdit.setText(getIntent().getIntExtra("people",0)+"");
        }
        for(int i=0;i<list.size();i++)
        {
            AttachedBean bean=new AttachedBean();
            bean.setID(list.get(i).getID());
            bean.setCount(list.get(i).getNumber());
            if(list.get(i).getSubItems().size()>0){
                ArrayList<AttachedBean.SubItemBean> subItemBeans=new ArrayList<>();
                for(MealListBean.MealRight mealRightqqqq:list.get(i).getSubItems()) {
                    AttachedBean.SubItemBean subItemBean = new AttachedBean.SubItemBean();
                    subItemBean.setID(mealRightqqqq.getID());
                    subItemBean.setCount(mealRightqqqq.getCount());
                    subItemBeans.add(subItemBean);
                }
                bean.setSubItems(subItemBeans);
            }
            ArrayList<AttachedBean.AttrBean>  attrBeans=new ArrayList<>();
            if(list.get(i).getAttrs()!=null&&list.get(i).getAttrs().size()>0)
            {
                for(int j=0;j<list.get(i).getAttrs().size();j++) {
                    AttachedBean.AttrBean attrBean=new AttachedBean.AttrBean();
                    attrBean.setID(list.get(i).getAttrs().get(j).getID());
                    attrBeans.add(attrBean);
                }
                bean.setAttrs(attrBeans);
            }
            if(list.get(i).getRemark()!=null&&list.get(i).getRemark().size()>0) {
                String remark="";
                for (int j = 0; j < list.get(i).getRemark().size(); j++) {
                            if(j==list.get(i).getRemark().size()-1){
                                remark=remark+list.get(i).getRemark().get(j).getName();
                            }else{
                                remark=remark+list.get(i).getRemark().get(j).getName()+",";
                            }
                }
                bean.setRemark(remark);
            }

            lists.add(bean);
        }
    }

    @OnClick(R.id.confirm_order_submit)
    public void submit() {
        JSONObject object=new JSONObject();
        object.put("remark",bz.getText().toString());
        int people=0;
        if(!numEdit.getText().toString().equals("")) {
            people= Integer.parseInt(numEdit.getText().toString());
        }
        object.put("totalNum",people);
        object.put("attached",lists);
        object.put("deskNO",getIntent().getStringExtra("deskNo"));

        if(getIntent().getBooleanExtra("meal",true))
        {
            data=object.toString();
            doHttp();
        }else{
            int id=getIntent().getIntExtra("orderID",0);
            object.put("ID",id);
            data=object.toString();
            doHttp1();
        }
    }


    @OnClick(R.id.confirm_order_submit2)
    public void submit2() {
        JSONObject object=new JSONObject();
        object.put("remark","（等叫）"+bz.getText().toString());
        int people=0;
        if(!numEdit.getText().toString().equals("")) {
            people= Integer.parseInt(numEdit.getText().toString());
        }
        object.put("totalNum",people);
        object.put("attached",lists);
        object.put("deskNO",getIntent().getStringExtra("deskNo"));

        if(getIntent().getBooleanExtra("meal",true))
        {
            data=object.toString();
            doHttp();
        }else{
            int id=getIntent().getIntExtra("orderID",0);
            object.put("ID",id);
            data=object.toString();
            doHttp1();
        }
    }

    public void doHttp() {
        httpUtil.HttpServer(this, data, 80, true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                bean=gson.fromJson(data1,ConfirmBean.class);
                if(bean!=null) {
                    Intent intent = new Intent(ConfirmOrderActivity.this, OrderPayActivity.class);
                    intent.putExtra("deskName", bean.getDeskName());
                    intent.putExtra("orderNo", bean.getOrderNO());
                    startActivity(intent);
                    finish();
                }else{
                    showMessage("没有数据");
                }
            }

            @Override
            public void fail(String Message,int code,String data1) {
                if(code==3)
                {
                    if(!data1.equals("")) {
                        bean = gson.fromJson(data1, ConfirmBean.class);
                        JSONObject object = JSONObject.parseObject(data);
                        object.put("ID", bean.getOrderID());
                        Intent intent = new Intent(ConfirmOrderActivity.this, ConfirmAddActivity.class);
                        intent.putExtra("data", object.toString());
                        intent.putExtra("list", list);
                        startActivity(intent);
                    }else{
                        doOrderID();
                   }
                }else {
                    showMessage(Message);
                }
            }
        });
    }


    public void doHttp1()
    {
        httpUtil.HttpServer(this, data, 92, true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                bean=gson.fromJson(data1,ConfirmBean.class);
                if(bean!=null) {
                    Intent intent = new Intent(ConfirmOrderActivity.this, OrderPayActivity.class);
                    intent.putExtra("deskName", bean.getDeskName());
                    intent.putExtra("orderNo", bean.getOrderNO());
                    startActivity(intent);
                }else{
                    showMessage("没有数据");
                }
            }

            @Override
            public void fail(String Message,int code,String data1) {
                if(code==3)
                {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ConfirmOrderActivity.this);
                    builder.setMessage("此桌子已被清理，是否下单？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                                    JSONObject object=  JSONObject.parseObject(data);
                                    object.remove("ID");
                                    data=object.toString();
                                    doHttp();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();

                }else {
                    showMessage(Message);
                }
            }
        });
    }

    public void doOrderID() {
        httpUtil.HttpServer(this, "{\"deskNO\":"+getIntent().getStringExtra("deskNo")+"}", 54, false, new HttpCallBack() {
            @Override
            public void back(String data) {
                DeskBean  desk=gson.fromJson(data,DeskBean.class);
                JSONObject object = JSONObject.parseObject(data);
                object.put("ID", desk.getOrderID());
                Intent intent = new Intent(ConfirmOrderActivity.this, ConfirmAddActivity.class);
                intent.putExtra("data", object.toString());
                intent.putExtra("list", list);
                startActivity(intent);
            }

            @Override
            public void fail(String Message, int code,String data) { }
        });
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("确认菜品订单"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("确认菜品订单"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}
