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

import com.heytap.msp.mobad.api.ad.NativeAdvanceAd;
import com.heytap.msp.mobad.api.listener.INativeAdvanceInteractListener;
import com.heytap.msp.mobad.api.listener.INativeAdvanceLoadListener;
import com.heytap.msp.mobad.api.params.INativeAdFile;
import com.heytap.msp.mobad.api.params.INativeAdvanceData;
import com.heytap.msp.mobad.api.params.NativeAdvanceContainer;
import com.hzklt.moduleplatform_oppo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;


public class OppoNativeInsertView extends PopupWindow implements INativeAdvanceLoadListener {
    private static final String TAG = "NativeInsertView";
    private NativeAdvanceAd mNativeAdvanceAd;
    /**
     * 原生广告数据对象。
     */
    private INativeAdvanceData mINativeAdData;
    Context mcontext;
    Activity mactivity;
    View mview;
    ImageLoader mImageLoader;

    TextView title_tv, desc_tv;
    ImageView close_iv;
    Button click_bn;
    public static OppoNativeInsertView oppoNativeInsertView;

    public static OppoNativeInsertView GetInstence(Activity activity) {
        if (oppoNativeInsertView == null) {
            oppoNativeInsertView = new OppoNativeInsertView(activity.getApplicationContext(), activity);
        }
        return oppoNativeInsertView;
    }

    public OppoNativeInsertView(Context context, Activity activity) {
        super(context);
        mcontext = context;
        mactivity = activity;
        LayoutInflater inflater = LayoutInflater.from(context);
        mview = inflater.inflate(R.layout.oppoinsert, null);
        setContentView(mview);
        initView();
    }

    private void initView() {
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(mcontext));
        title_tv = mview.findViewById(R.id.title_tv);
        desc_tv = mview.findViewById(R.id.desc_tv);
        close_iv = mview.findViewById(R.id.close_iv);
        click_bn = mview.findViewById(R.id.click_bn);
    }

    public void hide() {
        //全局组件置为空
        oppoNativeInsertView = null;
        mNativeAdvanceAd.destroyAd();
        this.dismiss();
    }

    public void loadAd(String NATIVE_AD_ID) {
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
             *展示主图、大小为640X320。
             */
            if (null != mINativeAdData.getImgFiles() && mINativeAdData.getImgFiles().size() > 0) {
                INativeAdFile iNativeAdFile = (INativeAdFile) mINativeAdData.getImgFiles().get(0);
                showImage(iNativeAdFile.getUrl(), (ImageView) mview.findViewById(R.id.img_iv));
            }
            /**
             * 判断是否需要展示“广告”Logo标签
             */
            if (null != mINativeAdData.getLogoFile()) {
                showImage(mINativeAdData.getLogoFile().getUrl(), (ImageView) mview.findViewById(R.id.logo_iv));
            }
            title_tv.setText(null != mINativeAdData.getTitle() ? mINativeAdData.getTitle() : "");
            desc_tv.setText(null != mINativeAdData.getDesc() ? mINativeAdData.getDesc() : "");
            /**
             * 处理“关闭”按钮交互行为
             */
            close_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
            click_bn.setText(null != mINativeAdData.getClickBnText() ? mINativeAdData.getClickBnText() : "");
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
                    Log.d(TAG, "出错：" + ",msg:" + msg);
                }
            });
            /**
             *原生广告的渲染内容必须渲染在NativeAdvanceContainer里面
             */
            NativeAdvanceContainer container = (NativeAdvanceContainer) mview.findViewById(R.id.native_ad_container);
            List<View> clickViewList = new ArrayList<>();
            /**
             * 响应广告点击事件的按钮
             */
            clickViewList.add(mview.findViewById(R.id.click_bn));
            clickViewList.add(container);
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
