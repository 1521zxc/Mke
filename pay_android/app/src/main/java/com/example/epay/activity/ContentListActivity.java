package com.example.epay.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContentListActivity extends BaseActivity {

    @Bind(R.id.content_ListView)
    ListView contentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);
        ButterKnife.bind(this);
    }
}
