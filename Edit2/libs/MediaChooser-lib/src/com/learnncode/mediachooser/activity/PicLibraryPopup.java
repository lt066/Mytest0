package com.learnncode.mediachooser.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.learnncode.mediachooser.R;

public class PicLibraryPopup extends PopupWindow {

	private onPopupItemClickListener onPopupItemClickListener;

	public void setOnPopupItemClickListener(onPopupItemClickListener onPopupItemClickListener) {
		this.onPopupItemClickListener = onPopupItemClickListener;
	}

	public interface onPopupItemClickListener {
		public void onTakePhotoClick();

		public void onSelectClick();

	}

	public PicLibraryPopup(Context mContext, View parent) {
//		if (Build.VERSION.SDK_INT <= 10) {
			super(parent, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		}

		View view = View.inflate(mContext, R.layout.item_popupwindows, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		update();

		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (onPopupItemClickListener != null) {
					onPopupItemClickListener.onTakePhotoClick();
				}
				// photo();
				dismiss();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Intent intent = new Intent(PublishedActivity.this,
				// TestPicActivity.class);
				// startActivity(intent);

				if (onPopupItemClickListener != null) {
					onPopupItemClickListener.onSelectClick();
				}
				dismiss();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

	}
}