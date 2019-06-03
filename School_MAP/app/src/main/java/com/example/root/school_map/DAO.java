package com.example.root.school_map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by root on 2018/7/27.
 *
 * 本地數據庫的操作
 */

public class DAO {

    DBOpenHelper helper;
    SQLiteDatabase db;
    Cursor cursor;

    Vector<String> vfind = new Vector<>();//find方法返回的數據集
    Vector<String> vfind2 = new Vector<>();//find方法返回的數據

    DAO(Context context) {
        helper = new DBOpenHelper(context);
    }

    //insert into student(number,name,SIM,isregist) values (?,?,?,?)
    //new Object[]{Return_V.CURRENTSTUNUMBER, Return_V.CURRENTNAME, SIM,"N"
    void add(String sql,Object[] obj) {
        db = helper.getWritableDatabase();

        db.execSQL(sql, obj);

    }

    //參數一，要查找的列  參數二：通過SIM來查找
    //操作student 表格
    Vector<String> find(String colum, String SIM) {

        db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from student where SIM=?", new String[]{
                SIM
        });
        while (cursor.moveToNext()) {
            vfind.add(cursor.getString(cursor.getColumnIndex(colum)));
        }

        return vfind;

    }
    //操作說明
    //select select_colun from table where [where參數]=where_eq
    Vector<String> find(String table,String select_colum,String where,String where_eq)
    {


        db=helper.getReadableDatabase();
        cursor=db.rawQuery("select * from ? where ?=?",new String[]{
                table,where,where_eq
        });

        while (cursor.moveToNext()) {
            vfind2.add(cursor.getString(cursor.getColumnIndex(select_colum)));
        }

        return vfind2;

    }

    /**
     * 更新表table,設置字段set的值為value 當where(條件 格式為 如 name=?)
     * ?的值是whereva裡的元素
     */


    void update(String table,String set,String value,String where,String whereva[])
    {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(set,value);
        db.update(table,values,where,whereva);

    }

    //dao.deleteall("student",null,null);是刪除全部記錄
    int deleteall(String table,String wherevalue,String va[])
    {
        db = helper.getWritableDatabase();
        return db.delete(table,wherevalue,va);
    }
    void closeall()
    {
        db.close();
        cursor.close();
    }

//    void delete(Integer... id) {
//        if (id.length > 0) {
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < id.length; i++) {
//                sb.append('?').append(',');
//            }
//            sb.deleteCharAt(sb.length() - 1);
//            db = helper.getWritableDatabase();
//            db.execSQL("delete from t_student where id in (" + sb + ")", id);
//        }
//    }
}
//
//    List<Student> getScrollData(int start,int count)
//    {
//        List<Student> students =new ArrayList<>();
//        db=helper.getWritableDatabase();
//        cursor=db.rawQuery("select * from t_student limit ?,?",new String[]{
//                String.valueOf(start),String.valueOf(count)
//        });
//while(cursor.moveToNext())
//{
//    students.add(new Student(cursor.getInt(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("name")),cursor.getColumnIndex("age")));
//}
//
//        return students;
//    }
//
//    long getCount()
//    {
//        db=helper.getWritableDatabase();
//        cursor=db.rawQuery("select count(id) from t_student",null);
//        return cursor.moveToNext()?cursor.getLong(0):0;
//    }
//
//    void close()
//    {
//        db.close();
//        cursor.close();
//    }
//
//}
