package com.example.epay.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.epay.R;
import com.example.epay.adapter.DeskListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.DeskBean;
import com.example.epay.bean.DeskListBean;
import com.example.epay.bean.User;
import com.example.epay.cache.CacheData;
import com.example.epay.doHttp.HttpCallBack;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

public class DeskManageActivity extends BaseActivity {

    @Bind(R.id.desk_list_type1) TextView type1;
    @Bind(R.id.desk_list_type1_text) TextView typeText1;
    @Bind(R.id.desk_list_type2) TextView type2;
    @Bind(R.id.desk_list_type2_text) TextView typeText2;
    @Bind(R.id.desk_list_type3) TextView type3;
    @Bind(R.id.desk_list_type3_text) TextView typeText3;
    @Bind(R.id.desk_list_type4) TextView type4;
    @Bind(R.id.desk_list_type4_text) TextView typeText4;

    @Bind(R.id.desk_statue_qu) TextView qu;
    @Bind(R.id.desk_statue_xiu) LinearLayout xiu;
    @Bind(R.id.desk_statue_xuan) TextView xuan;
    @Bind(R.id.desk_search_edit) EditText editText;

    @Bind(R.id.desk_gridView) GridView gridView;
    @Bind(R.id.desk_clear) TextView clearText;
    @Bind(R.id.desk_yin) TextView yinText;

