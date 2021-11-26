package com.hzklt.moduleplatform_vivo.VIVOAD;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hzklt.moduleplatform_vivo.R;
import com.vivo.ad.model.AdError;
import com.vivo.ad.splash.SplashAdListener;
import com.vivo.mobilead.model.BackUrlInfo;
import com.vivo.mobilead.splash.SplashAdParams;
import com.vivo.mobilead.splash.VivoSplashAd;


/**
 * 广告类：VivoSplashAd
 * 可供调用接口：
 * loadAd  开始加载广告
 * 回调类：SplashAdListener
 * 包含的回调接口：
 * onADDismissed  广告结束了，触发场景：1、倒计时结束  2、点击了跳过按钮
 * onNoAD  广告请求错误
 * onADPresent 广告准备好并开始展示了
 * onADClicked 广告被点击了
 */

public class VivoSplashView extends Activity {
    private static final String TAG = "Splash";
    private SplashAdParams adParams;
    private boolean isForceMain = false;
    private String splashid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashid = this.getIntent().getStringExtra("splashid");
        initLandscapeParams();
        new VivoSplashAd(this, splashAdListener, adParams).loadAd();
    }

    protected int getLayoutRes() {
        return 0;
    }

    private void initLandscapeParams() {
        SplashAdParams.Builder builder = new SplashAdParams.Builder(splashid);
        // 拉取广告的超时时长：即开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长）取值范围[3000, 5000]
        builder.setFetchTimeout(4000);
        /**
         * 标题最长5个中文字符 描述最长8个中文字符
         */
        builder.setAppTitle(this.getBaseContext().getResources().getString(R.string.app_name));
        /**
         * 广告下面半屏的应用标题+应用描述:应用标题和应用描述是必传字段，不传将抛出异常
         */
        builder.setAppDesc("在这里驾车遨游城市吧！");
        String backUrl = "vivobrowser://browser.vivo.com?i=12";
        String btnName = "test";
        builder.setBackUrlInfo(new BackUrlInfo(backUrl, btnName));
        builder.setSplashOrientation(SplashAdParams.ORIENTATION_LANDSCAPE);
        adParams = builder.build();
    }

    /**
     * 此处逻辑可自行定义
     */
    private void goToMainActivity() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isForceMain) {
            goToMainActivity();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForceMain = true;
    }


    /**
     * 开屏广告屏蔽快捷键
     */
    @Override
    public void onBackPressed() {
        return;
    }

    private SplashAdListener splashAdListener = new SplashAdListener() {
        @Override
        public void onADDismissed() {
            Log.d(TAG, "onADDismissed");
            finish();
        }

        @Override
        public void onNoAD(AdError error) {
            Log.d(TAG, "onNoAD: " + error.toString());
            finish();
            //此处代表加载广告有误，为避免一直等待问题可干掉当前Activity，或者使用vivoSplashAd.close()接口移除当前广告页面
        }

        @Override
        public void onADPresent() {
            Log.d(TAG, "onADPresent");
            //此处广告已经自动展示在Activity上了
        }

        @Override
        public void onADClicked() {
            Log.d(TAG, "onADClicked");
        }
    };

}
