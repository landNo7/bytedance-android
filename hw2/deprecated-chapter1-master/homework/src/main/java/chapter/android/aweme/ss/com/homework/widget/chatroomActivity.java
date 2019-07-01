package chapter.android.aweme.ss.com.homework.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import chapter.android.aweme.ss.com.homework.R;

public class chatroomActivity extends AppCompatActivity {

    private TextView chatroom_tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        final String name = getIntent().getStringExtra("itemNum");
        chatroom_tv1 = findViewById(R.id.tv_with_name);
        chatroom_tv1.setText(name);
    }
}
