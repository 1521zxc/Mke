package com.example.epay.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;
import com.example.epay.view.CustomDatePicker;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class UpEnvelopeNameActivity extends BaseActivity {
    @Bind(R.id.envelope_start)
    TextView startText;
    @Bind(R.id.envelope_end)
    TextView endText;
    @Bind(R.id.envelope_name)
    EditText name;
    @Bind(R.id.envelope_s0)
    RadioButton s0;
    @Bind(R.id.envelope_s1)
    RadioButton s1;

    int b=0;
    private CustomDatePicker customDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_envelope_name);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        initDatePicker();
        Drawable drawable = getResources().getDrawable(R.drawable.dot_selector);
        drawable.setBounds(0, 0, (int) (0.05 * width), (int) (0.05 * width));
        Drawable drawable2 = getResources().getDrawable(R.drawable.dot_selector);
        drawable2.setBounds(0, 0, (int) (0.05 * width), (int) (0.05 * width));
        s0.setCompoundDrawables(null, null, drawable, null);
        s1.setCompoundDrawables(null, null, drawable2, null);
        s0.setChecked(true);
        s0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    s0.setChecked(true);
                    s1.setChecked(false);
                }
            }
        });
        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    s1.setChecked(true);
                    s0.setChecked(false);
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

    @OnClick(R.id.envelope_start)
    public void start()
    {
        b=1;
        customDatePicker.show(startText.getText().toString());
    }
    @OnClick(R.id.envelope_end)
    public void end()
    {
        b=2;
        customDatePicker.show(endText.getText().toString());
    }
    @OnClick(R.id.envelope_name_up)
    public void up()
    {
        Intent intent=new Intent(this,UpEnvelopeContActivity.class);
        intent.putExtra("name",name.getText().toString());
        intent.putExtra("start",startText.getText().toString());
        intent.putExtra("end",endText.getText().toString());
        intent.putExtra("type",getIntent().getStringExtra("type"));
        intent.putExtra("type1",s0.isChecked());
        startActivity(intent);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("创建会员红包"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("创建会员红包"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
