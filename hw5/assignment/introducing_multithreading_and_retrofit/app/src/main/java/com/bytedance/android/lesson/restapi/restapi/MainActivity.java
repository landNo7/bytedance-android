package com.bytedance.android.lesson.restapi.restapi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bytedance.android.lesson.restapi.restapi.bean.Joke;
import com.bytedance.android.lesson.restapi.restapi.bean.OSList;
import com.bytedance.android.lesson.restapi.restapi.utils.NetworkUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static String RAW =
            "{\"os\":[{\"name\":\"Pie\",\"code\":28}," +
                    "{\"name\":\"Oreo\",\"code\":27}]}";
    public TextView mTv;
    private View mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.tv);
        mBtn = findViewById(R.id.btn);
//        mTv.setText(parseFirstNameWithJSON()); // json test
//        mTv.setText(parseFirstNameWithGson()); // json test
        initListeners();
    }

    private void initListeners() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                requestData(v);
            }
        });
    }

    private static String parseFirstNameWithGson() {
        OSList list = new Gson()
                .fromJson(RAW, OSList.class);
        return list.getOs()[0].getName();
    }

    private static String parseFirstNameWithJSON() {
        String result = null;
        try {
            JSONObject root = new JSONObject(RAW);
            JSONArray os = root.optJSONArray("os");
            result = os.optJSONObject(0).
                    optString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void mtv_setText(String text){
        mTv.setText(text);
    }
    private Joke j;
    public void requestData(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = NetworkUtils.getResponseWithHttpURLConnection("https://api.icndb.com/jokes/random");
                System.out.println(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject valueObject = jsonObject.getJSONObject("value");
                    int id = valueObject.optInt("value");
                    String joke = valueObject.getString("joke");
                    System.out.println(id+joke);
                    //mTv.setText(joke);
                    //mHandler.sendMessage(Message.obtain(mHandler,SET_TEXT,joke));
                    j = NetworkUtils.getResponseWithRetrofit();
                    System.out.println("j="+j);
                    mHandler.sendMessage(Message.obtain(mHandler,Retrofit));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //mTv.setText(s);
            }
        }).start();
        // HttpURLConnection
//        String s = NetworkUtils.getResponseWithHttpURLConnection("https://api.icndb.com/jokes/random");
//        mTv.setText(s);

        // Retrofit
        j = NetworkUtils.getResponseWithRetrofit();
        mTv.setText(j.getValue().getJoke());
    }
    private static final int SET_TEXT = 0;
    private static final int Retrofit = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case SET_TEXT:
                    mTv.setText(""+msg.obj);break;
                case Retrofit:
                    mTv.setText(j.getValue().getJoke());
                default:break;
            }
        }
    };
}