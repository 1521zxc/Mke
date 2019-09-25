package com.example.epay.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.InfosBean;
import com.example.epay.bean.StatisticListBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.DateUtil;
import com.example.epay.view.CancelSelectorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class StatisticActivity extends BaseActivity {
    @Bind(R.id.chart)
    LineChartView chartView;
    @Bind(R.id.statistic_name)
    TextView name;
    @Bind(R.id.statistic_time)
    TextView time;
    @Bind(R.id.statistic_time_money)
    TextView time_money;
    @Bind(R.id.statistic_jie_money)
    TextView jie_money;
    @Bind(R.id.statistic_no_money)
    TextView no_money;
    @Bind(R.id.statistic_num)
    TextView num;
    @Bind(R.id.statistic_zuo_money)
    TextView zuo_money;
    @Bind(R.id.statistic_qi_money)
    TextView qi_money;
    @Bind(R.id.statistic_month_money)
    TextView month_money;
    @Bind(R.id.statistic_qi_time_money)
    TextView qi_time_money;
    @Bind(R.id.statistic_zuo_qi_money)
    TextView zuo_qi_money;
    @Bind(R.id.statistic_zuo_month_money)
    TextView zuo_month_money;


    @Bind(R.id.new_money)
    TextView new_money;
    @Bind(R.id.new_people)
    TextView new_people;
    @Bind(R.id.statistic_discount_money)
    TextView discount_money;




    StatisticListBean statisticListBean;
    int id=0;
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<>();
    InfosBean infosBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        time.setText("今日("+ DateUtil.getTime("yyyy-MM-dd")+")预收入");

        doDetail();
    }

    @OnClick(R.id.statistic_refresh)
    public void redresh() {
        initView();
    }
    @OnClick(R.id.statistic_name)
    public  void name()
    {
        CancelSelectorView selectorView=new CancelSelectorView(StatisticActivity.this,statisticListBean.getStores());
        selectorView.setCanceledOnTouchOutside(true);
        Window window = selectorView.getWindow();
        window.setGravity(Gravity.BOTTOM);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        WindowManager.LayoutParams p = selectorView.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = d.getWidth(); //宽度设置为屏幕
        p.height = d.getWidth()*2/3; //宽度设置为屏幕
        selectorView.getWindow().setAttributes(p); //设置生效

        selectorView.setStringListener(new CancelSelectorView.StringListener() {
            @Override
            public void StringClick(int index) {
                if (index<0||index>statisticListBean.getStores().size()-1)
                {
                    showMessage("未选择店铺");
                    return;
                }
                id=statisticListBean.getStores().get(index).getID();
                infosBean=statisticListBean.getInfos().get(index);
                initdata();


            }
        });
        selectorView.show();
    }

    private void doDetail() {
        httpUtil.HttpServer(this, "", 69, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                statisticListBean=gson.fromJson(data, StatisticListBean.class);
                if(statisticListBean.getInfos().size()>0) {
                    infosBean=statisticListBean.getInfos().get(0);
                    initdata();
                    name.setText(statisticListBean.getStores().get(0).getBrandName());
                }
                id=statisticListBean.getStores().get(0).getID();
            }

            @Override
            public void fail(String Message, int code,String data) {
                showMessage(Message);
            }
        });
    }



    private void initdata()
    {
        String today=String.valueOf(infosBean.getToday());
        String timeMoney="<big>"+today.split("\\.")[0]+".</big><small><small>"+today.split("\\.")[1]+"</small></small>";
        time_money.setText(Html.fromHtml(timeMoney));

        String paid=String.valueOf(infosBean.getPaid());
        String jieMoney="已结金额<br><big>"+paid.split("\\.")[0]+".</big><small><small>"+paid.split("\\.")[1]+"</small></small>";
        jie_money.setText(Html.fromHtml(jieMoney));

        String unPaid=String.valueOf(infosBean.getUnpaid());
        String noMoney="未结金额<br><big>"+unPaid.split("\\.")[0]+".</big><small><small>"+unPaid.split("\\.")[1]+"</small></small>";
        no_money.setText(Html.fromHtml(noMoney));

        String numText="就餐人数<br>"+(int)infosBean.getAttendNum()+"/"+(int)infosBean.getReserveNum();
        num.setText(Html.fromHtml(numText));

        String lastday=String.valueOf(infosBean.getLastDay());
        String zuoMoney="昨日收入<br><big>"+lastday.split("\\.")[0]+".</big><small><small>"+lastday.split("\\.")[1]+"</small></small>";
        zuo_money.setText(Html.fromHtml(zuoMoney));

        String thisWeek=String.valueOf(infosBean.getThisWeek());
        String qiMoney="本周累计<br><big>"+thisWeek.split("\\.")[0]+".</big><small><small>"+thisWeek.split("\\.")[1]+"</small></small>";
        qi_money.setText(Html.fromHtml(qiMoney));

        String getThisMoneth=String.valueOf(infosBean.getThisMoneth());
        String monthMoney="本月累计<br><big>"+getThisMoneth.split("\\.")[0]+".</big><small><small>"+getThisMoneth.split("\\.")[1]+"</small></small>";
        month_money.setText(Html.fromHtml(monthMoney));

        String getTheDayBeforeWeek=String.valueOf(infosBean.getTheDayBeforeWeek());
        String qitimeMoney="上周本日<br><big>"+getTheDayBeforeWeek.split("\\.")[0]+".</big><small><small>"+getTheDayBeforeWeek.split("\\.")[1]+"</small></small>";
        qi_time_money.setText(Html.fromHtml(qitimeMoney));

        String getLastWeak=String.valueOf(infosBean.getLastWeak());
        String zuoqiMoney="上周累计<br><big>"+getLastWeak.split("\\.")[0]+".</big><small><small>"+getLastWeak.split("\\.")[1]+"</small></small>";
        zuo_qi_money.setText(Html.fromHtml(zuoqiMoney));

        String getLastMonth=String.valueOf(infosBean.getLastMonth());
        String zuomonthMoney="上月累计<br><big>"+getLastMonth.split("\\.")[0]+".</big><small><small>"+getLastMonth.split("\\.")[1]+"</small></small>";
        zuo_month_money.setText(Html.fromHtml(zuomonthMoney));



        String getDiscount=String.valueOf(infosBean.getDiscount());
        String disMoney="优惠金额<br><big>"+getDiscount.split("\\.")[0]+".</big><small><small>"+getDiscount.split("\\.")[1]+"</small></small>";
        discount_money.setText(Html.fromHtml(disMoney));





        getAxisXLables();
        getAxisPoints();
        Line line = new Line(mPointValues).setColor(Color.parseColor("#3188ff")).setStrokeWidth(1);
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);    //折线图上每个数据点的形状，这里是圆形
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        line.setPointRadius(2);
        //座标点大小
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.parseColor("#D6D6D9"));//设置字体颜色

        axisX.setTextSize(8);//设置字体大小
        axisX.setMaxLabelChars(8);//最多几个X轴坐标
        axisX.setValues(mAxisXValues);
        data.setAxisXBottom(axisX);
        axisX.setHasLines(true);


        Axis axisY = new Axis();
        axisY.setName("");
        axisY.setTextSize(8);
        data.setAxisYLeft(axisY);
        //设置行为属性，缩放、滑动、平移
        chartView.setInteractive(false);
        chartView.setZoomEnabled(true);
        chartView.setMaxZoom((float) 3);
        chartView.setLineChartData(data);
        chartView.setVisibility(View.VISIBLE);
        //设置X轴数据的显示个数（x轴0-7个数据）
        Viewport v = new Viewport(chartView.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        chartView.setCurrentViewport(v);





        String newMoney="营业额<br><big>"+today.split("\\.")[0]+".</big><small><small>"+today.split("\\.")[1]+"</small></small>";
        new_money.setText(Html.fromHtml(newMoney));

        String newpeople="客流量<br><big>"+(int)infosBean.getAttendNum()+"</big>";
        new_people.setText(Html.fromHtml(newpeople));
    }


    private void getAxisXLables(){
        if(infosBean!=null) {
            for (int i = 0; i < infosBean.getSamples().size(); i++) {
                mAxisXValues.add(new AxisValue(i).setLabel(DateUtil.format2(infosBean.getSamples().get(i).getDatetime(), "MM-dd")));
            }
        }
    }

    private void getAxisPoints(){
        if(infosBean!=null) {
            for (int i = 0; i < infosBean.getSamples().size(); i++) {
                mPointValues.add(new PointValue(i, (float) DateUtil.doubleValue(infosBean.getSamples().get(i).getValue())));
            }
        }
    }

    @OnClick(R.id.business_daily)
    public void business() {
        Intent intent=new Intent(this,BusinessDailyActivity.class);
        intent.putExtra("storesId",id);
        startActivity(intent);
    }
    @OnClick(R.id.desk_manage)
    public void desk() {
        startActivity(this,DeskManageActivity.class);
    }
    @OnClick(R.id.cata_manage)
    public void cataManage() {
        Intent intent=new Intent(this,CataCashActivity.class);
        intent.putExtra("storesId",id);
        startActivity(intent);
    }
    @OnClick(R.id.money_manage)
    public void moneyManage()
    {
        Intent intent=new Intent(this,PayDailyActivity.class);
        intent.putExtra("storesId",id);
        startActivity(intent);
    }
    @OnClick(R.id.member_all)
    public void Member() {
        startActivity(this,MembersActivity.class);
    }

    @OnClick(R.id.cata_re)
    public void cata_re() {
        Intent intent=new Intent(this,BestSellingDishesActivity.class);
        intent.putExtra("storesId",id);
        startActivity(intent);
    }

    @OnClick(R.id.e_manage)
    public void e_manage() {
     //   if() {
            startActivity(this, ErrorActivity.class);
    //    }
    }

    @OnClick(R.id.new_more)
    public void new_more() {
     //   if() {
            startActivity(this, MoreStatisticActivity.class);
     //   }
    }
    @OnClick(R.id.member_content)
    public void member_content() {
       // if() {
            startActivity(this, ContentListActivity.class);
      //  }
    }

    @OnClick(R.id.new_member)
    public void MemberMa() {
        Intent intent=new Intent(this,MemberManageActivity.class);
        intent.putExtra("storesId",id);
        startActivity(intent);
    }
}
