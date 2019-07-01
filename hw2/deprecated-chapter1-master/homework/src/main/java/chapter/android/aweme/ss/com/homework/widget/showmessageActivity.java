package chapter.android.aweme.ss.com.homework.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import chapter.android.aweme.ss.com.homework.R;

public class showmessageActivity extends AppCompatActivity{

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);
        final String msg = getIntent().getStringExtra("buttonName");
        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(showmessageActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }


}