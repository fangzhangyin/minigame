package com.hzklt.moduleplatform_oppo.OPPOAD;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.heytap.msp.mobad.api.ad.NativeAdvanceAd;
import com.heytap.msp.mobad.api.listener.INativeAdvanceInteractListener;
import com.heytap.msp.mobad.api.listener.INativeAdvanceLoadListener;
import com.heytap.msp.mobad.api.params.INativeAdFile;
import com.heytap.msp.mobad.api.params.INativeAdvanceData;
import com.heytap.msp.mobad.api.params.NativeAdvanceContainer;
import com.hzklt.moduleplatform_oppo.OppoSDK;
import com.hzklt.moduleplatform_oppo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;


public class OppoNativeBanner extends PopupWindow implements INativeAdvanceLoadListener {

    private static final String TAG = "NativeBanner";
    TextView closetext, title_tv, desc_tv;
    NativeAdvanceAd mNativeAdvanceAd;
    Context mcontext;
    Activity mactivity;
    View mview;
    AQuery mAQuery;
    ImageLoader mImageLoader;
    Button action_bn;
    private static OppoNativeBanner oppoNativeBanner;

//    Button load_native_ad_bn, show_native_ad_bn;
    /**
     * 原生广告数据对象。
     */
    private INativeAdvanceData mINativeAdData;

    public static OppoNativeBanner GetInstance(Activity activity) {
        if (oppoNativeBanner == null) {
            oppoNativeBanner = new OppoNativeBanner(activity.getApplicationContext(), activity);
        }
        return oppoNativeBanner;
    }

    public OppoNativeBanner(Context context, Activity activity) {
        super(context);
        mcontext = context;
        mactivity = activity;
        mAQuery = new AQuery(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        mview = inflater.inflate(R.layout.oppobanner, null);
        setContentView(mview);
        initView();
//        this.setHeight(height);
//        this.setWidth(width);
        //设置是否获得焦点
        setFocusable(false);
        //是否可以通过点击屏幕外来关闭
        setOutsideTouchable(false);
    }

    private void initView() {
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(mcontext));
        closetext = mview.findViewById(R.id.closeAD);
        mview.findViewById(R.id.native_ad_container).setVisibility(View.GONE);
        closetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        title_tv = mview.findViewById(R.id.title_tv);
        desc_tv = mview.findViewById(R.id.desc_tv);
        action_bn = mview.findViewById(R.id.action_bn);
//        load_native_ad_bn = mview.findViewById(R.id.load_native_ad_bn);
//        show_native_ad_bn = mview.findViewById(R.id.show_native_ad_bn);
//        load_native_ad_bn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadAD2();
//            }
//        });
//        show_native_ad_bn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAd();
//            }
//        });
    }

    public void loadAD(String NATIVE_AD_ID) {
        Log.d(TAG, NATIVE_AD_ID);
        /**
         *构造NativeAd对象。
         */
        mNativeAdvanceAd = new NativeAdvanceAd(mcontext, NATIVE_AD_ID, this);
        if (null != mNativeAdvanceAd) {
            /**
             *调用loadAd方法加载原生广告。
             */
            mNativeAdvanceAd.loadAd();
        }
    }

    public void showAd() {
        /**
         *在展示原生广告前调用isAdValid判断当前广告是否有效，否则即使展示了广告，也是无效曝光、点击，不计费的
         *注意：每个INativeAdData对象只有一次有效曝光、一次有效点击；多次曝光，多次点击都只扣一次费。
         */
        if (null != mINativeAdData && mINativeAdData.isAdValid()) {
            mview.findViewById(R.id.native_ad_container).setVisibility(View.VISIBLE);
            /**
             *展示推广应用的ICON，大小为512X512。
             */
            if (null != mINativeAdData.getIconFiles() && mINativeAdData.getIconFiles().size() > 0) {
                INativeAdFile iNativeAdFile = (INativeAdFile) mINativeAdData.getIconFiles().get(0);
                showImage(iNativeAdFile.getUrl(), (ImageView) mview.findViewById(R.id.icon_iv));
            }
            /**
             * 判断是否需要展示“广告”Logo标签
             */
            if (null != mINativeAdData.getLogoFile()) {
                showImage(mINativeAdData.getLogoFile().getUrl(), (ImageView) mview.findViewById(R.id.logo_iv));
            }
            mINativeAdData.setInteractListener(new INativeAdvanceInteractListener() {

                @Override
                public void onClick() {
                    Log.d(TAG, "原生广告点击");
                }

                @Override
                public void onShow() {
                    Log.d(TAG, "原生广告展示");
                }

                @Override
                public void onError(int code, String msg) {
                    Log.d(TAG, "原生广告出错，ret:" + code + ",msg:" + msg);
                }
            });
            title_tv.setText(null != mINativeAdData.getTitle() ? mINativeAdData.getTitle() : "");
            desc_tv.setText(null != mINativeAdData.getDesc() ? mINativeAdData.getDesc() : "");
            action_bn.setText(null != mINativeAdData.getClickBnText() ? mINativeAdData.getClickBnText() : "");
//            mAQuery.id(R.id.title_tv).text(null != mINativeAdData.getTitle() ? mINativeAdData.getTitle() : "");
//            mAQuery.id(R.id.desc_tv).text(null != mINativeAdData.getDesc() ? mINativeAdData.getDesc() : "");
//            mAQuery.id(R.id.action_bn).text(null != mINativeAdData.getClickBnText() ? mINativeAdData.getClickBnText() : "");
            /**
             *原生广告的渲染内容必须渲染在NativeAdvanceContainer里面
             */
            NativeAdvanceContainer container = (NativeAdvanceContainer) mview.findViewById(R.id.native_ad_container);
            List<View> clickViewList = new ArrayList<>();
            /**
             * 响应广告点击事件的按钮
             */
            clickViewList.add(mview.findViewById(R.id.action_bn));
            clickViewList.add(mview.findViewById(R.id.adbg));
            /**
             * 绑定广告点击事件与点击按钮
             */
            mINativeAdData.bindToView(mcontext, container, clickViewList);
            showAtLocation(mactivity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        }
    }

    private void showImage(String url, ImageView imageView) {
        ImageLoader.getInstance().displayImage(url, imageView);
    }

    public void hide() {
        //全局banner组件置为空
        oppoNativeBanner = null;
        mNativeAdvanceAd.destroyAd();
        this.dismiss();
    }


    /**
     * 广告数据加载成功
     *
     * @param dataList
     */
    @Override
    public void onAdSuccess(List<INativeAdvanceData> dataList) {
        if (null != dataList && dataList.size() > 0) {
            mINativeAdData = dataList.get(0);
            Log.d(TAG, "广告加载成功");
            showAd();
        }
    }

    @Override
    public void onAdFailed(int ret, String msg) {
        Log.d(TAG, "加载原生广告失败,错误码：" + ret + ",msg:" + msg);
    }
}
