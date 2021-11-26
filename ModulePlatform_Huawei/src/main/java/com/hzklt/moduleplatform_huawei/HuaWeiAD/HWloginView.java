package com.hzklt.moduleplatform_huawei.HuaWeiAD;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.hzklt.moduleplatform_huawei.R;


public class HWloginView extends PopupWindow {

    View mview;

    public HWloginView(Context context, Activity activity) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        mview = inflater.inflate(R.layout.hwlogin, null);
        setContentView(mview);
        //设置是否获得焦点
        setFocusable(false);
        //是否可以通过点击屏幕外来关闭
        setOutsideTouchable(false);
        showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }
}
