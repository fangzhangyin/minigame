package com.hzklt.moduleplatform_vivo;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.hzklt.minigame.base.IPlatform2Unity;
import com.hzklt.minigame.base.PlatformBase;
import com.hzklt.moduleplatform_vivo.VIVOAD.VivoBannerView;
import com.hzklt.moduleplatform_vivo.VIVOAD.VivoIconView;
import com.hzklt.moduleplatform_vivo.VIVOAD.VivoInterstADView;
import com.hzklt.moduleplatform_vivo.VIVOAD.VivoManagerHolder;
import com.hzklt.moduleplatform_vivo.VIVOAD.VivoNativeADView;
import com.hzklt.moduleplatform_vivo.VIVOAD.VivoRewardVideoView;
import com.hzklt.moduleplatform_vivo.VIVOAD.VivoSplashView;
import com.hzklt.moduleplatform_vivo.VIVOAD.WindowsUtils;
import com.vivo.unionsdk.open.VivoAccountCallback;
import com.vivo.unionsdk.open.VivoExitCallback;
import com.vivo.unionsdk.open.VivoUnionSDK;

public class VivoSDK extends PlatformBase {

    private static final long TIME_MILLS = 30000L;

    @Override
    public void InitCallBack(IPlatform2Unity iPlatform2Unity, Activity activity) {
        super.InitCallBack(iPlatform2Unity, activity);
    }

    @Override
    public void readMethod(int Method) {
        super.readMethod(Method);
        int tag = Method % 100;
        switch (tag) {
            case 1:
                initADSDSK(Method);
                break;
            case 2:
                initSDK(Method);
                break;
            case 3:
                showbanner();
                break;
            case 4:
                showNativeInsert();
//                showNativeAD();
                break;
            case 5:
                Rewardvideo(Method);
                break;
            case 6:
                showsplash();
                break;
            case 7:
                ShowIcon();
                break;
            case 8:
                showNativeAD();
                break;
            case -1:
                exitgame();
            default:
                break;
        }
    }

    public void scheduleTimeout() {
        showbanner();
        android.os.Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                VivoBannerView.GetInstance(mactivity).hide();
                showbanner();
                handler.postDelayed(this, TIME_MILLS);
            }
        };
        handler.postDelayed(runnable, TIME_MILLS);
    }

    private void exitgame() {
        VivoUnionSDK.exit(mactivity, new VivoExitCallback() {
            @Override
            public void onExitCancel() {

            }

            @Override
            public void onExitConfirm() {
                //????????????
                mactivity.finish();
                System.exit(0);
            }
        });
    }

    /**
     * ????????????
     */
    private void showNativeAD() {
        String id = mactivity.getString(R.string.NativeADid);
        VivoNativeADView.getInstance(mactivity);
        VivoNativeADView.vivoNativeADView.loadAD(id);
    }

    /**
     * ????????????
     */
    private void showsplash() {
        String appid = mactivity.getString(R.string.Splashid);
        Intent intentL = new Intent(mactivity, VivoSplashView.class);
        intentL.putExtra("splashid", appid);
        mactivity.startActivity(intentL);
    }

    /**
     * ??????????????????
     *
     * @param method
     */
    private void Rewardvideo(int method) {
        String appid = mactivity.getString(R.string.VideoID);
        VivoRewardVideoView vivoRewardVideoView = new VivoRewardVideoView();
        vivoRewardVideoView.loadAD(mactivity, appid, method, m_IPlatform2Unity);
    }

    /**
     * ??????????????????
     */
    private void showNativeInsert() {
        String appid = mactivity.getString(R.string.InterVideoID);
        VivoInterstADView vivoInterstADView = new VivoInterstADView();
        vivoInterstADView.loadvideoAD(mactivity, appid);
    }

    /**
     * ??????banner??????
     */
    private void showbanner() {
        String appid = mactivity.getString(R.string.BannerID);
        VivoBannerView vivoBannerView = VivoBannerView.GetInstance(mactivity);
        vivoBannerView.loadAD(mactivity, appid);
        vivoBannerView.showad();
    }

    /**
     * ?????????????????????sdk
     *
     * @param method
     */
    private void initSDK(int method) {
        String TAG = "VIVOSDK";
        VivoUnionSDK.initSdk(mactivity.getApplicationContext(), mactivity.getString(R.string.appid), false);
        VivoUnionSDK.registerAccountCallback(mactivity, new VivoAccountCallback() {
            @Override
            public void onVivoAccountLogin(String username, String openid, String authtoken) {
                Log.d(TAG, "onVivoAccountLogin: ");
                //????????????
                m_IPlatform2Unity.UnitySendMessage(method, 1, 0, 0, "????????????sdk???????????????", "", "");
                //????????????banner
                scheduleTimeout();
                //??????icon??????
                ShowIcon();
            }

            @Override
            public void onVivoAccountLogout(int i) {
                Log.d(TAG, "onVivoAccountLogout: ");
                m_IPlatform2Unity.UnitySendMessage(method, -1, 0, 0, "????????????sdk???????????????", "", "");
            }

            @Override
            public void onVivoAccountLoginCancel() {
                Log.d(TAG, "onVivoAccountLoginCancel: ");
                m_IPlatform2Unity.UnitySendMessage(method, -1, 0, 0, "????????????sdk???????????????", "", "");
            }
        });
        VivoUnionSDK.login(mactivity);
    }

    /**
     * ???????????????sdk
     *
     * @param method
     */
    private void initADSDSK(int method) {
        //?????????Vivo??????sdk
        VivoManagerHolder.init(mactivity.getApplication(), mactivity.getString(R.string.mediaid), method, m_IPlatform2Unity);
        m_IPlatform2Unity.UnitySendMessage(method, 1, 0, 0, "vivo??????suceess", "", "");
    }

    /**
     * ??????icon??????
     */
    private void ShowIcon() {
        String appid = mactivity.getString(R.string.IconID);
        VivoIconView vivoIconView = new VivoIconView();
        vivoIconView.loadAD(mactivity, appid);
    }
}
