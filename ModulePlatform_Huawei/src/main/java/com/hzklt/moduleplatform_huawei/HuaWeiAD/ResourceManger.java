package com.hzklt.moduleplatform_huawei.HuaWeiAD;

import android.app.Activity;
import android.content.Context;

public class ResourceManger {

    public static int getId(Context context, String name, String defType) {
        if (context == null || (context instanceof Activity && ((Activity)context).isFinishing()))
            return 0;

        return context.getResources().getIdentifier(name, defType,
                context.getPackageName());
    }

    public static int getId(Context context, String sign) {
        String[] strs = sign.split("\\.");
        if (!isVaildContent(context))
            return 0;
        String packageName = context.getPackageName();
        return context.getResources().getIdentifier(strs[2], strs[1],
                packageName);
    }

    public static boolean isVaildContent(Context context) {
        return !(context == null || (context instanceof Activity && ((Activity)context).isFinishing()));
    }
}
