package com.hzklt.moduleplatform_vivo.VIVOAD;

import android.app.Activity;
import android.util.Log;

import com.vivo.mobilead.model.BackUrlInfo;
import com.vivo.mobilead.unified.base.AdParams;
import com.vivo.mobilead.unified.base.VivoAdError;
import com.vivo.mobilead.unified.base.callback.MediaListener;
import com.vivo.mobilead.unified.interstitial.UnifiedVivoInterstitialAd;
import com.vivo.mobilead.unified.interstitial.UnifiedVivoInterstitialAdListener;


//插屏视频广告
public class VivoInterstADView {
    private static final String TAG = "InterstAD";
    private AdParams videoAdParams;
    private UnifiedVivoInterstitialAd vivoInterstitialAd;
    private Activity mactivity;


    public void showAd() {
        if (vivoInterstitialAd != null) {
            vivoInterstitialAd.showVideoAd(this.mactivity);
        }
    }

    public void loadvideoAD(Activity activity, String INTERSTITIAL_POSITION_ID) {
        this.mactivity = activity;
        AdParams.Builder videoBuilder = new AdParams.Builder(INTERSTITIAL_POSITION_ID);
        String backUrl = "vivobrowser://browser.vivo.com";
        String btnName = "testabcdteststststststtsst";
        BackUrlInfo backUrlInfo = new BackUrlInfo(backUrl, btnName);
        videoBuilder.setBackUrlInfo(backUrlInfo);
        videoAdParams = videoBuilder.build();
        vivoInterstitialAd = new UnifiedVivoInterstitialAd(activity, videoAdParams, interstitialAdListener);
        vivoInterstitialAd.setMediaListener(mediaListener);
        vivoInterstitialAd.loadVideoAd();
    }

    private UnifiedVivoInterstitialAdListener interstitialAdListener = new UnifiedVivoInterstitialAdListener() {
        @Override
        public void onAdReady(boolean hasCache) {
            Log.d(TAG, "onAdReady");
        }

        @Override
        public void onAdFailed(VivoAdError error) {
            Log.d(TAG, "onAdFailed: " + error.toString());
        }

        @Override
        public void onAdClick() {
            Log.d(TAG, "onAdClick");
        }

        @Override
        public void onAdShow() {
            Log.d(TAG, "onAdShow");
        }

        @Override
        public void onAdClose() {
            Log.d(TAG, "onAdClose");
        }
    };


    private MediaListener mediaListener = new MediaListener() {
        @Override
        public void onVideoStart() {
            Log.d(TAG, "onVideoStart");
        }

        @Override
        public void onVideoPause() {
            Log.d(TAG, "onVideoPause");
        }

        @Override
        public void onVideoPlay() {
            Log.d(TAG, "onVideoPlay");
        }

        @Override
        public void onVideoError(VivoAdError error) {
            Log.d(TAG, "onVideoError: " + error.toString());
        }

        @Override
        public void onVideoCompletion() {
            Log.d(TAG, "onVideoCompletion");

        }

        @Override
        public void onVideoCached() {
            Log.d(TAG, "onVideoCached");
            showAd();
        }
    };


}
