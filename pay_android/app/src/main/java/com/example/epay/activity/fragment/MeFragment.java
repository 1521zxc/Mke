package com.example.epay.activity.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.epay.R;
import com.example.epay.activity.AccoutActivity;
import com.example.epay.activity.MyServiceActivity;
import com.example.epay.activity.OrderCodeActivity;
import com.example.epay.activity.RecomActivity;
import com.example.epay.activity.SettingActivity;
import com.example.epay.activity.StoreCodeActivity;
import com.example.epay.base.BaseFragment;
import com.example.epay.bean.UserBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.ImageViewLoader;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liujin on 2018/1/18.
 */

public class MeFragment extends BaseFragment {
//
//    @Bind(R.id.epa_my_iconbg)
//    ImageView bg;
//    @Bind(R.id.me_icon)
//    ImageView icon;
//    @Bind(R.id.me_name)
//    TextView name;
//    @Bind(R.id.me_acount)
//    TextView phone;
//    UserBean userBean;
//
    @Override
    public int initViewId() {
        return R.layout.fragment_me;
    }

    @Override
     public void initView() {
//        doMy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

//        ButterKnife.bind(this,rootView);
        return rootView;
    }
//
//    @OnClick(R.id.me_head)
//    public void head(View view) {
//        startActivity(AccoutActivity.class);
//    }
//
//    @OnClick(R.id.epa_accout)
//    public void accout(View view) {
//        startActivity(AccoutActivity.class);
//    }
//
//    @OnClick(R.id.epa_cotract)
//    public void cotract() {
//        toast("正在开发中，敬请期待");
//        //startActivity(CotractActivity.class);
//    }
//
//    @OnClick(R.id.epa_store_code)
//    public void StoreCode() {
//        startActivity(StoreCodeActivity.class);
//    }
//
//    @OnClick(R.id.epa_order_code)
//    public void OrderCode() {
//        startActivity(OrderCodeActivity.class);
//    }
//
//    @OnClick(R.id.epa_invoice)
//    public void Invoice() {
//        toast("正在开发中，敬请期待");
//        //startActivity(InvoiceActivity.class);
//    }
//
//    @OnClick(R.id.epa_cashier)
//    public void Cashier() {
//        //startActivity(CashierActivity.class);
//    }
//
//    @OnClick(R.id.epa_bank)
//    public void bank() {
//        toast("正在开发中，敬请期待");
//        // startActivity(BankActivity.class);
//    }
//
//    @OnClick(R.id.epa_my_service)
//    public void MyService() {
//        startActivity(MyServiceActivity.class);
//    }
//
//    @OnClick(R.id.epa_recom)
//    public void Recom() {
//        startActivity(RecomActivity.class);
//    }
//
//    @OnClick(R.id.epa_service)
//    public void Service() {
//        Intent localIntent = new Intent("android.intent.action.CALL");
//        localIntent.setData(Uri.parse("tel:15201365138"));
//        if (ActivityCompat.checkSelfPermission(getContext(), "android.permission.CALL_PHONE") != 0)
//        {
//            if (Build.VERSION.SDK_INT >= 23) requestPermissions(new String[] { "android.permission.CALL_PHONE" }, 111);
//            return;
//        }
//        startActivity(localIntent);
//    }
//
//    //个人
//    private void doMy() {
//        httpUtil.HttpServer(getActivity(), "", 41, true, new HttpCallBack() {
//            @Override
//            public void back(String data) {
//                userBean = gson.fromJson(data, UserBean.class);
//                if (userBean != null && getContext() != null) {
//                    ImageViewLoader.loadCircle(userBean.getIconURL(), icon, 0);
//                    Glide.with(getContext()).load(userBean.getCoverURL()).into(bg);
//                    name.setText(userBean.getBrandName());
//                    phone.setText(userBean.getMobile());
//                } else {
//                    toast("没有数据");
//                }
//            }
//            @Override
//            public void fail(String Message, int code, String data) {
//                toast(Message);
//            }
//        });
//    }
//
//    @OnClick(R.id.epa_setting)
//    public void Setting() {
//        startActivity(SettingActivity.class);
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        doMy();
//    }

    public void onResume() {
        super.onResume();
        initView();
        MobclickAgent.onPageStart("个人主页"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人主页");
    }
}
