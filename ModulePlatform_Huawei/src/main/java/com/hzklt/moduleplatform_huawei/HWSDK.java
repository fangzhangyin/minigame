package com.hzklt.moduleplatform_huawei;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.api.HuaweiMobileServicesUtil;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.jos.AntiAddictionCallback;
import com.huawei.hms.jos.AppParams;
import com.huawei.hms.jos.AppUpdateClient;
import com.huawei.hms.jos.JosApps;
import com.huawei.hms.jos.JosAppsClient;
import com.huawei.hms.jos.JosStatusCodes;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.utils.ResourceLoaderUtil;
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo;
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack;
import com.hzklt.minigame.base.IPlatform2Unity;
import com.hzklt.minigame.base.PlatformBase;
import com.hzklt.moduleplatform_huawei.HuaWeiAD.AdBannerView;
import com.hzklt.moduleplatform_huawei.HuaWeiAD.AdSplashActivity;
import com.hzklt.moduleplatform_huawei.HuaWeiAD.HwGameCenter;
import com.hzklt.moduleplatform_huawei.HuaWeiAD.InsertAdActivity;
import com.hzklt.moduleplatform_huawei.HuaWeiAD.NativelView;
import com.hzklt.moduleplatform_huawei.HuaWeiAD.RewardActivity;

import java.io.Serializable;

public class HWSDK extends PlatformBase {

    public static IPlatform2Unity iPlatform2Unity;

