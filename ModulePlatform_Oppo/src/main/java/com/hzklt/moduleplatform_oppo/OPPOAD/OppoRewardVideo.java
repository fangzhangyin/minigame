package com.hzklt.moduleplatform_oppo.OPPOAD;

import android.content.Context;
import android.util.Log;

import com.heytap.msp.mobad.api.ad.RewardVideoAd;
import com.heytap.msp.mobad.api.listener.IRewardVideoAdListener;
import com.heytap.msp.mobad.api.params.RewardVideoAdParams;
import com.hzklt.minigame.base.IPlatform2Unity;
import com.hzklt.moduleplatform_oppo.R;

import org.json.JSONException;
import org.json.JSONObject;

public class OppoRewardVideo implements IRewardVideoAdListener {

    private static final String TAG = "OppoRewardVideo";

    IPlatform2Unity m_IPlatform2Unity;

    int IMsgID;
    /**
     * add 2018-10-25
     * TODO 激励视频广告前置引导提示，主要用于提示用户如何操作才能获取奖励。目前支持三种种激励场景：1、视频播放完成；2、点击下载安装完成获取激励；3、点击下载安装后打开获取激励。应用在接入的时候，需要根据自己应用场景，对用户进行一定的引导。
     */
    private static final String REWARD_SCENE_PLAY_COMPLETE_TIPS = "视频播放完成可以获取一次免费复活机会";
    private static final String REWARD_SCENE_INSTALL_COMPLETE_TIPS = "应用安装完成可以获取一次免费复活机会";
    private static final String REWARD_SCENE_LAUNCH_APP_TIPS = "应用安装完成点击打开可以获取一次免费复活机会";
    //
    private static final String REWARD_TOAST_TEXT = "完成任务、恭喜成功获取一次免费复活机会";
    //
    private RewardVideoAd mRewardVideoAd;

    public void loadad(Context context, IPlatform2Unity iPlatform2Unity, int iMsgId) {
        mRewardVideoAd = new RewardVideoAd(context, context.getString(R.string.VideoID), this);
        /**
         * 调用loadAd方法请求激励视频广告;通过RewardVideoAdParams.setFetchTimeout方法可以设置请求
         * 视频广告最大超时时间，单位毫秒；
         */
        RewardVideoAdParams rewardVideoAdParams = new RewardVideoAdParams.Builder()
                .setFetchTimeout(3000)
                .build();
        mRewardVideoAd.loadAd(rewardVideoAdParams);
        m_IPlatform2Unity = iPlatform2Unity;
        IMsgID = iMsgId;
    }

    private void playVideo() {
        /**
         * TODO 在播放广告时候，先调用isReady方法判断当前是否有广告可以播放；如果有、再调用showAd方法播放激励视频广告。
         */
        if (mRewardVideoAd.isReady()) {
            mRewardVideoAd.showAd();
        }
    }


    @Override
    public void onAdSuccess() {
        Log.d(TAG, "onAdSuccess");
        playVideo();
    }

    private void destroyVideo() {
        /**
         * 销毁激励视频广告
         */
        mRewardVideoAd.destroyAd();
    }

    @Override
    public void onAdFailed(String s) {
        Log.d(TAG, "onAdFailed===" + s);
    }

    @Override
    public void onAdFailed(int i, String s) {
        Log.d(TAG, "onAdFailed===" + s);
    }

    @Override
    public void onAdClick(long l) {
        Log.d(TAG, "onAdClick===" + l);
    }

    @Override
    public void onVideoPlayStart() {
        Log.d(TAG, "onVideoPlayStart");
    }

    @Override
    public void onVideoPlayComplete() {
        Log.d(TAG, "onVideoPlayComplete");
        //方法回调
        m_IPlatform2Unity.UnitySendMessage(IMsgID, 1, 0, 0, "激励视频播放成功", "", "");

    }

    @Override
    public void onVideoPlayError(String s) {
        Log.d(TAG, "onVideoPlayError===" + s);
        //方法回调
        m_IPlatform2Unity.UnitySendMessage(IMsgID, -1, 0, 0, "激励视频播放成功", "", "");
    }

    @Override
    public void onVideoPlayClose(long l) {
        Log.d(TAG, "onVideoPlayClose===" + l);
    }

    @Override
    public void onLandingPageOpen() {
        Log.d(TAG, "onLandingPageOpen");
    }

    @Override
    public void onLandingPageClose() {
        Log.d(TAG, "onLandingPageClose");
    }

    @Override
    public void onReward(Object... objects) {
        Log.d(TAG, "onReward");
    }
}
