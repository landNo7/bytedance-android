package chapter.android.aweme.ss.com.homework;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import chapter.android.aweme.ss.com.homework.model.Message;
import chapter.android.aweme.ss.com.homework.model.PullParser;
import chapter.android.aweme.ss.com.homework.widget.chatroomActivity;
import chapter.android.aweme.ss.com.homework.widget.showmessageActivity;

/**
 * 大作业:实现一个抖音消息页面,所需资源已放在res下面
 */
public class Exercises3 extends AppCompatActivity {

    private LinearLayout icon1;
    private LinearLayout icon2;
    private LinearLayout icon3;
    private LinearLayout icon4;
    private ListView lv1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        icon1 = findViewById(R.id.icon1);
        icon2 = findViewById(R.id.icon2);
        icon3 = findViewById(R.id.icon3);
        icon4 = findViewById(R.id.icon4);

        lv1 = findViewById(R.id.rv_list);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(Exercises3.this,chatroomActivity.class);
                it.putExtra("itemNum",String.valueOf(position));
                startActivity(it);
            }
        });
        try {
            lv1.setAdapter(new listViewAdapter(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.icon1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Exercises3.this, showmessageActivity.class);
                TextView tv = (TextView)(icon1.getChildAt(1));
                intent.putExtra("buttonName",tv.getText().toString());
                startActivity(intent);
            }
        });

        findViewById(R.id.icon2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Exercises3.this, showmessageActivity.class);
                TextView tv = (TextView)(icon2.getChildAt(1));
                intent.putExtra("buttonName",tv.getText().toString());
                startActivity(intent);
            }
        });

        findViewById(R.id.icon3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Exercises3.this, showmessageActivity.class);
                TextView tv = (TextView)(icon3.getChildAt(1));
                intent.putExtra("buttonName",tv.getText().toString());
                startActivity(intent);
            }
        });

        findViewById(R.id.icon4).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Exercises3.this, showmessageActivity.class);
                TextView tv = (TextView)(icon4.getChildAt(1));
                intent.putExtra("buttonName",tv.getText().toString());
                startActivity(intent);
            }
        });
    }

    public static class listViewAdapter extends BaseAdapter {
        private Context mContext;
        private List<Message> items ;
        public listViewAdapter(Context mContext) throws Exception {
            this.mContext = mContext;
            items = PullParser.getMessage(mContext);
        }
        @Override
        public int getCount(){
            return items.size();
        }
        @Override
        public Object getItem(int position){
            return null;
        }
        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view;
            if (convertView==null){
                view = LayoutInflater.from(mContext).inflate(R.layout.im_list_item,parent,false);
            }else {
                view = convertView;
            }
            TextView tv_title = view.findViewById(R.id.tv_title);
            TextView tv_description = view.findViewById(R.id.tv_description);
            TextView tv_time = view.findViewById(R.id.tv_time);
            ImageView imv = view.findViewById(R.id.robot_notice);
            imv.vis
            tv_title.setText(items.get(position).getTitle());
            tv_description.setText(items.get(position).getDescription());
            tv_time.setText(items.get(position).getTime());
            return view;
        }

//        @Override
//        public void onChildAttachedToWindow(View child) {
//
//            if (!child.hasOnClickListeners()) {
//                child.setOnClickListener(mItemListener);
//            }
//
//            if (child.getOnFocusChangeListener() == null) {
//                child.setOnFocusChangeListener(mItemListener);
//            }
//
//            child.setOnKeyListener(mItemListener);
//        }

    }


}
