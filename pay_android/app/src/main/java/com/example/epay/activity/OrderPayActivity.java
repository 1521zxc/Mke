package com.example.epay.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.adapter.OrderInfoListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.OrderInfoBean;
import com.example.epay.bean.PayNoBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.DateUtil;
import com.example.epay.util.MyDialog;
import com.example.epay.view.EPayListView;
import com.umeng.analytics.MobclickAgent;


import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class OrderPayActivity extends BaseActivity {
    @Bind(R.id.order_pay_listView)
    EPayListView listView;
    @Bind(R.id.order_pay_price1)
    TextView price1;
    @Bind(R.id.order_pay_price2)
    TextView price2;
    @Bind(R.id.order_pay_price4)
    TextView price4;
    @Bind(R.id.order_pay_price5)
    TextView price5;
    @Bind(R.id.order_pay_desk)
    TextView desk;
    @Bind(R.id.order_pay_add)
    TextView addText;
    @Bind(R.id.order_pay_h)
    TextView hText;
    @Bind(R.id.order_pay_reduct)
    TextView reductText;
    @Bind(R.id.order_pay_desk_h)
    TextView deskHText;
    @Bind(R.id.order_pay_QR)
    ImageView QRImage;

    @Bind(R.id.order_pay_layout)
    LinearLayout layout;
    @Bind(R.id.order_pay_type1)
    TextView payType1;
    @Bind(R.id.order_pay_type2)
    TextView payType2;
    @Bind(R.id.order_pay_pay)
    TextView payPay;
    @Bind(R.id.order_pay_cata_on)
    TextView cataOn;
    @Bind(R.id.order_pay_cata_off)
    TextView cataOff;
    @Bind(R.id.order_pay_remark)
    TextView remarkText;
    @Bind(R.id.wm_layout)
    LinearLayout wmLayout;
    @Bind(R.id.wm_name)
    TextView wmName;
    @Bind(R.id.wm_phone)
    TextView wmPhone;
    @Bind(R.id.wm_address)
    TextView wmAddress;


    ArrayList<OrderInfoBean.ProductSimple> list1=new ArrayList<>();
    ArrayList<OrderInfoBean.ProductSimple> list=new ArrayList<>();
    OrderInfoListAdapter adapter;
    OrderInfoBean listBean;
    String data = "";
    int type = 0;
    ArrayList<OrderInfoBean.ProdAttrItem> lists;
    ArrayList<OrderInfoBean.ProdAttrItem> attachedBeans = new ArrayList<>();
    String orderNo = "";
    ArrayList<PayNoBean> trueList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        changeDrawable();
        list = new ArrayList<>();
        lists = new ArrayList<>();
        desk.setText(getIntent().getStringExtra("deskName"));
        adapter = new OrderInfoListAdapter(this, list);
        listView.setAdapter(adapter);
        JSONObject object = new JSONObject();
        orderNo = getIntent().getStringExtra("orderNo");
        object.put("orderNO", getIntent().getStringExtra("orderNo"));
        object.put("confirm", getIntent().getIntExtra("confirm", 0));
        data = object.toString();
        if (CacheData.getUser(this, String.valueOf(CacheData.getId(this))).getOnlinePay() != 1) {
            layout.setVisibility(View.INVISIBLE);
            payPay.setVisibility(View.GONE);
        } else if (CacheData.getUser(this, String.valueOf(CacheData.getId(this))).getUserIsPay() != 1) {
            layout.setVisibility(View.INVISIBLE);
            payPay.setVisibility(View.GONE);
        }
        doSpecial();
        setPayType1();
        layout.setVisibility(View.INVISIBLE);
    }

    public void changeDrawable() {
        Drawable drawable = getResources().getDrawable(R.drawable.desk_btn_add);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable.setBounds(0, 0, (int) (0.08 * width), (int) (0.08 * width));
        //drawable   第一个是文字TOP
        addText.setCompoundDrawables(null, drawable, null, null);
        Drawable drawable1 = getResources().getDrawable(R.drawable.desk_cata_h);
        drawable1.setBounds(0, 0, (int) (0.08 * width), (int) (0.08 * width));
        hText.setCompoundDrawables(null, drawable1, null, null);
        Drawable drawable2 = getResources().getDrawable(R.drawable.desk_btn_reduct);
        drawable2.setBounds(0, 0, (int) (0.08 * width), (int) (0.08 * width));
        reductText.setCompoundDrawables(null, drawable2, null, null);
        Drawable drawable3 = getResources().getDrawable(R.drawable.desk_btn_h);
        drawable3.setBounds(0, 0, (int) (0.08 * width), (int) (0.08 * width));
        deskHText.setCompoundDrawables(null, drawable3, null, null);
    }

    //活动
    private void doSpecial() {
        httpUtil.HttpServer(this, data, 86, true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                listBean = gson.fromJson(data1, OrderInfoBean.class);
                if (listBean != null) {
                    list1 = listBean.getAttached();
                    cataOn.setText("编辑");
                    cataOff.setVisibility(View.GONE);
                    list.clear();
                    if(list1.size()>0){
                        for(OrderInfoBean.ProductSimple sm:list1){
                            if(sm.getIsSub()!=1){
                                list.add(sm);
                            }
                        }
                    }
                    adapter.setList(list);
                    desk.setText(listBean.getDeskName());
                    price1.setText("消费：￥" + listBean.getPrimeMoney());
                    price4.setText("实收款：￥" + listBean.getPaidMoney());
                    price5.setText("优惠券：￥" + Math.abs(listBean.getRefundMoney()));
                    remarkText.setText("备注：" + listBean.getRemark());
                    if (!listBean.getContactInfo().getFullName().equals("")) {
                        wmLayout.setVisibility(View.VISIBLE);
                        wmAddress.setVisibility(View.VISIBLE);
                        wmName.setText("收货人：" + listBean.getContactInfo().getFullName());
                        wmPhone.setText("电话：" + listBean.getContactInfo().getTelephone());
                        wmAddress.setText("配送地址：" + listBean.getContactInfo().getRegion() + listBean.getContactInfo().getDetail());
                    } else {
                        wmLayout.setVisibility(View.GONE);
                        wmAddress.setVisibility(View.GONE);
                    }
                    DecimalFormat df = new DecimalFormat("###0.00");
                    price2.setText("优惠：￥" + df.format(listBean.getDiscountMoney()));
                    if (listBean.getPayments().size() > 0) {
                        for (int i = 0; i < listBean.getPayments().size(); i++) {
                            if (listBean.getPayments().get(i).getPayMoney() > 0 && i != listBean.getPayments().size() - 1) {
                                for (int j = 0; j < listBean.getPayments().size(); j++) {
                                    if (listBean.getPayments().get(j).getPayMoney() < 0) {
                                        if (listBean.getPayments().get(i).getPayNO().equals(listBean.getPayments().get(j).getPayNO().split("R")[0])) {
                                            listBean.getPayments().get(i).setPayMoney(listBean.getPayments().get(i).getPayMoney() + listBean.getPayments().get(j).getPayMoney());
                                            //  listBean.getPayments().get(i).setPayMoney(listBean.getPayments().get(i).getPayMoney() + listBean.getPayments().get(j).getPayMoney());
                                        }
                                    }
                                }
                            }
                        }


                        for (int i = 0; i < listBean.getPayments().size(); i++) {
                            if (listBean.getPayments().get(i).getPayMoney() > 0) {
                                PayNoBean payNoBean = listBean.getPayments().get(i);
                                trueList.add(payNoBean);
                            }
                        }
                    }
                } else {
                    showMessage("没有数据");
                }
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    @OnClick(R.id.order_pay_add)
    public void add() {
        Intent intent = new Intent(this, OrderMealActivity.class);
        intent.putExtra("meal", false);
        intent.putExtra("deskNo", listBean.getDeskNO());
        intent.putExtra("deskName", listBean.getDeskName());
        intent.putExtra("orderID", listBean.getID());
        intent.putExtra("remark", listBean.getRemark());
        intent.putExtra("people", listBean.getTotalNum());
        startActivity(intent);
    }

    @OnClick(R.id.order_pay_reduct)
    public void reduct() {
        lists = new ArrayList<>();
        ArrayList<String> listString = adapter.getProdAttrItems();
        if (listString.size() > 0) {
            for (int i = 0; i < listString.size(); i++) {
                OrderInfoBean.ProdAttrItem item = new OrderInfoBean.ProdAttrItem();
                item.setODID(list.get(Integer.parseInt(listString.get(i))).getODID());
                item.setCount(-list.get(Integer.parseInt(listString.get(i))).getCount());
                lists.add(item);
            }
            JSONObject object = new JSONObject();
            object.put("ID", listBean.getID());
            object.put("removes", lists);
            data = object.toString();
            doReduct();
        } else {
            if (cataOn.getText().toString().equals("编辑")) {
                adapter.setCheck(false);
                cataOn.setText("确定");
                cataOff.setVisibility(View.VISIBLE);
            } else {
                ArrayList<OrderInfoBean.ProductSimple> simpleArrayList = adapter.getProItems();
                attachedBeans = new ArrayList<>();
                if (simpleArrayList.size() > 0) {
                    for (int i = 0; i < simpleArrayList.size(); i++) {
                        OrderInfoBean.ProdAttrItem bean = new OrderInfoBean.ProdAttrItem();
                        bean.setODID(simpleArrayList.get(i).getODID());
                        bean.setCount(simpleArrayList.get(i).getCount() - list.get(simpleArrayList.get(i).getIndex()).getCount());
                        attachedBeans.add(bean);
                    }
                    JSONObject object = new JSONObject();
                    object.put("removes", attachedBeans);
                    object.put("ID", listBean.getID());
                    data = object.toString();
                    doReduct();
                } else {
                    showMessage("没有改变菜品");
                }
            }
        }
    }

    @OnClick(R.id.order_pay_delete)
    public void delete() {
        if (listBean.getPaidMoney() > 0) {
            showMessage("请先退款或者反结账");
        } else if (listBean.getPayStatus() == 6) {
            showMessage("支付中，暂时不能取消订单");
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OrderPayActivity.this);
            builder.setMessage("取消订单吗？？？");


            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    JSONObject object = new JSONObject();
                    object.put("orderNO", orderNo);
                    object.put("deskNO", listBean.getDeskNO());
                    data = object.toString();
                    dodelete();
                }
            });
            builder.setNegativeButton("不取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();

        }
    }

    @OnClick(R.id.order_pay_desk_h)
    public void deskH() {
        Intent intent = new Intent(this, OrderDeskChangeActivity.class);
        intent.putExtra("orderID", listBean.getID());
        intent.putExtra("reserve", getIntent().getIntExtra("reserve", 0));
        startActivityForResult(intent, 0);
    }

