package com.byted.camp.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.byted.camp.todolist.db.TodoContract.TodoEntry;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public class TodoDbHelper extends SQLiteOpenHelper {

    // TODO 定义数据库名、版本；创建数据库
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TodoNote.db";
    public TodoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TodoEntry.TABLE_NAME + "(" +
                        TodoEntry._ID + " INTEGER PRIMARY KEY," +
                        TodoEntry.COLUMN_TODO_DATE + " INTEGER," +
                        TodoEntry.COLUMN_TODO_STATE + " INTEGER," +
                        TodoEntry.COLUMN_TODO_CONTENT + " TEXT," +
                        TodoEntry.COLUMN_TODO_LEVEL + " INTEGER NOT NULL)";
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*for (int i = oldVersion;i < newVersion;i++){
            switch (i) {
                case 1:
                    try {
                        db.execSQL("ALTER TABLE " + TodoEntry.TABLE_NAME + " ADD " + " text");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;

            }
        }*/
    }

}
