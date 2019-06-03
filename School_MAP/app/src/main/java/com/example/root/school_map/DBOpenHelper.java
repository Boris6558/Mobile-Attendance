package com.example.root.school_map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by root on 2018/7/27.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    final String TAG="DBOpenHelper";
    static final int VERSION=1;
    static final String DGNAME="data.db";
    public DBOpenHelper(Context context) {
        super(context, DGNAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.e(TAG,"onCreate");
        //學生的表格
        sqLiteDatabase.execSQL("create table student(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "number int(10)," +//學號
                "name char(10)," +//姓名
                "SIM char(30)," +//SIM卡號
                "isregist char(1)" +//是否註冊 Y OR N
                ")"
        );
        //位置
        sqLiteDatabase.execSQL("create table GPS(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+ //編號
                "longitude double(10,7)," +//經度
                "latitude double(10,7)"+//緯度
                ")"
        );

        //教學班對應老師  教學班-----老師
        sqLiteDatabase.execSQL("create table classes_teacher(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+ //編號
                "classes char(8)," +//教學班
                "job_number int(4)"+//教學班對應的工號
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        Log.e(TAG,"onUpgrade");
        //如果旧表存在，删除，所以数据将会消失
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS student");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS GPS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS classes_teacher");


        //再次创建表
        onCreate(sqLiteDatabase);

    }
}
