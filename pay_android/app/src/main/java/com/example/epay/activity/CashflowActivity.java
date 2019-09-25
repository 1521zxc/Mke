package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.example.epay.R;
import com.example.epay.adapter.CashGridAdapter;
import com.example.epay.adapter.CashflowListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.base.BaseRecyclerAdapter;
import com.example.epay.bean.CashTypeBean;
import com.example.epay.bean.CashflowBean;
import com.example.epay.bean.CashflowListBean;
import com.example.epay.bean.CashflowListBean2;
import com.example.epay.bean.OrderPayTypeBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.DateUtil;
import com.example.epay.view.DatePicker;
import com.example.epay.view.HorizontalListView;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class CashflowActivity extends BaseActivity {

    @Bind(R.id.cashflow_listView)
    IRecyclerView listView;
    @Bind(R.id.cashflow_gridView)
    HorizontalListView girdView;
    @Bind(R.id.cashflow_time)
    TextView timeText;

    long timeStart = 0;
    long timeEnd = 0;
    CashflowListAdapter adapter;
    ArrayList<CashflowListBean2> arrayList;
    CashflowListBean cashflowBeans;
    ArrayList<OrderPayTypeBean> payTypes=new ArrayList<>();
    ArrayList<CashTypeBean> CashTypes=new ArrayList<>();

    HashMap<Integer,String> payMap=new HashMap<>();
    HashMap<String,Double> payValue=new HashMap<>();
    CashGridAdapter gridAdapter;
    DatePicker customDatePicker;
    int index=0;
    View view;
    boolean isIndex=true;

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            gridAdapter.setList(CashTypes);
            girdView.setAdapter(gridAdapter);
            if(arrayList.size()>100) {
                index=1;
                adapter.setList(new ArrayList<CashflowListBean2>(arrayList.subList(0, 99)));
            }else{
                isIndex=false;
                adapter.setList(arrayList);
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashflow);
        ButterKnife.bind(this);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setLoadMoreFooterView(R.layout.load_layout);
        view = listView.getLoadMoreFooterView();
        timeStart = DateUtil.getTimesmorning();
        timeEnd = DateUtil.getTimesnight();
        initView();
        initDatePicker();
    }
    @Override
    public void initView() {
        super.initView();
        arrayList=new ArrayList<CashflowListBean2>();
        adapter=new CashflowListAdapter(arrayList,this,R.layout.item_cashflow_list);
        listView.setAdapter(adapter);
        gridAdapter=new CashGridAdapter(this,CashTypes);
        girdView.setAdapter(gridAdapter);
        timeText.setText("时间："+DateUtil.getTime("yyyy-MM-dd")+" 00:00至"+DateUtil.getTime("yyyy-MM-dd")+" 23:59");
        doCashflow();

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                Intent intent=new Intent(CashflowActivity.this,CashDetailActivity.class);
                intent.putExtra("id",arrayList.get(position).getID());
                startActivity(intent);
            }
        });

        listView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                view.setVisibility(View.VISIBLE);
                if(isIndex) {
                    if ((arrayList.size() - 100 * index) / 100 > 0) {
                        index++;
                        adapter.setList(new ArrayList<CashflowListBean2>(arrayList.subList(0, 100 * index - 1)));
                        view.setVisibility(View.GONE);
                    } else {
                        isIndex=false;
                        adapter.setList(new ArrayList<CashflowListBean2>(arrayList.subList(0, 100 * index + (arrayList.size() - 100 * index) % 100)));
                        view.setVisibility(View.GONE);
                    }
                }else{
                    showMessage("没有更多数据");
                }
            }
        });
    }

    @OnClick(R.id.cashflow_time)
    public void deta() {
        customDatePicker.show(DateUtil.format2(timeStart, "yyyy-MM-dd"));
    }



    //流水
    private void doCashflow() {
        httpUtil.HttpServer(this, "{\"fromTime\":"+timeStart+",\"toTime\":"+timeEnd+",\"pageSize\":-1"+"}", 81, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                cashflowBeans=gson.fromJson(data, CashflowListBean.class);
                isIndex=true;
                if(cashflowBeans!=null) {
                    arrayList.clear();
                    CashTypes.clear();
                    payTypes = gson.fromJson(data, CashflowListBean.class).getPayTypes();
                    payValue.clear();
                    payMap.clear();
                    if(payTypes.size()>0)
                    {
                        for (int i=0;i<payTypes.size();i++)
                        {
                            payMap.put(payTypes.get(i).getType(),payTypes.get(i).getName());
                            if(payTypes.get(i).getName().contains("扫码"))
                            {

                            }else{
                                payValue.put(payTypes.get(i).getName(), (double) 0);
                            }

                        }
                    }
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            load();
                        }
                    }.start();




                }else{
                    showMessage( "没有数据");
                }
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage(Message);
            }
        });
    }
    //对数据进行标记时间
    public void load( ) {
        int b = 0;
        double money = 0;
        DecimalFormat df = new DecimalFormat("######0.00");
        CashflowListBean2 cashflowTimeListBean;
        for (int i = 0; i < cashflowBeans.getItems().size(); i++) {
            CashflowBean cashflowBean = cashflowBeans.getItems().get(i);
            String time = DateUtil.getday2(cashflowBean.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
            String a = time.substring(0, 10);
            String c = time.substring(19, 22);
            String title_time = a + "  " + c;
            String item_time = time.substring(11, 20);
            cashflowTimeListBean = new CashflowListBean2();
            cashflowTimeListBean.setDateTime(item_time);
            cashflowTimeListBean.setID(cashflowBean.getID());
            cashflowTimeListBean.setSum(cashflowBean.getSum());
            cashflowTimeListBean.setType(cashflowBean.getPayType());
            cashflowTimeListBean.setNickName(cashflowBean.getNickName());
            cashflowTimeListBean.setIconURL(cashflowBean.getIconURL());
            cashflowTimeListBean.setType(cashflowBean.getPayType());
            if (payMap.get(cashflowBean.getPayType()) == null || payMap.get(cashflowBean.getPayType()).equals("")) {
                cashflowTimeListBean.setTypeName("未知");
                if (payValue.get("未知") == null || payValue.get("未知").equals("")) {
                    payValue.put("未知", cashflowBean.getSum());
                } else {
                    payValue.put("未知", payValue.get("未知") + cashflowBean.getSum());
                }
            } else {
                cashflowTimeListBean.setTypeName(payMap.get(cashflowBean.getPayType()));
                if (payValue.get(payMap.get(cashflowBean.getPayType())) == null || payValue.get(payMap.get(cashflowBean.getPayType())).equals("")) {
                    payValue.put(payMap.get(cashflowBean.getPayType()), cashflowBean.getSum());
                } else {
                    payValue.put(payMap.get(cashflowBean.getPayType()), payValue.get(payMap.get(cashflowBean.getPayType())) + cashflowBean.getSum());
                }
            }
            if (arrayList.isEmpty() || arrayList.size() == 0) {
                b++;
                money = money + cashflowBean.getSum();
                cashflowTimeListBean.setTime(title_time);
                arrayList.add(cashflowTimeListBean);
                continue;
            }
            if (i == cashflowBeans.getItems().size() - 1) {
                b++;
                money = money + cashflowBean.getSum();
                arrayList.get(arrayList.size() - b + 1).setAll(b + "");
                arrayList.get(arrayList.size() - b + 1).setMoney(money);
                cashflowTimeListBean.setTime(title_time);
                arrayList.add(cashflowTimeListBean);
                continue;
            }
            if (arrayList.get(arrayList.size() - 1).getTime().equals(title_time)) {
                b++;
                money = money + cashflowBean.getSum();
                cashflowTimeListBean.setTime(title_time);
                arrayList.add(cashflowTimeListBean);
                continue;
            } else {
                arrayList.get(arrayList.size() - b).setAll(b + "");
                arrayList.get(arrayList.size() - b).setMoney(money);
                cashflowTimeListBean.setTime(title_time);
                b = 1;
                money = cashflowBean.getSum();
                arrayList.add(cashflowTimeListBean);
                continue;
            }
        }

        if(payTypes.size()>0) {
            Iterator it = payValue.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                double value = (double) entry.getValue();
                CashTypeBean bean = new CashTypeBean();
                bean.setName(key);
                bean.setSum(value);
                CashTypes.add(bean);
            }
        }
        handler.sendEmptyMessage(0);
    }









    private void initDatePicker() {
        customDatePicker = new DatePicker(this, new DatePicker.ResultHandler() {
            @Override
            public void handle(long startTime, long endTime) { // 回调接口，获得选中的时间
                timeStart = startTime;
                if(endTime>0){
                    timeEnd = endTime+24*60*60*1000;
                }else {
                    timeEnd = DateUtil.getTimesnight();
                }
                timeText.setText("时间："+DateUtil.format2(timeStart,"yyyy-MM-dd")+" 00:00至"+DateUtil.format2(timeEnd,"yyyy-MM-dd")+" 00:00");
                doCashflow();
            }
        }, "2018-01-01 00:00", DateUtil.format2(DateUtil.getTimesnight(), "yyyy-MM-dd HH:mm")); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动
    }









    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("交易流水"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("交易流水"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
