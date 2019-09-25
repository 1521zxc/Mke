package com.example.epay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by liujin on 2018/3/12.
 */

public class EPayHListView extends ListView {
    public EPayHListView(Context context) {
        super(context);
    }

    public EPayHListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EPayHListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private int listViewHeight;

    public int getListViewHeight() {
        return listViewHeight;
    }

    public void setListViewHeight(int listViewHeight) {
        this.listViewHeight = listViewHeight;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (listViewHeight > -1) {
            listViewHeight = MeasureSpec.makeMeasureSpec(listViewHeight,
                    MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, listViewHeight);
    }
}