package com.hzklt.moduleplatform_huawei.HuaWeiAD;

import android.app.Activity;
import android.util.Log;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.reward.Reward;
import com.huawei.hms.ads.reward.RewardAd;
import com.huawei.hms.ads.reward.RewardAdLoadListener;
import com.huawei.hms.ads.reward.RewardAdStatusListener;


public class AdRewardVideoView{
    private static final String TAG = "RewardVideo";
    private static AdRewardVideoView showView;
    private RewardAd rewardAd;
    private Activity mactivity;
    private String videoAdID;
    private boolean isLoaded;

    public static AdRewardVideoView getInstance(Activity context, String adId) {

        if (null == showView) {
            showView = new AdRewardVideoView(context,adId);
        }
        return showView;
    }

    private AdRewardVideoView(Activity context, String adId) {
        mactivity = context;
        videoAdID = adId;
        rewardAd = new RewardAd(context, adId);
        loadAD();
    }

    public void loadAD(){
        if (rewardAd == null) {
            rewardAd = new RewardAd(mactivity, videoAdID);
        }
        RewardAdLoadListener listener= new RewardAdLoadListener() {
            @Override
            public void onRewardedLoaded() {
                // 激励广告加载成功
                Log.d(TAG, "激励广告加载成功");
                isLoaded = true;

            }
            @Override
            public void onRewardAdFailedToLoad(int errorCode) {
                // 激励广告加载失败
                Log.d(TAG, "激励广告加载失败");
                isLoaded = false;

            }
        };
        rewardAd.loadAd(new AdParam.Builder().build(), listener);
    }


    public void showAd(){
        if (rewardAd.isLoaded()) {
            rewardAd.show(mactivity, new RewardAdStatusListener() {
                @Override
                public void onRewardAdOpened() {
                    // 激励广告被打开
                    Log.d(TAG, "激励广告被打开");
                }
                @Override
                public void onRewardAdFailedToShow(int errorCode) {
                    // 激励广告展示失败
                    Log.d(TAG, "激励广告展示失败");

                }
                @Override
                public void onRewardAdClosed() {
                    // 激励广告被关闭
                    Log.d(TAG, "激励广告被关闭");
//                    JSBridge.JsCall(NativeCallType.RewardAd, ADStatus.ADStatusShowClosed,null);
                }
                @Override
                public void onRewarded(Reward reward){
                    // 激励广告奖励达成，发放奖励
                    Log.d(TAG, "激励广告奖励达成");
//                    JSBridge.JsCall(NativeCallType.RewardAd, ADStatus.ADStatusShowRewardSuccess,null);
                }
            });
        }
    }

}
