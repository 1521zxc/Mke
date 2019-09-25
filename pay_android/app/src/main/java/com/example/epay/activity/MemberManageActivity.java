package com.example.epay.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.BusinessBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.DateUtil;
import com.example.epay.util.TypeUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemberManageActivity extends BaseActivity {

    @Bind(R.id.MemberManage_day_text)
    TextView dayText;
    @Bind(R.id.MemberManage_day_h)
    TextView dayH;
    @Bind(R.id.MemberManage_week_text)
    TextView weekText;
    @Bind(R.id.MemberManage_week_h)
    TextView weekH;
    @Bind(R.id.MemberManage_year_text)
    TextView yearText;
    @Bind(R.id.MemberManage_year_h)
    TextView yearH;
    @Bind(R.id.MemberManage_ji_text)
    TextView jiText;
    @Bind(R.id.MemberManage_ji_h)
    TextView jiH;
    @Bind(R.id.MemberManage_month_text)
    TextView monthText;
    @Bind(R.id.MemberManage_month_h)
    TextView monthH;

    @Bind(R.id.MemberManage_last_text)
    TextView lastText;
    @Bind(R.id.MemberManage_h_text)
    TextView hText;
    @Bind(R.id.MemberManage_time_text)
    TextView timeText;

    String[] quarters;
    @Bind(R.id.member_manage_total_recharge_tv)
    TextView memberManageTotalRechargeTv;
    @Bind(R.id.member_manage_general_presentation_tv)
    TextView memberManageGeneralPresentationTv;
    @Bind(R.id.member_manage_total_recharge_bc)
    BarChart memberManageTotalRechargeBc;
    @Bind(R.id.member_manage_total_gift_bc)
    BarChart memberManageTotalGiftBc;

    BusinessBean businessBean;
    JSONObject object = new JSONObject();
    long time = 0;
    long lastTime=0;
    int type = 1;
    int dayNum = 1;
    int monthNum = 1;
    int lastMonthNum = 1;
    int yearNum = 1;
    int lastYearNum = 1;
    int quarIndex = 0;

    ArrayList<Long> times=new ArrayList<>();

    // 控件初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_manage);
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
        lastTime=time - 60 * 60 * 1000 * 24;
        times.add(lastTime);
        times.add(time);
        timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        doDetail();
    }

    // 网络请求
    private void doDetail() {
        object.clear();
        times.set(0,lastTime);
        times.set(1,time);
        object.put("datetime", times);
        object.put("type", type);
        object.put("storeID", getIntent().getIntExtra("storesId", 0));
        httpUtil.HttpServer(this, object.toString(), 67, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                businessBean = gson.fromJson(data, BusinessBean.class);
                initView();
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    public void initView() {
        quarIndex = 0;
        DecimalFormat df = new DecimalFormat("###,##0.00");
        String Earn = df.format(businessBean.getEarnings());

        String gift = df.format(businessBean.getGift());

        String totalRechargeTv = "总充值<br><big>" + Earn.split("\\.")[0] + ".</big><small><small>"
                + Earn.split("\\.")[1] + "</small></small>";
        memberManageTotalRechargeTv.setText(Html.fromHtml(totalRechargeTv));

        String generalPresentationTv = "总赠送<br><big>" + gift.split("\\.")[0] + ".</big><small><small>"
                + gift.split("\\.")[1] + "</small></small>";
        memberManageGeneralPresentationTv.setText(Html.fromHtml(generalPresentationTv));


        ArrayList<BarEntry> entries3 = new ArrayList<>();
        if (businessBean.getDetails() != null) {
            if (businessBean.getDetails().size() > 0) {
                int[] colorList = new int[businessBean.getDetails().size()];
                for (int i = 0; i < businessBean.getDetails().size(); i++) {
                    Random random = new Random();
                    int color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                    colorList[i] = color;
                }
                quarters = new String[businessBean.getDetails().size()];
                for (int i = 0; i < businessBean.getDetails().size(); i++) {
                    BarEntry barEntry = new BarEntry((float) i, Float.valueOf(String.format("%.2f", businessBean.getDetails().get(i).getSum())));
                    entries3.add(barEntry);
                    quarIndex = i;
                    quarters[i] = TypeUtil.name(businessBean.getDetails().get(i).getPayType());
                }
                IAxisValueFormatter formatter3 = new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        int index = (int) value;
                        if (index < 0 || index >= quarters.length) {
                            return "";
                        } else {
                            return quarters[(int) value];
                        }
                    }
                };

                //获取此图表的x轴
                XAxis xAxis3 = memberManageTotalRechargeBc.getXAxis();
                xAxis3.setEnabled(true);//设置轴启用或禁用 如果禁用以下的设置全部不生效
                xAxis3.setDrawAxisLine(true);//是否绘制轴线
                xAxis3.setDrawGridLines(false);//设置x轴上每个点对应的线
                xAxis3.setDrawLabels(true);//绘制标签  指x轴上的对应数值
                xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
                xAxis3.enableGridDashedLine(4f, 4f, 0f);
                xAxis3.setAvoidFirstLastClipping(true);//图表将避免第一个和最后一个标签条目被减掉在图表或屏幕的边缘
                xAxis3.setGranularity(1f);
                xAxis3.setTextSize(2f);
                xAxis3.setValueFormatter(formatter3);//格式化x轴标签显示字符

                /**
                 * Y轴默认显示左右两个轴线
                 */
                //获取右边的轴线
                YAxis rightAxis3 = memberManageTotalRechargeBc.getAxisRight();
                //设置图表右边的y轴禁用
                rightAxis3.setEnabled(false);
                //获取左边的轴线
                YAxis leftAxis3 = memberManageTotalRechargeBc.getAxisLeft();
                //设置网格线为虚线效果
                leftAxis3.enableGridDashedLine(10f, 10f, 0f);
                //是否绘制0所在的网格线
                leftAxis3.setDrawZeroLine(false);
                leftAxis3.setValueFormatter(new LargeValueFormatter());

                BarDataSet set3 = new BarDataSet(entries3, "充值总额与昨日的对比");
                set3.setColors(colorList);
                BarData data3 = new BarData(set3);
                data3.setBarWidth(0.8f); //设置自定义条形宽度
                memberManageTotalRechargeBc.setData(data3);
                memberManageTotalRechargeBc.setFitBars(false); //使x轴完全适合所有条形
                memberManageTotalRechargeBc.invalidate(); // refresh
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        memberManageTotalRechargeBc.setNoDataText("暂无数据");
                        memberManageTotalRechargeBc.setNoDataTextColor(Color.BLACK);
                        memberManageTotalRechargeBc.invalidate();
                    }
                },100);
            }
        }
    }

    // 日布局监听
    @OnClick(R.id.MemberManage_day_layout)
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
        lastTime = time - 60 * 60 * 1000 * 24;
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        lastText.setText("前一天");
        hText.setText("后一天");
        doDetail();
    }

    // 周布局监听
    @OnClick(R.id.MemberManage_week_layout)
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
        lastTime = time - 60 * 60 * 1000 * 24 *7;
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        lastText.setText("前一周");
        hText.setText("后一周");
        doDetail();
    }

    // 月布局监听
    @OnClick(R.id.MemberManage_month_layout)
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
        if (monthNum == 1) {
            lastYearNum = yearNum - 1;
            lastMonthNum = 12;
        } else {
            lastYearNum=yearNum;
            lastMonthNum= monthNum-1;
        }
       lastTime = DateUtil.getStringToDate(lastYearNum + "-" + lastMonthNum + "-" + dayNum, "yyyy-MM-dd");

        lastText.setText("前一月");
        hText.setText("后一月");
        doDetail();
    }

    // 季布局监听
    @OnClick(R.id.MemberManage_ji_layout)
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
        if ((monthNum % 3) == 0) {
            timeText.setText(yearNum + "年第" + (monthNum / 3) + "季");
        } else {
            timeText.setText(yearNum + "年第" + ((monthNum / 3) + 1) + "季");
        }
        if (monthNum / 3 > 0) {
            lastYearNum=yearNum;
            lastMonthNum = monthNum - 3;
        } else {
            lastYearNum = yearNum - 1;
            lastMonthNum = monthNum + 9;
        }
        lastTime = DateUtil.getStringToDate(lastYearNum + "-" + lastMonthNum + "-" + dayNum, "yyyy-MM-dd");
        lastText.setText("前一季");
        hText.setText("后一季");
        doDetail();
    }

    // 年布局监听
    @OnClick(R.id.MemberManage_year_layout)
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
        lastYearNum = yearNum - 1;
        lastTime = DateUtil.getStringToDate(lastYearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
        timeText.setText(yearNum + "年");
        lastText.setText("前一年");
        hText.setText("后一年");
        doDetail();
    }
