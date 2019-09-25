package com.example.epay.activity;

import android.os.Bundle;

import com.example.epay.R;
import com.example.epay.base.BaseActivity;

import butterknife.ButterKnife;

public class MoreStatisticActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_statistic);
        ButterKnife.bind(this);
    }
}
