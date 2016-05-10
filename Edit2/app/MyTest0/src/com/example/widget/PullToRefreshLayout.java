package com.example.widget;

import java.util.Timer;
import java.util.TimerTask;

import com.example.mytest0.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.interfaces.Pullable;


/**
 * 鑷畾涔夌殑甯冨眬锛岀敤鏉ョ鐞嗕笁涓瓙鎺т欢锛屽叾涓竴涓槸涓嬫媺澶达紝涓�涓槸鍖呭惈鍐呭鐨刾ullableView锛堝彲浠ユ槸瀹炵幇Pullable鎺ュ彛鐨勭殑浠讳綍View锛�
 * 
 * @author longhf
 */
public class PullToRefreshLayout extends RelativeLayout
{
	public static final String TAG = "PullToRefreshLayout";
	// 鍒濆鐘舵��
	public static final int INIT = 0;
	// 閲婃斁鍒锋柊
	public static final int RELEASE_TO_REFRESH = 1;
	// 姝ｅ湪鍒锋柊
	public static final int REFRESHING = 2;
	// 閲婃斁鍔犺浇
	public static final int RELEASE_TO_LOAD = 3;
	// 姝ｅ湪鍔犺浇
	public static final int LOADING = 4;
	// 鎿嶄綔瀹屾瘯
	public static final int DONE = 5;
	// 褰撳墠鐘舵��
	private int state = INIT;
	// 鍒锋柊鍥炶皟鎺ュ彛
	private OnRefreshListener mListener;
	// 鍒锋柊鎴愬姛
	public static final int SUCCEED = 0;
	// 鍒锋柊澶辫触
	public static final int FAIL = 1;
	// 鎸変笅Y鍧愭爣锛屼笂涓�涓簨浠剁偣Y鍧愭爣
	private float downY, lastY;

	// 涓嬫媺鐨勮窛绂汇�傛敞鎰忥細pullDownY鍜宲ullUpY涓嶅彲鑳藉悓鏃朵笉涓�0
	public float pullDownY = 0;
	// 涓婃媺鐨勮窛绂�
	private float pullUpY = 0;

	// 閲婃斁鍒锋柊鐨勮窛绂�
	private float refreshDist = 150;
	// 閲婃斁鍔犺浇鐨勮窛绂�
	private float loadmoreDist = 150;

	private MyTimer timer;
	// 鍥炴粴閫熷害
	public float MOVE_SPEED = 8;
	// 绗竴娆℃墽琛屽竷灞�
	private boolean isLayout = false;
	// 鍦ㄥ埛鏂拌繃绋嬩腑婊戝姩鎿嶄綔
	private boolean isTouch = false;
	// 鎵嬫寚婊戝姩璺濈涓庝笅鎷夊ご鐨勬粦鍔ㄨ窛绂绘瘮锛屼腑闂翠細闅忔鍒囧嚱鏁板彉鍖�
	private float radio = 2;

	// 涓嬫媺绠ご鐨勮浆180掳鍔ㄧ敾
	private RotateAnimation rotateAnimation;
	// 鍧囧寑鏃嬭浆鍔ㄧ敾
	private RotateAnimation refreshingAnimation;

	// 涓嬫媺澶�
	private View refreshView;
	// 涓嬫媺鐨勭澶�
	private View pullView;
	// 姝ｅ湪鍒锋柊鐨勫浘鏍�
	private View refreshingView;
	// 鍒锋柊缁撴灉鍥炬爣
	private View refreshStateImageView;
	// 鍒锋柊缁撴灉锛氭垚鍔熸垨澶辫触
	private TextView refreshStateTextView;

	// 涓婃媺澶�
	private View loadmoreView;
	// 涓婃媺鐨勭澶�
	private View pullUpView;
	// 姝ｅ湪鍔犺浇鐨勫浘鏍�
	private View loadingView;
	// 鍔犺浇缁撴灉鍥炬爣
	private View loadStateImageView;
	// 鍔犺浇缁撴灉锛氭垚鍔熸垨澶辫触
	private TextView loadStateTextView;

