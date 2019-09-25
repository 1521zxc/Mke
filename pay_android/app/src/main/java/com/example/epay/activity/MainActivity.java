package com.example.epay.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.epay.BaseApplication;
import com.example.epay.R;
import com.example.epay.activity.fragment.HomeFragment;
import com.example.epay.activity.fragment.MeFragment;
import com.example.epay.activity.fragment.MessageFragment;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.UserBean;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.util.ImageViewLoader;
import com.google.android.material.navigation.NavigationView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.media.Base;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.epay.BaseApplication.getContext;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.main_fragment)
    FrameLayout mainFragment;
    @Bind(R.id.tab01)
    RadioButton tabBut01;
    @Bind(R.id.tab02)
    RadioButton tabBut02;
    @Bind(R.id.tab03)
    RadioButton tabBut03;
    @Bind(R.id.nav_main)
    NavigationView navMain;
    @Bind(R.id.draw_layout)
    DrawerLayout drawLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;


    private MeFragment tab03;
    private HomeFragment tab01;
    private MessageFragment tab02;
    private Fragment curremtFragment;
    private UserBean userBean;
    private TextView name;
    private TextView phone;
    private ImageView h_bg;
    private ImageView h_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        init();
        h_icon = navMain.getHeaderView(0).findViewById(R.id.head_icon);
        h_bg = navMain.getHeaderView(0).findViewById(R.id.head_iconbg);
        name = navMain.getHeaderView(0).findViewById(R.id.head_name);
        phone = navMain.getHeaderView(0).findViewById(R.id.head_acount);
        doMy();
        h_bg.setOnClickListener(this);
    }

    private void doMy() {
        httpUtil.HttpServer(this, "", 41, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                userBean = gson.fromJson(data, UserBean.class);
                if (userBean != null && getContext() != null) {
                    ImageViewLoader.loadCircle(userBean.getIconURL(), h_icon, 0);
                    Glide.with(getContext()).load(userBean.getCoverURL()).into(h_bg);
                    name.setText(userBean.getBrandName());
                    phone.setText(userBean.getMobile());
                } else {

                    BaseApplication.show("没有数据");
                }
            }

            @Override
            public void fail(String Message, int code, String data) {
                BaseApplication.show(Message);

            }
        });
    }

    private void init() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        navMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                String string = null;
                switch (id) {
                    case R.id.item_1:
                        startActivity(new Intent(MainActivity.this, AccoutActivity.class));
                        string = "我的账户信息";
                        break;
                    case R.id.item_2:
                        string = "店铺签约信息,正在开发中，敬请期待";

                        break;
                    case R.id.item_3:
                        startActivity(new Intent(MainActivity.this, StoreCodeActivity.class));
                        string = "店铺收款码";
                        break;
                    case R.id.item_4:
                        startActivity(new Intent(MainActivity.this, OrderCodeActivity.class));
                        string = "点餐码";
                        break;
                    case R.id.item_5:
//                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        string = "发票打印码,正在开发中，敬请期待";
                        break;
                    case R.id.item_6:
//                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        string = "我的银行卡,正在开发中，敬请期待";
                        break;
                    case R.id.item_7:
                        startActivity(new Intent(MainActivity.this, MyServiceActivity.class));
                        string = "我的服务";
                        break;
                    case R.id.item_8:
                        startActivity(new Intent(MainActivity.this, RecomActivity.class));
                        string = "推荐给好友";
                        break;
                    case R.id.item_9:
                        Intent localIntent = new Intent("android.intent.action.CALL");
                        localIntent.setData(Uri.parse("tel:15201365138"));
                        if (ActivityCompat.checkSelfPermission(getContext(), "android.permission.CALL_PHONE") != 0) {
                            if (Build.VERSION.SDK_INT >= 23)
                                requestPermissions(new String[]{"android.permission.CALL_PHONE"}, 111);
                        }
                        startActivity(localIntent);
                        string = "联系客服";
                        break;
                    case R.id.item_10:
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        string = "设置";
                        break;
                    case R.id.item_11:
                        showExitDialog();
                        string = "退出";
                        break;
                }
                if (!TextUtils.isEmpty(string))
                    BaseApplication.show("正在进入" + "\"" + string + "\"");
                drawLayout.closeDrawers();
                return true;
            }
        });
    }
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定退出吗");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }
    @Override
    public void initView() {
        super.initView();
        tabClick(tabBut01);
    }

    @OnClick(R.id.tab01)
    public void tabClick(View view) {
        if (tab01 == null)
            tab01 = new HomeFragment();
        if (curremtFragment != tab01) {
            replace(tab01);
            checked(tabBut01);
        }
    }

    @OnClick(R.id.tab02)
    public void tabClick2(View view) {
        if (tab02 == null)
            tab02 = new MessageFragment();
        if (curremtFragment != tab02) {
            replace(tab02);
            checked(tabBut02);
        }
    }

    @OnClick(R.id.tab03)
    public void tabClick3(View view) {
        if (tab03 == null)
            tab03 = new MeFragment();
        if (curremtFragment != tab03) {
            replace(tab03);
            checked(tabBut03);
        }
    }

    private void replace(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        if (curremtFragment != null)
            ft.hide(curremtFragment);
        if (fragment.isAdded()) {
            ft.show(fragment);
        } else {
            ft.add(R.id.main_fragment, fragment);
        }
        ft.commitAllowingStateLoss();
        curremtFragment = fragment;
    }

    private void checked(View view) {
        tabBut01.setChecked(view.getId() == tabBut01.getId());
        tabBut02.setChecked(view.getId() == tabBut02.getId());
        tabBut03.setChecked(view.getId() == tabBut03.getId());
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("主页"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); // 统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("主页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        if (drawLayout.isDrawerOpen(GravityCompat.START)) {    /*打开或关闭左边的菜单*/
            drawLayout.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressedSupport();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.head_iconbg) {    /*点击头像跳转登录界面*/
            Intent intent = new Intent(this, AccoutActivity.class);
            startActivity(intent);
        }
        drawLayout.closeDrawer(GravityCompat.START);
    }
}