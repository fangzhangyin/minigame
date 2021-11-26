package com.hzklt.moduleplatform_oppo.OPPOAD;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.heytap.msp.mobad.api.ad.LandSplashAd;
import com.heytap.msp.mobad.api.listener.ISplashAdListener;
import com.heytap.msp.mobad.api.params.SplashAdParams;
import com.hzklt.moduleplatform_oppo.R;

import org.jetbrains.annotations.Nullable;

public class OppoSplashView extends Activity implements ISplashAdListener {
    private static final String TAG = "SplashView";
    private static final int REQUEST_PERMISSIONS_CODE = 100;
    /**
     * 从请求广告到广告展示出来最大耗时时间，只能在[3000,5000]ms之内。
     */
    private static final int FETCH_TIME_OUT = 3000;
    /**
     * 闪屏广告是半屏广告，广告下面半屏是:应用ICON+应用标题+应用描述，
     * 应用标题和应用描述由应用在SplashAd构造函数里传入，
     * 应用标题限制最多不超过个 8 个汉字，应用描述限制不超过 13 个汉字。
     */
    private static final String APP_TITLE = "OPPO广告联盟";
    private static final String APP_DESC = "让天下没有难做的广告";
    private static String LAND_SPLASH_POS_ID = "";

    private LandSplashAd mLandSplashAd;
    /**
     * 判断是否可以立刻跳转应用主页面。
     */
    private boolean mCanJump = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在Android P上，必须制定凹型区域显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
        //
        setContentView(R.layout.activity_splash);
        LAND_SPLASH_POS_ID = this.getIntent().getStringExtra("splashid");
        fetchSplashAd();
    }

    private void fetchSplashAd() {
        try {
            /**
             * SplashAd初始化参数、这里可以设置获取广告最大超时时间，
             */
            //
            SplashAdParams splashAdParams = new SplashAdParams.Builder()
                    .setFetchTimeout(FETCH_TIME_OUT)
                    .setTitle(APP_TITLE)
                    .setDesc(APP_DESC)
                    .build();
            /**
             * 构造SplashAd对象
             * 注意：构造函数传入的几个形参都不能为空，否则将抛出NullPointerException异常。
             */
            mLandSplashAd = new LandSplashAd(this, LAND_SPLASH_POS_ID, this, splashAdParams);
        } catch (Exception e) {
            Log.w(TAG, "", e);
            /**
             *  出错，直接finish(),跳转应用主页面。
             */
            finish();
        }
    }

    /**
     * 结束闪屏页面，跳转主页面。
     */
    private void next() {
        if (mCanJump) {
            finish();
        } else {
            mCanJump = true;
        }
    }


    @Override
    public void onAdShow() {
        Log.d(TAG, "onAdShow");
    }

    @Override
    public void onAdShow(String s) {
        Log.d(TAG, "onAdShow:" + s);
    }

    @Override
    public void onAdFailed(String errMsg) {
        // 已废弃，使用onAdFailed(int i, String s)
    }

    @Override
    public void onAdFailed(int i, String s) {
        /**
         * 如果加载广告失败，直接finish(),跳转应用主页面。
         */
        Log.d(TAG, "onAdFailed code:" + i + ",msg:" + s);
        finish();
    }

    @Override
    public void onAdClick() {
        Log.d(TAG, "onAdClick");
    }

    @Override
    public void onAdDismissed() {
        /**
         *广告播放完毕或者用户点击“跳过”按钮，跳转应用主页面。
         */
        Log.d(TAG, "onAdDismissed");
        next();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        /**
         * 这里包含对于点击闪屏广告以后、然后返回闪屏广告页面立刻跳转应用主页面的处理。
         */
        if (mCanJump) {
            next();
        }
        mCanJump = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        /**
         * 这里包含对于点击闪屏广告以后、然后返回闪屏广告页面立刻跳转应用主页面的处理。
         */
        mCanJump = false;
    }

    @Override
    protected void onDestroy() {
        if (null != mLandSplashAd) {
            mLandSplashAd.destroyAd();
        }
        super.onDestroy();
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费。
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
