/*
 * Copyright 2013 - learnNcode (learnncode@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.learnncode.mediachooser.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.MediaChooserConstants;
import com.learnncode.mediachooser.R;
import com.learnncode.mediachooser.fragment.ImageFragment;
import com.learnncode.mediachooser.fragment.VideoFragment;

/**
 * 选择图片的主页面Activity，显示全部图片，没有相册区分
 * @author Administrator
 *
 */
public class HomeFragmentActivity extends ActionBarActivity implements ImageFragment.OnImageSelectedListener,
		VideoFragment.OnVideoSelectedListener {

	private FragmentTabHost mTabHost;
	// private TextView headerBarTitle;
	// private ImageView headerBarCamera;
	// private ImageView headerBarBack;
	// private ImageView headerBarDone;

	private static Uri fileUri;

	private final Handler handler = new Handler();

	private boolean isVedio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home_media_chooser);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// headerBarTitle =
		// (TextView)findViewById(R.id.titleTextViewFromMediaChooserHeaderBar);
		// headerBarCamera =
		// (ImageView)findViewById(R.id.cameraImageViewFromMediaChooserHeaderBar);
		// headerBarBack =
		// (ImageView)findViewById(R.id.backArrowImageViewFromMediaChooserHeaderView);
		// headerBarDone =
		// (ImageView)findViewById(R.id.doneImageViewFromMediaChooserHeaderView);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		if(Build.VERSION.SDK_INT<=10){
			mTabHost.setBackgroundResource(android.R.color.black);
		}
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabcontent);
		// headerBarBack.setOnClickListener(clickListener);
		// headerBarCamera.setOnClickListener(clickListener);
		// headerBarDone.setOnClickListener(clickListener);

		// if(! MediaChooserConstants.showCameraVideo){
		// headerBarCamera.setVisibility(View.GONE);
		// }

		if (getIntent() != null && (getIntent().getBooleanExtra("isFromBucket", false))) {

			if (getIntent().getBooleanExtra("image", false)) {

				isVedio = false;
				changeState();
				// headerBarTitle.setText(getResources().);
				// setHeaderBarCameraBackground(getResources().getDrawable(R.drawable.selector_camera_button));

				// headerBarCamera.setTag(getResources().getString(R.string.image));

				Bundle bundle = new Bundle();
				bundle.putString("name", getIntent().getStringExtra("name"));
				mTabHost.addTab(
						mTabHost.newTabSpec("tab1").setIndicator(getResources().getString(R.string.image) + "     "),
						ImageFragment.class, bundle);

			} else {

				isVedio = true;
				changeState();

				// headerBarTitle.setText(getResources().getString(R.string.video));
				// setHeaderBarCameraBackground(getResources().getDrawable(R.drawable.selector_video_button));
				// headerBarCamera.setTag(getResources().getString(R.string.video));

				Bundle bundle = new Bundle();
				bundle.putString("name", getIntent().getStringExtra("name"));
				mTabHost.addTab(
						mTabHost.newTabSpec("tab2").setIndicator(getResources().getString(R.string.video) + "      "),
						VideoFragment.class, bundle);
			}
		} else {

			if (MediaChooserConstants.showVideo) {
				mTabHost.addTab(
						mTabHost.newTabSpec("tab2").setIndicator(getResources().getString(R.string.video) + "      "),
						VideoFragment.class, null);
			}

			if (MediaChooserConstants.showImage) {

				isVedio = false;
				changeState();
				mTabHost.addTab(
						mTabHost.newTabSpec("tab1").setIndicator(getResources().getString(R.string.image) + "      "),
						ImageFragment.class, null);
			}

			if (MediaChooserConstants.showVideo) {

				isVedio = true;
				changeState();
				// headerBarTitle.setText(getResources().getString(R.string.video));
				// setHeaderBarCameraBackground(getResources().getDrawable(R.drawable.selector_video_button));
				// headerBarCamera.setTag(getResources().getString(R.string.video));
			}
		}

		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

			TextView textView = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			if (textView.getLayoutParams() instanceof RelativeLayout.LayoutParams) {

				RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) textView
						.getLayoutParams();
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
				params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
				mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title).setLayoutParams(params);

			} else if (textView.getLayoutParams() instanceof LinearLayout.LayoutParams) {
				LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) textView
						.getLayoutParams();
				params.gravity = Gravity.CENTER;
				mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title).setLayoutParams(params);
			}

		}

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {

				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				ImageFragment imageFragment = (ImageFragment) fragmentManager.findFragmentByTag("tab1");
				VideoFragment videoFragment = (VideoFragment) fragmentManager.findFragmentByTag("tab2");
				android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

				if (tabId.equalsIgnoreCase("tab1")) {
					isVedio = false;
					changeState();

					// headerBarTitle.setText(getResources().getString(R.string.image));
					// setHeaderBarCameraBackground(getResources().getDrawable(R.drawable.selector_camera_button));
					// headerBarCamera.setTag(getResources().getString(R.string.image));

					if (imageFragment != null) {

						if (videoFragment != null) {
							fragmentTransaction.hide(videoFragment);
						}
						fragmentTransaction.show(imageFragment);
					}
				} else {

					isVedio = true;
					changeState();

					// headerBarTitle.setText(getResources().getString(R.string.video));
					// setHeaderBarCameraBackground(getResources().getDrawable(R.drawable.selector_video_button));
					// headerBarCamera.setTag(getResources().getString(R.string.video));

					if (videoFragment != null) {

						if (imageFragment != null) {
							fragmentTransaction.hide(imageFragment);
						}

						fragmentTransaction.show(videoFragment);
						if (videoFragment.getAdapter() != null) {
							videoFragment.getAdapter().notifyDataSetChanged();
						}
					}
				}
				fragmentTransaction.commit();
			}
		});
	}

	private void changeState() {
		if (cameraMenuItem != null) {
			if (isVedio) {
				cameraMenuItem.setIcon(R.drawable.selector_video_button);
				setTitle(getString(R.string.video));
			} else {
				cameraMenuItem.setIcon(R.drawable.selector_camera_button);
				setTitle(getString(R.string.image));
			}
		}
	}

	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// Log.d(getClass().getName(), "onPrepareOptionsMenu");
	// if (isVedio) {
	// menu.findItem(R.id.cameraImageViewFromMediaChooserHeaderBar).setTitle("拍摄");
	// menu.findItem(R.id.cameraImageViewFromMediaChooserHeaderBar).setIcon(R.drawable.selector_video_button);
	// } else {
	// menu.findItem(R.id.cameraImageViewFromMediaChooserHeaderBar).setTitle("照相");
	// menu.findItem(R.id.cameraImageViewFromMediaChooserHeaderBar).setIcon(R.drawable.selector_camera_button);
	//
	// }
	// return super.onPrepareOptionsMenu(menu);
	// }

	MenuItem cameraMenuItem;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_camera, menu);
		cameraMenuItem = menu.findItem(R.id.cameraImageViewFromMediaChooserHeaderBar);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		int id = item.getItemId();

		if (id == android.R.id.home) {
			finish();
		}

		else if (id == R.id.cameraImageViewFromMediaChooserHeaderBar) {
			if (isVedio) {
				intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_VIDEO);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(intent, MediaChooserConstants.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
			} else {
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_IMAGE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(intent, MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		} else if (id == R.id.doneImageViewFromMediaChooserHeaderView) {
			android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			ImageFragment imageFragment = (ImageFragment) fragmentManager.findFragmentByTag("tab1");
			VideoFragment videoFragment = (VideoFragment) fragmentManager.findFragmentByTag("tab2");

			if (videoFragment != null || imageFragment != null) {

				if (videoFragment != null) {
					if (videoFragment.getSelectedVideoList() != null && videoFragment.getSelectedVideoList().size() > 0) {
						Intent videoIntent = new Intent();
						videoIntent.setAction(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
						videoIntent.putStringArrayListExtra("list", videoFragment.getSelectedVideoList());
						sendBroadcast(videoIntent);
					}
				}

				if (imageFragment != null) {
					if (imageFragment.getSelectedImageList() != null && imageFragment.getSelectedImageList().size() > 0) {
						Intent imageIntent = new Intent();
						imageIntent.setAction(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
						imageIntent.putStringArrayListExtra("list", imageFragment.getSelectedImageList());
						sendBroadcast(imageIntent);
					}
				}

				finish();
			} else {
				Toast.makeText(HomeFragmentActivity.this, getString(R.string.plaese_select_file), Toast.LENGTH_SHORT)
						.show();
			}
		}

		return super.onOptionsItemSelected(item);
	}

	// OnClickListener clickListener = new OnClickListener() {
	//
	// @Override
	// public void onClick(View view) {
	// if(view == headerBarCamera){
	//
	// if(view.getTag().toString().equals(getResources().getString(R.string.video))){
	// Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	// fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_VIDEO);
	// // create a file to save the image
	// intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
	// name
	//
	// // start the image capture Intent
	// startActivityForResult(intent,
	// MediaChooserConstants.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
	//
	// }else{
	// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	// fileUri = getOutputMediaFileUri(MediaChooserConstants.MEDIA_TYPE_IMAGE);
	// // create a file to save the image
	// intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
	// name
	//
	// // start the image capture Intent
	// startActivityForResult(intent,
	// MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	// }
	// }else if(view == headerBarDone){
	//
	// android.support.v4.app.FragmentManager fragmentManager =
	// getSupportFragmentManager();
	// ImageFragment imageFragment = (ImageFragment)
	// fragmentManager.findFragmentByTag("tab1");
	// VideoFragment videoFragment = (VideoFragment)
	// fragmentManager.findFragmentByTag("tab2");
	//
	// if(videoFragment != null || imageFragment != null){
	//
	// if(videoFragment != null){
	// if(videoFragment.getSelectedVideoList() != null &&
	// videoFragment.getSelectedVideoList() .size() > 0){
	// Intent videoIntent = new Intent();
	// videoIntent.setAction(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER
	// );
	// videoIntent.putStringArrayListExtra("list",
	// videoFragment.getSelectedVideoList());
	// sendBroadcast(videoIntent);
	// }
	// }
	//
	// if(imageFragment != null){
	// if(imageFragment.getSelectedImageList() != null &&
	// imageFragment.getSelectedImageList().size() > 0){
	// Intent imageIntent = new Intent();
	// imageIntent.setAction(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
	// imageIntent.putStringArrayListExtra("list",
	// imageFragment.getSelectedImageList());
	// sendBroadcast(imageIntent);
	// }
	// }
	//
	// finish();
	// }else{
	// Toast.makeText(HomeFragmentActivity.this,
	// getString(R.string.plaese_select_file), Toast.LENGTH_SHORT).show();
	// }
	//
	// }else if(view == headerBarBack){
	// finish();
	// }
	// }
	// };

	/** Create a file Uri for saving an image or video */
	private Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				MediaChooserConstants.folderName);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MediaChooserConstants.MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} else if (type == MediaChooserConstants.MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == MediaChooserConstants.CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
				final AlertDialog alertDialog = MediaChooserConstants.getDialog(HomeFragmentActivity.this).create();
				alertDialog.show();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// Do something after 5000ms
						String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
						android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
						VideoFragment videoFragment = (VideoFragment) fragmentManager.findFragmentByTag("tab2");
						//
						if (videoFragment == null) {
							VideoFragment newVideoFragment = new VideoFragment();
							newVideoFragment.addItem(fileUriString);

						} else {
							videoFragment.addItem(fileUriString);
						}
						alertDialog.cancel();
					}
				}, 5000);

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the video capture
			} else {
				// Video capture failed, advise user
			}
		} else if (requestCode == MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));

				final AlertDialog alertDialog = MediaChooserConstants.getDialog(HomeFragmentActivity.this).create();
				alertDialog.show();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// Do something after 5000ms
						String fileUriString = fileUri.toString().replaceFirst("file:///", "/").trim();
						android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
						ImageFragment imageFragment = (ImageFragment) fragmentManager.findFragmentByTag("tab1");
						if (imageFragment == null) {
							ImageFragment newImageFragment = new ImageFragment();
							newImageFragment.addItem(fileUriString);

						} else {
							imageFragment.addItem(fileUriString);
						}
						alertDialog.cancel();
					}
				}, 5000);
			}
		}
	}

	@Override
	public void onImageSelected(int count) {
		if (mTabHost.getTabWidget().getChildAt(1) != null) {
			if (count != 0) {
				((TextView) mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title))
						.setText(getResources().getString(R.string.images_tab) + "  " + count);

			} else {
				((TextView) mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title))
						.setText(getResources().getString(R.string.image));
			}
		} else {
			if (count != 0) {
				((TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title))
						.setText(getResources().getString(R.string.images_tab) + "  " + count);

			} else {
				((TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title))
						.setText(getResources().getString(R.string.image));
			}
		}
	}

	@Override
	public void onVideoSelected(int count) {
		if (count != 0) {
			((TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setText(getResources()
					.getString(R.string.videos_tab) + "  " + count);

		} else {
			((TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setText(getResources()
					.getString(R.string.video));
		}
	}

	// @SuppressLint("NewApi")
	// @SuppressWarnings("deprecation")
	// private void setHeaderBarCameraBackground(Drawable drawable) {
	// int sdk = android.os.Build.VERSION.SDK_INT;
	// if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
	// headerBarCamera.setBackgroundDrawable(drawable);
	// } else {
	// headerBarCamera.setBackground(drawable);
	// }
	// }
}
