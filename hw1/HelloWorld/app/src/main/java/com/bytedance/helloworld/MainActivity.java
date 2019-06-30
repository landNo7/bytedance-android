package com.bytedance.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView tv1;
    private ImageView imv1;
    private EditText user_name;
    private EditText user_psw;
    private RadioGroup rg1;
    private RadioButton rb1;
    private RadioButton rb2;
    private Button bt1;
    public String sex;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.textView);
        imv1 = findViewById(R.id.imageView);
        user_name = findViewById(R.id.user_name);
        user_psw = findViewById(R.id.user_password);
        rg1 = findViewById(R.id.sex);
        rb1 = findViewById(R.id.nan);
        rb2 = findViewById(R.id.nv);
        bt1 = findViewById(R.id.button);
        //Log.d(TAG, "MainActivity");

        bt1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (user_name.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this,"Plz input username",Toast.LENGTH_SHORT).show();
                else if (user_psw.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this,"Plz input userpsw",Toast.LENGTH_SHORT).show();
                else if (user_name.getText().toString().length()<3)
                    Toast.makeText(MainActivity.this,"username is too short",Toast.LENGTH_SHORT).show();
                else if (user_psw.getText().toString().length()<3)
                    Toast.makeText(MainActivity.this,"userpsw is too short",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this,"User:"+user_name.getText().toString()+"\nsex:"+sex+"\nSuccessfully registered!",Toast.LENGTH_SHORT).show();
            }
        });

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if (i == R.id.nan)
                    sex = "男";
                else
                    sex = "女";
            }
        });

    }
}
