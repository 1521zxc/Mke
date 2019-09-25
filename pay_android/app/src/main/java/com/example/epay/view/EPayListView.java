package com.example.epay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by liujin on 2018/3/12.
 */

public class EPayListView extends ListView {
    public EPayListView(Context context) {
        super(context);
    }

    public EPayListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EPayListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}