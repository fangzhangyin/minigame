package com.hzklt.moduleplatform_oppo.OPPOAD;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.heytap.msp.mobad.api.ad.BannerAd;
import com.heytap.msp.mobad.api.listener.IBannerAdListener;
import com.hzklt.moduleplatform_oppo.R;


public class OppoBannerView extends PopupWindow implements IBannerAdListener {

    private String TAG = "OppoBannerView";

    private BannerAd bannerAd;
    private Activity mactivity;
    private FrameLayout flContainer;


    public static OppoBannerView oppoBannerView = null;

    public static OppoBannerView getInstance(Activity activity, String id) {
        if (oppoBannerView == null) {
            oppoBannerView = new OppoBannerView(activity.getApplicationContext(), activity, id);
        }
        return oppoBannerView;
    }

    public OppoBannerView(Context context, Activity activity, String id) {
        super(context);
        mactivity = activity;
        bannerAd = new BannerAd(mactivity, id);
        bannerAd.setAdListener(this);
        LayoutInflater inflater = LayoutInflater.from(context);
        View mview = inflater.inflate(R.layout.banner, null);
        setContentView(mview);
//        this.setHeight(height);
//        this.setWidth(width);
        setFocusable(false);
        setOutsideTouchable(false);
        flContainer = mview.findViewById(R.id.fl_container);
    }

    public void loadAD() {
        View adview = bannerAd.getAdView();
        if (null != adview) {
            flContainer.removeAllViews();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            flContainer.addView(adview, layoutParams);
        }
        bannerAd.loadAd();
    }

    public void show() {
        showAtLocation(mactivity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
    }

    public void hide() {
        oppoBannerView = null;
        bannerAd.destroyAd();
        this.dismiss();
    }

    @Override
    public void onAdReady() {
        Log.d(TAG, "onAdReady: ");
        show();
    }

    @Override
    public void onAdClose() {
        Log.d(TAG, "onAdClose: ");
        hide();
    }

    @Override
    public void onAdShow() {
        Log.d(TAG, "onAdShow: ");
    }

    @Override
    public void onAdFailed(String s) {
        Log.d(TAG, "onAdFailed: ");
        hide();
    }

    @Override
    public void onAdFailed(int i, String s) {
        Log.d(TAG, "onAdFailed: ");
        hide();
    }

    @Override
    public void onAdClick() {
        Log.d(TAG, "onAdClick: ");
    }
}
