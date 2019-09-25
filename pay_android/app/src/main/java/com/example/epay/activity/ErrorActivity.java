package com.example.epay.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.BusinessBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.DateUtil;
import com.example.epay.view.EPayGridView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ErrorActivity extends BaseActivity {

    @Bind(R.id.error_day_text)
    TextView errorDayText;
    @Bind(R.id.error_day_h)
    TextView errorDayH;
    @Bind(R.id.error_day_layout)
    LinearLayout errorDayLayout;
    @Bind(R.id.error_week_text)
    TextView errorWeekText;
    @Bind(R.id.error_week_h)
    TextView errorWeekH;
    @Bind(R.id.error_week_layout)
    LinearLayout errorWeekLayout;
    @Bind(R.id.error_month_text)
    TextView errorMonthText;
    @Bind(R.id.error_month_h)
    TextView errorMonthH;
    @Bind(R.id.error_month_layout)
    LinearLayout errorMonthLayout;
    @Bind(R.id.error_ji_text)
    TextView errorJiText;
    @Bind(R.id.error_ji_h)
    TextView errorJiH;
    @Bind(R.id.error_ji_layout)
    LinearLayout errorJiLayout;
    @Bind(R.id.error_year_text)
    TextView errorYearText;
    @Bind(R.id.error_year_h)
    TextView errorYearH;
    @Bind(R.id.error_year_layout)
    LinearLayout errorYearLayout;
    @Bind(R.id.error_last_text)
    TextView errorLastText;
    @Bind(R.id.error_time_text)
    TextView errorTimeText;
    @Bind(R.id.error_h_text)
    TextView errorHText;
    @Bind(R.id.error_gridView)
    EPayGridView errorGridView;

    long time = 0;
    int type = 1;
    int dayNum = 1;
    int monthNum = 1;
    int yearNum = 1;

    JSONObject object = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        ButterKnife.bind(this);
    }



    @OnClick(R.id.error_day_layout)
    public void day() {
        errorDayText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        errorDayH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        errorWeekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorWeekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorYearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorYearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorJiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorJiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorMonthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorMonthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        type = 1;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        errorTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        errorLastText.setText("前一天");
        errorHText.setText("后一天");
        doDetail();
    }

    @OnClick(R.id.error_week_layout)
    public void week() {
        errorDayText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorDayH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorWeekText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        errorWeekH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        errorYearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorYearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorJiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorJiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorMonthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorMonthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        type = 7;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        errorTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        errorLastText.setText("前一周");
        errorHText.setText("后一周");
        doDetail();
    }

    @OnClick(R.id.error_month_layout)
    public void month() {
        errorDayText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorDayH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorWeekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorWeekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorYearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorYearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorJiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorJiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorMonthText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        errorMonthH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        type = 30;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        errorTimeText.setText(yearNum + "年" + monthNum + "月");
        errorLastText.setText("前一月");
        errorHText.setText("后一月");
        doDetail();
    }

    @OnClick(R.id.error_ji_layout)
    public void ji() {
        errorDayText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorDayH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorWeekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorWeekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorYearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorYearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorJiText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        errorJiH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        errorMonthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorMonthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        type = 4;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        if((monthNum%3)==0)
        {
            errorTimeText.setText(yearNum + "年第" + (monthNum / 3) + "季");
        }else{
            errorTimeText.setText(yearNum + "年第" + ((monthNum / 3) + 1) + "季");
        }
        errorLastText.setText("前一季");
        errorHText.setText("后一季");
        doDetail();
    }

    @OnClick(R.id.error_year_layout)
    public void year() {
        errorDayText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorDayH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorWeekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorWeekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorYearText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        errorYearH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        errorJiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorJiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        errorMonthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        errorMonthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        type = 12;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        errorTimeText.setText(yearNum + "年");
        errorLastText.setText("前一年");
        errorHText.setText("后一年");
        doDetail();
    }

    @OnClick(R.id.error_last_text)
    public void lastday() {
        if (type == 1) {
            time = time - 60 * 60 * 1000 * 24;
            errorTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        } else if (type == 4) {
            if (monthNum>3) {
                monthNum = monthNum - 3;
            } else {
                yearNum = yearNum - 1;
                monthNum = monthNum + 9;
            }
            if((monthNum%3)==0)
            {
                errorTimeText.setText(yearNum + "年第" + (monthNum / 3) + "季");
            }else{
                errorTimeText.setText(yearNum + "年第" + ((monthNum / 3) + 1) + "季");
            }
            time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
        } else if (type == 7) {
            time = time - 60 * 60 * 1000 * 24 * 7;
            errorTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        } else if (type == 12) {
            yearNum = yearNum - 1;
            errorTimeText.setText(yearNum + "年");
            time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
        } else if (type == 30) {
            if (monthNum == 1) {
                yearNum = yearNum - 1;
                monthNum = 12;
            } else {
                monthNum--;
            }
            errorTimeText.setText(yearNum + "年" + monthNum + "月");
            time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
        }
        doDetail();
    }

    @OnClick(R.id.error_h_text)
    public void hText() {
        if (time == DateUtil.getTimesmorning()) {
            showMessage("不能超过今天");
        } else {

            if (type == 1) {
                time = time + 60 * 60 * 1000 * 24;
                errorTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
            } else if (type == 4) {
                if (monthNum >9) {
                    monthNum = monthNum - 9;
                    yearNum = yearNum + 1;
                } else {
                    monthNum = monthNum + 3;
                }

                if((monthNum%3)==0)
                {
                    errorTimeText.setText(yearNum + "年第" + (monthNum / 3) + "季");
                }else{
                    errorTimeText.setText(yearNum + "年第" + ((monthNum / 3) + 1) + "季");
                }
                time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
            } else if (type == 7) {

                time = time + 60 * 60 * 1000 * 24 * 7;
                errorTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
            } else if (type == 12) {
                yearNum = yearNum + 1;
                errorTimeText.setText(yearNum + "年");
                time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
            } else if (type == 30) {
                if (monthNum == 12) {
                    yearNum = yearNum + 1;
                    monthNum = 1;
                } else {
                    monthNum++;
                }
                errorTimeText.setText(yearNum + "年" + monthNum + "月");
                time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
            }
            doDetail();
        }
    }

    private void doDetail() {
        object.clear();
        object.put("datetime", time);
        object.put("type", type);
        object.put("isFood",0);
        object.put("storeID",getIntent().getIntExtra("storesId",0));
        httpUtil.HttpServer(this, object.toString(), 71, true, new HttpCallBack() {
            @Override
            public void back(String data) {
//                businessBean = gson.fromJson(data, BusinessBean.class);
//                if(businessBean.getItems().size()>0) {
//                    pieChart.setVisibility(View.VISIBLE);
//                    listView.setVisibility(View.VISIBLE);
//                    initdata();
//                }else{
//                    showMessage("当天没开张");
//                    pieChart.setVisibility(View.GONE);
//                    listView.setVisibility(View.GONE);
//                }
            }

            @Override
            public void fail(String Message,int code,String data) {
                showMessage( Message);
            }
        });
    }
}
