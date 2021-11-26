package com.hzklt.moduleplatform_huawei.HuaWeiAD;

import android.app.Activity;
import android.util.Log;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.InterstitialAd;


public class AdInterstitialView {

    private static final String TAG = "huawei Interstitial";

    private InterstitialAd interstitialAd;
    private String AdId;

    private static AdInterstitialView showView;
    private Activity mactivity;

    public static AdInterstitialView getInstance(Activity context, String adId) {

        if (null == showView) {
            showView = new AdInterstitialView(context, adId);
        }
        return showView;
    }

    private AdInterstitialView(Activity context, String adId) {
        AdId = adId;
        mactivity = context;
        interstitialAd = new InterstitialAd(context.getApplicationContext());
        interstitialAd.setAdId(adId);
        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);
        // 设置激励监听器
        interstitialAd.setAdListener(adListener);
    }

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // 广告加载成功时调用
            Log.d(TAG, "Interstitial 广告加载成功");
            show();
        }

        @Override
        public void onAdFailed(int errorCode) {
            // 广告加载失败时调用
            Log.d(TAG, "Interstitial 广告加载失败");
        }

        @Override
        public void onAdOpened() {
            // 广告打开时调用
            Log.d(TAG, "Interstitial 广告打开");
        }

        @Override
        public void onAdClicked() {
            // 广告点击时调用
            Log.d(TAG, "Interstitial 广告点击");

        }

        @Override
        public void onAdLeave() {
            // 广告离开应用时调用
            Log.d(TAG, "Interstitial 广告关闭");
        }

        @Override
        public void onAdClosed() {
            // 广告关闭时调用
            Log.d(TAG, "Interstitial 广告关闭");
        }
    };

    public void show() {
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show(mactivity);
        }
    }

    public void closeAd() {

    }
}
