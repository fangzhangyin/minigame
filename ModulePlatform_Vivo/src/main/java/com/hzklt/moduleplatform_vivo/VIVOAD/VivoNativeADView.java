package com.hzklt.moduleplatform_vivo.VIVOAD;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hzklt.moduleplatform_vivo.R;
import com.squareup.picasso.Picasso;
import com.vivo.ad.model.AdError;
import com.vivo.ad.nativead.NativeAdListener;
import com.vivo.ad.nativead.NativeResponse;
import com.vivo.mobilead.nativead.NativeAdParams;
import com.vivo.mobilead.nativead.VivoNativeAd;
import com.vivo.mobilead.unified.base.VivoAdError;
import com.vivo.mobilead.unified.base.callback.MediaListener;
import com.vivo.mobilead.unified.base.view.NativeVideoView;
import com.vivo.mobilead.unified.base.view.VivoNativeAdContainer;

import java.util.List;

public class VivoNativeADView extends PopupWindow {

    /**
     * 网址类
     */
    int AD_WEBSITE = 1;
    /**
     * 应用下载
     */
    int AD_APP_DOWNLOADER = 2;
    /**
     * 快生态
     */
    int AD_RPK = 8;
    /**
     * 预约类广告
     */
    int AD_APP_APPOINTMENT = 9;

    String TAG = "VivoNativeADView";

    VivoNativeAd mvivoNativeAd;
    private NativeResponse nativeResponse;
    private FrameLayout flContainer;
    private NativeVideoView videoView;
    Activity mactivity;
    public static VivoNativeADView vivoNativeADView;

    public static VivoNativeADView getInstance(Activity activity) {
        if (vivoNativeADView == null) {
            vivoNativeADView = new VivoNativeADView(activity.getApplicationContext(),activity);
        }
        return vivoNativeADView;
    }

    public void hide() {
        this.dismiss();
        vivoNativeADView = null;
    }

    public VivoNativeADView(Context context, Activity mactivity) {
        super(context);
        this.mactivity = mactivity;
        LayoutInflater inflater = LayoutInflater.from(context);
        View mview = inflater.inflate(R.layout.vivonativeadwin, null);
        setContentView(mview);
        setFocusable(false);
        setTouchable(false);
        flContainer = mview.findViewById(R.id.fl_container);

    }

    public void loadAD(String id) {
        NativeAdParams.Builder builder = new NativeAdParams.Builder(id);
        mvivoNativeAd = new VivoNativeAd(mactivity, builder.build(), nativeAdListener);
        mvivoNativeAd.loadAd();
    }

