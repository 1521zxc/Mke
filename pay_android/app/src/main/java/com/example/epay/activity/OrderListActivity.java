package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.example.epay.R;
import com.example.epay.adapter.OrderManageAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.base.BaseRecyclerAdapter;
import com.example.epay.bean.OrderInfoBean;
import com.example.epay.bean.OrderListBean;
import com.example.epay.util.RecyclerViewCallBack;
import com.example.epay.util.RecyclerViewUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderListActivity extends BaseActivity {
    @Bind(R.id.order_manage_listView)
    IRecyclerView listView;
    @Bind(R.id.search_edit)
    EditText search;
    @Bind(R.id.code_title)
    TextView title;

    ArrayList<OrderInfoBean> list;
    OrderManageAdapter adapter;
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    private RecyclerViewUtil<OrderInfoBean> RecyclerUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        initView();
        doOrderType1();
    }


    @Override
    public void initView() {
        super.initView();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                hashMap = new HashMap<>();
                hashMap.put("pageSize", 100);
                hashMap.put("totalCount", 1);
                hashMap.put("search", search.getText().toString());
                doOrderType1();
            }
        });
    }

    //初始化RecyclerView
    public void doOrderType1() {
        list=new ArrayList<>();
        adapter = new OrderManageAdapter(list, this, R.layout.item_order_manage_tyle1);
        RecyclerViewCallBack callBack = new RecyclerViewCallBack() {
            @Override
            public void getData(String data, int type) {
                if (type == 1) {
                    list.clear();
                } else {
                    ArrayList<OrderInfoBean> lists = gson.fromJson(data, OrderListBean.class).getItems();
                    list.addAll(lists);
                    if (lists.size() == 0) {
                        showMessage("没有更多数据了");
                    }
                }
                Message message = new Message();
                message.what = type;
                message.obj = list;
                RecyclerUtil.handler.sendMessage(message);
            }
        };
        RecyclerUtil = new RecyclerViewUtil<OrderInfoBean>(this, listView, adapter, list, callBack, 90, 0, hashMap, false, true);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                Intent intent=new Intent(OrderListActivity.this,OrderPayActivity.class);
                if(list.get(position-2).getPayStatus()==0||list.get(position-2).getPayStatus()==6||list.get(position-2).getPayStatus()==7||list.get(position-2).getPayStatus()==10)
                {
                    intent.putExtra("confirm",1);
                }
                intent.putExtra("isOrderList", true);
                intent.putExtra("deskName", list.get(position-2).getDeskName());
                intent.putExtra("orderNo", list.get(position-2).getOrderNO());
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.order_meal_reach)
    public void sreach()
    {
        search.setVisibility(View.VISIBLE);
        title.setVisibility(View.GONE);
    }

    @Override
    public void back(View view) {
        if(search.getVisibility()==View.VISIBLE)
        {
            search.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            hashMap=new HashMap<>();
            doOrderType1();
        }else {
            super.back(view);
        }
    }
}
