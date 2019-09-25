package com.example.epay.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.adapter.CataHListAdapter;
import com.example.epay.adapter.CataHtypeListAdapter;
import com.example.epay.adapter.CataMenuAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.AttachedBean;
import com.example.epay.bean.CataBean;
import com.example.epay.bean.CatatypeBean;
import com.example.epay.bean.OrderInfoBean;
import com.example.epay.bean.OrderMealAttrBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.view.EPayHListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class CataHActivity extends BaseActivity {
    @Bind(R.id.cata_h_type)
    TextView typeText;
    @Bind(R.id.cata_h_listView)
    ListView listView;
    @Bind(R.id.cata_h_listView2)
    ListView listView1;


    String data = "";
    ArrayList<CatatypeBean.catatype> catatypelist;
    ArrayList<CataBean.cataItem> cataItems;
    ArrayList<CataBean.cataItem> cataItems2;
    ArrayList<AttachedBean> Attachs;
    ArrayList<OrderInfoBean.ProdAttrItem> removes;
    CataHtypeListAdapter typeAdapter;
    CataHListAdapter adapter;
    CataMenuAdapter adapter2;
    CataBean bean;
    OrderInfoBean listBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cata_h);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();

        catatypelist = new ArrayList<>();
        cataItems = new ArrayList<>();
        cataItems2 = new ArrayList<>();
        Attachs = new ArrayList<>();
        removes = new ArrayList<>();
        typeAdapter = new CataHtypeListAdapter(this, catatypelist);
        adapter = new CataHListAdapter(this, cataItems, cataItems2);
        adapter2 = new CataMenuAdapter(this, cataItems2);
        listView.setAdapter(adapter);
        listView1.setAdapter(adapter2);

        doCatatype();
        doCata();


        adapter.setOnNtClickListener(new CataHListAdapter.OnNtClickListener() {
            @Override
            public void onNtClick(ArrayList<CataBean.cataItem> list) {
                cataItems2 = list;
                adapter.setData(cataItems2);
                adapter2.setList(cataItems2);
            }
        });
        adapter2.setOnNtClickListener(new CataMenuAdapter.OnNtClickListener() {
            @Override
            public void onNtClick(ArrayList<CataBean.cataItem> list) {
                cataItems2 = list;
                adapter.setData(cataItems2);
                adapter2.setList(cataItems2);
            }
        });
    }

    @OnClick(R.id.cata_h_que)
    public void que() {
        inits();
    }

    @OnClick(R.id.cata_h_type)
    public void type() {
        PopupWindow popupWindow = initListPopuptWindow();
        if (Build.VERSION.SDK_INT != 24) {
            //只有24这个版本有问题，好像是源码的问题
            popupWindow.showAsDropDown(typeText);
        } else {
            //7.0 showAsDropDown没卵子用 得这么写
            int[] location = new int[2];
            typeText.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            popupWindow.showAtLocation(typeText, Gravity.NO_GRAVITY, 0, y + typeText.getHeight());
        }
    }


    public void initdata() {
        ArrayList<CataBean.cataItem> list = bean.getItems();
        ArrayList<OrderMealAttrBean> lists = bean.getProductAttrs();
        cataItems = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).getSellStatus() != 0 && list.get(j).getSellStatus() != -1) {
                CataBean.cataItem right = new CataBean.cataItem();
                right.setCataID(list.get(j).getCataID());
                right.setSoldCount(list.get(j).getSoldCount());
                right.setSetMeal(list.get(j).getSetMeal());
                right.setSellStatus(list.get(j).getSellStatus());
                right.setPrice(list.get(j).getPrice());
                right.setIconURL(list.get(j).getIconURL());
                right.setName(list.get(j).getName());
                right.setID(list.get(j).getID());
                right.setVipPrice(list.get(j).getVipPrice());
                ArrayList<OrderMealAttrBean> list2 = new ArrayList<>();
                if (lists.size() > 0) {
                    for (int n = 0; n < lists.size(); n++) {
                        if (lists.get(n).getProductID() == list.get(j).getID()) {
                            OrderMealAttrBean bean1 = lists.get(n);
                            list2.add(bean1);
                        }
                    }
                }
                right.setAttrs(list2);
                cataItems.add(right);
            }
        }
        adapter.setList(cataItems);
    }


    //菜类
    private void doCatatype() {
        httpUtil.HttpServer(this, data, 4, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                CatatypeBean bean = gson.fromJson(data, CatatypeBean.class);
                catatypelist = bean.getItems();
                if (catatypelist != null) {

                } else {
                    showMessage("没有数据");
                }
            }

            @Override
            public void fail(String Message, int code,String data) {
                showMessage(Message);
            }
        });
    }

    //菜品
    private void doCata() {
        httpUtil.HttpServer(this, data, 2, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                bean = gson.fromJson(data, CataBean.class);
                if (cataItems != null) {
                    initdata();
                } else {
                    showMessage("没有数据");
                }
            }

            @Override
            public void fail(String Message, int code,String data) {
                showMessage(Message);
            }
        });
    }


    //换菜
    private void doHcata() {
        httpUtil.HttpServer(this, data, 92, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                listBean = gson.fromJson(data, OrderInfoBean.class);
                Intent intent = new Intent(CataHActivity.this, OrderPayActivity.class);
                intent.putExtra("deskName", listBean.getDeskName());
                intent.putExtra("orderNo", listBean.getOrderNO());
                startActivity(intent);
                finish();
            }

            @Override
            public void fail(String Message, int code,String data) {
                showMessage(Message);
            }
        });
    }


    private PopupWindow mPopupWindow;

    public PopupWindow initListPopuptWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View popupWindow = layoutInflater.inflate(R.layout.cata_popup, null);
        final EPayHListView listView1 = (EPayHListView) popupWindow.findViewById(R.id.cata_popup_listView);
        mPopupWindow = new PopupWindow(popupWindow, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        mPopupWindow.setOutsideTouchable(true);// 设置外部触摸会关闭窗口
        mPopupWindow.setFocusable(true);
        listView1.setAdapter(typeAdapter);
        if (!catatypelist.get(0).getName().equals("全部菜品")) {
            CatatypeBean.catatype all = new CatatypeBean.catatype();
            all.setID(0);
            all.setName("全部菜品");
            catatypelist.add(0, all);
        }
        typeAdapter.setList(catatypelist);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPopupWindow.dismiss();
                typeText.setText(catatypelist.get(i).getName());
                JSONObject object = new JSONObject();
                object.put("cataID", catatypelist.get(i).getID());
                data = object.toString();
                doCata();
            }
        });
        listView1.setListViewHeight((int) (0.6 * width));
        return mPopupWindow;
    }


    public void inits() {
        for (int i = 0; i < cataItems2.size(); i++) {
            AttachedBean bean = new AttachedBean();
            bean.setID(cataItems2.get(i).getID());
            bean.setCount(cataItems2.get(i).getNumber());
            ArrayList<AttachedBean.AttrBean> attrBeans = new ArrayList<>();
            if (cataItems2.get(i).getAttrs() != null && cataItems2.get(i).getAttrs().size() > 0) {
                for (int j = 0; j < cataItems2.get(i).getAttrs().size(); j++) {
                    AttachedBean.AttrBean attrBean = new AttachedBean.AttrBean();
                    attrBean.setID(cataItems2.get(i).getAttrs().get(j).getID());
                    attrBeans.add(attrBean);
                }
                bean.setAttrs(attrBeans);
            }
            Attachs.add(bean);
        }


        ArrayList<OrderInfoBean.ProdAttrItem> listString = (ArrayList<OrderInfoBean.ProdAttrItem>) getIntent().getSerializableExtra("list");
        for (int i = 0; i < listString.size(); i++) {
            OrderInfoBean.ProdAttrItem item = new OrderInfoBean.ProdAttrItem();
            item.setODID(listString.get(i).getODID());
            item.setCount(listString.get(i).getCount());
            removes.add(item);
        }
        JSONObject object = new JSONObject();
        object.put("ID", getIntent().getIntExtra("id", 0));
        object.put("updates", removes);
        object.put("attached", Attachs);
        data = object.toString();
        doHcata();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("菜品订单换菜"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("菜品订单换菜"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
