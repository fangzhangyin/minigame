/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.hzklt.moduleplatform_huawei.HuaWeiAD;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.reward.Reward;
import com.huawei.hms.ads.reward.RewardAd;
import com.huawei.hms.ads.reward.RewardAdLoadListener;
import com.huawei.hms.ads.reward.RewardAdStatusListener;
import com.hzklt.minigame.base.IPlatform2Unity;
import com.hzklt.minigame.base.PlatformBase;
import com.hzklt.moduleplatform_huawei.HWSDK;
import com.hzklt.moduleplatform_huawei.R;

import java.util.Random;


/**
 * Activity for displaying a rewarded ad.
 */
public class RewardActivity extends Activity {
    private static final String TAG = "RewardVideo";

    private static final int PLUS_SCORE = 1;

    private static final int MINUS_SCORE = 5;

    private static final int RANGE = 2;

    private String adId;

    private RewardAd rewardedAd;

    private int score = 1;

    private final int defaultScore = 10;

    int method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.reward_ad));
        setContentView(R.layout.activity_reward);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        adId = this.getIntent().getStringExtra("splashid");
        method = this.getIntent().getIntExtra("method", 0);
//        rewardedTitle = findViewById(R.id.text_reward);
//        rewardedTitle.setText(R.string.reward_ad_title);

        // Load a rewarded ad.
        loadRewardAd();

        // Load a score view.
//        loadScoreView();

        // Load the button for watching a rewarded ad.
//        loadWatchButton();

        // Load the button for starting a game.
//        loadPlayButton();
    }

    /**
     * Load a rewarded ad.
     */
    private void loadRewardAd() {
        if (rewardedAd == null) {
            rewardedAd = new RewardAd(RewardActivity.this, adId);
        }

        RewardAdLoadListener rewardAdLoadListener = new RewardAdLoadListener() {
            @Override
            public void onRewardAdFailedToLoad(int errorCode) {
//                showToast("onRewardAdFailedToLoad " + "errorCode is :" + errorCode);.
                AlertDialog.Builder builder = new AlertDialog.Builder(RewardActivity.this);
                builder.setTitle("提示").setMessage("暂无广告")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).setCancelable(false);
                builder.show();
            }

            @Override
            public void onRewardedLoaded() {
                showToast("onRewardedLoaded");
                rewardAdShow();
            }
        };

        rewardedAd.loadAd(new AdParam.Builder().build(), rewardAdLoadListener);
    }

    /**
     * Display a rewarded ad.
     */
    private void rewardAdShow() {
        if (rewardedAd.isLoaded()) {
            rewardedAd.show(RewardActivity.this, new RewardAdStatusListener() {
                @Override
                public void onRewardAdOpened() {
                    // 激励广告被打开
                    Log.d(TAG, "激励广告被打开");
                }

                @Override
                public void onRewardAdFailedToShow(int errorCode) {
                    // 激励广告展示失败
                    Log.d(TAG, "激励广告展示失败");
                    finish();
                }

                @Override
                public void onRewardAdClosed() {
                    // 激励广告被关闭
                    Log.d(TAG, "激励广告被关闭");
                    finish();
                }

                @Override
                public void onRewarded(Reward reward) {
                    // 激励广告奖励达成，发放奖励
                    Log.d(TAG, "激励广告奖励达成");
                    HWSDK.iPlatform2Unity.UnitySendMessage(method, 1, 0, 0, "激励视频播放完成", "", "");
                }
            });
        }
    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RewardActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Set a score.
     *
     * @param score
     */
//    private void setScore(int score) {
//        scoreView.setText("Score:" + score);
//    }

    /**
     * Load the button for watching a rewarded ad.
     */
//    private void loadWatchButton() {
//        watchAdButton = findViewById(R.id.show_video_button);
//        watchAdButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rewardAdShow();
//            }
//        });
//    }

    /**
     * Load the button for starting a game.
     */
//    private void loadPlayButton() {
//        reStartButton = findViewById(R.id.play_button);
//        reStartButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                play();
//            }
//        });
//    }

//    private void loadScoreView() {
//        scoreView = findViewById(R.id.score_count_text);
//        scoreView.setText("Score:" + score);
//    }

    /**
     * Used to play a game.
     */
    private void play() {
        // If the score is 0, a message is displayed, asking users to watch the ad in exchange for scores.
        if (score == 0) {
            Toast.makeText(RewardActivity.this, "Watch video ad to add score", Toast.LENGTH_SHORT).show();
            return;
        }

        // The value 0 or 1 is returned randomly. If the value is 1, the score increases by 1. If the value is 0, the
        // score decreases by 5. If the score is a negative number, the score is set to 0.
        int random = new Random().nextInt(RANGE);
        if (random == 1) {
            score += PLUS_SCORE;
            Toast.makeText(RewardActivity.this, "You win！", Toast.LENGTH_SHORT).show();
        } else {
            score -= MINUS_SCORE;
            score = score < 0 ? 0 : score;
            Toast.makeText(RewardActivity.this, "You lose！", Toast.LENGTH_SHORT).show();
        }
//        setScore(score);
    }
}
