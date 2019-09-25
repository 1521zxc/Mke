package com.example.epay.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.adapter.BestSellAdapter;
import com.example.epay.adapter.BestSellingDishesAdapter;
import com.example.epay.adapter.CataCashAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.BestSellBean;
import com.example.epay.bean.BusinessBean;
import com.example.epay.bean.CataItemBean;
import com.example.epay.bean.OrderMealLeftBean;
import com.example.epay.bean.OrderMealListBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.DateUtil;
import com.example.epay.view.EPayListView;
import com.example.epay.view.HorizontalListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class BestSellingDishesActivity extends BaseActivity {

    @Bind(R.id.BestSelling_day_text)
    TextView BestSellingDayText;
    @Bind(R.id.BestSelling_day_h)
    TextView BestSellingDayH;
    @Bind(R.id.BestSelling_day_layout)
    LinearLayout BestSellingDayLayout;
    @Bind(R.id.BestSelling_week_text)
    TextView BestSellingWeekText;
    @Bind(R.id.BestSelling_week_h)
    TextView BestSellingWeekH;
    @Bind(R.id.BestSelling_week_layout)
    LinearLayout BestSellingWeekLayout;
    @Bind(R.id.BestSelling_month_text)
    TextView BestSellingMonthText;
    @Bind(R.id.BestSelling_month_h)
    TextView BestSellingMonthH;
    @Bind(R.id.BestSelling_month_layout)
    LinearLayout BestSellingMonthLayout;
    @Bind(R.id.BestSelling_ji_text)
    TextView BestSellingJiText;
    @Bind(R.id.BestSelling_ji_h)
    TextView BestSellingJiH;
    @Bind(R.id.BestSelling_ji_layout)
    LinearLayout BestSellingJiLayout;
    @Bind(R.id.BestSelling_year_text)
    TextView BestSellingYearText;
    @Bind(R.id.BestSelling_year_h)
    TextView BestSellingYearH;
    @Bind(R.id.BestSelling_year_layout)
    LinearLayout BestSellingYearLayout;
    @Bind(R.id.BestSelling_last_text)
    TextView BestSellingLastText;
    @Bind(R.id.BestSelling_time_text)
    TextView BestSellingTimeText;
    @Bind(R.id.BestSelling_h_text)
    TextView BestSellingHText;
    @Bind(R.id.best_selling_dishes_layout)
    LinearLayout bestSellingDishesLayout;
    @Bind(R.id.best_selling_dishes_barChart)
    PieChartView bestSellingDishesBarChart;
    @Bind(R.id.best_selling_dishes_listView)
    EPayListView bestSellingDishesListView;

    long time = 0;
    int type = 1;
    int dayNum = 1;
    int monthNum = 1;
    int yearNum = 1;
    int cataID = -1;
    int ranking = 1;
    int count = 10;
    CataCashAdapter adapter;
    ArrayList<CataItemBean> list;
    BusinessBean businessBean;
    JSONObject object = new JSONObject();
    @Bind(R.id.best_selling_dishes_top1)
    TextView bestSellingDishesTop1;
    @Bind(R.id.best_selling_dishes_top2)
    TextView bestSellingDishesTop2;
    @Bind(R.id.best_selling_dishes_top3)
    TextView bestSellingDishesTop3;
    @Bind(R.id.best_selling_dishes_barChart_listView)
    EPayListView bestSellingDishesBarChartListView;
    @Bind(R.id.best_selling_horizontal_listView)
    HorizontalListView bestSellingHorizontalListView;

    BestSellingDishesAdapter bestSellingDishesAdapter;
    private boolean hasLabels = false;// 是否显示数据
    private boolean hasLabelsOutside = true; // 数据是否显示在外面
    private boolean hasCenterCircle = true; // 是否含有中圈，显示下面的内容这个必须为true
    private boolean hasCenterText1 = true; // 圆中是否含有内容1
    private boolean hasCenterText2 = true; // 圆中是否含有内容2
    private boolean isExploded = false; // 是否爆破形式
    private boolean hasLabelForSelected = false; // 是否选中显示数据，一般为false
    ArrayList<OrderMealLeftBean> menuList = new ArrayList<>();
    ArrayList<BestSellBean> sellBeans=new ArrayList<>();
    BestSellAdapter bestSelladapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_selling_dishes);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        BestSellingDayText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        BestSellingDayH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        BestSellingWeekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingWeekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingYearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingYearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingJiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingJiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingMonthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingMonthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        time = DateUtil.getTimesmorning();
        BestSellingTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        list = new ArrayList<>();
        adapter = new CataCashAdapter(this, list);
        bestSellingDishesListView.setAdapter(adapter);
        bestSellingDishesAdapter=new BestSellingDishesAdapter(this,menuList);
        bestSellingHorizontalListView.setAdapter(bestSellingDishesAdapter);
        bestSelladapter= new BestSellAdapter(this,sellBeans);
        bestSellingDishesBarChartListView.setAdapter(bestSelladapter);
        bestSellingHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bestSellingDishesAdapter.setIndex(i);
                cataID=menuList.get(i).getID();
                doDetail();
            }
        });

        httpCata();

    }

    @OnClick(R.id.best_selling_dishes_top1)
    public void top1() {
        ranking = 1;
        bestSellingDishesTop1.setBackground(getResources().getDrawable(R.drawable.corner_white_stroke3));
        bestSellingDishesTop2.setBackground(getResources().getDrawable(R.drawable.corner_white_stroke4));
        bestSellingDishesTop3.setBackground(getResources().getDrawable(R.drawable.corner_white_stroke4));
        doDetail();
    }

    @OnClick(R.id.best_selling_dishes_top2)
    public void top2() {
        ranking = 11;
        bestSellingDishesTop2.setBackground(getResources().getDrawable(R.drawable.corner_white_stroke3));
        bestSellingDishesTop1.setBackground(getResources().getDrawable(R.drawable.corner_white_stroke4));
        bestSellingDishesTop3.setBackground(getResources().getDrawable(R.drawable.corner_white_stroke4));
        doDetail();
    }

    @OnClick(R.id.best_selling_dishes_top3)
    public void top3() {
        ranking = 21;
        bestSellingDishesTop3.setBackground(getResources().getDrawable(R.drawable.corner_white_stroke3));
        bestSellingDishesTop2.setBackground(getResources().getDrawable(R.drawable.corner_white_stroke4));
        bestSellingDishesTop1.setBackground(getResources().getDrawable(R.drawable.corner_white_stroke4));
        doDetail();
    }

    @OnClick(R.id.BestSelling_day_layout)
    public void day() {
        BestSellingDayText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        BestSellingDayH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        BestSellingWeekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingWeekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingYearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingYearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingJiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingJiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingMonthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingMonthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        type = 1;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        BestSellingTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        BestSellingLastText.setText("前一天");
        BestSellingHText.setText("后一天");
        doDetail();
    }

    @OnClick(R.id.BestSelling_week_layout)
    public void week() {
        BestSellingDayText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingDayH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingWeekText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        BestSellingWeekH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        BestSellingYearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingYearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingJiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingJiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingMonthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingMonthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        type = 7;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        BestSellingTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        BestSellingLastText.setText("前一周");
        BestSellingHText.setText("后一周");
        doDetail();
    }

    @OnClick(R.id.BestSelling_month_layout)
    public void month() {
        BestSellingDayText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingDayH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingWeekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingWeekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingYearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingYearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingJiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingJiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingMonthText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        BestSellingMonthH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        type = 30;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        BestSellingTimeText.setText(yearNum + "年" + monthNum + "月");
        BestSellingLastText.setText("前一月");
        BestSellingHText.setText("后一月");
        doDetail();
    }

    @OnClick(R.id.BestSelling_ji_layout)
    public void ji() {
        BestSellingDayText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingDayH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingWeekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingWeekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingYearText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingYearH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingJiText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        BestSellingJiH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        BestSellingMonthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingMonthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        type = 4;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        if ((monthNum % 3) == 0) {
            BestSellingTimeText.setText(yearNum + "年第" + (monthNum / 3) + "季");
        } else {
            BestSellingTimeText.setText(yearNum + "年第" + ((monthNum / 3) + 1) + "季");
        }
        BestSellingLastText.setText("前一季");
        BestSellingHText.setText("后一季");
        doDetail();
    }

    @OnClick(R.id.BestSelling_year_layout)
    public void year() {
        BestSellingDayText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingDayH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingWeekText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingWeekH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingYearText.setTextColor(getResources().getColor(R.color.appHeaderColor1));
        BestSellingYearH.setBackgroundColor(getResources().getColor(R.color.appHeaderColor1));
        BestSellingJiText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingJiH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        BestSellingMonthText.setTextColor(getResources().getColor(R.color.textColor_grey));
        BestSellingMonthH.setBackgroundColor(getResources().getColor(R.color.textColor_grey));
        type = 12;
        time = DateUtil.getTimesmorning();
        yearNum = Integer.parseInt(DateUtil.format2(time, "yyyy"));
        monthNum = Integer.parseInt(DateUtil.format2(time, "MM"));
        dayNum = Integer.parseInt(DateUtil.format2(time, "dd"));
        BestSellingTimeText.setText(yearNum + "年");
        BestSellingLastText.setText("前一年");
        BestSellingHText.setText("后一年");
        doDetail();
    }

    @OnClick(R.id.BestSelling_last_text)
    public void lastday() {
        if (type == 1) {
            time = time - 60 * 60 * 1000 * 24;
            BestSellingTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        } else if (type == 4) {
            if (monthNum > 3) {
                monthNum = monthNum - 3;
            } else {
                yearNum = yearNum - 1;
                monthNum = monthNum + 9;
            }
            if ((monthNum % 3) == 0) {
                BestSellingTimeText.setText(yearNum + "年第" + (monthNum / 3) + "季");
            } else {
                BestSellingTimeText.setText(yearNum + "年第" + ((monthNum / 3) + 1) + "季");
            }
            time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
        } else if (type == 7) {
            time = time - 60 * 60 * 1000 * 24 * 7;
            BestSellingTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
        } else if (type == 12) {
            yearNum = yearNum - 1;
            BestSellingTimeText.setText(yearNum + "年");
            time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
        } else if (type == 30) {
            if (monthNum == 1) {
                yearNum = yearNum - 1;
                monthNum = 12;
            } else {
                monthNum--;
            }
            BestSellingTimeText.setText(yearNum + "年" + monthNum + "月");
            time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
        }
        doDetail();
    }

    @OnClick(R.id.BestSelling_h_text)
    public void hText() {
        if (time == DateUtil.getTimesmorning()) {
            showMessage("不能超过今天");
        } else {

            if (type == 1) {
                time = time + 60 * 60 * 1000 * 24;
                BestSellingTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
            } else if (type == 4) {
                if (monthNum > 9) {
                    monthNum = monthNum - 9;
                    yearNum = yearNum + 1;
                } else {
                    monthNum = monthNum + 3;
                }

                if ((monthNum % 3) == 0) {
                    BestSellingTimeText.setText(yearNum + "年第" + (monthNum / 3) + "季");
                } else {
                    BestSellingTimeText.setText(yearNum + "年第" + ((monthNum / 3) + 1) + "季");
                }
                time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
            } else if (type == 7) {

                time = time + 60 * 60 * 1000 * 24 * 7;
                BestSellingTimeText.setText(DateUtil.format2(time, "yyyy年MM月dd日"));
            } else if (type == 12) {
                yearNum = yearNum + 1;
                BestSellingTimeText.setText(yearNum + "年");
                time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
            } else if (type == 30) {
                if (monthNum == 12) {
                    yearNum = yearNum + 1;
                    monthNum = 1;
                } else {
                    monthNum++;
                }
                BestSellingTimeText.setText(yearNum + "年" + monthNum + "月");
                time = DateUtil.getStringToDate(yearNum + "-" + monthNum + "-" + dayNum, "yyyy-MM-dd");
            }
            doDetail();
        }
    }

    private void httpCata() {
        object.clear();
        object.put("cataIndex", 10000);
        httpUtil.HttpServer(this, object.toString(), 4, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                OrderMealListBean bean = gson.fromJson(data, OrderMealListBean.class);
                menuList = bean.getItems();
                OrderMealLeftBean leftBean=new OrderMealLeftBean();
                leftBean.setID(-1);
                leftBean.setName("全部类型");
                menuList.add(0,leftBean);
                bestSellingDishesAdapter.setList(menuList);
                doDetail();
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    private void doDetail() {
        object.clear();
        object.put("datetime", time);
        object.put("type", type);
        object.put("cataID", cataID);
        object.put("ranking", ranking);
        object.put("count", count);
        object.put("storeID", getIntent().getIntExtra("storesId", 0));
        httpUtil.HttpServer(this, object.toString(), 75, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                businessBean = gson.fromJson(data, BusinessBean.class);
                if (businessBean.getItems().size() > 0) {
                    bestSellingDishesBarChart.setVisibility(View.VISIBLE);
                    bestSellingDishesListView.setVisibility(View.VISIBLE);
                    initdata();
                } else {
                    showMessage("当天没开张");
                    sellBeans.clear();
                    bestSelladapter.setList(sellBeans);
                    bestSellingDishesBarChart.setVisibility(View.GONE);
                    bestSellingDishesListView.setVisibility(View.GONE);
                }
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }


    private void initdata() {
        double value = 0;
        double valueB = 0;
        double value1 = 0;
        double valueB1 = 0;
        //颜色list

        for (int i = 0; i < businessBean.getItems().size(); i++) {
            value = value + businessBean.getItems().get(i).getSum();
            value1 = value1 + businessBean.getItems().get(i).getNum();
        }
        for (int i = 0; i < businessBean.getItems().size(); i++) {
            double s = (businessBean.getItems().get(i).getSum() * 10000 / value);
            if (i == businessBean.getItems().size() - 1) {
                businessBean.getItems().get(i).setBai(new BigDecimal(100 - valueB).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
            } else {
                valueB = valueB + s / 100;
                businessBean.getItems().get(i).setBai((float) (new BigDecimal(s / 100).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue()));
            }
            businessBean.getItems().get(i).setSum(DateUtil.doubleValue(businessBean.getItems().get(i).getSum()));
            double s1 = (businessBean.getItems().get(i).getNum() * 10000 / value1);
            if (i == businessBean.getItems().size() - 1) {
                businessBean.getItems().get(i).setNumbai(new BigDecimal(100 - valueB1).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
            } else {
                valueB1 = valueB1 + s1 / 100;
                businessBean.getItems().get(i).setNumbai((float) (new BigDecimal(s1 / 100).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue()));
            }

        }
        generateData();
        list = businessBean.getItems();
        adapter.setList(list);

    }

    private void generateData() {
        PieChartData data;
        int numValues = businessBean.getItems().size();
        List<SliceValue> values = new ArrayList<SliceValue>();
        sellBeans.clear();
        for (int i = 0; i < numValues; i++) {
            SliceValue sliceValue = new SliceValue(businessBean.getItems().get(i).getNumbai(), ChartUtils.pickColor());
            values.add(sliceValue);
            BestSellBean bestSellBean=new BestSellBean();
            bestSellBean.setName(businessBean.getItems().get(i).getName());
            bestSellBean.setColor(values.get(i).getColor());
            sellBeans.add(bestSellBean);
        }
        bestSelladapter.setList(sellBeans);
        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);
        // 设置不显示数据的背景颜色
        data.setValueLabelBackgroundEnabled(true);
        if (isExploded) {
            data.setSlicesSpacing(24);
        }
        if (hasCenterText1) {
            data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity, (int) getResources().getDimension(R.dimen.sp_16)));
            data.setCenterText1Color(getResources().getColor(R.color.textColor_grey));
        }
        if (hasCenterText2) {
            data.setCenterText2("未做占比");
            data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity, (int) getResources().getDimension(R.dimen.sp_16)));
            data.setCenterText2Color(getResources().getColor(R.color.textColor_grey));
        }
        bestSellingDishesBarChart.setPieChartData(data);
    }


}
