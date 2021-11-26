package com.hzklt.minigame;

import com.hzklt.minigame.base.IPlatform2Unity;
import com.hzklt.minigame.base.PlatformBase;
import com.umeng.commonsdk.UMConfigure;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends UnityPlayerActivity {

    PlatformBase mplatformBase = null;

    class Platform2Unity implements IPlatform2Unity {

        @Override
        public void UnitySendMessage(int iMsgId, int iParam1, int iParam2, int iParam3, String strParam1, String strParam2, String strParam3) {
            try {
                JSONObject object = new JSONObject();
                object.put("iMsgId", iMsgId);
                object.put("iPararm1", iParam1);
                object.put("iPararm2", iParam2);
                object.put("iPararm3", iParam3);
                object.put("strParam1", strParam1);
                object.put("strParam2", strParam2);
                object.put("strParam3", strParam3);
                UnityPlayer.UnitySendMessage(Constants.PLATFORM_OBJECT, Constants.METHOD_NAME, object.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    IPlatform2Unity iPlatform2Unity = new Platform2Unity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.TAG, "MainActivity onCreate");
        initPlatform();
    }

    private void initPlatform() {
        ModulePlatform modulePlatform = ModulePlatform.findBychannel(BuildConfig.Channel);
        String packeg = modulePlatform.getPackageName();
        UMConfigure.init(this, modulePlatform.getUmengID(), packeg, UMConfigure.DEVICE_TYPE_PHONE, "null");
        try {
            Class<?> cls = Class.forName(packeg);
            Object obj = cls.newInstance();
            PlatformBase platformBase = (PlatformBase) obj;
            mplatformBase = platformBase;
            mplatformBase.InitCallBack(iPlatform2Unity, this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //UnityGameHelper.Destroy();
    }


    //接收unity请求调用
    public void SendUnityMessageToPlatform(int iMsgId, String strJson) {
        //Toast.makeText(this.getApplicationContext(), "unity调用android成功" + iMsgId + strJson, Toast.LENGTH_LONG).show();
        try {
            if (strJson != null) {
                JSONObject object = new JSONObject(strJson);
                object.put("iMsgId", iMsgId);
                String msg = object.toString();
            }
            if (mplatformBase != null) {
                Handler handler = new Handler(Looper.getMainLooper());
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mplatformBase.readMethod(iMsgId);
                    }
                });
                handler.post(thread);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getInfo() {
        try {
            JSONObject object = new JSONObject();
            object.put("mServertype", BuildConfig.ServerType);
            object.put("mChannel", BuildConfig.Channel);
            object.put("mProtocol", 1);
            //System.out.println("Unity请求数据"+object.toString());
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    //向unity发送消息
    public void SendMessageToUnity(String strJson) {
        UnityPlayer.UnitySendMessage(Constants.PLATFORM_OBJECT, Constants.METHOD_NAME, strJson);
    }

    private long firstTime = 0;

    //双击退出
    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            long keydowntime = System.currentTimeMillis();
            if (keydowntime - firstTime > 2000) {
                firstTime = keydowntime;
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            } else {
                mplatformBase.readMethod(-1);
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //华为登录返回
        if (requestCode == 8888) {
            mplatformBase.HWloginCallback(307, intent);
        }
    }
}
