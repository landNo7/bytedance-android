package com.domker.study.androidstudy;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.domker.study.androidstudy.player.VideoPlayerIJK;
import com.domker.study.androidstudy.player.VideoPlayerListener;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IJKPlayerActivity extends AppCompatActivity {

    public static final float SHOW_SCALE = 16 * 1.0f / 9;
    private static final String TAG = "IJKPlayerActivity";
    private VideoPlayerIJK ijkPlayer;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private SeekBar VideoSeekBar;
    private long currentPosition;
    private DisplayMetrics displayMetrics;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean isLand;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ijkplayer);
        setTitle("ijkPlayer");

        displayMetrics = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;

        ijkPlayer = findViewById(R.id.ijkPlayer);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        VideoSeekBar = findViewById(R.id.seekBarVideo);

        //加载native库
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            this.finish();
        }
        ijkPlayer.setListener(new VideoPlayerListener());
        ijkPlayer.setVideoResource(R.raw.yuminhong);
        //ijkPlayer.pause();
//        ijkPlayer.setVideoPath(getVideoPath());
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getLong("currentPosition");
            Log.d(TAG, "onCreate: "+ijkPlayer.getCurrentPosition());
        }

        mIjkPlayerHandler.sendEmptyMessageDelayed(0,200);

        findViewById(R.id.buttonPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ijkPlayer.start();
            }
        });

        findViewById(R.id.buttonPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ijkPlayer.pause();
            }
        });

        findViewById(R.id.buttonSeek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ijkPlayer.seekTo(20 * 1000);
            }
        });

        ijkPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                RelativeLayout Re_SeekBar = findViewById(R.id.Re_SeekBar);
                if (Re_SeekBar.getVisibility() == View.GONE)
                    Re_SeekBar.setVisibility(View.VISIBLE);
                else
                    Re_SeekBar.setVisibility(View.GONE);
                return false;
            }
        });
        VideoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekPos = seekBar.getProgress();
                ijkPlayer.seekTo(seekPos);
            }
        });
    }


    private String getVideoPath() {
        return "http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8";
//        return "android.resource://" + this.getPackageName() + "/" + resId;
    }

    @SuppressLint("HandlerLeak")
    Handler mIjkPlayerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (ijkPlayer.getDuration() > 0) {
                        VideoSeekBar.setMax((int) ijkPlayer.getDuration());
                        VideoSeekBar.setProgress((int) ijkPlayer.getCurrentPosition());
                    }
                    updateTextViewWithTimeFormat(tvCurrentTime,(int)ijkPlayer.getCurrentPosition() / 1000);
                    updateTextViewWithTimeFormat(tvTotalTime,(int)ijkPlayer.getDuration() / 1000);
                    mIjkPlayerHandler.sendEmptyMessageDelayed(0,200);
                    break;
            }
        }
    };

    @SuppressLint("DefaultLocale")
    private void updateTextViewWithTimeFormat(TextView textView, int second) {
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String stringTemp;
        if (0 != hh) {
            stringTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            stringTemp = String.format("%02d:%02d", mm, ss);
        }
        textView.setText(stringTemp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ijkPlayer!=null){
            ijkPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ijkPlayer != null) {
            ijkPlayer.release();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (ijkPlayer.isPlaying()) {
            ijkPlayer.stop();
        }
        currentPosition = ijkPlayer.getCurrentPosition();
        IjkMediaPlayer.native_profileEnd();
        mIjkPlayerHandler.removeCallbacksAndMessages(0);
        ijkPlayer.release();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (ijkPlayer != null) {
//            ijkPlayer.dispatchConfigurationChanged(newConfig);
//        }
//        /*isLand = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        mScreenWidth = displayMetrics.widthPixels;
//        mScreenHeight = displayMetrics.heightPixels;*/
//
////        resize();
//            ijkPlayer.onConfigurationChanged(newConfig);
//            // 切换为小屏
//            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//                fullScreen.setVisibility(View.GONE);
//                fullScreen.removeAllViews();
//                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.video_screen);
//                frameLayout.removeAllViews();
//                ViewGroup last = (ViewGroup) player.getParent();//找到videoitemview的父类，然后remove
//                if (last != null) {
////                    last.removeAllViews();
//                    last.removeView(player);
//                }
//                frameLayout.addView(player);
//                int mShowFlags =
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//                fullScreen.setSystemUiVisibility(mShowFlags);
//            } else {
//                //切换为全屏
//                ViewGroup viewGroup = (ViewGroup) player.getParent();
//                if (viewGroup == null)
//                    return;
//                viewGroup.removeAllViews();
//                fullScreen.addView(player);
//                fullScreen.setVisibility(View.VISIBLE);
//                int mHideFlags =
//                        View.SYSTEM_UI_FLAG_LOW_PROFILE
//                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_IMMERSIVE
//                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//                fullScreen.setSystemUiVisibility(mHideFlags);
//            }
//        } else {
//            fullScreen.setVisibility(View.GONE);
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong("currentPosition",currentPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: "+savedInstanceState.getLong("currentPosition"));
        currentPosition = savedInstanceState.getLong("currentPosition");
        super.onRestoreInstanceState(savedInstanceState);
    }
/*
    public void resize() {
        float areaWH = 0.0f;
        int height;

        if (!isLand) {
            // 竖屏16:9
            height = (int) (mScreenWidth / SHOW_SCALE);
            areaWH = SHOW_SCALE;
        } else {
            //横屏按照手机屏幕宽高计算比例
            height = mScreenHeight;
            areaWH = mScreenWidth / mScreenHeight;
        }

        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        mSurfaceLayout.setLayoutParams(layoutParams);

        int mediaWidth = mMediaPlayer.getVideoWidth();
        int mediaHeight = mMediaPlayer.getVideoHeight();


        float mediaWH = mediaWidth * 1.0f / mediaHeight;

        RelativeLayout.LayoutParams layoutParamsSV = null;


        if (areaWH > mediaWH) {
            //直接放会矮胖
            int svWidth = (int) (height * mediaWH);
            layoutParamsSV = new RelativeLayout.LayoutParams(svWidth, height);
            layoutParamsSV.addRule(RelativeLayout.CENTER_IN_PARENT);
            mSurfaceView.setLayoutParams(layoutParamsSV);
        }

        if (areaWH < mediaWH) {
            //直接放会瘦高。
            int svHeight = (int) (mScreenWidth / mediaWH);
            layoutParamsSV = new RelativeLayout.LayoutParams(mScreenWidth, svHeight);
            layoutParamsSV.addRule(RelativeLayout.CENTER_IN_PARENT);
            mSurfaceView.setLayoutParams(layoutParamsSV);
        }
    }*/
}
