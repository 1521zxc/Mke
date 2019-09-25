package com.example.epay.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.view.CustomDatePicker;
import com.example.epay.view.LineRadioGroup;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class SetPointsActivity extends BaseActivity {
    @Bind(R.id.c0)
    CheckBox s;
    @Bind(R.id.radio_group)
    LineRadioGroup radioGroup;
    @Bind(R.id.set_point_money)
    EditText money;
    @Bind(R.id.set_point_name)
    EditText name;
    @Bind(R.id.set_point_price)
    EditText price;
    @Bind(R.id.set_point_start)
    TextView startText;
    @Bind(R.id.set_point_end)
    TextView endText;


    int a=0,b=0;
    private CustomDatePicker customDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_points);
        ButterKnife.bind(this);
        initView();
    }
    @Override
    public void initView() {
        super.initView();
        initDatePicker();
        Drawable drawable2 = getResources().getDrawable(R.drawable.pay_selector);
        drawable2.setBounds(0, 0, (int) (0.05 * width), (int) (0.05 * width));
        s.setCompoundDrawables(null, null, drawable2, null);
        radioGroup.setOnCheckedChangeListener(new LineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(LineRadioGroup group, int checkedId) {
               switch (checkedId){
                   case R.id.radio1:
                       a=1;
                       break;
                   case R.id.radio2:
                       a=2;
                       break;
                   case R.id.radio3:
                       a=3;
                       break;
                   case R.id.radio4:
                       a=4;
                       break;
                   case R.id.radio5:
                       a=5;
                       break;
                   case R.id.radio6:
                       a=6;
                       break;
                   case R.id.radio7:
                       a=7;
                       break;
                   case R.id.radio8:
                       a=8;
                       break;
                   case R.id.radio9:
                       a=9;
                       break;
                   case R.id.radio10:
                       a=10;
                       break;
                   default:
                           break;


               }
            }
        });
    }

    private void initDatePicker() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        startText.setText(now.subSequence(0,10));
        endText.setText(now.subSequence(0,10));
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                if(b==1) {
                    startText.setText(time.split(" ")[0]);
                }else if(b==2){
                    endText.setText(time.split(" ")[0]);
                }
            }
        }, "2010-04-13 00:00", "2088-04-13 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动
    }

    @OnClick(R.id.set_point_start)
    public void start()
    {
        b=1;
        customDatePicker.show(startText.getText().toString());
    }
    @OnClick(R.id.set_point_end)
    public void end()
    {
        b=2;
        customDatePicker.show(endText.getText().toString());
    }


    @OnClick(R.id.action_su)
    public void su()
    {
        Intent intent=new Intent(this,SetPointrActivity.class);
        intent.putExtra("Nomber",a);
        intent.putExtra("money",money.getText().toString());
        intent.putExtra("name",name.getText().toString());
        intent.putExtra("price",price.getText().toString());
        intent.putExtra("startTime",startText.getText());
        intent.putExtra("endTime",endText.getText());
        intent.putExtra("is",s.isChecked());
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("创建集点活动"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("创建集点活动"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
