package com.hzklt.minigame.base;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class PlatformBase {
    protected IPlatform2Unity m_IPlatform2Unity;
    protected Activity mactivity;

    public void InitCallBack(IPlatform2Unity iPlatform2Unity, Activity activity) {
        m_IPlatform2Unity = iPlatform2Unity;
        mactivity = activity;
    }

    public void readMethod(int Method) {
        System.out.println(Method);
    }


    //发送消息到unity
    protected final void SendMessageToUnity(int iMsgId, int iParam1, int iParam2, int iParam3, String strParam1, String strParam2, String strParam3) {
        if (m_IPlatform2Unity == null) {
            Log.d("SendtoUnity", "m_IPlatform2Unity is null");
            return;
        }
        m_IPlatform2Unity.UnitySendMessage(iMsgId, iParam1, iParam2, iParam3, strParam1, strParam2, strParam3);
    }

    public void HWloginCallback(int Method, Intent intent) {
        System.out.println("华为登录回调");
    }

}
