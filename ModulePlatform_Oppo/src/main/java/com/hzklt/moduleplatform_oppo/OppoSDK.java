package com.hzklt.moduleplatform_oppo;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.heytap.msp.mobad.api.InitParams;
import com.heytap.msp.mobad.api.MobAdManager;
import com.heytap.msp.mobad.api.listener.IInitListener;
import com.hzklt.minigame.base.IPlatform2Unity;
import com.hzklt.minigame.base.PlatformBase;
import com.hzklt.moduleplatform_oppo.OPPOAD.OppoBannerView;
import com.hzklt.moduleplatform_oppo.OPPOAD.OppoNativeBanner;
import com.hzklt.moduleplatform_oppo.OPPOAD.OppoNativeInsertView;
import com.hzklt.moduleplatform_oppo.OPPOAD.OppoRewardVideo;
import com.hzklt.moduleplatform_oppo.OPPOAD.OppoSplashView;
import com.hzklt.moduleplatform_oppo.OPPOAD.InterstitialVideo;
import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.ApiCallback;
import com.nearme.game.sdk.callback.GameExitCallback;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

public class OppoSDK extends PlatformBase {

    private static final long TIME_MILLS = 60000L;


    @Override
    public void readMethod(int Method) {
        super.readMethod(Method);
        int tag = Method % 100;
        switch (tag) {
            case 1:
                initADSDSK(Method); //初始化广告sdk
                break;
            case 2:
                initSDK(Method);    //接入游戏中心
                break;
            case 3:
//                shownativebanner();       //banner
                showbanner();
                break;
            case 4:
                showNativeInsert(); //原生插屏
                break;
            case 5:
                Rewardvideo(Method); //激励视频
                break;
            case 6:
                showsplash();     //开屏
                break;
            case 7:
                jumpgamecenter();    //更多精彩
                break;
            case 8:
                InsertVideo(Method);   //插屏视频
                break;
            case 9:
                showbanner();
                break;
            case -1:
                exitgame();
                break;
            default:
                break;
        }
    }

    private void exitgame() {
        GameCenterSDK.getInstance().onExit(mactivity, new GameExitCallback() {
            @Override
            public void exitGame() {
                mactivity.finish();
                mactivity = null;
                System.exit(0);
            }
        });
    }


    public void scheduleTimeout() {
        showbanner();
        android.os.Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                showbanner();
                handler.postDelayed(this, TIME_MILLS);
            }
        };
        handler.postDelayed(runnable, TIME_MILLS);
    }

    private void showbanner() {
        if(OppoBannerView.oppoBannerView!=null){
            OppoBannerView.oppoBannerView.hide();
        }
        String id = mactivity.getString(R.string.bannerid);
        OppoBannerView.getInstance(mactivity, id);
        OppoBannerView.oppoBannerView.loadAD();
    }

    /**
     * 接入游戏中心
     *
     * @param iMsgId
     */
    public void initSDK(int iMsgId) {
        //初始化游戏中心
        String appSecret = mactivity.getString(R.string.appSecret);
        GameCenterSDK.init(appSecret, mactivity.getApplicationContext());
        GameCenterSDK.getInstance().doLogin(mactivity, new ApiCallback() {
            @Override
            public void onSuccess(String s) {
                SendMessageToUnity(iMsgId, 1, 0, 0, "游戏中心初始化成功", "", "");
                //定时执行展示banner;
                scheduleTimeout();
            }

            @Override
            public void onFailure(String s, int i) {
                Toast.makeText(mactivity.getApplicationContext(), "登录失败！请登录后进入游戏", Toast.LENGTH_LONG);
                SendMessageToUnity(iMsgId, -1, 0, 0, "游戏中心初始化失败", "", "");
                mactivity.finish();
            }
        });
    }

    /**
     * 初始化广告sdk
     *
     * @param iMsgId
     */
    public void initADSDSK(int iMsgId) {
        String appid = mactivity.getString(R.string.appid);
        InitParams initParams = new InitParams.Builder()
                .setDebug(true)
                //true打开SDK日志，当应用发布Release版本时，必须注释掉这行代码的调用，或者设为false
                .build();
        MobAdManager.getInstance().init(mactivity, appid, initParams, new IInitListener() {
            @Override
            public void onSuccess() {
                Log.d("MobAdDemoApp", "IInitListener onSuccess");
                SendMessageToUnity(iMsgId, 1, 0, 0, "广告sdk初始化成功", "", "");
            }

            @Override
            public void onFailed(String s) {
                Log.d("MobAdDemoApp", "IInitListener onFailed====" + s);
                SendMessageToUnity(iMsgId, -1, 0, 0, "广告sdk初始化失败", "", "");
            }
        });
    }

    /**
     * 展示banner
     */
    public void shownativebanner() {
        String id = mactivity.getString(R.string.NativeADid);
        OppoNativeBanner oppoNativeBanner = OppoNativeBanner.GetInstance(mactivity);
        oppoNativeBanner.loadAD(id);
    }

    /**
     * oppo原生插屏
     */
    public void showNativeInsert() {
        String id = mactivity.getString(R.string.NativeADid);
        OppoNativeInsertView oppoNativeInsertView = OppoNativeInsertView.GetInstence(mactivity);
        oppoNativeInsertView.loadAd(id);
    }

    /**
     * 激励视频
     *
     * @param iMsgId
     */
    public void Rewardvideo(int iMsgId) {
        OppoRewardVideo oppoRewardVideo = new OppoRewardVideo();
        oppoRewardVideo.loadad(mactivity.getApplicationContext(), m_IPlatform2Unity, iMsgId);
    }

    public void showsplash() {
        String appid = mactivity.getString(R.string.Splashid);
        Intent intentL = new Intent(mactivity, OppoSplashView.class);
        intentL.putExtra("splashid", appid);
        mactivity.startActivity(intentL);
    }

    public void jumpgamecenter() {
        GameCenterSDK.getInstance().jumpLeisureSubject();
    }

    public void InsertVideo(int iMsgId) {
        InterstitialVideo.getInstance(mactivity, m_IPlatform2Unity, iMsgId).loadAD();
    }


}
