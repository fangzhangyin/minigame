package com.hzklt.moduleplatform_oppo.OPPOAD;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.heytap.msp.mobad.api.ad.InterstitialVideoAd;
import com.heytap.msp.mobad.api.listener.IInterstitialVideoAdListener;

import com.hzklt.minigame.base.IPlatform2Unity;
import com.hzklt.moduleplatform_oppo.R;

public class InterstitialVideo implements IInterstitialVideoAdListener {
    private static InterstitialVideo instance;
    private static final String TAG = "IVideoActivity";

    private InterstitialVideoAd mInterstitialAd;

    private String id;
    private Activity activity;
    private boolean compile;
    private IPlatform2Unity m_IPlatform2Unity;
    private int IMsgID;

    public static InterstitialVideo getInstance(Activity activity,IPlatform2Unity iPlatform2Unity,int iMsgId) {
        if (instance == null) {
            instance = new InterstitialVideo();
            instance.activity = activity;
            instance.id = activity.getString(R.string.InterVideoID);
            instance.m_IPlatform2Unity = iPlatform2Unity;
            instance.IMsgID = iMsgId;
        }
        return instance;
    }

    public void loadAD() {
        compile = false;
        mInterstitialAd = new InterstitialVideoAd(activity, id, this);
        mInterstitialAd.loadAd();
    }

    @Override
    public void onVideoPlayComplete() {
        Log.d(TAG, "onVideoPlayComplete: ");
        compile = true;
        //回調
        m_IPlatform2Unity.UnitySendMessage(IMsgID, 1, 0, 0, "插屏视频播放成功", "", "");
    }

    @Override
    public void onAdReady() {
        Log.d(TAG, "onAdReady: ");
        mInterstitialAd.showAd();
    }

    @Override
    public void onAdClose() {
        Log.d(TAG, "olnAdCose: ");
        if (!compile) {
            Toast.makeText(activity, "请观看完整视频获取奖励", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAdShow() {
        Log.d(TAG, "onAdShow");
    }

    @Override
    public void onAdFailed(String s) {
        Log.d(TAG, "onAdFailed: ");
        m_IPlatform2Unity.UnitySendMessage(IMsgID, -1, 0, 0, "插屏视频加载失败", "", "");
    }

    @Override
    public void onAdFailed(int i, String s) {
        Log.d(TAG, "onAdFailed: ");
        m_IPlatform2Unity.UnitySendMessage(IMsgID, -1, 0, 0, "插屏视频加载失败", "", "");
    }

    @Override
    public void onAdClick() {
        Log.d(TAG, "onAdClick: ");
    }
}
