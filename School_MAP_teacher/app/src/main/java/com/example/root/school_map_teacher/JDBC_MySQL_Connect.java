package com.example.root.school_map_teacher;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by root on 2018/9/11.
 *
 * 这个类负责与服务器的MYSQL进行连接 操作
 * IP:209.250.248.95
 * USER:root
 * passwodrs:sqlroot
 *
 * 移动考勤1.0
 */

public class JDBC_MySQL_Connect {

    // JDBC 驱动名及数据库 URL
     final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
     final String DB_URL = "jdbc:mysql://209.250.248.95:3306/Check_on_school";
    // 数据库的用户名与密码
     final String USER = "root";
     final String PASS = "rootroot";

    //通過學號返回的SIM卡
     String return_SIM="";

    //SQL需要的类
     Connection conn = null;
     Statement stmt = null;

    //负责执行SQL语句
     ResultSet resultset_sql=null;
     PreparedStatement pst=null;

    //数据是否添加成功
    boolean is_added=false;
    //是否查询成功
    boolean is_selected=false;

    //返回一個字段的集合
    ArrayList<String> vectors=new ArrayList<>();



    JDBC_MySQL_Connect()
    {
        //注册驱动
        try {
            Class.forName(JDBC_DRIVER);

            //打开连接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //关闭流
    void close_all() {
        try {
            conn.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

/**
 * 下面代碼  把學生SIM卡
 * 註冊到數據庫
 * 成功返回true 失敗返回false
 *
 */
boolean DB_register_studen_update(String sql)
{

    try {
        pst = conn.prepareStatement(sql);
        pst.executeUpdate();

        is_added=true;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return is_added;
}

    /**
     *  通過某個字段獲得其他字段信息
     *
     * 1.要查找（返回的字段）
     * 2. where 的條件語句
     *  3.格式為"select * from tablename where xxx=?"
     *
     *  句式 select some from tablename where xxx=where_other
     */
String DB_getSome_by_other(String some,String where_other,String sql )
{
    try {
//"select * from students where stu_number=?"
        pst = conn.prepareStatement(sql);
        pst.setString(1,where_other);
        resultset_sql=pst.executeQuery();

        while(resultset_sql.next())
        {
            return_SIM=resultset_sql.getString(some);
        }


    } catch (Exception e) {
        e.printStackTrace();
    }


    return return_SIM;
}

    /**
     *  通過某個字段獲得其他字段信息 集合
     *
     * 1.要查找（返回的字段）
     * 2. where 的條件語句
     *  3.格式為"select * from tablename where xxx=?"
     *
     *  句式 select some from tablename where xxx=where_other
     */

    ArrayList<String> DB_part_of_table(String some, String where_other, String sql)
{

    try {
//"select * from students where stu_number=?"
        pst = conn.prepareStatement(sql);
        pst.setString(1,where_other);
        resultset_sql=pst.executeQuery();

        while(resultset_sql.next())
        {
            vectors.add(resultset_sql.getString(some));
        }


    } catch (Exception e) {
        e.printStackTrace();
    }

    return vectors;
}
    //数据的添加
    //jdbc.DB_add(String.format("insert into jingweidu (jingdu,weidu) values ('%s','%s')",jingdu+"",weidu+""))

    //修改
    //DB_add(String.format("update stu_timetable set longitude='%s',latitude='%s' where stu_number='%s' and classes_number='%s'",
    //Return_V.CURRENT_LATITUDE+"",Return_V.CURRENT_LATITUDE+"",Return_V.CURRENTSTUNUMBER,"HG01"));
    boolean DB_add(String sql_str)  {

        if( conn==null || stmt==null )
        {
            is_added=false;
        }else{

            try {
//                pst = conn.prepareStatement(sql_str);
//                pst.executeUpdate();
                stmt=conn.createStatement();
                stmt.executeUpdate(sql_str);
            }catch (Exception e){
                e.printStackTrace();
            }


            is_added=true;
        }


        return is_added;
    }









































///////////////////////////////////////////以下是測試區//////////////////////////////////////////////////////

    /**
     *
     * @param sql_str
     * @return
     * @throws SQLException
     * explain:
     *
     *
     *              以下代碼為測試
     *
     */
    //数据的添加
    boolean test_data_add(String sql_str) throws SQLException {

        if( conn==null || stmt==null )
        {
            is_added=false;
        }else{

            PreparedStatement pst = conn.prepareStatement(sql_str);
            pst.executeUpdate();

            is_added=true;
        }


    return is_added;
    }

    boolean test_data_select( String sql_str ) throws SQLException {


        if( conn==null || stmt==null )
        {
            is_selected=false;
        }else{

            resultset_sql = stmt.executeQuery(sql_str);
            while(resultset_sql.next()){

                String jingdu = resultset_sql.getString("jingdu");
                //获取address这列数据
                String weidu = resultset_sql.getString("weidu");


                Log.e("TAG","精度："+jingdu+" 纬度："+weidu);



            }

            is_selected=true;
        }



        return is_selected;
    }

}
