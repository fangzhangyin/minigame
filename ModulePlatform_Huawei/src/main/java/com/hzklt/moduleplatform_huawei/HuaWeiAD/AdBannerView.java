package com.hzklt.moduleplatform_huawei.HuaWeiAD;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.banner.BannerView;
import com.hzklt.moduleplatform_huawei.R;


public class AdBannerView extends PopupWindow {

    public String TAG = "bannerad";

    Context mcontext;
    Activity mactivity;
    View mview;
    BannerView bannerView;

    private static AdBannerView adBannerView;

    public static AdBannerView GetIntence(Activity activity, String adid) {
        if (adBannerView == null) {
            adBannerView = new AdBannerView(activity.getApplicationContext(), 0, 0, activity, adid);
        }
        return adBannerView;
    }

    public AdBannerView(Context context, int width, int height, Activity activity, String adid) {
        super(context);
        mcontext = context;
        mactivity = activity;
        LayoutInflater inflater = LayoutInflater.from(context);
        mview = inflater.inflate(R.layout.banner_hw, null);
        setContentView(mview);
        bannerView = mview.findViewById(R.id.myhw_banner_view);
        // 设置广告位ID和广告尺寸，"testw6vs28auh3"为测试专用的广告位ID
        bannerView.setAdId(adid);
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        // 设置轮播时间间隔为30秒
        bannerView.setBannerRefresh(60);
        // 创建广告请求，加载广告
        AdParam adParam = new AdParam.Builder().build();
        bannerView.loadAd(adParam);
        bannerView.setAdListener(adListener);
//        this.setWidth(width);
        this.setHeight(WindowsUtils.dp2px(57));
        //设置是否获得焦点
        setFocusable(false);
        //是否可以通过点击屏幕外来关闭
        setOutsideTouchable(false);
    }

    public void showAd() {
        showAtLocation(mactivity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
    }

    public void close() {
        try {
            if (bannerView != null) {

                //移除悬浮窗口
                bannerView.destroy();
                bannerView = null;
            }
        } catch (Exception e) {
            Log.d(TAG, "close: "+e.getMessage());
        }finally {
            adBannerView=null;
            this.dismiss();
        }
    }

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // 广告加载成功时调用
            showAd();
            Log.d(TAG, "onAdLoaded: ");
        }

        @Override
        public void onAdFailed(int errorCode) {
            // 广告加载失败时调用
            Log.d(TAG, "onAdFailed: ");
            close();
        }

        @Override
        public void onAdOpened() {
            // 广告打开时调用
            Log.d(TAG, "onAdOpened: ");
        }

        @Override
        public void onAdClicked() {
            // 广告点击时调用
            Log.d(TAG, "onAdClicked: ");
        }

        @Override
        public void onAdLeave() {
            // 广告离开应用时调用
            Log.d(TAG, "onAdLeave: ");
        }

        @Override
        public void onAdClosed() {
            // 广告关闭时调用
            Log.d(TAG, "onAdClosed: ");
            close();
        }
    };

}
