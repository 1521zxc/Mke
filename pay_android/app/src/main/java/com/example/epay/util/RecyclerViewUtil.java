package com.example.epay.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.example.epay.R;
import com.example.epay.base.BaseRecyclerAdapter;
import com.example.epay.doHttp.HttpCallBack;
import com.example.epay.doHttp.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;


public class RecyclerViewUtil<T> {
    IRecyclerView iRecyclerView;
    ArrayList<T> mList;
    BaseRecyclerAdapter adapter;
    Activity context;
    View view;
    int action;
    String data;
    RecyclerViewCallBack callBack;
    int type = 1;
    int layoutType = 0;
    int pageNO = -1;
    boolean isLoca;
    boolean isinit;
    HashMap<String, Object> hashMap;
    HttpUtil httpUtil = new HttpUtil();
    //声明记录停止滚动时候，可见的位置
    private int stop_position;

    public RecyclerViewUtil() {

    }

    public RecyclerViewUtil(Activity context, IRecyclerView iRecyclerView, BaseRecyclerAdapter adapter, ArrayList<T> mList, RecyclerViewCallBack callBack, int action, int layoutType, HashMap<String, Object> hashMap, boolean isLoca, boolean isInit) {
        this.iRecyclerView = iRecyclerView;
        this.mList = mList;
        this.adapter = adapter;
        this.context = context;
        this.action = action;
        this.callBack = callBack;
        this.layoutType = layoutType;
        this.hashMap = hashMap;
        this.isLoca = isLoca;
        this.isinit = isInit;
        initView();
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mList = (ArrayList<T>) msg.obj;
                    if (mList == null) {
                        mList = new ArrayList<T>();
                    }
                    adapter.setList(mList);
//                    iRecyclerView.setRefreshing(false);
                    break;
                case 2:
                    mList = (ArrayList<T>) msg.obj;
                    if (mList == null) {
                        mList = new ArrayList<T>();
                    }
                    adapter.setList(mList);
                    view.setVisibility(View.GONE);
                    break;
            }
        }
    };


    private void initView() {
        if (layoutType == 0) {
            iRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else if (layoutType == 1) {
            iRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        } else if (layoutType == 2) {
            final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);//定义瀑布流管理器，第一个参数是列数，第二个是方向。
            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//不设置的话，图片闪烁错位，有可能有整列错位的情况。
            iRecyclerView.setLayoutManager(layoutManager);//设置瀑布流管理器
        }
        iRecyclerView.setIAdapter(adapter);
        iRecyclerView.setLoadMoreFooterView(R.layout.load_layout);
        view = iRecyclerView.getLoadMoreFooterView();
        if (isinit) {
            addData();
        } else {
            pageNO++;
        }


        iRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                iRecyclerView.setRefreshing(true);
                refreshData(hashMap);
            }
        });
        iRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                view.setVisibility(View.VISIBLE);
                addData();
            }
        });
    }

    private void addData() {
            pageNO++;
            type = 2;
            dotext();

    }

    public void refreshData(HashMap hashMap) {

            pageNO = 0;
            type = 1;
            this.hashMap = hashMap;
            dotext();

    }


    private void dotext() {
        JSONObject jsonObject = new JSONObject();
        for (String key : hashMap.keySet()) {
            jsonObject.put(key, hashMap.get(key));
        }
        jsonObject.put("pageNO", pageNO);
        httpUtil.HttpServer(context, JSON.toJSONString(jsonObject), action, false, new HttpCallBack() {
            @Override
            public void back(String data) {
                if (type == 1) {
                    iRecyclerView.setRefreshing(false);
                }
                callBack.getData(data, type);
            }

            @Override
            public void fail(String Message,int code,String data) {
                Toast.makeText(context,Message,Toast.LENGTH_LONG).show();
                if (type == 1) {
                    pageNO = 0;
                    iRecyclerView.setRefreshing(false);
                } else if (type == 2) {
                    pageNO--;
                    view.setVisibility(View.GONE);
                }

            }
        });

    }


}
