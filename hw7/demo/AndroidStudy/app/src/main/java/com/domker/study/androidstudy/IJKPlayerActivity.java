package com.domker.study.androidstudy;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.domker.study.androidstudy.player.VideoPlayerIJK;
import com.domker.study.androidstudy.player.VideoPlayerListener;

import java.io.IOException;

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
    private boolean isPlaying;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ijkplayer);
        setTitle("ijkPlayer");

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
        if (!loadVideo()){
            ijkPlayer.setVideoResource(R.raw.yuminhong);
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
        changeVideoSize();
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

        int mVideoWidth = ijkPlayer.getmMediaPlayer().getVideoWidth();
        int mVideoHeight = ijkPlayer.getmMediaPlayer().getVideoHeight();
        Log.d(TAG, "changeVideoSize() called width="+mVideoWidth+ ",height="+mVideoHeight);
        //窗口大小
        int VideoWidth = ijkPlayer.getWidth();
        int VideoHeight = ijkPlayer.getHeight();
        //视频原大小
        int surfaceWidth = ijkPlayer.getmMediaPlayer().getVideoWidth();
        int surfaceHeight = ijkPlayer.getmMediaPlayer().getVideoHeight();
        Log.d(TAG, "changeVideoSize() called width="+VideoWidth+ ",height="+VideoHeight);
        Log.d(TAG, "changeVideoSize() called width="+surfaceWidth+ ",height="+surfaceHeight);
/*

        //根据视频尺寸去计算->视频可以在窗口中放大的最小倍数。
        float Wscale = (float) VideoWidth / surfaceWidth;
        float Hscale = (float) VideoHeight / surfaceHeight;
        Log.d(TAG, "changeVideoSize: scale = "+scale);
        //视频宽高乘以最小倍数的到视频播放窗口大小
        VideoWidth = (int) (surfaceWidth * scale);
        VideoHeight = (int) (surfaceHeight * scale);
*/

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。

        ViewGroup.LayoutParams layoutParams = ijkPlayer.getLayoutParams();
        layoutParams.width = VideoWidth;
        layoutParams.height = VideoHeight;

        ijkPlayer.setLayoutParams(layoutParams);
        Log.d(TAG, "changeVideoSize() called width="+VideoWidth+ ",height="+VideoHeight);
    }

    private boolean loadVideo(){
        Uri url=getIntent().getData();
        if(url!=null){
            ijkPlayer.setVideoPath(this,url);
            Log.d(TAG, "loadVideo() called successfully");
            return true;
        }else {
            Log.d(TAG, "loadVideo() failed to call");
            return false;
        }
    }

}
