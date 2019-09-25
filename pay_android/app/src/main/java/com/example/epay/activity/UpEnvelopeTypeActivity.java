package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.adapter.UpEnvelopePageAdapter;
import com.example.epay.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;

public class UpEnvelopeTypeActivity extends BaseActivity {
    @Bind(R.id.envelope_viewpager)
    ViewPager viewPager;
    @Bind(R.id.envelope_ll)
    LinearLayout layout;

    //底部小店图片
    private ImageView[] dots;
    ArrayList<View> list;
    UpEnvelopePageAdapter adapter;
    //记录当前选中位置
    private int currentIndex;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_envelope_type);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
         intent=new Intent(UpEnvelopeTypeActivity.this,UpEnvelopeNameActivity.class);
        list=new ArrayList<View>();
        initList();
        adapter=new UpEnvelopePageAdapter(list);
        initDots();
        viewPager.setAdapter(adapter);
    }
    private void initList() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.item_page_view1, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.page_ll);// 加载布局
        // main.xml中的ImageView
        layout.setBackground(getResources().getDrawable(R.drawable.page_icon2));
        TextView name = (TextView) v.findViewById(R.id.page_type_name);
        TextView cont = (TextView) v.findViewById(R.id.page_type_cont);
        TextView up = (TextView) v.findViewById(R.id.page_type_up);
        name.setText("创建消费分享领红包");
        cont.setText("会员消费后，可将红包分享到微信群，帮你拉更多会员，带来更多消费，大幅提升店铺的客流量");
        View v2 = inflater.inflate(R.layout.item_page_view1, null);// 得到加载view
        LinearLayout layout2 = (LinearLayout) v2.findViewById(R.id.page_ll);// 加载布局
        layout2.setBackground(getResources().getDrawable(R.drawable.page_icon1));
        // main.xml中的ImageView
        TextView name2 = (TextView) v2.findViewById(R.id.page_type_name);
        TextView cont2 = (TextView) v2.findViewById(R.id.page_type_cont);
        TextView up2 = (TextView) v2.findViewById(R.id.page_type_up);
        name2.setText("创建消费返红包");
        cont2.setText("消费返红包活动，在会员消费后，立刻获得红包，下次微信支付时使用。大大提升会员再次到店消费的意愿。");

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type",0);
                startActivity(intent);
            }
        });
        up2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });
        list.add(v);
        list.add(v2);
    }
    private void initDots() {
        layout.removeAllViews();
        dots = new ImageView[list.size()];
        //循环取得小点图片
        for (int i = 0; i < list.size(); i++) {
            LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(15, 15);
            imgvwDimens.setMargins(0, 0, 8, 0);
            ImageView img = new ImageView(this);
            img.setBackgroundResource(R.drawable.dot_selector);
            img.setLayoutParams(imgvwDimens);
            dots[i] = img;
            dots[i].setEnabled(false);//都设为灰色
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
            layout.addView(img);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(true);//设置，即选中状态
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("会员红包类型"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("会员红包类型"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
