package com.hzklt.moduleplatform_huawei.HuaWeiAD;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.banner.BannerView;


public class AdBannerExpressView extends PopupWindow {
    private String TAG = "huawei AdBannerExpressView";
    private ViewGroup container;
    private String BANNER_DOWNLOAD_ID;
    private String position;
    private Activity activity;
    private LinearLayout mFloatLayout;
    private WindowManager mWindowManager;
    private int x;
    private int y;
    private int w;
    private int h;
    private static AdBannerExpressView showView;
    private BannerView bannerView;

    public static AdBannerExpressView getInstance(Activity context, String appid, String position, int x, int y, int imgWidth, int imgHeight) {
        if (null == showView) {
            showView = new AdBannerExpressView(context, appid, position, x, y, imgWidth, imgHeight);
        }
        return showView;
    }

    private AdBannerExpressView(Activity context, String adid, String position, int x, int y, int imgWidth, int imgHeight) {
        BANNER_DOWNLOAD_ID = adid;
        activity = context;
        this.position = position;
        this.x = x;
        this.y = y;

        this.w = UIUtils.px2dip(context, imgWidth);
        this.h = UIUtils.px2dip(context, imgHeight);
        initview();

        bannerView = new BannerView(context.getApplicationContext());

        // Set an ad slot ID.
        bannerView.setAdId(adid);
        // Set the background color and size based on user selection.
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
//        setContentView(bannerView);
        mFloatLayout.addView(bannerView);
        bannerView.setBannerRefresh(30);
        bannerView.setAdListener(adListener);
        bannerView.loadAd(new AdParam.Builder().build());

    }

    private void initview() {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) activity.getSystemService(activity.WINDOW_SERVICE);
        wmParams.gravity = Gravity.TOP;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        wmParams.y = y;
        wmParams.x = x;
        LayoutInflater inflater = LayoutInflater.from(activity);
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(ResourceManger.getId(activity, "R.layout.activity_toutiao_banner_ad"), null);
        mWindowManager.addView(mFloatLayout, wmParams);
//        activity.addContentView(mFloatLayout, wmParams);
        container = (ViewGroup) mFloatLayout.findViewById(ResourceManger.getId(activity, "R.id.toutiao_banner_container"));
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // 广告加载成功时调用
            Log.d(TAG, "banner 广告加载成功");
        }

        @Override
        public void onAdFailed(int errorCode) {
            // 广告加载失败时调用
            Log.d(TAG, "banner 广告加载失败");
        }

        @Override
        public void onAdOpened() {
            // 广告打开时调用
            Log.d(TAG, "banner 广告打开");
        }

        @Override
        public void onAdClicked() {
            // 广告点击时调用
            Log.d(TAG, "banner 广告点击");
        }

        @Override
        public void onAdLeave() {
            // 广告离开应用时调用
            Log.d(TAG, "banner 广告离开");
        }

        @Override
        public void onAdClosed() {
            // 广告关闭时调用
            Log.d(TAG, "banner 广告关闭");
            closeAd();
        }
    };


    private void displayAD() {
    }

    public void showAd() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void closeAd() {
        try {
            if (mFloatLayout != null) {
                //移除悬浮窗口
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mFloatLayout.setVisibility(View.INVISIBLE);
                            bannerView.destroy();
                            bannerView = null;
                            dismiss();
                        } catch (Exception e) {
                        }
                    }
                });
            }
            showView = null;
        } catch (Exception e) {
        }
    }
}
