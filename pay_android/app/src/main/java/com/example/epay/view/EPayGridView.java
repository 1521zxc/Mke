package com.example.epay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by liujin on 2018/3/12.
 */

public class EPayGridView extends GridView {
    public EPayGridView(Context context) {
        super(context);
    }

    public EPayGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EPayGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}