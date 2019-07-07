package com.domker.study.androidstudy;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
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

import tv.danmaku.ijk.media.player.IMediaPlayer;
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
    private boolean isPlaying;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ijkplayer);
        setTitle("ijkPlayer");

        displayMetrics = new DisplayMetrics();
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
        ijkPlayer.getmMediaPlayer().setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                ijkPlayer.getmMediaPlayer().seekTo(currentPosition);
                Log.d(TAG, "onPrepared() called with: PlayerState = [" + iMediaPlayer.isPlaying()+ "] , savedState = ["+isPlaying+"]");
                if(isPlaying){
                    ijkPlayer.getmMediaPlayer().start();
                }else {
                    ijkPlayer.getmMediaPlayer().pause();
                }
                VideoSeekBar.setMax((int)iMediaPlayer.getDuration());
                VideoSeekBar.setProgress((int)iMediaPlayer.getCurrentPosition());
            }
        });
        ijkPlayer.setListener(new VideoPlayerListener());
        ijkPlayer.setVideoResource(R.raw.yuminhong);
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

        ijkPlayer.getmMediaPlayer().setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                changeVideoSize();
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong("currentPosition",currentPosition);
        outState.putBoolean("isplaying",ijkPlayer.isPlaying());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: "+savedInstanceState.getLong("currentPosition"));
        currentPosition = savedInstanceState.getLong("currentPosition");
        isPlaying = savedInstanceState.getBoolean("isplaying");
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void changeVideoSize() {

        int videoWidth = ijkPlayer.getmMediaPlayer().getVideoWidth();
        int videoHeight = ijkPlayer.getmMediaPlayer().getVideoHeight();

        int surfaceWidth = ijkPlayer.getSurfaceView().getWidth();
        int surfaceHeight = ijkPlayer.getSurfaceView().getHeight();
        Log.d(TAG, "changeVideoSize() called width="+videoWidth+ ",height="+videoHeight);
        Log.d(TAG, "changeVideoSize() called width="+surfaceWidth+ ",height="+surfaceHeight);

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
        } else {
            //横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) videoWidth / (float) surfaceHeight), (float) videoHeight / (float) surfaceWidth);
            //max = Math.max(max,getResources().getDisplayMetrics().heightPixels-40);
        }

        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        videoWidth = (int) Math.ceil((float) videoWidth / max);
        videoHeight = (int) Math.ceil((float) videoHeight / max);

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。

        ConstraintLayout.LayoutParams params = new Constraints.LayoutParams(videoWidth, videoHeight);
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.horizontalBias = 0.5f;
        params.verticalBias = 0.4f;

        ijkPlayer.getSurfaceView().setLayoutParams(params);
        Log.d(TAG, "changeVideoSize() called width="+videoWidth+ ",height="+videoHeight);
    }

}
