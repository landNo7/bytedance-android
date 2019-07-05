package com.byted.camp.todolist.db;

import android.provider.BaseColumns;

import com.byted.camp.todolist.beans.State;

import java.util.Date;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public final class TodoContract {

    // TODO 定义表结构和 SQL 语句常量

    private TodoContract() {
    }
    public static final class TodoEntry implements BaseColumns{

        public final static String TABLE_NAME = "todo_list";
        public final static String COLUMN_TODO_ID = "id";
        public final static String COLUMN_TODO_DATE = "date";
        public final static String COLUMN_TODO_STATE = "state";
        public final static String COLUMN_TODO_CONTENT = "content";
        public final static String COLUMN_TODO_LEVEL = "level";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE" + TodoEntry.TABLE_NAME + "(" +
                        TodoEntry.COLUMN_TODO_ID + " INTEGER PRIMARY KEY," +
                        TodoEntry.COLUMN_TODO_DATE + " INTEGER," +
                        TodoEntry.COLUMN_TODO_STATE + " INTEGER," +
                        TodoEntry.COLUMN_TODO_CONTENT + " TEXT," +
                        TodoEntry.COLUMN_TODO_LEVEL + " INTEGER NOT NULL)";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TodoEntry.TABLE_NAME;

        public final String getSQL_CREATE_ENTRIES(){
            return SQL_CREATE_ENTRIES;
        }

        public final String getSQL_DELETE_ENTRIES(){
            return SQL_DELETE_ENTRIES;
        }
    }
}
