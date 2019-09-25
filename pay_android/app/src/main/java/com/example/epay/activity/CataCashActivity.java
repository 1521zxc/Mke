package com.example.epay.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.adapter.CacaCashAdapter;
import com.example.epay.adapter.CataCashAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.BusinessBean;
import com.example.epay.bean.CataItemBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.DateUtil;
import com.example.epay.view.EPayListView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class CataCashActivity extends BaseActivity {
    @Bind(R.id.cata_chart)
    EPayListView pieChart;
    @Bind(R.id.cata_chart_list)
    EPayListView listView;


    @Bind(R.id.cata_day_text)
    TextView dayText;
    @Bind(R.id.cata_day_h)
    TextView dayH;
    @Bind(R.id.cata_week_text)
    TextView weekText;
    @Bind(R.id.cata_week_h)
    TextView weekH;
    @Bind(R.id.cata_year_text)
    TextView yearText;
    @Bind(R.id.cata_year_h)
    TextView yearH;
    @Bind(R.id.cata_ji_text)
    TextView jiText;
    @Bind(R.id.cata_ji_h)
    TextView jiH;
    @Bind(R.id.cata_month_text)
    TextView monthText;
    @Bind(R.id.cata_month_h)
    TextView monthH;
    @Bind(R.id.cata_time_text)
    TextView timeText;
    @Bind(R.id.cata_last_text)
    TextView lastText;
    @Bind(R.id.cata_h_text)
    TextView hText;
    @Bind(R.id.cata_type_text)
    TextView typeText;
    @Bind(R.id.cata_type_h)
    TextView typeH;
    @Bind(R.id.cata_item_text)
    TextView itemText;
    @Bind(R.id.cata_item_h)
    TextView itemH;
    @Bind(R.id.text)
    TextView text;


    long time = 0;
    int type = 1;
    int dayNum = 1;
    int monthNum = 1;
    int yearNum = 1;
    int isFood=2;


    JSONObject object = new JSONObject();

    BusinessBean businessBean;
    CataCashAdapter adapter;
    CacaCashAdapter adapter2;
    ArrayList<CataItemBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cata_cash);
        ButterKnife.bind(this);
        list = new ArrayList<>();
        adapter = new CataCashAdapter(this, list);
        listView.setAdapter(adapter);
        adapter2 = new CacaCashAdapter(this, list);
        pieChart.setAdapter(adapter2);

        text.setText("菜类销售份数占比");
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
        typeText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        typeH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        itemText.setTextColor(getResources().getColor(R.color.textColor_grey));
        itemH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        time = DateUtil.getTimesmorning();
        timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        doDetail();
    }

    private void doDetail() {
        object.clear();
        object.put("datetime", time);
        object.put("type", type);
        object.put("isFood",isFood);
        object.put("storeID",getIntent().getIntExtra("storesId",0));
        httpUtil.HttpServer(this, object.toString(), 71, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                businessBean = gson.fromJson(data, BusinessBean.class);
                if(businessBean.getItems().size()>0) {
                    pieChart.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    initdata();
                }else{
                    showMessage("当天没开张");
                    pieChart.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                }
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage( Message);
            }
        });
    }


    private void initdata() {

        double value=0;
        double valueB=0;
        double value1=0;
        double valueB1=0;
        //颜色list

        for (int i=0;i<businessBean.getItems().size();i++)
        {
           value=value+businessBean.getItems().get(i).getSum();
            value1=value1+businessBean.getItems().get(i).getNum();
        }
        for (int i=0;i<businessBean.getItems().size();i++)
        {
            double s=(businessBean.getItems().get(i).getSum()*10000/value);
            if(i==businessBean.getItems().size()-1){
                businessBean.getItems().get(i).setBai(new BigDecimal(100-valueB).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
            }else {
                valueB=valueB+s / 100;
                businessBean.getItems().get(i).setBai((float)(new BigDecimal(s/100).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue()));
            }
            businessBean.getItems().get(i).setSum(DateUtil.doubleValue(businessBean.getItems().get(i).getSum()));
            double s1=(businessBean.getItems().get(i).getNum()*10000/value1);
            if(i==businessBean.getItems().size()-1){
                businessBean.getItems().get(i).setNumbai(new BigDecimal(100-valueB1).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
            }else {
                valueB1=valueB1+s1 / 100;
                businessBean.getItems().get(i).setNumbai((float)(new BigDecimal(s1/100).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue()));
            }

        }
        list=businessBean.getItems();
        adapter.setList(list);
        adapter2.setList(list);
    }


    @OnClick(R.id.cata_day_layout)
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
        doDetail();
    }

    @OnClick(R.id.cata_week_layout)
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
        doDetail();
    }

    @OnClick(R.id.cata_month_layout)
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
        doDetail();
    }

    @OnClick(R.id.cata_ji_layout)
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
        doDetail();
    }

    @OnClick(R.id.cata_year_layout)
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
        doDetail();
    }

    @OnClick(R.id.cata_last_text)
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
        doDetail();
    }

    @OnClick(R.id.cata_h_text)
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
            doDetail();
        }
    }

    @OnClick(R.id.cata_type_layout)
    public void type() {
        typeText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        typeH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        itemText.setTextColor(getResources().getColor(R.color.textColor_grey));
        itemH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        isFood=2;
        text.setText("菜类销售份数占比");
        doDetail();
    }
    @OnClick(R.id.cata_item_layout)
    public void item() {
        typeText.setTextColor(getResources().getColor(R.color.textColor_grey));
        typeH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        itemText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        itemH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        isFood=1;
        text.setText("菜品销售份数占比");
        doDetail();
    }
}