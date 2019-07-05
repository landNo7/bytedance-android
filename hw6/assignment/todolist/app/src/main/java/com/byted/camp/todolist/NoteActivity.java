package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.MainActivity;

import java.util.Calendar;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;
    private static final String TAG = "NoteActivity";

    private RadioGroup mLevelRG;
    private RadioButton mLevel_h;
    private RadioButton mLevel_m;
    private int note_level;
    private static final int LEVEL_HEIGHT = 2;
    private static final int LEVEL_MEDIUM = 1;
    private static final int LEVEL_NONE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);

        editText = findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }

        addBtn = findViewById(R.id.btn_add);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence content = editText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NoteActivity.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = saveNote2Database(content.toString().trim());
                if (succeed) {
                    Toast.makeText(NoteActivity.this,
                            "Note added", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                } else {
                    Toast.makeText(NoteActivity.this,
                            "Error", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        mLevelRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if (i == R.id.lv_height)
                    note_level = LEVEL_HEIGHT;
                else if (i == R.id.lv_medium)
                    note_level = LEVEL_MEDIUM;
                else
                    note_level = LEVEL_NONE;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean saveNote2Database(String content) {
        // TODO 插入一条新数据，返回是否插入成功
        ContentValues values = new ContentValues();
        Date mDate = Calendar.getInstance().getTime();
        values.put(TodoContract.TodoEntry.COLUMN_TODO_CONTENT,content);
        values.put(TodoContract.TodoEntry.COLUMN_TODO_DATE,mDate.getTime());
        values.put(TodoContract.TodoEntry.COLUMN_TODO_LEVEL,note_level);
        values.put(TodoContract.TodoEntry.COLUMN_TODO_STATE,0);

        long newId = MainActivity.getDatabase().insert(TodoContract.TodoEntry.TABLE_NAME,null,values);
        Log.d(TAG, "saveNote2Database: ");
        return newId!=0;
    }
}
