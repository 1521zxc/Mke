package com.example.epay.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.epay.R;
import com.example.epay.activity.WebActivity;
import com.example.epay.adapter.MessageListAdapter;
import com.example.epay.base.BaseFragment;
import com.example.epay.bean.MessageBean;
import com.example.epay.bean.MessageListBean;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by liujin on 2018/1/18.
 */

public class MessageFragment extends BaseFragment {
    @Bind(R.id.sysListView)
    ListView sysListView;
    @Bind(R.id.dataListView)
    ListView dataListView;
    @Bind(R.id.sys)
    RadioButton sysButton;
    @Bind(R.id.data)
    RadioButton dataButton;
    @Bind(R.id.pro)
    TextView pro;

    MessageListAdapter sysAdapter;
    ArrayList<MessageBean> sysList;
    TranslateAnimation animation;
    MessageListBean messageListBean;

    @Override
    public int initViewId() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView() {
        sysList = new ArrayList<MessageBean>();
        sysAdapter = new MessageListAdapter(getActivity(), sysList);
        sysListView.setAdapter(sysAdapter);

        pro.setWidth(width / 2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / 2, (int) (width / 120));
        pro.setLayoutParams(params);

        sysListView.setOnItemClickListener(this::onItemClick);
        tabClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        initView();
        doMessage();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.sys)
    public void tabClick() {
        anima(width / 2, 0, 0, 0);
        checked(sysButton);
        pro.startAnimation(animation);
    }

    @OnClick(R.id.data)
    public void tabClick2() {
        anima(0, width / 2, 0, 0);
        pro.startAnimation(animation);
        checked(dataButton);
    }

    private void checked(View view) {
        sysButton.setChecked(view.getId() == sysButton.getId());
        dataButton.setChecked(view.getId() == dataButton.getId());
        if (view.getId() == sysButton.getId()) {
            sysList = CacheData.getMessageBeans(getContext());
        } else if (view.getId() == dataButton.getId()) {
            sysList.clear();
        }
        sysAdapter.setList(sysList);
    }

    //消息
    private void doMessage() {
        httpUtil.HttpServer(getActivity(), "", 60, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                messageListBean = gson.fromJson(data, MessageListBean.class);
                if (messageListBean != null) {
                    CacheData.cacheMessageBeans(getActivity(), messageListBean.getItems());
                } else {
                    toast("没有数据");
                }
            }

            @Override
            public void fail(String Message, int code, String data) {
                toast(Message);
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("消息"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("消息");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        doMessage();
    }

    public void anima(float x1, float x2, float y1, float y2) {
        animation = new TranslateAnimation(x1, x2, y1, y2);
        animation.setDuration(300);//设置动画持续时间
        animation.setFillAfter(true);
    }

    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("title", "消息详情");
        intent.putExtra("url", sysList.get(i).getLink());
        startActivity(intent);
    }
}
