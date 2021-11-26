package com.hzklt.moduleplatform_vivo.VIVOAD;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.vivo.mobilead.unified.base.AdParams;
import com.vivo.mobilead.unified.base.VivoAdError;
import com.vivo.mobilead.unified.icon.UnifiedVivoFloatIconAd;
import com.vivo.mobilead.unified.icon.UnifiedVivoFloatIconAdListener;


public class VivoIconView {
    private static final String TAG = "IconView";
    private UnifiedVivoFloatIconAd vivoFloatIconAd;
    private AdParams adParams;
    private Activity mactivity;


    public void loadAD(Activity activity, String FLOAT_ICON) {
        mactivity=activity;
        adParams = new AdParams.Builder(FLOAT_ICON).build();
        vivoFloatIconAd = new UnifiedVivoFloatIconAd(activity, adParams, floatIconAdListener);
        vivoFloatIconAd.loadAd();
    }

    public void showAD() {
        if (vivoFloatIconAd != null) {
            vivoFloatIconAd.showAd(mactivity);
        }
    }

    public void hide(){
        if (vivoFloatIconAd != null) {
            vivoFloatIconAd.destroy();
        }
    }

    private UnifiedVivoFloatIconAdListener floatIconAdListener = new UnifiedVivoFloatIconAdListener() {
        @Override
        public void onAdShow() {
            Log.d(TAG, "onAdShow");
        }

        @Override
        public void onAdFailed(@NonNull VivoAdError vivoAdError) {
            Log.d(TAG, "onAdFailed: " + vivoAdError.toString());
            hide();
        }

        @Override
        public void onAdReady() {
            Log.d(TAG, "onAdReady");
            showAD();
        }

        @Override
        public void onAdClick() {
            Log.d(TAG, "onAdClick");
        }

        @Override
        public void onAdClose() {
            Log.d(TAG, "onAdClose");
            hide();
        }
    };
}
