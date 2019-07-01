package chapter.android.aweme.ss.com.homework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * 作业2：一个抖音笔试题：统计页面所有view的个数
 * Tips：ViewGroup有两个API
 * {@link android.view.ViewGroup #getChildAt(int) #getChildCount()}
 * 用一个TextView展示出来
 */
public class Exercises2 extends AppCompatActivity {
    private static String TAG = Exercises2.class.getSimpleName();
    private TextView tv1;
    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex2);
        //RelativeLayout
        tv1 = findViewById(R.id.tv1_ex2);
        tv1.setText(""+getViewCount(this.getWindow().getDecorView()));
    }

    public static int getViewCount(View view) {
        //todo 补全你的代码
        if (view == null) {
            return 0;
        }
        return get_count(view);
    }

    public static int get_count(View view){
        int count = 0;
        if (view == null)
            return 0;
        Log.d(TAG,view.toString());
        if (view instanceof ViewGroup){
            count++;
            for (int i = 0;i < ((ViewGroup) view).getChildCount();i++){
                View view1 = ((ViewGroup) view).getChildAt(i);
                if (view1 instanceof ViewGroup){
                    count += get_count(view1);
                }
                else{
                    count++;
                }
            }
        }
        else
            count++;
        return count;
    }
}
