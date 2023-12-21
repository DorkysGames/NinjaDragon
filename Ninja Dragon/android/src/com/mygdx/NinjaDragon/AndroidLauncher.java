package com.mygdx.NinjaDragon;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.mygdx.NinjaDragon.Tools.AdHandler;

import java.util.Random;


public class AndroidLauncher extends AndroidApplication implements AdHandler {

	final private UnityAdsListener unityAdsListener = new UnityAdsListener();
    Random rand = new Random();
    protected AdView adView;
	private boolean rewardEarned = false;
    private final int CHECK_VIDEO = 0;
	private final int PLAY_VIDEO = 1;
    private final int HIDE_BANNER_AD = 2;
    private final int SHOW_BANNER_AD = 3;
	private boolean videoLoaded = false;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// getting the height of the device for banner ad placement
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = (int) (displaymetrics.heightPixels / 4.785);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        View gameView = initializeForView(new NinjaDragon(this), config);

		FrameLayout layout = new FrameLayout(this);
        layout.addView(gameView);
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId("ca-app-pub-6559096844237675/6694915544");

		AdRequest.Builder builder = new AdRequest.Builder();
		//builder.addTestDevice("9B55F76AF302A8F7047B0688EACD1D96");

		FrameLayout.LayoutParams adParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT,
				Gravity.CENTER
		);

		// places the banner at the correct height
		adParams.setMargins(0,height,0,0);

		layout.addView(adView, adParams);
		adView.loadAd(builder.build());
		adView.setVisibility(View.GONE);

		setContentView(layout);
		// initialize the first video ad
		initializeUnityAd();
		Gdx.input.setCatchBackKey(true);
	}

	// handler used for unity ads to communicate between game thread and main activity to load and play video
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
                case CHECK_VIDEO:
                    initializeUnityAd();
                    break;
				case PLAY_VIDEO:
					showUnityAd();
					break;
                case HIDE_BANNER_AD:
                    adView.setVisibility(View.GONE);
                    break;
                case SHOW_BANNER_AD:
                    adView.setVisibility(View.VISIBLE);
                    break;
			}
		}
	};

	// initialize the unity ad, playing test ads right now
	private void initializeUnityAd(){
        if (!UnityAds.isReady()) {
			videoLoaded = false;
            UnityAds.initialize(this,"1222166", unityAdsListener);
        } else {
            videoLoaded = true;
        }
	}

	// show the unity ad
	private void showUnityAd(){
		if (UnityAds.isReady()) {
			UnityAds.show(this);
			videoLoaded = false;
		}
	}

	public boolean getVideoLoaded() {
		return videoLoaded;
	}

    @Override
    public void showBannerAds(boolean show) {
    handler.sendEmptyMessage(show ? SHOW_BANNER_AD : HIDE_BANNER_AD);
    }

    // method to check if the video is ready, if not it loads one
	@Override
	public boolean isVideoAdReady() {
		handler.sendEmptyMessage(CHECK_VIDEO);
		return getVideoLoaded();
	}

	// if the video it ready, send an empty message the play the video
	@Override
	public void playVideo() {
			handler.sendEmptyMessage(PLAY_VIDEO);
	}

    public boolean getRewardType() {
        int i = rand.nextInt(2);
		return i == 0;
    }

	public boolean getRewardEarned() {
		return rewardEarned;
	}

	@Override
	public void resetReward() {
		setRewardEarned(false);
	}

	public void setRewardEarned(boolean rewardEarned) {
		this.rewardEarned = rewardEarned;
	}

    private class UnityAdsListener implements IUnityAdsListener {

		@Override
		public void onUnityAdsReady(String s) {
		}

		@Override
		public void onUnityAdsStart(String s) {

		}

		@Override
		public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
			if (finishState != UnityAds.FinishState.SKIPPED) {
				setRewardEarned(true);
			}
		}

		@Override
		public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
			videoLoaded = false;
		}
	}

	@Override
	public void onDestroy()
	{
		adView.destroy();
		super.onDestroy();
		System.exit(0);
	}
}