    public void getIPlatform2Unity() {
        iPlatform2Unity = this.m_IPlatform2Unity;
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
                ShowNativeAD();
                break;
            case 5:
                Rewardvideo(Method);
                break;
            case 6:
                showsplash();
                break;
            case 7:
                login(Method);
                break;
            case 8:
                showInsertVideo();
                break;
            default:
                break;
        }
    }



    @Override
    public void HWloginCallback(int Method, Intent intent) {
        Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(intent);
        if (authAccountTask.isSuccessful()) {
            //登录成功，获取用户的帐号信息和Authorization Code
            AuthAccount authAccount = authAccountTask.getResult();
            Log.i("HWlogin", "serverAuthCode:" + authAccount.getAuthorizationCode());
            HwGameCenter.getGamePlayer(mactivity);   // 获取玩家信息
            m_IPlatform2Unity.UnitySendMessage(Method, 1, 0, 0, "华为登录成功", "", "");
        } else {
            //登录失败
            Log.e("HWlogin", "sign in failed:" + ((ApiException) authAccountTask.getException()).getStatusCode());
            Toast.makeText(mactivity, "登录才能进行游戏！", Toast.LENGTH_LONG).show();
            m_IPlatform2Unity.UnitySendMessage(Method, -1, 0, 0, "华为登录成功", "", "");
        }
        super.HWloginCallback(Method, intent);
    }

    private void initSDK(int method) {
        HuaweiMobileServicesUtil.setApplication(mactivity.getApplication());
        AccountAuthParams params = AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME;
        JosAppsClient appsClient = JosApps.getJosAppsClient(mactivity);
        Task<Void> initTask;
        ResourceLoaderUtil.setmContext(mactivity.getApplicationContext());
        initTask = appsClient.init(new AppParams(params, new AntiAddictionCallback() {
            @Override
            public void onExit() {
                //在此处实现游戏防沉迷功能，如保存游戏、调用帐号退出接口
                mactivity.finish();
            }
        }));
        initTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("HW init", "onSuccess: ");
                checkUpdate();
                m_IPlatform2Unity.UnitySendMessage(method, 1, 0, 0, "游戏中心始化完成", "", "");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    int statusCode = apiException.getStatusCode();
                    //错误码为7401时表示用户未同意华为联运隐私协议
                    if (statusCode == JosStatusCodes.JOS_PRIVACY_PROTOCOL_REJECTED) {
                        Log.d("HW init", "onFailure: ");
                        AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
                        builder.setTitle("提示信息").setMessage("同意华为联运隐私协议后才可进入游戏").setPositiveButton("重新授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                initSDK(method);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mactivity.finish();
                            }
                        }).setCancelable(false);
                        builder.show();
                    } else if (statusCode == 907135003) {//HMS Core服务安装失败
                        AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
                        builder.setTitle("提示信息").setMessage("安装华为移动服务才能进入游戏").setPositiveButton("安装重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                initSDK(method);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mactivity.finish();
                            }
                        }).setCancelable(false);
                        builder.show();
                    } else {
                        m_IPlatform2Unity.UnitySendMessage(method, -1, 0, 0, "游戏中心始化失败", "", "");
                    }
                    //在此处实现其他错误码的处理
                    Log.e("HW init", apiException.getStatusCode() + "====" + apiException.getMessage());
                }
            }
        });
    }

    private void checkUpdate() {
        AppUpdateClient client = JosApps.getAppUpdateClient(mactivity);
        client.checkAppUpdate(mactivity.getApplicationContext(), new UpdateCallBack(mactivity));
    }

    private static class UpdateCallBack implements CheckUpdateCallBack {
        private Activity apiActivity;

        private UpdateCallBack(Activity apiActivity) {
            this.apiActivity = apiActivity;
        }

        @Override
        public void onUpdateInfo(Intent intent) {
            if (intent != null) {
                Serializable info = intent.getSerializableExtra("updatesdk_update_info");
                if (info instanceof ApkUpgradeInfo) {
                    AppUpdateClient client = JosApps.getAppUpdateClient(apiActivity);
                    client.showUpdateDialog(apiActivity, (ApkUpgradeInfo) info, false);
                } else {
                    //无升级
                }
            }
        }

        // ignored
        // 预留, 无需处理
        @Override
        public void onMarketInstallInfo(Intent intent) {
            Log.w("AppUpdateManager", "info not instanceof ApkUpgradeInfo");
        }

        // ignored
        // 预留, 无需处理
        @Override
        public void onMarketStoreError(int responseCode) {
        }

        // ignored
        // 预留, 无需处理
        @Override
        public void onUpdateStoreError(int responseCode) {
        }
    }

    private void login(int method) {
        HwGameCenter.login(mactivity, m_IPlatform2Unity, method);
        scheduleTimeout();
    }


    private void initADSDSK(int method) {
        HwAds.init(mactivity);
        m_IPlatform2Unity.UnitySendMessage(method, 1, 0, 0, "广告初始化完成", "", "");
    }

    public long TIME_MILLS = 30000L;

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


    /**
     * banner广告
     */
    private void showbanner() {
        String bannerid = mactivity.getString(R.string.BannerID);
        AdBannerView adBannerView = AdBannerView.GetIntence(mactivity, bannerid);
        if(adBannerView!=null){
            adBannerView.close();
            adBannerView = AdBannerView.GetIntence(mactivity, bannerid);
        }
        adBannerView.showAd();
    }

    /**
     * 插屏视频
     */
    private void showInsertVideo() {
        String appid = mactivity.getString(R.string.InsertID);
        Intent intent = new Intent(mactivity, InsertAdActivity.class);
        intent.putExtra("id", appid);
        mactivity.startActivity(intent);
    }

    /**
     * 激励视频
     *
     * @param method
     */
    private void Rewardvideo(int method) {
        getIPlatform2Unity();
        String appid = mactivity.getString(R.string.VideoID);
        System.out.println("视频id:" + appid);
        Intent intentL = new Intent(mactivity, RewardActivity.class);
        intentL.putExtra("splashid", appid);
        intentL.putExtra("method", method);
        mactivity.startActivity(intentL);
    }

    /**
     * 插屏
     */
    private void showsplash() {
        String appid = mactivity.getString(R.string.Splashid);
        Intent intentL = new Intent(mactivity, AdSplashActivity.class);
        intentL.putExtra("splashid", appid);
        mactivity.startActivity(intentL);
    }

    /**
     * 原生广告
     */
    private void ShowNativeAD() {
        String nativeId = mactivity.getString(R.string.NativeADid);
        NativelView nativelView = NativelView.GetInstance(mactivity);
        nativelView.loadAd(nativeId);
    }

}
