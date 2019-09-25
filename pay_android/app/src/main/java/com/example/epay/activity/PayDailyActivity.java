package com.example.epay.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.adapter.PayDailyListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.BusinessBean;
import com.example.epay.bean.OrderPayTypeBean;
import com.example.epay.bean.PayTypeBean;
import com.example.epay.doHttp.CuncResponse;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.doHttp.ReturnResquest;
import com.example.epay.doHttp.Server;
import com.example.epay.util.DateUtil;
import com.example.epay.util.TypeUtil;
import com.example.epay.view.EPayListView;
import com.umeng.analytics.MobclickAgent;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class PayDailyActivity extends BaseActivity {
    @Bind(R.id.pay_daily_barChart3)
    ColumnChartView barChart3;
    @Bind(R.id.pay_daily_listView)
    EPayListView listView;
    @Bind(R.id.pay_day_text)
    TextView dayText;
    @Bind(R.id.pay_day_h)
    TextView dayH;
    @Bind(R.id.pay_week_text)
    TextView weekText;
    @Bind(R.id.pay_week_h)
    TextView weekH;
    @Bind(R.id.pay_year_text)
    TextView yearText;
    @Bind(R.id.pay_year_h)
    TextView yearH;
    @Bind(R.id.pay_ji_text)
    TextView jiText;
    @Bind(R.id.pay_ji_h)
    TextView jiH;
    @Bind(R.id.pay_month_text)
    TextView monthText;
    @Bind(R.id.pay_month_h)
    TextView monthH;
    @Bind(R.id.pay_time_text)
    TextView timeText;
    @Bind(R.id.business_earnings)
    TextView earnText;
    @Bind(R.id.business_discount)
    TextView discountText;
    @Bind(R.id.business_duesum)
    TextView duesumText;

    @Bind(R.id.pay_last_text)
    TextView lastText;
    @Bind(R.id.pay_h_text)
    TextView hText;


    long time = 0;
    int type = 1;
    int dayNum = 1;
    int monthNum = 1;
    int yearNum = 1;


    BusinessBean businessBean;
    JSONObject object = new JSONObject();
    String[] quarters3;



    int quarIndex = 0;
    HashMap<Integer,String> payMap=new HashMap<>();
    PayDailyListAdapter adapter;
    ArrayList<PayTypeBean>  typeBeans=new ArrayList<>();


    ColumnChartData columnData3;
    List<Column> lsColumn3 = new ArrayList<Column>();
    List<SubcolumnValue> lsValue3= new ArrayList<SubcolumnValue>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_daily);
        ButterKnife.bind(this);

        dayText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        dayH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        weekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        weekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        yearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        yearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        jiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        jiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        monthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        monthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        time = DateUtil.getTimesmorning();
        timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        adapter=new PayDailyListAdapter(this,typeBeans);
        listView.setAdapter(adapter);
        doDetail();
    }


    private void doDetail() {
        object.clear();
        object.put("datetime", time);
        object.put("type", type);
        object.put("storeID",getIntent().getIntExtra("storesId",0));
        httpUtil.HttpServer(this, object.toString(), 74, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                businessBean = gson.fromJson(data, BusinessBean.class);
               ArrayList<OrderPayTypeBean>  payTypes = businessBean.getPayTypes();
                if(payTypes.size()>0)
                {
                    for (int i=0;i<payTypes.size();i++)
                    {
                        payMap.put(payTypes.get(i).getType(),payTypes.get(i).getName());
                    }
                }
                adapter.setMap(payMap);
                initdata();
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage(Message);
            }
        });
    }


    private void initdata() {
        quarIndex = 0;
        barChart3=(ColumnChartView)findViewById(R.id.pay_daily_barChart3);
        String yingMoney = "";
        if(type==1){
            yingMoney= "当天入账<br><big>" + (int) businessBean.getEarnings() + ".</big><small><small>" + String.valueOf(businessBean.getEarnings()).split("\\.")[1] + "</small></small>";
        }else if(type==7){
            yingMoney= "当周入账<br><big>" + (int) businessBean.getEarnings() + ".</big><small><small>" + String.valueOf(businessBean.getEarnings()).split("\\.")[1] + "</small></small>";
        }else if(type==30){
            yingMoney= "当月入账<br><big>" + (int) businessBean.getEarnings() + ".</big><small><small>" + String.valueOf(businessBean.getEarnings()).split("\\.")[1] + "</small></small>";
        }else if(type==4){
            yingMoney= "当季入账<br><big>" + (int) businessBean.getEarnings() + ".</big><small><small>" + String.valueOf(businessBean.getEarnings()).split("\\.")[1] + "</small></small>";
        }else if(type==12){
            yingMoney= "当年入账<br><big>" + (int) businessBean.getEarnings() + ".</big><small><small>" + String.valueOf(businessBean.getEarnings()).split("\\.")[1] + "</small></small>";
        }
         earnText.setText(Html.fromHtml(yingMoney));
        String discountMoney = "下单次数<br><big>" + businessBean.getOrdernum() + "</big>";
        discountText.setText(Html.fromHtml(discountMoney));
        String duesumMoney = "支付笔数<br><big>" +  businessBean.getPaynum() + "</big>";
        duesumText.setText(Html.fromHtml(duesumMoney));


        lsColumn3.clear();

        if (businessBean.getDetails() != null) {
            if (businessBean.getDetails().size() > 0) {
                barChart3.setVisibility(View.VISIBLE);
                adapter.setList(businessBean.getDetails());
                int numColumns = businessBean.getDetails().size();
                List<AxisValue> axisValues3 = new ArrayList<AxisValue>();

                for (int i = 0; i < numColumns; ++i) {
                    lsValue3=new ArrayList<>();
                    for (int j = 0; j < numColumns; ++j) {
                        if(j==i) {
                            lsValue3.add(new SubcolumnValue((float) DateUtil.doubleValue(businessBean.getDetails().get(i).getSum()),
                                    ChartUtils.pickColor()));
                        }
                    }
                    // 点击柱状图就展示数据量
                    if(payMap.get(businessBean.getDetails().get(i).getPayType())!=null&&!payMap.get(businessBean.getDetails().get(i).getPayType()).equals("")) {
                        axisValues3.add(new AxisValue(i).setLabel(payMap.get(businessBean.getDetails().get(i).getPayType())));
                    }else{
                        axisValues3.add(new AxisValue(i).setLabel("未知"));
                    }

                    lsColumn3.add(new Column(lsValue3).setHasLabels(true));
                }

                columnData3 = new ColumnChartData(lsColumn3);
                columnData3.setAxisXBottom(new Axis(axisValues3).setHasLines(true)
                        .setTextColor(Color.BLACK));
                columnData3.setAxisYLeft(new Axis().setHasLines(true)
                        .setTextColor(Color.BLACK).setMaxLabelChars(2));
                if(columnData3==null){
                    showMessage("colum");
                }else if(barChart3==null){
                    showMessage("barChart3");
                }else {
                    barChart3.setColumnChartData(columnData3);
                    barChart3.setValueSelectionEnabled(false);
                }
            }else{
                adapter.setList(typeBeans);
                barChart3.setVisibility(View.INVISIBLE);
            }
        }else{
            adapter.setList(typeBeans);
            barChart3.setVisibility(View.INVISIBLE);
        }
    }


    @OnClick(R.id.pay_day_layout)
    public void day() {
        dayText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        dayH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        weekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        weekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        yearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        yearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        jiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        jiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        monthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        monthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        type = 1;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        lastText.setText("前一天");
        hText.setText("后一天");
        barChart3=null;
        doDetail();
    }

    @OnClick(R.id.pay_week_layout)
    public void week() {
        dayText.setTextColor(getResources().getColor(R.color.textColor_grey));
        dayH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        weekText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        weekH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        yearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        yearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        jiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        jiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        monthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        monthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        type = 7;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        lastText.setText("前一周");
        hText.setText("后一周");
        barChart3=null;
        doDetail();
    }

    @OnClick(R.id.pay_month_layout)
    public void month() {
        dayText.setTextColor(getResources().getColor(R.color.textColor_grey));
        dayH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        weekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        weekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        yearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        yearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        jiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        jiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        monthText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        monthH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        type = 30;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        timeText.setText(yearNum + "年" + monthNum + "月");
        lastText.setText("前一月");
        hText.setText("后一月");
        barChart3=null;
        doDetail();
    }

    @OnClick(R.id.pay_ji_layout)
    public void ji() {
        dayText.setTextColor(getResources().getColor(R.color.textColor_grey));
        dayH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        weekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        weekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        yearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        yearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        jiText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        jiH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        monthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        monthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        type = 4;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        if((monthNum%3)==0)
        {
            timeText.setText(yearNum + "年第" + (monthNum / 3) + "季");
        }else{
            timeText.setText(yearNum + "年第" + ((monthNum / 3) + 1) + "季");
        }
        lastText.setText("前一季");
        hText.setText("后一季");
        barChart3=null;
        doDetail();
    }

    @OnClick(R.id.pay_year_layout)
    public void year() {
        dayText.setTextColor(getResources().getColor(R.color.textColor_grey));
        dayH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        weekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        weekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        yearText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        yearH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        jiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        jiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        monthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        monthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        type = 12;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        timeText.setText(yearNum + "年");
        lastText.setText("前一年");
        hText.setText("后一年");
        barChart3=null;
        doDetail();
    }

    @OnClick(R.id.pay_last_text)
    public void lastday() {
        if (type == 1) {
            time = time - 60 * 60 * 1000 * 24;
            timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        } else if (type == 4) {
            if (monthNum>3) {
                monthNum = monthNum - 3;
            } else {
                yearNum = yearNum - 1;
                monthNum = monthNum + 9;
            }
            if((monthNum%3)==0)
            {
                timeText.setText(yearNum + "年第" + (monthNum / 3) + "季");
            }else{
                timeText.setText(yearNum + "年第" + ((monthNum / 3) + 1) + "季");
            }
            time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
        } else if (type == 7) {
            time = time - 60 * 60 * 1000 * 24 * 7;
            timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        } else if (type == 12) {
            yearNum = yearNum - 1;
            timeText.setText(yearNum + "年");
            time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
        } else if (type == 30) {
            if (monthNum == 1) {
                yearNum = yearNum - 1;
                monthNum = 12;
            } else {
                monthNum--;
            }
            timeText.setText(yearNum + "年" + monthNum + "月");
            time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
        }
        barChart3=null;
        doDetail();
    }

    @OnClick(R.id.pay_h_text)
    public void hText() {
        if (time == DateUtil.getTimesmorning()) {
            showMessage("不能超过今天");
        } else {

            if (type == 1) {
                time = time + 60 * 60 * 1000 * 24;
                timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
            } else if (type == 4) {
                if (monthNum >9) {
                    monthNum = monthNum - 9;
                    yearNum = yearNum + 1;
                } else {
                    monthNum = monthNum + 3;
                }

                if((monthNum%3)==0)
                {
                    timeText.setText(yearNum + "年第" + (monthNum / 3) + "季");
                }else{
                    timeText.setText(yearNum + "年第" + ((monthNum / 3) + 1) + "季");
                }
                time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
            } else if (type == 7) {

                time = time + 60 * 60 * 1000 * 24 * 7;
                timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
            } else if (type == 12) {
                yearNum = yearNum + 1;
                timeText.setText(yearNum + "年");
                time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
            } else if (type == 30) {
                if (monthNum == 12) {
                    yearNum = yearNum + 1;
                    monthNum = 1;
                } else {
                    monthNum++;
                }
                timeText.setText(yearNum + "年" + monthNum + "月");
                time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
            }
            barChart3=null;
            doDetail();
        }
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("营业日报"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("营业日报"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
