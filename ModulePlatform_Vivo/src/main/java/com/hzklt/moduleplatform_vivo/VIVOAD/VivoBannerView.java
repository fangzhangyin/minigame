package com.hzklt.moduleplatform_vivo.VIVOAD;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.hzklt.moduleplatform_vivo.R;
import com.vivo.mobilead.banner.BannerAdParams;
import com.vivo.mobilead.banner.VivoBannerAd;
import com.vivo.mobilead.listener.IAdListener;
import com.vivo.mobilead.model.BackUrlInfo;
import com.vivo.mobilead.model.VivoAdError;

//banner广告
public class VivoBannerView extends PopupWindow {
    private static final String TAG = "Banner";
    private VivoBannerAd vivoBannerAd;
    private FrameLayout flContainer;
    private BannerAdParams adParams;
    private static VivoBannerView vivoBannerView = null;

    Activity mactivity;

    public static VivoBannerView GetInstance(Activity activity) {
        if (vivoBannerView == null) {
            vivoBannerView = new VivoBannerView(activity.getApplication(),activity);
        }
        return vivoBannerView;
    }

    public void showad() {
        flContainer.removeAllViews();
        View adView = vivoBannerAd.getAdView();
        if (adView != null) {
            flContainer.addView(adView);
        }
        flContainer.setVisibility(View.VISIBLE);
        showAtLocation(mactivity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
    }


    /***
     *
     * @param context
     */
    public VivoBannerView(Context context,Activity activity) {
        super(context);
        mactivity=activity;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.vivobannerad, null);
        flContainer = view.findViewById(R.id.fl_container);
        setContentView(view);
        initView();
//        this.setWidth(WindowsUtils.dp2px(370));
        this.setHeight(WindowsUtils.dp2px(60));
        //设置是否获得焦点
        setFocusable(false);
        //是否可以通过点击屏幕外来关闭
        setOutsideTouchable(false);
    }

    private void initView() {

    }


    public void hide() {
        if(vivoBannerAd!=null){
            vivoBannerAd.destroy();
        }
        this.dismiss();
        vivoBannerView = null;
    }

    public void loadAD(Activity activity, String BANNER_POSITION_ID) {
        BannerAdParams.Builder builder = new BannerAdParams.Builder(BANNER_POSITION_ID);
        builder.setRefreshIntervalSeconds(15);
        BackUrlInfo backUrlInfo = new BackUrlInfo("", "我是Banner的btn_Name");
        builder.setBackUrlInfo(backUrlInfo);
        adParams = builder.build();
        if (vivoBannerAd != null) {
            vivoBannerAd.destroy();
        }
        vivoBannerAd = new VivoBannerAd(activity, adParams, adListener);
    }


    IAdListener adListener = new IAdListener() {
        @Override
        public void onAdShow() {
            Log.d(TAG, "onAdShow");
        }

        @Override
        public void onAdFailed(VivoAdError error) {
            Log.d(TAG, "onAdFailed：" + error.toString());
            hide();
        }

        @Override
        public void onAdReady() {
            Log.d(TAG, "onAdReady=======可展示");
            /**
             * 此处可以开始显示广告了，即调用showAd()
             */
        }

        @Override
        public void onAdClick() {
            Log.d(TAG, "onAdClick");
        }

        @Override
        public void onAdClosed() {
            Log.d(TAG, "onAdClosed");
            hide();
        }
    };

}
