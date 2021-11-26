package com.hzklt.moduleplatform_huawei.HuaWeiAD;

import android.app.Activity;
import android.util.Log;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.PlayersClient;
import com.huawei.hms.jos.games.player.GameTrialProcess;
import com.huawei.hms.jos.games.player.Player;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.hzklt.minigame.base.IPlatform2Unity;
import com.hzklt.moduleplatform_huawei.HWSDK;

public class HwGameCenter {


    public static String TAG = "HwGameCenter";


    public static void login(Activity activity, IPlatform2Unity m_IPlatform2Unity,int IMsgID) {
        // 必须在init成功后，才可以实现登录功能
        AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME).createParams();
        Task<AuthAccount> authAccountTask = AccountAuthManager.getService(activity, authParams).silentSignIn();
        authAccountTask.addOnSuccessListener(
                new OnSuccessListener<AuthAccount>() {
                    @Override
                    public void onSuccess(AuthAccount authAccount) {
                        Log.d(TAG, "signIn success: ");
                        Log.d(TAG, "display:" + authAccount.getDisplayName());
                        //TODO 返回时显示华为游戏中心浮标
                        Games.getBuoyClient(activity).showFloatWindow();// 华为游戏
                        getGamePlayer(activity);   // 获取玩家信息
                        m_IPlatform2Unity.UnitySendMessage(IMsgID, 1, 0, 0, "登录成功", "", "");
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                if (e instanceof ApiException) {
                                    ApiException apiException = (ApiException) e;
                                    Log.d(TAG, "signIn failed:" + apiException.getStatusCode());
                                    Log.d(TAG, "start getSignInIntent");
                                    //在此处实现显式登录
                                    AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME).setAuthorizationCode().createParams();
                                    AccountAuthService service = AccountAuthManager.getService(activity, authParams);
                                    activity.startActivityForResult(service.getSignInIntent(), 8888);
                                }
                            }
                        });
    }

    //获取玩家信息
    public static void getGamePlayer(Activity activity) {
        //调用getPlayersClient方法初始化
        PlayersClient client = Games.getPlayersClient(activity);
        //获取玩家信息
        Task<Player> task = client.getGamePlayer();
        task.addOnSuccessListener(new OnSuccessListener<Player>() {
            @Override
            public void onSuccess(Player player) {
                String accessToken = player.getAccessToken();
                String displayName = player.getDisplayName();
                String unionId = player.getUnionId();
                String openId = player.getOpenId();
                //获取玩家信息成功，校验accessToken，校验通过后启动游戏
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    String result = "rtnCode:" + ((ApiException) e).getStatusCode();
                    //获取玩家信息失败，请根据错误码处理
                }
            }
        });
    }

    public static void getreal(Activity activity) {
        PlayersClient client = Games.getPlayersClient(activity);
        client.setGameTrialProcess(new GameTrialProcess() {
            @Override
            public void onTrialTimeout() {
                //试玩时间结束
            }

            @Override
            public void onCheckRealNameResult(boolean hasRealName) {
                if (hasRealName) {
                    //已实名，继续后续的游戏登录处理
                    return;
                } else {
                    activity.finish();
                }
                //未实名，建议您提示玩家后退出游戏或引导玩家重新登录并实名认证
            }
        });
    }


}
