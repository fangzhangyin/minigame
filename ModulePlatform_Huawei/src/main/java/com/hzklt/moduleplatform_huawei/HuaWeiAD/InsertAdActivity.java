package com.hzklt.moduleplatform_huawei.HuaWeiAD;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.InterstitialAd;

public class InsertAdActivity extends Activity {

    private String TAG = "InsertAdActivity";
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interstitialAd = new InterstitialAd(this);
        // "testb4znbuh3n2"为测试专用的广告位ID，App正式发布时需要改为正式的广告位ID
        Intent intent = getIntent();
        String appid = intent.getStringExtra("id");
        interstitialAd.setAdId(appid);
        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);
        interstitialAd.setAdListener(adListener);
    }

    private void showInterstitialAd() {
        // 显示广告
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show(this);
        } else {
//            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            Log.d(TAG, "onAdLoaded");
            // 广告加载成功时调用
            showInterstitialAd();
        }

        @Override
        public void onAdFailed(int errorCode) {
            // 广告加载失败时调用
            Log.d(TAG, "onAdFailed:");
            finish();
        }

        @Override
        public void onAdClosed() {
            // 广告关闭时调用
            InsertAdActivity.this.finish();
            Log.d(TAG, "onAdClosed: ");
        }

        @Override
        public void onAdClicked() {
            // 广告点击时调用
            Log.d(TAG, "onAdClicked: ");
        }

        @Override
        public void onAdLeave() {
            // 广告离开时调用
        }

        @Override
        public void onAdOpened() {
            // 广告打开时调用
        }

        @Override
        public void onAdImpression() {
            // 广告曝光时调用
        }
    };

}
