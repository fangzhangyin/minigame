package com.hzklt.moduleplatform_vivo.VIVOAD;

import android.app.Application;

import androidx.annotation.NonNull;

import com.hzklt.minigame.base.IPlatform2Unity;
import com.vivo.mobilead.manager.VInitCallback;
import com.vivo.mobilead.manager.VivoAdManager;
import com.vivo.mobilead.unified.base.VivoAdError;
import com.vivo.mobilead.util.VOpenLog;


public class VivoManagerHolder {
    public static VivoAdManager vivoAdManager;

    public static void init(Application application, String MediaID,int method,IPlatform2Unity iPlatform2Unity) {
        VOpenLog.setEnableLog(true);
        vivoAdManager = VivoAdManager.getInstance();
        vivoAdManager.init(application, MediaID, new VInitCallback() {
            @Override
            public void suceess() {
                VOpenLog.d("SDKInit", "vivo广告suceess");
//                iPlatform2Unity.UnitySendMessage(method, 1, 0, 0, "vivo广告suceess", "", "");
            }

            @Override
            public void failed(@NonNull VivoAdError vivoAdError) {
                VOpenLog.e("SDKInit", "failed: " + vivoAdError.toString());
                vivoAdManager = null;
//                iPlatform2Unity.UnitySendMessage(method, -1, 0, 0, "vivo广告fail", "", "");
            }
        });
    }



}
