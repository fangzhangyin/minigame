package com.hzklt.moduleplatform_vivo.VIVOAD;

import android.app.Activity;
import android.util.Log;

import com.hzklt.minigame.base.IPlatform2Unity;
import com.vivo.ad.video.VideoAdListener;
import com.vivo.mobilead.video.VideoAdParams;
import com.vivo.mobilead.video.VivoVideoAd;


//激励视频广告
public class VivoRewardVideoView {
    private static final String TAG = "RewardVideo";
    private VivoVideoAd vivoVideoAd;
    private VideoAdParams adParams;
    private Activity mactivity;
    IPlatform2Unity m_IPlatform2Unity;
    int IMsgID;

    public void showad() {
        if (vivoVideoAd != null) {
            vivoVideoAd.showAd(this.mactivity);
        }
    }


    private void initView() {

    }


    public void loadAD(Activity activity, String VIDEO_POSITION_ID, int method, IPlatform2Unity iPlatform2Unity) {
        VideoAdParams.Builder builder = new VideoAdParams.Builder(VIDEO_POSITION_ID);
        this.mactivity = activity;
        m_IPlatform2Unity = iPlatform2Unity;
        IMsgID = method;
        adParams = builder.build();
        vivoVideoAd = new VivoVideoAd(activity, adParams, videoAdListener);
        vivoVideoAd.loadAd();
    }


    private VideoAdListener videoAdListener = new VideoAdListener() {
        @Override
        public void onAdLoad() {
            Log.d(TAG, "onAdLoad");
            //此回调收到之后即可调用showAd展示广告，但也可以等收到onVideoCached回调再展示广告

        }

        @Override
        public void onAdFailed(String error) {
            Log.d(TAG, "onAdFailed: " + error);
        }

        @Override
        public void onVideoStart() {
            Log.d(TAG, "onVideoStart");
        }

        @Override
        public void onVideoCompletion() {
            Log.d(TAG, "onVideoCompletion");
            //回调
            m_IPlatform2Unity.UnitySendMessage(IMsgID, 1, 0, 0, "视屏广告播放完毕", "", "");
        }

        @Override
        public void onVideoClose(int currentPosition) {
            Log.d(TAG, "onVideoClose");
        }

        @Override
        public void onVideoCloseAfterComplete() {
            Log.d(TAG, "onVideoCloseAfterComplete");
        }

        @Override
        public void onVideoError(String error) {
            Log.d(TAG, "onVideoError");
            //回调
            m_IPlatform2Unity.UnitySendMessage(IMsgID, -1, 0, 0, "视屏广告播放失败", "", "");
        }

        @Override
        public void onFrequency() {
            Log.d(TAG, "onFrequency");
        }

        @Override
        public void onNetError(String error) {
            Log.d(TAG, "onNetError");
            ;
        }

        @Override
        public void onRequestLimit() {
            Log.d(TAG, "onRequestLimit");
        }

        @Override
        public void onVideoCached() {
            Log.d(TAG, "onVideoCached");
            //此回调收到之后即可调用showAd展示广告
            showad();
        }

        @Override
        public void onRewardVerify() {
            Log.d(TAG, "onRewardVerify");
        }
    };

}
