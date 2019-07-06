package com.bytedance.videoplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bytedance.videoplayer.player.VideoPlayerIJK;
import com.bytedance.videoplayer.player.VideoPlayerListener;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IJKPlayerActivity extends AppCompatActivity {

    private static final String TAG = "IJKPlayerActivity";
    private VideoPlayerIJK ijkPlayer;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private SeekBar VideoSeekBar;
    private long currentPosition;

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
        ijkPlayer.setVideoResource(R.raw.yuminhong);
        //ijkPlayer.pause();
//        ijkPlayer.setVideoPath(getVideoPath());
//        if (savedInstanceState != null) {
//            Log.d(TAG, "onCreate: ");
//            currentPosition = savedInstanceState.getLong("currentPosition");
//            ijkPlayer.seekTo(currentPosition);
            Log.d(TAG, "onCreate: "+ijkPlayer.getCurrentPosition());
//        }
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
//
//        findViewById(R.id.buttonSeek).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ijkPlayer.seekTo(20 * 1000);
//            }
//        });

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
        currentPosition = ijkPlayer.getCurrentPosition();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("currentPosition",currentPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: "+savedInstanceState.getLong("currentPosition"));
        currentPosition = savedInstanceState.getLong("currentPosition");
        ijkPlayer.seekTo(currentPosition);
    }
}
