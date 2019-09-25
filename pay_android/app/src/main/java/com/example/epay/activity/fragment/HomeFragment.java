package com.example.epay.activity.fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.example.epay.R;
import com.example.epay.activity.CashflowActivity;
import com.example.epay.activity.DeskManageActivity;
import com.example.epay.activity.KeyBoardActivity;
import com.example.epay.activity.MemberEnvelopesActivity;
import com.example.epay.activity.MemberStoredActivity;
import com.example.epay.activity.MemberStoredValueActivity;
import com.example.epay.activity.MembersActivity;
import com.example.epay.activity.MembershipListActivity;
import com.example.epay.activity.OrderListActivity;
import com.example.epay.activity.RoyaltyActivity;
import com.example.epay.activity.SetPointActivity;
import com.example.epay.activity.SpecialActivity;
import com.example.epay.activity.StatisticActivity;
import com.example.epay.adapter.HomeListAdapter;
import com.example.epay.base.BaseFragment;
import com.example.epay.bean.HomeListBean;
import com.example.epay.bean.User;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liujin on 2018/1/18.
 */
public class HomeFragment extends BaseFragment {
    @Bind(R.id.type0)
    Button type0;
    @Bind(R.id.type1)
    Button type1;
    @Bind(R.id.type_layout)
    LinearLayout typeLayout;

    @Bind(R.id.home_gridView)
    GridView gridView;
    HomeListAdapter adapter;
    ArrayList<HomeListBean.Deta> list = new ArrayList<>();
    User userBean;
    HomeListBean homeBean;

    @Override
    public int initViewId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        userBean = CacheData.getUser(getActivity(), CacheData.getId(getActivity()) + "");
        adapter = new HomeListAdapter(getActivity(), list);
        gridView.setAdapter(adapter);
        changeDrawable();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.get(i).getAction().equals("100")) {//会员管理
                  startActivity(new Intent(getContext(), MembershipListActivity.class));
                } else if (list.get(i).getAction().equals("101")) {//会员集点
                  startActivity(new Intent(getContext(), SetPointActivity.class));
                } else if (list.get(i).getAction().equals("102")) {//会员红包
                  startActivity(new Intent(getContext(), MemberEnvelopesActivity.class));
                } else if (list.get(i).getAction().equals("103")) {//会员储值
                   startActivity(new Intent(getContext(), MemberStoredValueActivity.class));
                } else if (list.get(i).getAction().equals("104")) {//点餐
                   startActivity(new Intent(getContext(), DeskManageActivity.class));
                } else if (list.get(i).getAction().equals("105")) {//官方活动
                   startActivity(new Intent(getContext(), SpecialActivity.class));
                   // getContext().startActivity(new Intent(getContext(), .class));
                } else if (list.get(i).getAction().equals("106")) {//数据统计
                   startActivity(new Intent(getContext(), StatisticActivity.class));
                } else if (list.get(i).getAction().equals("107")) {//服务员提成
                    startActivity(new Intent(getContext(), RoyaltyActivity.class));
                }else if (list.get(i).getAction().equals("108")) {//利润
                    startActivity(new Intent(getContext(), RoyaltyActivity.class));
                }else if (list.get(i).getAction().equals("109")) {//订单列表
                    startActivity(new Intent(getContext(), OrderListActivity.class));
                }else {
                    toast("努力开发中，敬请期待");
                }
            }
        });
        doHome();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this,rootView);
        initView();
        return rootView;
    }

    @OnClick(R.id.home_linear)
    public void Click(View view) {
        if (CacheData.getUser(getActivity(),
                CacheData.getId(getActivity()) + "").getOnlinePay() == 1 && CacheData.getUser(getActivity(),
                CacheData.getId(getActivity()) + "").getUserIsPay() == 1) {
            getContext().startActivity(new Intent(getContext(), KeyBoardActivity.class));
        } else {
            toast("此账号不可收款");
        }
    }

    @OnClick(R.id.home_pay)
    public void payClick(View view) {
        if (CacheData.getUser(getActivity(), CacheData.getId(getActivity()) + "").getOnlinePay() == 1 && CacheData.getUser(getActivity(),
                CacheData.getId(getActivity()) + "").getUserIsPay() == 1) {
            getContext().startActivity(new Intent(getContext(), KeyBoardActivity.class));
        } else {
            toast("此账号不可收款");
        }
    }

    @OnClick(R.id.type0)
    public void typeClick(View view) {
        getContext().startActivity(new Intent(getContext(), CashflowActivity.class));
    }

    @OnClick(R.id.type1)
    public void typeClick1(View view) {
        toast("努力开发中，敬请期待");
        // getContext().startActivity(new Intent(getContext(),TransferActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页");
    }

    public void changeDrawable() {
        Drawable drawable = getResources().getDrawable(R.drawable.lookflow);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable.setBounds(0, 0, (int) (0.15 * width), (int) (0.15 * width));
        //drawable   第一个是文字TOP
        type0.setCompoundDrawables(drawable, null, null, null);
        Drawable drawable1 = getResources().getDrawable(R.drawable.transfer);
        drawable1.setBounds(0, 0, (int) (0.15 * width), (int) (0.15 * width));
        type1.setCompoundDrawables(drawable1, null, null, null);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        initView();
    }

    //首页
    private void doHome() {
        userBean = CacheData.getUser(getActivity(), CacheData.getId(getActivity()) + "");
        httpUtil.HttpServer(getActivity(), "", 1, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                homeBean = gson.fromJson(data, HomeListBean.class);
                CacheData.cacheHomeBeans(getActivity(), homeBean);
                if (homeBean != null) {
                    list = homeBean.getActionCata();
                    typeLayout.setVisibility(View.VISIBLE);
                    if (userBean.getRoleType() == 3 || userBean.getRoleType() == 4 || userBean.getRoleType() == 5) {
                        typeLayout.setVisibility(View.GONE);
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getAction().equals("104")) {
                                HomeListBean.Deta action = list.get(i);
                                list.clear();
                                list.add(action);
                            }
                        }
                    } else if (userBean.getRoleType() == 6) {
                        list.clear();
                    }
//                    HomeListBean.Deta data107=new HomeListBean.Deta();
//                    data107.setAction("107");
//                    data107.setText("提成统计");
//                    data107.setIconURL("https://file.jqepay.com/temp123456.png");//
//                    list.add(data107);
//                    HomeListBean.Deta data108=new HomeListBean.Deta();
//                    data108.setAction("108");
//                    data108.setText("利润");
//                    data108.setIconURL("https://file.jqepay.com/temp123456.png");//
//                    list.add(data108);
                    HomeListBean.Deta data109=new HomeListBean.Deta();
                    data109.setAction("109");
                    data109.setText("订单列表");
                    data109.setIconURL("https://file.jqepay.com/temp123456.png");//
                    list.add(data109);
                    adapter.setList(list);
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
}
