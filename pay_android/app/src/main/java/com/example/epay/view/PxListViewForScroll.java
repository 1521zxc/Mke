package com.example.epay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by tpp01 on 2015/4/14.
 */
public class PxListViewForScroll extends PxListView {
        public PxListViewForScroll(Context context) {
            super(context);
        }

        public PxListViewForScroll(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public PxListViewForScroll(Context context, AttributeSet attrs,
                                     int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if(ev.getAction() == MotionEvent.ACTION_MOVE){
                return true;
            }
            return super.dispatchTouchEvent(ev);
        }

        @Override
        /**
         * 重写该方法，达到使ListView适应ScrollView的效果
         */
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }
    }