    DeskListAdapter adapter;
    ArrayList<DeskBean> list;
    String data = "";
    int deskStatus = 4;
    DeskListBean listBean;
    private boolean statue = false;
    private boolean isCreate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desk_manage);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        isCreate = false;
        list = new ArrayList<>();
        adapter = new DeskListAdapter(this, list);
        gridView.setAdapter(adapter);
        desk(type1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (xuan.getText().toString().equals("编辑")) {
                    if (list.get(i).getDeskStatus() == 0) {
                        Intent intent = new Intent(DeskManageActivity.this, OrderMealActivity.class);
                        intent.putExtra("meal", true);
                        intent.putExtra("deskNo", list.get(i).getDeskNO());
                        intent.putExtra("deskName", list.get(i).getDeskName());
                        startActivity(intent);
                    } else if (list.get(i).getDeskStatus() == 1) {
                        Intent intent = new Intent(DeskManageActivity.this, OrderPayActivity.class);
                        if (list.get(i).getPayStatus() == 0 || list.get(i).getPayStatus() == 6 || list.get(i).getPayStatus() == 7 || list.get(i).getPayStatus() == 10) {
                            intent.putExtra("confirm", 1);
                        }
                        intent.putExtra("deskName", list.get(i).getDeskName());
                        intent.putExtra("orderNo", list.get(i).getOrderNO());
                        intent.putExtra("reserve", list.get(i).getDeskStatus());
                        startActivity(intent);
                    } else {
                    }
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                xuan();

                return true;
            }
        });

        Drawable drawable = getResources().getDrawable(R.drawable.table_btn_clear);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable.setBounds(0, 0, (int) (0.05 * width), (int) (0.05 * width));
        //drawable   第一个是文字TOP
        clearText.setCompoundDrawables(null, drawable, null, null);
        Drawable drawable1 = getResources().getDrawable(R.drawable.tab_btn_yin);
        //第一是距左边距离，第二是距上边距离，第三第四分别是长宽
        drawable1.setBounds(0, 0, (int) (0.05 * width), (int) (0.05 * width));
        //drawable   第一个是文字TOP
        yinText.setCompoundDrawables(null, drawable1, null, null);

        //编辑单选
        adapter.setOnNtClickListener(position -> {
            if (list.get(position).getIndex() == -1) {
                list.get(position).setIndex(0);
            } else {
                list.get(position).setIndex(-1);
            }

            adapter.setList(list);
        });
    }

    //活动
    private void doSpecial() {
        httpUtil.HttpServer(this, data, 48, true, new HttpCallBack() {
            @Override
            public void back(String data1) {
                listBean = gson.fromJson(data1, DeskListBean.class);
                list = listBean.getItems();
                if (list != null) {
                    User userBean = CacheData.getUser(DeskManageActivity.this,
                            CacheData.getId(DeskManageActivity.this) + "");
                    userBean.setIsClose(listBean.getIsClose());
                    CacheData.cacheUser(DeskManageActivity.this, userBean,
                            CacheData.getId(DeskManageActivity.this));
                    adapter.setList(list);
                } else {
                    showMessage("没有数据");
                }
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    // 活动
    private void doClear() {
        httpUtil.HttpServer(DeskManageActivity.this, data, 49, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                desk(type1);
            }

            @Override
            public void fail(String Message, int code, String data) {
                showMessage(Message);
            }
        });
    }

    //活动
    private void doYin() {
        httpUtil.HttpServer(this, data, 52, true, new HttpCallBack() {
            @Override
            public void back(String data) {
                desk(type1);
            }

            @Override
            public void fail(String Message, int code, String data) {
                if (Message.contains("ID")) {
                    showMessage("选择桌台有打印过的桌台，请选择未打印桌台");
                } else {
                    showMessage(Message);
                }
            }
        });
    }

    @OnClick({R.id.desk_list_type1, R.id.desk_list_type2,
            R.id.desk_list_type3, R.id.desk_list_type4,
            R.id.desk_search, R.id.desk_clear,
            R.id.desk_yin, R.id.desk_reload})
    public void desk(View view) {
        if (view.getId() == R.id.desk_list_type1) {
            type1.setTextColor(getResources().getColor(R.color.appHeaderColor));
            typeText1.setVisibility(View.VISIBLE);
            type2.setTextColor(getResources().getColor(R.color.textColor_grey));
            typeText2.setVisibility(View.INVISIBLE);
            type3.setTextColor(getResources().getColor(R.color.textColor_grey));
            typeText3.setVisibility(View.INVISIBLE);
            type4.setTextColor(getResources().getColor(R.color.textColor_grey));
            typeText4.setVisibility(View.INVISIBLE);
            JSONObject object = new JSONObject();
            object.put("pageSize", 100);
            data = object.toString();
            doSpecial();
        } else if (view.getId() == R.id.desk_list_type2) {
            type2.setTextColor(getResources().getColor(R.color.desk1));
            typeText2.setVisibility(View.VISIBLE);
            type1.setTextColor(getResources().getColor(R.color.textColor_grey));
            typeText1.setVisibility(View.INVISIBLE);
            type3.setTextColor(getResources().getColor(R.color.textColor_grey));
            typeText3.setVisibility(View.INVISIBLE);
            type4.setTextColor(getResources().getColor(R.color.textColor_grey));
            typeText4.setVisibility(View.INVISIBLE);
            deskStatus = 1;
            JSONObject object = new JSONObject();
            object.put("pageSize", 100);
            object.put("deskStatus", 1);
            data = object.toString();
            doSpecial();
        } else if (view.getId() == R.id.desk_list_type3) {
            type3.setTextColor(getResources().getColor(R.color.desk2));
            typeText3.setVisibility(View.VISIBLE);
            type2.setTextColor(getResources().getColor(R.color.textColor_grey));
            typeText2.setVisibility(View.INVISIBLE);
            type1.setTextColor(getResources().getColor(R.color.textColor_grey));
            typeText1.setVisibility(View.INVISIBLE);
            type4.setTextColor(getResources().getColor(R.color.textColor_grey));
            typeText4.setVisibility(View.INVISIBLE);
            deskStatus = 0;
            JSONObject object = new JSONObject();
            object.put("pageSize", 100);
            object.put("deskStatus", 0);
            data = object.toString();
            doSpecial();
        } else if (view.getId() == R.id.desk_list_type4) {
            type4.setTextColor(getResources().getColor(R.color.desk3));
            typeText4.setVisibility(View.VISIBLE);
            type2.setTextColor(getResources().getColor(R.color.textColor_grey));
            typeText2.setVisibility(View.INVISIBLE);
            type3.setTextColor(getResources().getColor(R.color.textColor_grey));
            typeText3.setVisibility(View.INVISIBLE);
            type1.setTextColor(getResources().getColor(R.color.textColor_grey));
            typeText1.setVisibility(View.INVISIBLE);
            JSONObject object = new JSONObject();
            object.put("pageSize", 100);
            object.put("deskStatus", 2);
            data = object.toString();
            doSpecial();
        } else if (view.getId() == R.id.desk_search) {
            JSONObject object = new JSONObject();
            object.put("pageSize", 100);
            if (deskStatus != 4) {
                object.put("deskStatus", deskStatus);
            }
            object.put("search", editText.getText().toString());
            data = object.toString();
            doSpecial();
        } else if (view.getId() == R.id.desk_clear) {

            AlertDialog.Builder builder = new AlertDialog.Builder(DeskManageActivity.this);
            builder.setMessage("确定清理所选桌台");
            builder.setPositiveButton("确定", (dialogInterface, i1) -> {
                dialogInterface.dismiss();

                JSONObject object = new JSONObject();

                int[] intTemp;
                String[] NOTemp;
                if (xuan.getText().equals("全选")) {
                    ArrayList<String> list = adapter.getlist();
                    ArrayList<Boolean> trueList = adapter.getTrueList();
                    ArrayList<String> NOlist = adapter.getNOlist();

                    intTemp = new int[list.size()];
                    NOTemp = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) != "") {
                            if (trueList.get(i)) {
                                intTemp[i] = Integer.parseInt(list.get(i));
                                NOTemp[i] = NOlist.get(i);
                            } else {
                                intTemp[i] = 0;
                                NOTemp[i] = "";
                            }
                        }
                    }
                } else {
                    intTemp = new int[list.size()];
                    NOTemp = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getOrderID().equals("")) {
                            intTemp[i] = 0;
                            NOTemp[i] = "";
                        } else {
                            if (list.get(i).getDeskStatus() == 1 && list.get(i).getPayStatus() == 1) {
                                intTemp[i] = Integer.parseInt(list.get(i).getOrderID());
                                NOTemp[i] = list.get(i).getDeskNO();
                            } else {
                                intTemp[i] = 0;
                                NOTemp[i] = "";
                            }
                        }
                    }
                }
                object.put("IDs", intTemp);
                object.put("deskNOs", NOTemp);
                data = object.toString();
                doClear();
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
            builder.show();


        } else if (view.getId() == R.id.desk_yin) {
            if (listBean.getIsClose() == 0) {
                JSONObject object = new JSONObject();
                int[] intTemp;
                if (xuan.getText().equals("全选")) {
                    ArrayList<String> list = adapter.getlist();
                    intTemp = new int[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) != "") {
                            intTemp[i] = Integer.parseInt(list.get(i));
                            //  object.put("ID", Integer.parseInt(list.get(i)));
                        }
                    }
                } else {
                    intTemp = new int[list.size()];
                    for (int i = 0; i < list.size(); i++) {

                        if (list.get(i).getOrderID().equals("")) {
                            intTemp[i] = 0;
                        } else {
                            intTemp[i] = Integer.parseInt(list.get(i).getOrderID());
                        }
                    }
                }
                object.put("IDs", intTemp);
                data = object.toString();
                doYin();
            }
        } else if (view.getId() == R.id.desk_reload) {
            doSpecial();
        }
    }

    @OnClick(R.id.desk_statue_xuan)
    public void xuan() {
        if (!statue) {
            xuan.setText("全选");
            qu.setVisibility(View.VISIBLE);
            xiu.setVisibility(View.VISIBLE);
            statue = true;
            adapter.setBoolean(1);
            adapter.notifyDataSetChanged();
        } else {
            if (xuan.getText().toString().equals("全选")) {

                xuan.setText("全不选");
                adapter.setBoolean(1);
                for(int i=0;i<list.size();i++){
                    list.get(i).setIndex(1);
                }
                adapter.notifyDataSetChanged();
            } else {
                xuan.setText("全选");
                adapter.setBoolean(1);
                for(int i=0;i<list.size();i++){
                    list.get(i).setIndex(-1);
                }

                adapter.setList(list);
            }
        }
    }

    @OnClick(R.id.desk_statue_qu)
    public void qu() {
        qu.setVisibility(View.GONE);
        xiu.setVisibility(View.GONE);
        statue = false;
        xuan.setText("编辑");
        adapter.setBoolean(0);
        adapter.notifyDataSetChanged();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("餐桌管理"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("餐桌管理"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isCreate) {
            doSpecial();
        } else {
            isCreate = true;
        }
    }

    @OnClick(R.id.administration_Return_img)
    public void onViewClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
