package com.example.epay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.aspsine.irecyclerview.IRecyclerView;

/**
 * Created by liujin on 2018/3/12.
 */

public class EPayIRecyclerView extends IRecyclerView {
    public EPayIRecyclerView(Context context) {
        super(context);
    }

    public EPayIRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EPayIRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}