    public void showAD() {
        if (nativeResponse != null) {
            flContainer.removeAllViews();
            switch (nativeResponse.getMaterialMode()) {
                case NativeResponse.MODE_VIDEO:
                    showVideo(nativeResponse, flContainer);
                    break;
                case NativeResponse.MODE_GROUP:
                    showMultiImageAd(nativeResponse, flContainer);
                    break;
                case NativeResponse.MODE_LARGE:
                    showLargeImageAd(nativeResponse, flContainer);
                    break;
                case NativeResponse.MODE_SMALL:
                    showTinyImageAd(nativeResponse, flContainer);
                    break;
                case NativeResponse.MODE_UNKNOW:
                    showTinyImageAd(nativeResponse, flContainer);
                    break;
            }
            showAtLocation(mactivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    }

    private void showTinyImageAd(NativeResponse nativeResponse, FrameLayout flContainer) {
        VivoNativeAdContainer vivoNativeAdContainer = (VivoNativeAdContainer) LayoutInflater.from(mactivity.getApplicationContext()).inflate(R.layout.layout_stream_tiny_image, null);
        ImageView ivImage = vivoNativeAdContainer.findViewById(R.id.iv_image);
        TextView tvTitle = vivoNativeAdContainer.findViewById(R.id.tv_title);
        //设置广告图片
        Picasso.with(mactivity.getApplicationContext()).load(nativeResponse.getImgUrl().get(0)).into(ivImage);
        //设置标题
        tvTitle.setText(nativeResponse.getTitle());
        //必须添加广告logo 否者审核不通过
        renderAdLogoAndTag(nativeResponse, vivoNativeAdContainer);
        //添加广告到视图树中
        flContainer.addView(vivoNativeAdContainer);
        nativeResponse.registerView(vivoNativeAdContainer, null, null);
    }

    private void showLargeImageAd(NativeResponse nativeResponse, FrameLayout flContainer) {
        final VivoNativeAdContainer adView = (VivoNativeAdContainer) LayoutInflater.from(mactivity.getApplicationContext()).inflate(R.layout.layout_stream_large_image, null);
        final ImageView ivImage = adView.findViewById(R.id.iv_image);
        ImageView ivIcon = adView.findViewById(R.id.iv_icon);
        LinearLayout llAppInfo = adView.findViewById(R.id.ll_app_info);
        TextView tvAppTitle = adView.findViewById(R.id.tv_app_title);
        Button btnInstall = adView.findViewById(R.id.btn_install);
        TextView tvTitle = adView.findViewById(R.id.tv_title);

        //广告视图成功添加后，根据下发图片尺寸等比缩放 广告ImageView
        adView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    adView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    adView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int bitmapWidth = nativeResponse.getImgDimensions()[0];
                int bitmapHeight = nativeResponse.getImgDimensions()[1];
                float imageViewWidth = ivImage.getMeasuredWidth();
                int imageViewHeight = Math.round(imageViewWidth / bitmapWidth * bitmapHeight);

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
                lp.height = imageViewHeight;
                ivImage.setLayoutParams(lp);

                if (!nativeResponse.getImgUrl().isEmpty()) {
                    Picasso.with(mactivity.getApplicationContext()).load(nativeResponse.getImgUrl().get(0)).noFade().into(ivImage);
                }


            }
        });
        //展示广告ICON
        if (!TextUtils.isEmpty(nativeResponse.getIconUrl())) {
            Picasso.with(mactivity.getApplicationContext()).load(nativeResponse.getIconUrl()).into(ivIcon);
        }
        //网址类广告不需要有应用信息
        if (nativeResponse.getAdType() == AD_WEBSITE) {
            llAppInfo.setVisibility(View.GONE);
            tvTitle.setText(nativeResponse.getTitle());
        } else {
            tvTitle.setVisibility(View.GONE);
            tvAppTitle.setText(nativeResponse.getTitle());
            //快生态不需要展示操作按钮
            if (nativeResponse.getAdType() == AD_RPK) {
                btnInstall.setVisibility(View.GONE);
            } else {
                setButton(nativeResponse, btnInstall);
            }
        }
        //必须添加广告logo 否者审核不通过
        renderAdLogoAndTag(nativeResponse, adView);
        flContainer.addView(adView);
        nativeResponse.registerView(adView, null, btnInstall);
    }

    private void showMultiImageAd(NativeResponse nativeResponse, FrameLayout flContainer) {
        final VivoNativeAdContainer adView = (VivoNativeAdContainer) LayoutInflater.from(mactivity.getApplicationContext()).inflate(R.layout.layout_stream_multi_image, null);
        final LinearLayout llMultiImage = adView.findViewById(R.id.ll_multi_image);
        final ImageView ivImage = adView.findViewById(R.id.iv_image);
        final ImageView ivImage1 = adView.findViewById(R.id.iv_image1);
        final ImageView ivImage2 = adView.findViewById(R.id.iv_image2);
        LinearLayout llAppInfo = adView.findViewById(R.id.ll_app_info);
        TextView tvAppTitle = adView.findViewById(R.id.tv_app_title);
        Button btnInstall = adView.findViewById(R.id.btn_install);

        //广告视图成功添加后，根据下发图片尺寸等比缩放 广告ImageView
        adView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    adView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    adView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int bitmapWidth = nativeResponse.getImgDimensions()[0];
                int bitmapHeight = nativeResponse.getImgDimensions()[1];
                int layoutWidth = llMultiImage.getMeasuredWidth();

                //组图样式 图片固定为 3 张, 根据图片规格等比缩放广告ImageView
                int adImageWidth = (layoutWidth - dp2px(mactivity.getApplicationContext(), 2)) / 3;
                float ratio = adImageWidth / (float) bitmapWidth;
                int adImageHeight = Math.round(ratio * bitmapHeight);

                LinearLayout.LayoutParams adLayoutParams = (LinearLayout.LayoutParams) ivImage.getLayoutParams();
                adLayoutParams.width = adImageWidth;
                adLayoutParams.height = adImageHeight;
                ivImage.setLayoutParams(adLayoutParams);

                LinearLayout.LayoutParams adLayoutParams1 = (LinearLayout.LayoutParams) ivImage1.getLayoutParams();
                adLayoutParams1.width = adImageWidth;
                adLayoutParams1.height = adImageHeight;
                ivImage1.setLayoutParams(adLayoutParams1);

                LinearLayout.LayoutParams adLayoutParams2 = (LinearLayout.LayoutParams) ivImage2.getLayoutParams();
                adLayoutParams2.width = adImageWidth;
                adLayoutParams2.height = adImageHeight;
                ivImage2.setLayoutParams(adLayoutParams2);

                Picasso.with(mactivity.getApplicationContext()).load(nativeResponse.getImgUrl().get(0)).noFade().into(ivImage);
                Picasso.with(mactivity.getApplicationContext()).load(nativeResponse.getImgUrl().get(1)).noFade().into(ivImage1);
                Picasso.with(mactivity.getApplicationContext()).load(nativeResponse.getImgUrl().get(2)).noFade().into(ivImage2);
            }
        });
        //网址类广告不需要有应用信息
        if (nativeResponse.getAdType() == AD_WEBSITE) {
            llAppInfo.setVisibility(View.GONE);
        } else {
            tvAppTitle.setText(nativeResponse.getTitle());
            //快生态不需要展示操作按钮
            if (nativeResponse.getAdType() == AD_RPK) {
                btnInstall.setVisibility(View.GONE);
            } else {
                setButton(nativeResponse, btnInstall);
            }
        }
        //必须添加广告logo 否者审核不通过
        renderAdLogoAndTag(nativeResponse, adView);
        //添加广告到视图树中
        flContainer.addView(adView);
        nativeResponse.registerView(adView, null, btnInstall);
    }

    private void setButton(NativeResponse nativeResponse, Button btn_adInstall) {
        switch (nativeResponse.getAPPStatus()) {
            case 0:
                btn_adInstall.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.bg_install_btn));
                break;
            case 1:
                btn_adInstall.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.bg_open_btn));
                break;
            default:
                btn_adInstall.setBackgroundDrawable(mactivity.getResources().getDrawable(R.drawable.bg_detail_btn));
                break;
        }
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private void showVideo(NativeResponse nativeResponse, FrameLayout flContainer) {
        VivoNativeAdContainer vivoNativeAdContainer = (VivoNativeAdContainer) LayoutInflater.from(mactivity.getApplicationContext()).inflate(R.layout.layout_stream_video, null);
        videoView = vivoNativeAdContainer.findViewById(R.id.nvv_video);
        Button btnInstall = vivoNativeAdContainer.findViewById(R.id.btn_install);
        TextView tvTitle = vivoNativeAdContainer.findViewById(R.id.tv_title);
        tvTitle.setText(nativeResponse.getTitle());
        //必须添加广告logo 否者审核不通过
        renderAdLogoAndTag(nativeResponse, vivoNativeAdContainer);
        //添加广告到视图树中
        flContainer.addView(vivoNativeAdContainer);
        /**
         * 原生视频一定要注册这个方法，不然视频无法播放
         */
        nativeResponse.registerView(vivoNativeAdContainer, null, btnInstall, videoView);
        /**
         * 播放时机接入方可自己参照时机设置，但是一定要在registerView方法之后才能播放
         */
        videoView.start();    //此为自动播放
        videoView.setMediaListener(new MediaListener() {
            @Override
            public void onVideoStart() {
                Log.v(TAG, "onVideoStart");
            }

            @Override
            public void onVideoPause() {
                Log.v(TAG, "onVideoPause");
            }

            @Override
            public void onVideoPlay() {
                Log.v(TAG, "onVideoPlay");
            }

            @Override
            public void onVideoError(VivoAdError error) {
                Log.v(TAG, "onVideoError");
            }

            @Override
            public void onVideoCompletion() {
                Log.v(TAG, "onVideoCompletion");
            }

            @Override
            public void onVideoCached() {
                Log.v(TAG, "onVideoCached");
            }
        });
    }

    private void renderAdLogoAndTag(NativeResponse nativeResponse, VivoNativeAdContainer vivoNativeAdContainer) {
        ImageView ivAdMarkLogo = vivoNativeAdContainer.findViewById(R.id.iv_ad_mark_logo);
        TextView tvAdMarkText = vivoNativeAdContainer.findViewById(R.id.tv_ad_mark_text);

        if (nativeResponse.getAdLogo() != null) {
            ivAdMarkLogo.setVisibility(View.VISIBLE);
            tvAdMarkText.setVisibility(View.GONE);
            ivAdMarkLogo.setImageBitmap(nativeResponse.getAdLogo());
        } else if (!TextUtils.isEmpty(nativeResponse.getAdMarkUrl())) {
            ivAdMarkLogo.setVisibility(View.VISIBLE);
            tvAdMarkText.setVisibility(View.GONE);
            Picasso.with(mactivity.getApplicationContext())
                    .load(nativeResponse.getAdMarkUrl())
                    .into(ivAdMarkLogo);
        } else {
            String adMark;
            if (!TextUtils.isEmpty(nativeResponse.getAdMarkText())) {
                adMark = nativeResponse.getAdMarkText();
            } else if (!TextUtils.isEmpty(nativeResponse.getAdTag())) {
                adMark = nativeResponse.getAdTag();
            } else {
                adMark = "广告";
            }

            tvAdMarkText.setVisibility(View.VISIBLE);
            ivAdMarkLogo.setVisibility(View.GONE);
            tvAdMarkText.setText(adMark);
        }
    }

    private NativeAdListener nativeAdListener = new NativeAdListener() {
        @Override
        public void onADLoaded(List<NativeResponse> nativeResponses) {
            if (nativeResponses != null && nativeResponses.size() > 0 && nativeResponses.get(0) != null) {  //此处有广告了，可以根据广告类型展示广告
                nativeResponse = nativeResponses.get(0);
                Log.i(TAG, "onADLoaded");
                showAD();
            }
        }

        @Override
        public void onNoAD(AdError adError) {
            Log.i(TAG, "onNoAD:" + adError);
        }

        @Override
        public void onClick(NativeResponse nativeResponse) {
            Log.i(TAG, "onClick");
        }

        @Override
        public void onAdShow(NativeResponse nativeResponse) {
            Log.i(TAG, "onAdShow");
        }
    };
}