	// 瀹炵幇浜哖ullable鎺ュ彛鐨刅iew
	private View pullableView;
	// 杩囨护澶氱偣瑙︾
	private int mEvents;
	// 杩欎袱涓彉閲忕敤鏉ユ帶鍒秔ull鐨勬柟鍚戯紝濡傛灉涓嶅姞鎺у埗锛屽綋鎯呭喌婊¤冻鍙笂鎷夊張鍙笅鎷夋椂娌℃硶涓嬫媺
	private boolean canPullDown = true;
	private boolean canPullUp = true;

	/**
	 * 鎵ц鑷姩鍥炴粴鐨刪andler
	 */
	Handler updateHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// 鍥炲脊閫熷害闅忎笅鎷夎窛绂籱oveDeltaY澧炲ぇ鑰屽澶�
			MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
					/ getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			if (!isTouch)
			{
				// 姝ｅ湪鍒锋柊锛屼笖娌℃湁寰�涓婃帹鐨勮瘽鍒欐偓鍋滐紝鏄剧ず"姝ｅ湪鍒锋柊..."
				if (state == REFRESHING && pullDownY <= refreshDist)
				{
					pullDownY = refreshDist;
					timer.cancel();
				} else if (state == LOADING && -pullUpY <= loadmoreDist)
				{
					pullUpY = -loadmoreDist;
					timer.cancel();
				}

			}
			if (pullDownY > 0)
				pullDownY -= MOVE_SPEED;
			else if (pullUpY < 0)
				pullUpY += MOVE_SPEED;
			if (pullDownY < 0)
			{
				// 宸插畬鎴愬洖寮�
				pullDownY = 0;
				pullView.clearAnimation();
				// 闅愯棌涓嬫媺澶存椂鏈夊彲鑳借繕鍦ㄥ埛鏂帮紝鍙湁褰撳墠鐘舵�佷笉鏄鍦ㄥ埛鏂版椂鎵嶆敼鍙樼姸鎬�
				if (state != REFRESHING && state != LOADING)
					changeState(INIT);
				timer.cancel();
			}
			if (pullUpY > 0)
			{
				// 宸插畬鎴愬洖寮�
				pullUpY = 0;
				pullUpView.clearAnimation();
				// 闅愯棌涓嬫媺澶存椂鏈夊彲鑳借繕鍦ㄥ埛鏂帮紝鍙湁褰撳墠鐘舵�佷笉鏄鍦ㄥ埛鏂版椂鎵嶆敼鍙樼姸鎬�
				if (state != REFRESHING && state != LOADING)
					changeState(INIT);
				timer.cancel();
			}
			// 鍒锋柊甯冨眬,浼氳嚜鍔ㄨ皟鐢╫nLayout
			requestLayout();
		}

	};

	public void setOnRefreshListener(OnRefreshListener listener)
	{
		mListener = listener;
	}

	public PullToRefreshLayout(Context context)
	{
		super(context);
		System.gc();
		initView(context);
	}

	public PullToRefreshLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		System.gc();
		initView(context);
	}

	public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		System.gc();
		initView(context);
	}

	private void initView(Context context)
	{
		timer = new MyTimer(updateHandler);
		rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.reverse_anim);
		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.rotating);
		// 娣诲姞鍖�閫熻浆鍔ㄥ姩鐢�
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);
		refreshingAnimation.setInterpolator(lir);
	}

	private void hide()
	{
		System.gc();
		timer.schedule(5);
	}

	/**
	 * 瀹屾垚鍒锋柊鎿嶄綔锛屾樉绀哄埛鏂扮粨鏋溿�傛敞鎰忥細鍒锋柊瀹屾垚鍚庝竴瀹氳璋冪敤杩欎釜鏂规硶
	 */
	/**
	 * @param refreshResult
	 *            PullToRefreshLayout.SUCCEED浠ｈ〃鎴愬姛锛孭ullToRefreshLayout.FAIL浠ｈ〃澶辫触
	 */
	public void refreshFinish(int refreshResult)
	{
		try{
			refreshingView.clearAnimation();
			refreshingView.setVisibility(View.GONE);
			switch (refreshResult)
			{
			case SUCCEED:
				// 鍒锋柊鎴愬姛
				refreshStateImageView.setVisibility(View.VISIBLE);
				refreshStateTextView.setText(R.string.refresh_succeed);
				refreshStateImageView
						.setBackgroundResource(R.drawable.refresh_succeed);
				break;
			case FAIL:
			default:
				// 鍒锋柊澶辫触
				refreshStateImageView.setVisibility(View.VISIBLE);
				refreshStateTextView.setText(R.string.refresh_fail);
				refreshStateImageView
						.setBackgroundResource(R.drawable.refresh_failed);
				break;
			}
			// 鍒锋柊缁撴灉鍋滅暀1绉�
			new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					changeState(DONE);
					System.gc();
					hide();
					
				}
			}.sendEmptyMessageDelayed(0, 400);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * 鍔犺浇瀹屾瘯锛屾樉绀哄姞杞界粨鏋溿�傛敞鎰忥細鍔犺浇瀹屾垚鍚庝竴瀹氳璋冪敤杩欎釜鏂规硶
	 * 
	 * @param refreshResult
	 *            PullToRefreshLayout.SUCCEED浠ｈ〃鎴愬姛锛孭ullToRefreshLayout.FAIL浠ｈ〃澶辫触
	 */
	public void loadmoreFinish(int refreshResult)
	{
		try{
			loadingView.clearAnimation();
			loadingView.setVisibility(View.GONE);
			switch (refreshResult)
			{
			case SUCCEED:
				// 鍔犺浇鎴愬姛
				loadStateImageView.setVisibility(View.VISIBLE);
				loadStateTextView.setText(R.string.load_succeed);
				loadStateImageView.setBackgroundResource(R.drawable.load_succeed);
				break;
			case FAIL:
			default:
				// 鍔犺浇澶辫触
				loadStateImageView.setVisibility(View.VISIBLE);
				loadStateTextView.setText(R.string.load_fail);
				loadStateImageView.setBackgroundResource(R.drawable.load_failed);
				break;
			}
			// 鍒锋柊缁撴灉鍋滅暀1绉�
			new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					changeState(DONE);
					System.gc();
					hide();
				}
			}.sendEmptyMessageDelayed(0, 400);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void changeState(int to)
	{
		state = to;
		switch (state)
		{
		case INIT:
			// 涓嬫媺甯冨眬鍒濆鐘舵��
			refreshStateImageView.setVisibility(View.GONE);
			refreshStateTextView.setText(R.string.pull_to_refresh);
			pullView.clearAnimation();
			pullView.setVisibility(View.VISIBLE);
			// 涓婃媺甯冨眬鍒濆鐘舵��
			loadStateImageView.setVisibility(View.GONE);
			loadStateTextView.setText(R.string.pullup_to_load);
			pullUpView.clearAnimation();
			pullUpView.setVisibility(View.VISIBLE);
			break;
		case RELEASE_TO_REFRESH:
			// 閲婃斁鍒锋柊鐘舵��
			refreshStateTextView.setText(R.string.release_to_refresh);
			pullView.startAnimation(rotateAnimation);
			break;
		case REFRESHING:
			// 姝ｅ湪鍒锋柊鐘舵��
			pullView.clearAnimation();
			refreshingView.setVisibility(View.VISIBLE);
			pullView.setVisibility(View.INVISIBLE);
			refreshingView.startAnimation(refreshingAnimation);
			refreshStateTextView.setText(R.string.refreshing);
			break;
		case RELEASE_TO_LOAD:
			// 閲婃斁鍔犺浇鐘舵��
			loadStateTextView.setText(R.string.release_to_load);
			pullUpView.startAnimation(rotateAnimation);
			break;
		case LOADING:
			// 姝ｅ湪鍔犺浇鐘舵��
			pullUpView.clearAnimation();
			loadingView.setVisibility(View.VISIBLE);
			pullUpView.setVisibility(View.INVISIBLE);
			loadingView.startAnimation(refreshingAnimation);
			loadStateTextView.setText(R.string.loading);
			break;
		case DONE:
			state = INIT; //鍒濆鍖栫姸鎬侊紝瑙ｅ喅闂撮殧鏃犳硶鍒锋柊鐨刡ug    hgz
			//瑙ｅ喅绗竴娆″埛鏂版椂鐘舵�佸浘鏍囦竴鐩存樉绀虹殑bug  hgz
			refreshStateImageView.setVisibility(View.GONE);
			loadStateImageView.setVisibility(View.GONE);
			break;
		}
	}

	/**
	 * 涓嶉檺鍒朵笂鎷夋垨涓嬫媺
	 */
	private void releasePull()
	{
		canPullDown = true;
		canPullUp = true;
	}

	/*
	 * 锛堥潪 Javadoc锛夌敱鐖舵帶浠跺喅瀹氭槸鍚﹀垎鍙戜簨浠讹紝闃叉浜嬩欢鍐茬獊
	 * 
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		switch (ev.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			lastY = downY;
			timer.cancel();
			mEvents = 0;
			releasePull();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			// 杩囨护澶氱偣瑙︾
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			
			if (mEvents == 0)
			{
				if (((Pullable) pullableView).canPullDown() && canPullDown
						&& state != LOADING)
				{
					// 鍙互涓嬫媺锛屾鍦ㄥ姞杞芥椂涓嶈兘涓嬫媺
					// 瀵瑰疄闄呮粦鍔ㄨ窛绂诲仛缂╁皬锛岄�犳垚鐢ㄥ姏鎷夌殑鎰熻
					pullDownY = pullDownY + (ev.getY() - lastY) / radio;
					if (pullDownY < 0)
					{
						pullDownY = 0;
						canPullDown = false;
						canPullUp = true;
					}
					if (pullDownY > getMeasuredHeight())
						pullDownY = getMeasuredHeight();
					if (state == REFRESHING)
					{
						// 姝ｅ湪鍒锋柊鐨勬椂鍊欒Е鎽哥Щ鍔�
						isTouch = true;
					}
				} else if (((Pullable) pullableView).canPullUp() && canPullUp
						&& state != REFRESHING)
				{
					// 鍙互涓婃媺锛屾鍦ㄥ埛鏂版椂涓嶈兘涓婃媺
					pullUpY = pullUpY + (ev.getY() - lastY) / radio;
					if (pullUpY > 0)
					{
						pullUpY = 0;
						canPullDown = true;
						canPullUp = false;
					}
					if (pullUpY < -getMeasuredHeight())
						pullUpY = -getMeasuredHeight();
					if (state == LOADING)
					{
						// 姝ｅ湪鍔犺浇鐨勬椂鍊欒Е鎽哥Щ鍔�
						isTouch = true;
					}
				} else
					releasePull();
			} else
				mEvents = 0;
			lastY = ev.getY();
			// 鏍规嵁涓嬫媺璺濈鏀瑰彉姣斾緥
			radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
					* (pullDownY + Math.abs(pullUpY))));
			requestLayout();
			if (pullDownY <= refreshDist && state == RELEASE_TO_REFRESH)
			{
				// 濡傛灉涓嬫媺璺濈娌¤揪鍒板埛鏂扮殑璺濈涓斿綋鍓嶇姸鎬佹槸閲婃斁鍒锋柊锛屾敼鍙樼姸鎬佷负涓嬫媺鍒锋柊
				changeState(INIT);
			}
			if (pullDownY >= refreshDist && state == INIT)
			{
				// 濡傛灉涓嬫媺璺濈杈惧埌鍒锋柊鐨勮窛绂讳笖褰撳墠鐘舵�佹槸鍒濆鐘舵�佸埛鏂帮紝鏀瑰彉鐘舵�佷负閲婃斁鍒锋柊
				changeState(RELEASE_TO_REFRESH);
			}
			// 涓嬮潰鏄垽鏂笂鎷夊姞杞界殑锛屽悓涓婏紝娉ㄦ剰pullUpY鏄礋鍊�
			if (-pullUpY <= loadmoreDist && state == RELEASE_TO_LOAD)
			{
				changeState(INIT);
			}
			if (-pullUpY >= loadmoreDist && state == INIT)
			{
				
				changeState(RELEASE_TO_LOAD);
			}
			// 鍥犱负鍒锋柊鍜屽姞杞芥搷浣滀笉鑳藉悓鏃惰繘琛岋紝鎵�浠ullDownY鍜宲ullUpY涓嶄細鍚屾椂涓嶄负0锛屽洜姝よ繖閲岀敤(pullDownY +
			// Math.abs(pullUpY))灏卞彲浠ヤ笉瀵瑰綋鍓嶇姸鎬佷綔鍖哄垎浜�
			if ((pullDownY + Math.abs(pullUpY)) > 8)
			{
				// 闃叉涓嬫媺杩囩▼涓瑙﹀彂闀挎寜浜嬩欢鍜岀偣鍑讳簨浠�
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (pullDownY > refreshDist || -pullUpY > loadmoreDist)
				// 姝ｅ湪鍒锋柊鏃跺線涓嬫媺锛堟鍦ㄥ姞杞芥椂寰�涓婃媺锛夛紝閲婃斁鍚庝笅鎷夊ご锛堜笂鎷夊ご锛変笉闅愯棌
				isTouch = false;
			if (state == RELEASE_TO_REFRESH)
			{
				changeState(REFRESHING);
				// 鍒锋柊鎿嶄綔
				if (mListener != null)
					mListener.onRefresh(this);
			} else if (state == RELEASE_TO_LOAD)
			{
				changeState(LOADING);
				// 鍔犺浇鎿嶄綔
				if (mListener != null)
					mListener.onLoadMore(this);
			}
			hide();
		default:
			break;
		}
		// 浜嬩欢鍒嗗彂浜ょ粰鐖剁被
		super.dispatchTouchEvent(ev);
		return true;
	}

	private void initView()
	{
		// 鍒濆鍖栦笅鎷夊竷灞�
		pullView = refreshView.findViewById(R.id.pull_icon);
		refreshStateTextView = (TextView) refreshView.findViewById(R.id.state_tv);
		refreshingView = refreshView.findViewById(R.id.refreshing_icon);
		refreshStateImageView = refreshView.findViewById(R.id.state_iv);
		refreshStateImageView.setVisibility(View.GONE);
		// 鍒濆鍖栦笂鎷夊竷灞�
		pullUpView = loadmoreView.findViewById(R.id.pullup_icon);
		loadStateTextView = (TextView) loadmoreView
				.findViewById(R.id.loadstate_tv);
		loadingView = loadmoreView.findViewById(R.id.loading_icon);
		loadStateImageView = loadmoreView.findViewById(R.id.loadstate_iv);
		loadStateImageView.setVisibility(View.GONE);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		if (!isLayout)
		{
			// 杩欓噷鏄涓�娆¤繘鏉ョ殑鏃跺�欏仛涓�浜涘垵濮嬪寲
			refreshView = getChildAt(0);
			pullableView = getChildAt(1);
			loadmoreView = getChildAt(2);
			isLayout = true;
			initView();
			refreshDist = ((ViewGroup) refreshView).getChildAt(0)
					.getMeasuredHeight();
			loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0)
					.getMeasuredHeight();
		}
		// 鏀瑰彉瀛愭帶浠剁殑甯冨眬锛岃繖閲岀洿鎺ョ敤(pullDownY + pullUpY)浣滀负鍋忕Щ閲忥紝杩欐牱灏卞彲浠ヤ笉瀵瑰綋鍓嶇姸鎬佷綔鍖哄垎
		refreshView.layout(0,
				(int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
				refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
		pullableView.layout(0, (int) (pullDownY + pullUpY),
				pullableView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
						+ pullableView.getMeasuredHeight());
		loadmoreView.layout(0,
				(int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight(),
				loadmoreView.getMeasuredWidth(),
				(int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight()
						+ loadmoreView.getMeasuredHeight());
	}

	class MyTimer
	{
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler)
		{
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period)
		{
			if (mTask != null)
			{
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel()
		{
			if (mTask != null)
			{
				mTask.cancel();
				mTask = null;
			}
		}

		class MyTask extends TimerTask
		{
			private Handler handler;

			public MyTask(Handler handler)
			{
				this.handler = handler;
			}

			@Override
			public void run()
			{
				handler.obtainMessage().sendToTarget();
			}

		}
	}

	/**
	 * 鍒锋柊鍔犺浇鍥炶皟鎺ュ彛
	 * 
	 * @author chenjing
	 * 
	 */
	public interface OnRefreshListener
	{
		/**
		 * 鍒锋柊鎿嶄綔
		 */
		void onRefresh(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 鍔犺浇鎿嶄綔
		 */
		void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
	}

}
