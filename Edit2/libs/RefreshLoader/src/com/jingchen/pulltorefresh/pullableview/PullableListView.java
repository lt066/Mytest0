package com.jingchen.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PullableListView extends ListView implements Pullable {

	public PullableListView(Context context) {
		super(context);
	}

	public PullableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		try {
			if (getCount() == 0) {

				return true;
			} else if (getFirstVisiblePosition() == 0 && getChildAt(0) != null && getChildAt(0).getTop() >= 0) {

				return true;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	@Override
	public boolean canPullUp() {
		try {
			if (getCount() == 0) {

				return true;
			} else if (getLastVisiblePosition() == (getCount() - 1)) {

				if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
						&& getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
}
