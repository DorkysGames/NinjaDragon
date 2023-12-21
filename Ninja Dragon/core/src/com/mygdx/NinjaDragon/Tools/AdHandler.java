package com.mygdx.NinjaDragon.Tools;

/**
 * Created by Brian on 12/1/2016.
 */

public interface AdHandler {
    void showBannerAds(boolean show);
    boolean isVideoAdReady();
    void playVideo();
    boolean getRewardType();
    boolean getRewardEarned();
    void resetReward();
}
