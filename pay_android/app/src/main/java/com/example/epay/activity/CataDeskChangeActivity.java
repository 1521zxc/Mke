package com.example.epay.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.epay.R;
import com.example.epay.adapter.CataDeskHAdapter;
import com.example.epay.adapter.CataMenuAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.OrderInfoBean;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Bind;

public class CataDeskChangeActivity extends BaseActivity {
    @Bind(R.id.cata_h_listView)
    ListView ListView;
    @Bind(R.id.cata_h_listView2)
    ListView hListView;
    @Bind(R.id.cata_deskNO)
    TextView deskNO;
    @Bind(R.id.cata_h_deskNO)
    TextView deskHNO;

    OrderInfoBean listBean;
    CataDeskHAdapter adapter;
    ArrayList<OrderInfoBean.ProductSimple> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cata_desk_change);
        ButterKnife.bind(this);
        initView();

    }

    @Override
    public void initView() {
        super.initView();
        listBean=(OrderInfoBean)getIntent().getSerializableExtra("bean");
        deskNO.setText(listBean.getDeskName());
        list.addAll(listBean.getAttached());
        adapter=new CataDeskHAdapter(this,list);
        ListView.setAdapter(adapter);
    }
}