// 前一天文本监听
    @OnClick(R.id.MemberManage_last_text)
    public void lastday() {
        if (type == 1) {
            time = time - 60 * 60 * 1000 * 24;
            lastTime = time - 60 * 60 * 1000 * 24;
            timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        } else if (type == 4) {
            if (monthNum / 3 > 0) {
                monthNum = monthNum - 3;
            } else {
                yearNum = yearNum - 1;
                monthNum = monthNum + 9;
            }
            if ((monthNum % 3) == 0) {
                timeText.setText(yearNum + "年第" + (monthNum / 3) + "季");
            } else {
                timeText.setText(yearNum + "年第" + ((monthNum / 3) + 1) + "季");
            }
            time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
            if (monthNum / 3 > 0) {
                lastMonthNum = monthNum - 3;
            } else {
                lastYearNum = yearNum - 1;
                lastMonthNum = monthNum + 9;
            }
            lastTime = DateUtil.getStringToDate(lastYearNum + "-" + lastMonthNum + "-" + dayNum, "yyyy-MM-dd");
        } else if (type == 7) {
            time = time - 60 * 60 * 1000 * 24 * 7;
            lastTime = time - 60 * 60 * 1000 * 24 * 7;
            timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        } else if (type == 12) {
            yearNum = yearNum - 1;
            timeText.setText(yearNum + "年");
            time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
            lastYearNum = yearNum - 1;
            lastTime = DateUtil.getStringToDate(lastYearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
        } else if (type == 30) {
            if (monthNum == 1) {
                yearNum = yearNum - 1;
                monthNum = 12;
            } else {
                monthNum--;
            }

            if (monthNum == 1) {
                lastYearNum = yearNum - 1;
                lastMonthNum = 12;
            } else {
                lastMonthNum= monthNum-1;
            }
            timeText.setText(yearNum + "年" + monthNum + "月");
            time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
            lastTime = DateUtil.getStringToDate(lastYearNum + "-" + lastMonthNum + "-" + dayNum, "yyyy-MM-dd");
        }
        doDetail();
    }

    // 后一天文本监听
    @OnClick(R.id.MemberManage_h_text)
    public void hText() {
        if (time == DateUtil.getTimesmorning()) {
            showMessage("不能超过今天");
        } else {
            lastTime = time;
            if (type == 1) {
                time = time + 60 * 60 * 1000 * 24;
                timeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
            } else if (type == 4) {
                if (monthNum / 3 > 2) {
                    monthNum = monthNum - 9;
                    yearNum = yearNum + 1;
                } else {
                    monthNum = monthNum + 3;
                }
                if ((monthNum % 3) == 0) {
                    timeText.setText(yearNum + "年第" + (monthNum / 3) + "季");
                } else {
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

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("会员充值统计"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("会员充值统计"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