//    @OnClick(R.id.order_pay_cata_desk_h)
//    public void cataDeskH() {
//        Intent intent = new Intent(this, CataDeskChangeActivity.class);
//        intent.putExtra("bean", listBean);
//        startActivityForResult(intent, 0);
//    }

    @OnClick(R.id.order_pay_type1)
    public void setPayType1() {
        payType1.setBackground(getResources().getDrawable(R.drawable.corner_white_stroke2));
        payType2.setBackgroundColor(getResources().getColor(R.color.textColor_white));
        type = 0;

    }

    @OnClick(R.id.order_pay_type2)
    public void setPayType2() {
        payType2.setBackground(getResources().getDrawable(R.drawable.corner_white_stroke2));
        payType1.setBackgroundColor(getResources().getColor(R.color.textColor_white));
        type = 1;
    }

    @OnClick(R.id.order_pay_cata_on)
    public void on() {
        if (cataOn.getText().toString().equals("编辑")) {
            adapter.setCheck(false);
            cataOn.setText("确定");
            cataOff.setVisibility(View.VISIBLE);
        } else {
            ArrayList<OrderInfoBean.ProductSimple> simpleArrayList = adapter.getProItems();
            attachedBeans = new ArrayList<>();
            if (simpleArrayList.size() > 0) {
                for (int i = 0; i < simpleArrayList.size(); i++) {
                    OrderInfoBean.ProdAttrItem bean = new OrderInfoBean.ProdAttrItem();
                    bean.setODID(simpleArrayList.get(i).getODID());
                    bean.setCount(simpleArrayList.get(i).getCount() - list.get(simpleArrayList.get(i).getIndex()).getCount());
                    attachedBeans.add(bean);
                }
                JSONObject object = new JSONObject();
                object.put("remark", listBean.getRemark());
                object.put("totalNum", listBean.getTotalNum());
                object.put("updates", attachedBeans);
                object.put("ID", listBean.getID());
                data = object.toString();
                doReduct();
            } else {
                showMessage("请选中要退的菜品");
            }
        }
    }

    @OnClick(R.id.order_pay_cata_off)
    public void off() {
        adapter.setCheck(true);
        cataOn.setText("编辑");
        cataOff.setVisibility(View.GONE);
    }

    @OnClick(R.id.order_pay_h)
    public void payh() {
        lists = new ArrayList<>();
        ArrayList<String> listString = adapter.getProdAttrItems();
        ArrayList<OrderInfoBean.ProductSimple> simpleArrayList1 = adapter.getProItems();
        if (listString.size() > 0) {
            for (int i = 0; i < listString.size(); i++) {
                OrderInfoBean.ProdAttrItem item = new OrderInfoBean.ProdAttrItem();

                item.setODID(list.get(Integer.parseInt(listString.get(i))).getODID());
                if (simpleArrayList1.size() > 0) {
                    for (int j = 0; j < simpleArrayList1.size(); j++) {
                        if (simpleArrayList1.get(j).getIndex() == Integer.parseInt(listString.get(i))) {
                            item.setCount(simpleArrayList1.get(j).getCount() - list.get(Integer.parseInt(listString.get(i))).getCount());
                        }
                    }
                } else {
                    item.setCount(-list.get(Integer.parseInt(listString.get(i))).getCount());
                }
                lists.add(item);
            }
            Intent intent = new Intent(this, CataHActivity.class);
            intent.putExtra("list", lists);
            intent.putExtra("id", listBean.getID());
            startActivity(intent);
        } else {
            if (cataOn.getText().toString().equals("编辑")) {
                adapter.setCheck(false);
                cataOn.setText("确定");
                cataOff.setVisibility(View.VISIBLE);
            } else {
                ArrayList<OrderInfoBean.ProductSimple> simpleArrayList = adapter.getProItems();
                attachedBeans = new ArrayList<>();
                if (simpleArrayList.size() > 0) {
                    for (int i = 0; i < simpleArrayList.size(); i++) {
                        OrderInfoBean.ProdAttrItem bean = new OrderInfoBean.ProdAttrItem();
                        bean.setODID(simpleArrayList.get(i).getODID());
                        bean.setCount(simpleArrayList.get(i).getCount() - list.get(simpleArrayList.get(i).getIndex()).getCount());
                        attachedBeans.add(bean);
                    }
                    JSONObject object = new JSONObject();
                    object.put("remark", listBean.getRemark());
                    object.put("totalNum", listBean.getTotalNum());
                    object.put("updates", attachedBeans);
                    object.put("ID", listBean.getID());
                    data = object.toString();
                    doReduct();
                } else {
                    showMessage("请选中要换的菜品");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra("isOrderList", false)) {
            startActivity(this, OrderListActivity.class);
            finish();
        } else {
            startActivity(this, DeskManageActivity.class);
        }
    }

    @OnClick(R.id.order_pay_pay)
    public void pay() {
        if (CacheData.getUser(this, CacheData.getId(this) + "").getOnlinePay() == 1 && CacheData.getUser(this, CacheData.getId(this) + "").getUserIsPay() == 1) {
            if (listBean.getPayStatus() == 1) {
            } else {
                Intent intent = new Intent(OrderPayActivity.this, OrderPayActivity2.class);
                intent.putExtra("orderID", listBean.getOrderNO());
                intent.putExtra("money", listBean.getPrimeMoney());

               if (listBean.getCanDiscountMoney()==0){
                   intent.putExtra("vipMoney", listBean.getPrimeMoney());
                   intent.putExtra("canDiscount", 0);
                   intent.putExtra("cantDiscount",listBean.getPrimeMoney());
                   intent.putExtra("canVipDiscount", 0);
                   intent.putExtra("cantVipDiscount", listBean.getPrimeMoney());
                   intent.putExtra("discount", listBean.getDiscountMoney());
               }else {
                   intent.putExtra("vipMoney", listBean.getVipMoney());
                   intent.putExtra("canDiscount", listBean.getCanDiscountMoney());
                   intent.putExtra("cantDiscount", listBean.getCantDiscountMoney());
                   intent.putExtra("canVipDiscount", listBean.getCanVipDiscountMoney());
                   intent.putExtra("cantVipDiscount", listBean.getVipMoney() - listBean.getCanVipDiscountMoney());
                   intent.putExtra("discount", listBean.getDiscountMoney());
               }
                intent.putExtra("paidMoney", listBean.getPaidMoney());
                intent.putExtra("payNo", trueList);
                intent.putExtra("payList", listBean.getPayTypes());
                startActivity(intent);
            }
        } else {
            showMessage("此账号不可结账");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        doSpecial();
        setPayType1();
    }

    private void doReduct() {
        httpUtil.HttpServer(this, data, 92, true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                adapter.setProdAttrItems(new ArrayList<String>());
                JSONObject object = new JSONObject();
                object.put("orderNO", orderNo);
                data = object.toString();
                doSpecial();
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    private void dodelete() {
        httpUtil.HttpServer(this, data, 72, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                startActivity(OrderPayActivity.this, DeskManageActivity.class);
                finish();
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    @OnClick(R.id.order_tui)
    public void tui() {
        if (CacheData.getUser(this, CacheData.getId(this) + "").getOnlinePay() == 1 && CacheData.getUser(this, CacheData.getId(this) + "").getUserIsPay() == 1) {
            if (listBean.getPaidMoney() > 0) {
                if (listBean.getPayments().size() > 0) {
                    Intent intent = new Intent(OrderPayActivity.this, OrderRefundActivity.class);
                    intent.putExtra("cash", listBean);
                    intent.putExtra("payNo", trueList);
                    startActivity(intent);
                } else {
                    showMessage("没有支付记录,请联系客服");
                }
            } else {
                showMessage("没有收钱，不能退哦");
            }
        } else {
            showMessage("此账号不能退款");
        }
    }

    @OnClick(R.id.ping)
    public void ping() {
        if (CacheData.getUser(this, CacheData.getId(this) + "").getOnlinePay() == 1 && CacheData.getUser(this, CacheData.getId(this) + "").getUserIsPay() == 1) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OrderPayActivity.this);
            builder.setMessage("确定平账吗？？？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    httpUtil.HttpServer(OrderPayActivity.this, "balance", "orderNO=" + orderNo, true, new HttpCallBack() {
                        @Override
                        public void back(String data) {
                            doSpecial();
                        }

                        @Override
                        public void fail(String Message, int code, String data) {
                            showMessage(Message);
                        }
                    });
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        } else {
            showMessage("此账号不能平账");
        }

    }


    /**
     * 字符串转byte数组
     */
    public static byte[] strTobytes(String str) {
        byte[] b = null, data = null;
        try {
            b = str.getBytes("utf-8");
            data = new String(b, "utf-8").getBytes("gbk");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("菜品订单详情"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("菜品订单详情"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}
