package com.hzklt.moduleplatform_huawei.HuaWeiAD;

import android.app.Activity;


public class AdApi {

//    private static ToutiaoAdFullScreenVideoView fullSceneVideoView;
    private static AdRewardVideoView rewardVideoView;
//    private static ToutiaoAdInteractionExpressView interactionExpressView;
    private static AdBannerExpressView bannerExpressView;
//    public static TTAdNative mTTAdNative;

    /**
     * banner广告
     * @param activity
     * @param appid
     * @param position
     * @param x
     * @param y
     */
    public static void loadBannerExpressAdView(Activity activity,String appid, String position, int x, int y,int imgWidth,int imgHeight){
        bannerExpressView=AdBannerExpressView.getInstance(activity,appid,position,x,y,imgWidth,imgHeight);
}
    public static void showBannerExpressAdView(){
        if (bannerExpressView!=null){
            bannerExpressView.showAd();

        }
    }
    public static void closeBannerExpressAdView(){
        if (bannerExpressView!=null){
            bannerExpressView.closeAd();
            bannerExpressView=null;

        }
    }
    /**
     *
     * @param context
     * @param appid
     */
//    public static void startFullSceneVideoAdView(Activity context, String appid){
//        fullSceneVideoView =ToutiaoAdFullScreenVideoView.getInstance(context,appid);
//    }
//
//    public static void showFullSceneVideoAd(){
//        if (fullSceneVideoView !=null){
//            fullSceneVideoView.showAd();
//        }
//    }
//    public static void closeFullScenevideo(){
//        if (fullSceneVideoView !=null){
//            fullSceneVideoView.closeAd();
//            fullSceneVideoView =null;
//        }
//    }
//
    public static void startRewardVideoAdView(Activity context, String appid){
        rewardVideoView = AdRewardVideoView.getInstance(context,appid);
    }
//
    public static void showRewardVideoAd(){
        if (rewardVideoView !=null){
            rewardVideoView.showAd();
        }
    }
//    public static void closeRewardvideo(){
//        if (rewardVideoView !=null){
//            rewardVideoView.closeAd();
//            rewardVideoView =null;
//
//        }
//    }
//
//    /**
//     *
//     * @param context
//     * @param appid
//     * @param x
//     * @param y
//     * @param count
//     */
//    public static void startNativeVerticalVideoView(Activity context, String appid,  String x, String y,  String count){
//        ToutiaoNativeVerticalVideoActivity.start(context,appid,x,y,count);
//    }
//
//
//    public static void loadExpressInteraction(Activity context, String appid,int x, int y){
//        interactionExpressView=ToutiaoAdInteractionExpressView.getInstance(context,appid,x,y);
//    }
//
//    public static void showExpressInteraction(){
//        if (interactionExpressView!=null){
//            interactionExpressView.show();
//        }
//    }
//    public static void closeExpressInteraction(){
//        if (interactionExpressView!=null){
//            interactionExpressView.closeAd();
//            interactionExpressView=null;
//
//        }
//    }
//
//    public static void startSplash(Activity activity,String appid){
//        ToutiaoSplashView.start(activity,appid,mTTAdNative);
//    }
}
