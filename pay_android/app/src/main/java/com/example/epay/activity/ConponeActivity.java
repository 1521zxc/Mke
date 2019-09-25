package com.example.epay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.epay.R;
import com.example.epay.adapter.DisCountListAdapter;
import com.example.epay.base.BaseActivity;
import com.example.epay.bean.OrderPayTypeBean;
import com.example.epay.bean.conponeBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ConponeActivity extends BaseActivity {
    @Bind(R.id.conpone_listView)
    ListView listView;
    DisCountListAdapter  adapter;
    ArrayList<conponeBean> disCountList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conpuo);
        ButterKnife.bind(this);
        disCountList=(ArrayList<conponeBean>) getIntent().getSerializableExtra("conpone");
        adapter=new DisCountListAdapter(this,disCountList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(ConponeActivity.this,OrderPayActivity2.class);
                if(i==disCountList.size())
                {
                    intent.putExtra("index",-1);
                }else{
                    intent.putExtra("index",i);
                }

                setResult(10012,intent);
                finish();
            }
        });
    }
}
