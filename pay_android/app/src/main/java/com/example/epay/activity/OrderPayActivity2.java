package com.example.epay.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epay.R;
import com.example.epay.adapter.OrderPayTypeMoneyAdapter;
import com.example.epay.adapter.OrderPayTypeSeteleAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.ComPayTypeBean;
import com.example.epay.bean.MemberGiftBean;
import com.example.epay.bean.MembersGiftListBean;
import com.example.epay.bean.OrderPayTypeBean;
import com.example.epay.bean.OrderPayZheBean;
import com.example.epay.bean.PayNoBean;
import com.example.epay.bean.conponeBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.DateUtil;
import com.example.epay.util.MyDialog;
import com.example.epay.view.OrderPaySelectorView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OrderPayActivity2 extends BaseActivity {
    @Bind(R.id.epa_zhe_text)
    TextView zheText;
    @Bind(R.id.epa_zhe_text_money)
    TextView moneyText;
    @Bind(R.id.epa_zhe_text_money1)
    TextView discountText;
    @Bind(R.id.order_pay_type_listView)
    ListView listView;
    @Bind(R.id.order_pay_type_layout)
    LinearLayout layout;
    @Bind(R.id.epa_mo)
    FrameLayout moLayout;
    @Bind(R.id.coupon_order_pay_tv)
    TextView couponText;

    @Bind(R.id.activty_order_pay_frameLayout)
    FrameLayout activityLayout;
    @Bind(R.id.activty_order_pay_tv)
    TextView activityText;
    @Bind(R.id.activty_order_pay_line)
    TextView activityline;

    int conponeIndex = -1;
    String orderNO = "";

    double money2 = 0, paidMoney = 0, disCountMoney = 0, canDiscount = 0, cantDiscount = 0, vipMoney = 0, canVipDiscount = 0, cantVipDiscount = 0, JSMoney = 0, JSCanDiscount = 0, JSCantDiscount = 0, dis = 0;
    OrderPayZheBean orderPayZheBean;
    int type = 0;
    Dialog walletDialog;
    ArrayList<PayNoBean> trueList = new ArrayList<>();

    OrderPayTypeMoneyAdapter adapter;
    ArrayList<OrderPayTypeBean> list = new ArrayList<>();
    ArrayList<OrderPayTypeBean> beanArrayList = new ArrayList<>();

    OrderPayTypeBean indexBean;
    double trueMoney = 0; // 收的部分款项
    boolean isOk = false; // 是否要平账
    boolean isVip = false; // 是否是VIP
    String vipCode = ""; // 判断是否是VIP
    double activityMoney = 0;
    ArrayList<MemberGiftBean> activityList = new ArrayList();
    ArrayList<conponeBean> conponeBeans = new ArrayList<>();

    //ddddd
    double zheMoney = 0, molMoney = 0, memberReductMoney = 0;
    int zheIndex = -1;
    String zheName = "";
    ArrayList<ComPayTypeBean> comPayTypeBeans = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay2);
        ButterKnife.bind(this);
        beanArrayList = (ArrayList<OrderPayTypeBean>) getIntent().getSerializableExtra("payList");
        money2 = getIntent().getDoubleExtra("money", 0);
        orderNO = getIntent().getStringExtra("orderID");
        paidMoney = getIntent().getDoubleExtra("paidMoney", 0);
        disCountMoney = getIntent().getDoubleExtra("discount", 0);
        canDiscount = getIntent().getDoubleExtra("canDiscount", 0);
        cantDiscount = getIntent().getDoubleExtra("cantDiscount", 0);
        vipMoney = getIntent().getDoubleExtra("vipMoney", 0);
        canVipDiscount = getIntent().getDoubleExtra("canVipDiscount", 0);
        cantVipDiscount = getIntent().getDoubleExtra("cantVipDiscount", 0);
        adapter = new OrderPayTypeMoneyAdapter(this, list, orderNO, beanArrayList);
        listView.setAdapter(adapter);


        moneyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    if (editable.toString().length() == 1 && editable.toString().equals("-")) {

                    } else {
                        if (Double.valueOf(moneyText.getText().toString()) >= 0) {
                            layout.setVisibility(View.VISIBLE);
                            moLayout.setVisibility(View.VISIBLE);
                            //tui.setVisibility(View.GONE);
                        } else {
                            layout.setVisibility(View.GONE);
                            moLayout.setVisibility(View.GONE);
                            // tui.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        trueList = (ArrayList<PayNoBean>) getIntent().getSerializableExtra("payNo");

        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            //1、获取main在窗体的可视区域
            decorView.getWindowVisibleDisplayFrame(rect);
            //2、获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
            int mainInvisibleHeight = decorView.getRootView().getHeight() - rect.bottom;
            int screenHeight = decorView.getRootView().getHeight();//屏幕高度
            //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
            if (mainInvisibleHeight > screenHeight / 4) {   //软键盘显示

            } else {                      //软键盘隐藏
                if (walletDialog != null) {        //在软键盘隐藏时，关闭Dialog。
                    walletDialog.dismiss();
                }
            }
        });
        adapter.setOnNtClickListener(bean -> {
            indexBean = bean;
            isOk = false;
            if (bean.getType() == 21) {
                Intent intent = new Intent(OrderPayActivity2.this, ScanQrCodeActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("money", bean.getMoney() + "");
                intent.putExtra("dis", "0.");
                intent.putExtra("orderID", orderNO);
                startActivityForResult(intent, 11);
            } else if (bean.getType() == 22) {
                Intent intent = new Intent(OrderPayActivity2.this, ScanQrCodeActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("money", bean.getMoney() + "");
                intent.putExtra("dis", "0.");
                intent.putExtra("orderID", orderNO);
                startActivityForResult(intent, 11);
            } else if (bean.getType() == 1) {
                Intent intent = new Intent(OrderPayActivity2.this, CollectCodeActivity.class);
                intent.putExtra("sum", bean.getMoney() + "");
                intent.putExtra("orderID", orderNO);
                intent.putExtra("sumType", 1);
                intent.putExtra("dis", "0.");
                startActivityForResult(intent, 11);
            } else if (bean.getType() == 2) {
                Intent intent = new Intent(OrderPayActivity2.this, CollectCodeActivity.class);
                intent.putExtra("sum", bean.getMoney() + "");
                intent.putExtra("orderID", orderNO);
                intent.putExtra("sumType", 0);
                intent.putExtra("dis", "0.");
                startActivityForResult(intent, 11);
            } else if (bean.getType() == 10) {
                if (isVip) {
                    discountText.setText(DateUtil.doubleValue1(money2 - trueMoney - paidMoney - bean.getMoney()));
                    doVip("");
                } else {
                    Intent intent = new Intent(OrderPayActivity2.this, MembershipCertificationActivity.class);
                    intent.putExtra("money", vipMoney - paidMoney - disCountMoney - trueMoney - activityMoney);
                    intent.putExtra("orderID", orderNO);
                    startActivityForResult(intent, 10111);
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderPayActivity2.this);
                builder.setMessage(bean.getName() + "收款");
                builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        type = bean.getType();
                        discountText.setText(DateUtil.doubleValue1(money2 - trueMoney - paidMoney - bean.getMoney() + activityMoney));
                        domoney("0.");
                    }
                });
                builder.setNegativeButton("点错了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();

            }
        });
        doZhe();
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        if (isVip) {
            JSMoney = vipMoney;
            JSCanDiscount = canVipDiscount;
            JSCantDiscount = cantVipDiscount;
            memberReductMoney = DateUtil.doubleValue((money2 - vipMoney));

        } else {
            JSMoney = money2;
            JSCanDiscount = canDiscount;
            JSCantDiscount = cantDiscount;
            memberReductMoney = 0;
        }
        if (conponeBeans.size() > 0 && conponeIndex > -1) {
            moneyText.setText(DateUtil.doubleValue1(
                    JSMoney
                            - paidMoney
                            - disCountMoney - trueMoney - conponeBeans.get(conponeIndex).getPrice() - activityMoney));
        } else {
            moneyText.setText(DateUtil.doubleValue1(
                    JSMoney
                            - paidMoney
                            - disCountMoney - trueMoney - activityMoney));
        }
        molMoney = DateUtil.doubleValue(JSMoney - paidMoney - trueMoney - Double.parseDouble(moneyText.getText().toString()) - activityMoney);

        if (DateUtil.doubleValue(JSMoney - paidMoney - disCountMoney - trueMoney - activityMoney) < 0) {
            layout.setVisibility(View.GONE);
            moLayout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
            moLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.pay_type_ok)
    public void ok() {
        if (list.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderPayActivity2.this);
            builder.setMessage("确定收款已完成，剩余未支付金额为优惠");
            builder.setPositiveButton("确定", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                isOk = false;
                type = 3;
                discountText.setText(DateUtil.doubleValue1(money2 - trueMoney - paidMoney - Double.valueOf(moneyText.getText().toString())));
                if (DateUtil.doubleValue(Double.valueOf(moneyText.getText().toString())) > 0) {
                    domoney(DateUtil.doubleValue1(Double.valueOf(discountText.getText().toString()) + Double.valueOf(moneyText.getText().toString())));
                } else {
                    domoney(discountText.getText().toString());
                }
            });
            builder.setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.show();
        } else {
            isOk = true;
            discountText.setText(DateUtil.doubleValue1(money2 - trueMoney - paidMoney - Double.valueOf(moneyText.getText().toString())));
            okClick();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1212) {
            list.add(indexBean);
            adapter.setList(list);
            trueMoney = trueMoney + indexBean.getMoney();
            moneyText.setText(DateUtil.doubleValue1(Double.valueOf(moneyText.getText().toString()) - indexBean.getMoney()));
        } else if (resultCode == 10111) {
            isVip = data.getBooleanExtra("isVip", false);
            vipCode = data.getStringExtra("vipCode");
            memberReductMoney = money2 - vipMoney;
            conponeBeans = (ArrayList<conponeBean>) data.getSerializableExtra("conpone");
            if (conponeBeans.size() > 0) {
                conponeIndex = 0;
                couponText.setText("优惠券：" + conponeBeans.get(conponeIndex).getPrice() + "");
                listView.setVisibility(View.INVISIBLE);
            } else {
                conponeIndex = -1;
                listView.setVisibility(View.VISIBLE);
            }
            activityMoney = data.getDoubleExtra("activity", 0);
            if (activityMoney > 0) {
                activityList = (ArrayList<MemberGiftBean>) data.getSerializableExtra("activityList");
                activityLayout.setVisibility(View.VISIBLE);
                activityline.setVisibility(View.VISIBLE);
                activityText.setText("活动优惠：" + String.valueOf(activityMoney));
            } else {
                activityLayout.setVisibility(View.GONE);
                activityline.setVisibility(View.GONE);
            }
            zheText.setText("折扣：");
            zheIndex = -1;
            zheName = "";
            zheMoney = 0;
            initView();
        } else if (resultCode == 10012) {
            conponeIndex = data.getIntExtra("index", -1);
            if (conponeIndex == -1) {
                couponText.setText("优惠券：不使用");
                listView.setVisibility(View.VISIBLE);
            } else {
                couponText.setText("优惠券：" + conponeBeans.get(conponeIndex).getPrice() + "");
                listView.setVisibility(View.INVISIBLE);
            }
            zheText.setText("折扣：");
            zheIndex = -1;
            zheName = "";
            zheMoney = 0;
            initView();
        }
    }

    @OnClick(R.id.epa_mo)
    public void mo() {
        View view2 = getLayoutInflater().inflate(R.layout.dialog_order_pay_money, null);
        final MyDialog mMyDialog2 = new MyDialog(this, view2, R.style.DialogTheme);
        final EditText moneyEdit = (EditText) view2.findViewById(R.id.money);
        TextView sure = (TextView) view2.findViewById(R.id.ok);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyDialog2.dismiss();
                if (!moneyEdit.getText().toString().equals("")) {
                    if (DateUtil.isNumeric00(moneyEdit.getText().toString())) {
                        if (JSMoney - paidMoney - trueMoney - activityMoney >= Double.valueOf(moneyEdit.getText().toString())) {
                            moneyText.setText(DateUtil.doubleValue1(Double.valueOf(moneyEdit.getText().toString())));
                            molMoney = DateUtil.doubleValue(JSMoney - paidMoney - trueMoney - Double.parseDouble(moneyText.getText().toString()) - activityMoney);
                            zheText.setText("折扣：");
                            zheIndex = -1;
                            zheName = "";
                            zheMoney = 0;
                        } else {
                            showMessage("超过了应收金额");
                        }
                    }
                }
            }
        });
        mMyDialog2.setCancelable(true);
        mMyDialog2.show();
    }


    @OnClick(R.id.epa_zhe)
    public void zhe() {
        if(activityMoney<0.01) {
            if (orderPayZheBean.getItems() != null && orderPayZheBean.getItems().size() > 0) {
                OrderPayZheBean.OrderPayZhe selfZhe = new OrderPayZheBean.OrderPayZhe();
                selfZhe.setDiscount(10);
                selfZhe.setName("不打折");
                orderPayZheBean.getItems().add(0, selfZhe);
                OrderPaySelectorView selectorView = new OrderPaySelectorView(OrderPayActivity2.this,
                        orderPayZheBean.getItems());
                selectorView.setCanceledOnTouchOutside(true);
                Window window = selectorView.getWindow();
                window.setGravity(Gravity.BOTTOM);

                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
                WindowManager.LayoutParams p = selectorView.getWindow().getAttributes(); //获取对话框当前的参数值
                p.width = d.getWidth(); //宽度设置为屏幕
                p.height = d.getWidth() * 2 / 3; //宽度设置为屏幕
                selectorView.getWindow().setAttributes(p); //设置生效

                selectorView.setStringListener(new OrderPaySelectorView.StringListener() {
                    @Override
                    public void StringClick(int index) {
                        if (index < 0 || index > orderPayZheBean.getItems().size() - 1) {
                            showMessage("未选择");
                            return;
                        }
                        if (orderPayZheBean.getItems().get(index).getName().length() > 10) {
                            zheText.setText("折扣：" + orderPayZheBean.getItems().get(index).getName().substring(0, 9) + "...");
                        } else {
                            zheText.setText("折扣：" + orderPayZheBean.getItems().get(index).getName());
                        }
                        zheIndex = index;
//                    if(orderPayZheBean.getItems().get(index).getID()!=0) {
                        dis = orderPayZheBean.getItems().get(index).getDiscount();
                        zheName = orderPayZheBean.getItems().get(index).getName();
//                    zheMoney=((10-dis) * JSCanDiscount) / 10;
                        molMoney = 0;
                        double money1 = dis * JSCanDiscount / 10;
                        double zhe = dis * JSCanDiscount / 10;
                        if (money1 < 0) {
                            showMessage("不能再打折");
                        }

                        moneyText.setText(DateUtil.doubleValue1(zhe + JSCantDiscount - paidMoney - trueMoney - activityMoney));
                        if (zhe + JSCantDiscount - paidMoney - trueMoney - activityMoney < 0) {
                            layout.setVisibility(View.GONE);
                            moLayout.setVisibility(View.GONE);
                        } else {
                            layout.setVisibility(View.VISIBLE);
                            moLayout.setVisibility(View.VISIBLE);
                        }
//                    }else{
//
//                    }
                    }
                });
                selectorView.show();
            }
        }else{
            showMessage("已有其他优惠活动");
        }
    }

    //打折
    private void doZhe() {
        httpUtil.HttpServer(this, "", 53, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                orderPayZheBean = gson.fromJson(data, OrderPayZheBean.class);
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    //现金
    private void domoney(final String disCount) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("orderNO=").append(orderNO);
        buffer.append("&muuid=").append(CacheData.getUser(OrderPayActivity2.this,
                CacheData.getId(OrderPayActivity2.this) + "").getMuuid());
        buffer.append("&payType=").append(type);
        comPayTypeBeans.clear();
        if (isOk) {
            disSet();

            if (isVip) {
                buffer.append("&phone=").append(vipCode);
            }
            buffer.append("&money=").append(moneyText.getText().toString());
            if (conponeIndex > -1) {
                buffer.append("&discount=").append(DateUtil.doubleValue1(Double.parseDouble(disCount) - conponeBeans.get(conponeIndex).getPrice() + activityMoney));
            } else {
                buffer.append("&discount=").append(disCount);
            }
            if (comPayTypeBeans.size() > 0) {
                buffer.append("&discountInfo=").append(gson.toJson(comPayTypeBeans));
            }
        } else {
            if (disCount.equals("0.")) {
                buffer.append("&discount=0&money=").append(indexBean.getMoney());
            } else {
                disSet();
                if (isVip) {
                    buffer.append("&phone=").append(vipCode);
                }
                buffer.append("&money=0&discount=").append(disCount);
                if (comPayTypeBeans.size() > 0) {
                    buffer.append("&discountInfo=").append(gson.toJson(comPayTypeBeans));
                }
            }
        }
        httpUtil.HttpServer(this, "cash/pay", buffer.toString(), true, new HttpCallBack() {
            @Override
            public void back(String data) {
                if (disCount.equals("0.")) {
                    trueMoney = trueMoney + indexBean.getMoney();
                    moneyText.setText(String.valueOf(DateUtil.doubleValue(Double.valueOf(moneyText.getText().toString()) - indexBean.getMoney())));
                    list.add(indexBean);
                    adapter.setList(list);
                } else {
                    getgift();
                }
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    //会员卡支付
    private void doVip(final String disCount) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("orderNO=").append(orderNO);
        buffer.append("&muuid=").append(CacheData.getUser(OrderPayActivity2.this,
                CacheData.getId(OrderPayActivity2.this) + "").getMuuid());
        buffer.append("&phone=").append(vipCode);
        comPayTypeBeans.clear();
        if (isOk) {
            disSet();
            buffer.append("&money=").append(moneyText.getText().toString());
            if (conponeIndex > -1) {
                buffer.append("&discount=").append(DateUtil.doubleValue1(Double.parseDouble(disCount) - conponeBeans.get(conponeIndex).getPrice() + activityMoney));
            } else {
                buffer.append("&discount=").append(disCount);
            }
            if (comPayTypeBeans.size() > 0) {
                buffer.append("&discountInfo=").append(gson.toJson(comPayTypeBeans));
            }
        } else {
            buffer.append("&discount=0&money=").append(indexBean.getMoney());
        }
        httpUtil.HttpServer(this, "vip/pay", buffer.toString(), true, new HttpCallBack() {
            @Override
            public void back(String data) {
                if (!isOk) {
                    trueMoney = trueMoney + indexBean.getMoney();
                    moneyText.setText(String.valueOf(DateUtil.doubleValue(Double.valueOf(moneyText.getText().toString()) - indexBean.getMoney())));
                    list.add(indexBean);
                    adapter.setList(list);
                } else {
                    getgift();
                }
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    private void okClick() {
        final OrderPayTypeBean[] typeBean1 = {new OrderPayTypeBean()};
        View view1 = getLayoutInflater().inflate(R.layout.dialog_order_pay_type_list, null);
        final MyDialog mMyDialog = new MyDialog(this, view1, R.style.DialogTheme);
        ListView listView = (ListView) view1.findViewById(R.id.listView);
        OrderPayTypeSeteleAdapter adapter = new OrderPayTypeSeteleAdapter(this, beanArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            mMyDialog.dismiss();
            comPayTypeBeans.clear();
            typeBean1[0] = beanArrayList.get(i);
            if (typeBean1[0].getType() == 21) {
                Intent intent = new Intent(OrderPayActivity2.this, ScanQrCodeActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("payType", 1);
                intent.putExtra("money", moneyText.getText().toString());
                disSet();

                if (isVip) {
                    intent.putExtra("vipCode", vipCode);
                }
                if (comPayTypeBeans.size() > 0) {
                    intent.putExtra("discountInfo", gson.toJson(comPayTypeBeans));
                }
                if (conponeIndex > -1) {
                    intent.putExtra("dis", DateUtil.doubleValue1(Double.parseDouble(discountText.getText().toString()) - conponeBeans.get(conponeIndex).getPrice() + activityMoney));
                } else {
                    intent.putExtra("dis", discountText.getText().toString());
                }
                intent.putExtra("orderID", orderNO);
                startActivity(intent);
            } else if (typeBean1[0].getType() == 22) {
                Intent intent = new Intent(OrderPayActivity2.this, ScanQrCodeActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("payType", 1);
                intent.putExtra("money", moneyText.getText().toString());
                disSet();
                if (isVip) {
                    intent.putExtra("vipCode", vipCode);
                }
                if (comPayTypeBeans.size() > 0) {
                    intent.putExtra("discountInfo", gson.toJson(comPayTypeBeans));
                }
                if (conponeIndex > -1) {
                    intent.putExtra("dis", DateUtil.doubleValue1(Double.parseDouble(discountText.getText().toString()) - conponeBeans.get(conponeIndex).getPrice() + activityMoney));
                } else {
                    intent.putExtra("dis", discountText.getText().toString());
                }
                intent.putExtra("orderID", orderNO);
                startActivity(intent);
            } else if (typeBean1[0].getType() == 1) {
                Intent intent = new Intent(OrderPayActivity2.this, CollectCodeActivity.class);
                intent.putExtra("sum", moneyText.getText().toString());
                intent.putExtra("payType", 1);
                intent.putExtra("sumType", 1);
                disSet();
                if (isVip) {
                    intent.putExtra("vipCode", vipCode);
                }
                if (comPayTypeBeans.size() > 0) {
                    intent.putExtra("discountInfo", gson.toJson(comPayTypeBeans));
                }
                if (conponeIndex > -1) {
                    intent.putExtra("dis", DateUtil.doubleValue1(Double.parseDouble(discountText.getText().toString()) - conponeBeans.get(conponeIndex).getPrice() + activityMoney));
                } else {
                    intent.putExtra("dis", discountText.getText().toString());
                }
                intent.putExtra("orderID", orderNO);
                startActivity(intent);
            } else if (typeBean1[0].getType() == 2) {
                Intent intent = new Intent(OrderPayActivity2.this, CollectCodeActivity.class);
                intent.putExtra("sum", moneyText.getText().toString());
                intent.putExtra("payType", 1);
                intent.putExtra("sumType", 0);
                disSet();
                if (isVip) {
                    intent.putExtra("vipCode", vipCode);
                }
                if (comPayTypeBeans.size() > 0) {
                    intent.putExtra("discountInfo", gson.toJson(comPayTypeBeans));
                }
                if (conponeIndex > -1) {
                    intent.putExtra("dis", DateUtil.doubleValue1(Double.parseDouble(discountText.getText().toString()) - conponeBeans.get(conponeIndex).getPrice() + activityMoney));
                } else {
                    intent.putExtra("dis", discountText.getText().toString());
                }
                intent.putExtra("orderID", orderNO);
                startActivity(intent);
            } else if (typeBean1[0].getType() == 10) {
                if (isVip) {
                    doVip(discountText.getText().toString());
                } else {
                    Intent intent = new Intent(OrderPayActivity2.this, MembershipCertificationActivity.class);
                    intent.putExtra("money", vipMoney - paidMoney - disCountMoney - trueMoney - activityMoney);
                    intent.putExtra("orderID", orderNO);
                    startActivityForResult(intent, 10111);
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderPayActivity2.this);
                builder.setMessage(typeBean1[0].getName() + "收款");
                builder.setPositiveButton("继续", (dialogInterface, i12) -> {
                    dialogInterface.dismiss();
                    type = typeBean1[0].getType();
                    domoney(discountText.getText().toString());
                });
                builder.setNegativeButton("点错了", (dialogInterface, i1) -> dialogInterface.dismiss());
                builder.show();
            }
        });
        mMyDialog.setCancelable(true);
        mMyDialog.show();
    }

    @OnClick({R.id.coupon_order_pay_tv, R.id.membership_certification_order_pay_tv})
    public void onViewClicked(View view) {
        final Intent[] intent = new Intent[1];
        switch (view.getId()) {
            case R.id.coupon_order_pay_tv:
                if (isVip) {
                    intent[0] = new Intent(OrderPayActivity2.this, ConponeActivity.class);
                    intent[0].putExtra("conpone", conponeBeans);
                    startActivityForResult(intent[0], 10111);
                } else {
                    showMessage("请认证会员");
                }
                break;
            case R.id.membership_certification_order_pay_tv:
                if (isVip) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderPayActivity2.this);
                    builder.setMessage("已认证会员，是否更换支付会员卡");
                    builder.setPositiveButton("确定", (dialogInterface, i) -> {
                        isVip = false;
                        intent[0] = new Intent(OrderPayActivity2.this, MembershipCertificationActivity.class);
                        intent[0].putExtra("money", vipMoney - paidMoney - disCountMoney - trueMoney - activityMoney);
                        intent[0].putExtra("orderID", orderNO);
                        startActivityForResult(intent[0], 10111);
                    });
                    builder.setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss());
                    builder.show();
                } else {
                    intent[0] = new Intent(OrderPayActivity2.this, MembershipCertificationActivity.class);
                    intent[0].putExtra("money", vipMoney - paidMoney - disCountMoney - trueMoney - activityMoney);
                    intent[0].putExtra("orderID", orderNO);
                    startActivityForResult(intent[0], 10111);
                }
                break;
        }
    }


    public void disSet() {
        if (molMoney > 0) {
            ComPayTypeBean comPayTypeBean = new ComPayTypeBean();
            comPayTypeBean.setType(0);
            comPayTypeBean.setName("抹零");
            comPayTypeBean.setMoney(molMoney);
            comPayTypeBeans.add(comPayTypeBean);
        }
        if (memberReductMoney > 0) {
            ComPayTypeBean comPayTypeBean1 = new ComPayTypeBean();
            comPayTypeBean1.setType(1);
            comPayTypeBean1.setName("会员优惠");
            comPayTypeBean1.setMoney(memberReductMoney);
            comPayTypeBeans.add(comPayTypeBean1);
        }
        if (zheIndex > -1) {
            ComPayTypeBean comPayTypeBean2 = new ComPayTypeBean();
            comPayTypeBean2.setType(2);
            comPayTypeBean2.setName(zheName);
            zheMoney = ((10 - orderPayZheBean.getItems().get(zheIndex).getDiscount()) * JSMoney) / 10;
            comPayTypeBean2.setMoney(zheMoney);
            comPayTypeBeans.add(comPayTypeBean2);
        }
        if (conponeIndex > -1) {
            ComPayTypeBean comPayTypeBean3 = new ComPayTypeBean();
            comPayTypeBean3.setType(3);
            comPayTypeBean3.setName(conponeBeans.get(conponeIndex).getName());
            comPayTypeBean3.setMoney(conponeBeans.get(conponeIndex).getPrice());
            comPayTypeBeans.add(comPayTypeBean3);
        }
        if (activityMoney > 0) {
            for (int i = 0; i < activityList.size(); i++) {
                ComPayTypeBean comPayTypeBean4 = new ComPayTypeBean();
                comPayTypeBean4.setType(4);
                comPayTypeBean4.setName(activityList.get(i).getName());
                comPayTypeBean4.setMoney(activityList.get(i).getVipMoney());
                comPayTypeBeans.add(comPayTypeBean4);
            }
        }
    }


    private void getgift() {
        httpUtil.HttpServer(this, "{\"orderNO\":\"" + orderNO + "\"}", 62, false, new HttpCallBack() {
            @Override
            public void back(String data) {
                MembersGiftListBean membersGiftListBean = gson.fromJson(data, MembersGiftListBean.class);
                if (membersGiftListBean.getCount() > 0) {
                    CacheData.cacheMemberGiftList(OrderPayActivity2.this, data);
                    if (!isVip) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OrderPayActivity2.this);
                        builder.setMessage("顾客是否是会员");
                        builder.setPositiveButton("不是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(OrderPayActivity2.this, AddMembershipActivity.class);
                                intent.putExtra("orderID", orderNO);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(OrderPayActivity2.this, MembershipCertificationActivity.class);
                                intent.putExtra("orderID", orderNO);
                                intent.putExtra("gift", false);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.show();
                    } else {
                        Intent intent = new Intent(OrderPayActivity2.this, ShopActivtyActivity.class);
                        intent.putExtra("orderNO", orderNO);
                        intent.putExtra("vipCode", vipCode);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    startActivity(OrderPayActivity2.this, DeskManageActivity.class);
                    finish();
                }
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });

    }
}
