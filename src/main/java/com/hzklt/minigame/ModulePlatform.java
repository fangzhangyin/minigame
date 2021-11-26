package com.hzklt.minigame;

import java.util.Arrays;

public enum ModulePlatform {
    OPPO(1, "com.hzklt.moduleplatform_oppo.OppoSDK","6195f6fbbc89fd13513c1dbb"),
    VIVO(2, "com.hzklt.moduleplatform_vivo.VivoSDK","6195f684533969630ee91d06"),
    HW(3, "com.hzklt.moduleplatform_huawei.HWSDK","");
    private int channel;
    private String PackageName;
    private String UmengID;

    ModulePlatform(int channel, String packageName, String umengID) {
        this.channel = channel;
        PackageName = packageName;
        UmengID = umengID;
    }

    @Override
    public String toString() {
        return "ModulePlatform{" +
                "channel=" + channel +
                ", PackageName='" + PackageName + '\'' +
                ", UmengID='" + UmengID + '\'' +
                '}';
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getUmengID() {
        return UmengID;
    }

    public void setUmengID(String umengID) {
        UmengID = umengID;
    }

    public static ModulePlatform findBychannel(int channel) {
        ModulePlatform modulePlatform = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            modulePlatform = Arrays.stream(ModulePlatform.values()).filter(ModulePlatform -> ModulePlatform.getChannel() == channel).findAny().orElse(null);
        }
        return modulePlatform;
    }
}
