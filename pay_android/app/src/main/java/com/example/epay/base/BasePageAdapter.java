package com.example.epay.base;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author qiuzhidi
 * @date 2015年3月23日 13:45:16
 */
public class BasePageAdapter extends PagerAdapter {
	private List<? extends View> viewList;

	public BasePageAdapter(ArrayList<? extends View> viewList) {
		this.viewList = viewList;
	}

	@Override
	public int getCount() {
		if (null == viewList) {
			return 0;
		} else {
			return viewList.size();
		}
	}
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
//		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(viewList.get(position));
		return viewList.get(position);
	}

}
