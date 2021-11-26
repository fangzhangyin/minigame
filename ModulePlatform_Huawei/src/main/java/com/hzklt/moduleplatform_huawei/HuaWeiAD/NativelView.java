package com.hzklt.moduleplatform_huawei.HuaWeiAD;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.VideoConfiguration;
import com.huawei.hms.ads.nativead.DislikeAdListener;
import com.huawei.hms.ads.nativead.MediaView;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeAdConfiguration;
import com.huawei.hms.ads.nativead.NativeAdLoader;
import com.huawei.hms.ads.nativead.NativeView;
import com.hzklt.moduleplatform_huawei.R;


public class NativelView extends PopupWindow {
    private static final String TAG = "NativeInterstitial";

    private NativeAd nativeAd;
    private LinearLayout nativeAdContainer;
    Context mcontext;
    Activity mactivity;
    View mview;
    private NativeAd globalNativeAd;
    private ScrollView adScrollView;

    private String curNativeAdId;

    private static NativelView nativelView;

    public static NativelView GetInstance(Activity activity) {
        if (nativelView == null) {
            nativelView = new NativelView(activity.getApplicationContext(), activity);
        }
        return nativelView;
    }

    public NativelView(Context context, Activity activity) {
        super(context);
        mcontext = context;
        mactivity = activity;
        LayoutInflater inflater = LayoutInflater.from(context);
        mview = inflater.inflate(R.layout.activity_native_interstitial, null);
        mview.findViewById(R.id.button_medium_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide();
            }
        });
        setContentView(mview);
        adScrollView = mview.findViewById(R.id.scroll_view_ad1);
    }

    public void hide() {
        adScrollView.removeAllViews();
        this.dismiss();
    }

    public void loadAd(String adId) {
        Log.d("0", "app loadAd 原生广告：" + adId);
        NativeAdLoader.Builder builder = new NativeAdLoader.Builder(mcontext, adId);
        builder.setNativeAdLoadedListener(new NativeAd.NativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                // Call this method when an ad is successfully loaded.
                // Display native ad.
                Log.i(TAG, "原生加载成功");
                showNativeAd(nativeAd);
            }
        }).setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d(TAG, "onAdClosed: ");
            }

            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailed(int errorCode) {
                // Call this method when an ad fails to be loaded.
                Log.i(TAG, "原生加载失败");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.d(TAG, "广告点击");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.d(TAG, "广告？？？");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.d(TAG, "广告已经点击");
            }

            @Override
            public void onAdLeave() {
                super.onAdLeave();
                Log.d(TAG, "广告离开");
                hide();
            }
        });

        VideoConfiguration videoConfiguration = new VideoConfiguration.Builder()
                .setStartMuted(true)
                .build();

        NativeAdConfiguration adConfiguration = new NativeAdConfiguration.Builder()
                .setChoicesPosition(NativeAdConfiguration.ChoicesPosition.BOTTOM_RIGHT) // Set custom attributes.
                .setVideoConfiguration(videoConfiguration)
                .setRequestMultiImages(true)
                .build();

        NativeAdLoader nativeAdLoader = builder.setNativeAdOptions(adConfiguration).build();
        nativeAdLoader.loadAd(new AdParam.Builder().build());
    }

    private View createNativeView(NativeAd nativeAd, ViewGroup parentView) {
        int createType = nativeAd.getCreativeType();
        Log.i(TAG, "Native ad createType is " + createType);
        if (createType == 2 || createType == 102) {
            // Large image
            return NativeViewFactory.createImageOnlyAdView(nativeAd, parentView);
        } else if (createType == 3 || createType == 6) {
            // Large image with text or video with text
            return NativeViewFactory.createMediumAdView(nativeAd, parentView);
        } else if (createType == 103 || createType == 106) {
            // Large image with text or Video with text, using AppDownloadButton template.
            return NativeViewFactory.createAppDownloadButtonAdView(nativeAd, parentView);
        } else if (createType == 7 || createType == 107) {
            // Small image with text-
            return NativeViewFactory.createSmallImageAdView(nativeAd, parentView);
        } else if (createType == 8 || createType == 108) {
            // Three small images with text
            return NativeViewFactory.createThreeImagesAdView(nativeAd, parentView);
        } else {
            // Undefined creative type
            return null;
        }
    }

    private void showNativeAd(NativeAd nativeAd) {
        // Destroy the original native ad.

        if (null != globalNativeAd) {
            globalNativeAd.destroy();
        }
        globalNativeAd = nativeAd;

        final View nativeView = createNativeView(nativeAd, adScrollView);
        if (nativeView != null) {
            Log.i(TAG, "展示原生");
            globalNativeAd.setDislikeAdListener(new DislikeAdListener() {
                @Override
                public void onAdDisliked() {
                    // Call this method when an ad is closed.
                    adScrollView.removeView(nativeView);
                }
            });

            // Add NativeView to the app UI.
            adScrollView.removeAllViews();
            adScrollView.addView(nativeView);
            //视图和手机屏幕一样宽
//            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            showAtLocation(mactivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    }

    private void initNativeAdView(NativeAd nativeAd, NativeView nativeView) {
        // 注册和填充标题素材视图
        nativeView.setTitleView(nativeView.findViewById(R.id.ad_title_tv));
        ((TextView) nativeView.getTitleView()).setText(nativeAd.getTitle());
        // 注册和填充多媒体素材视图
        nativeView.setMediaView((MediaView) nativeView.findViewById(R.id.ad_media));
        nativeView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        // 注册和填充其他素材视图
        // 注册原生广告对象
        nativeView.setNativeAd(nativeAd);
    }

}
