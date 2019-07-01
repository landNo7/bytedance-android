package chapter.android.aweme.ss.com.homework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * 作业1：
 * 打印出Activity屏幕切换 Activity会执行什么生命周期？
 */
public class Exercises1 extends AppCompatActivity {

    private static String TAG = Exercises1.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main2);
        //Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

    }


    @Override protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    /**
     * 界面用户可见
     */
    @Override protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    /**
     * 用户不可见
     */
    @Override protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    /**
     * 界面失去焦点
     */
    @Override protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    /**
     * 界面获得焦点
     */
    @Override protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }
}
