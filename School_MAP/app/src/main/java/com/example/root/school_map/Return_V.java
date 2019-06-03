package com.example.root.school_map;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by root on 2018/11/20.
 */

public class Return_V {

    //SQL
    final static int DATA_SUCCEED=110;//數據提交成功
    final static int DATA_SUBMING=120;//數據正在提交
    final static int STRING_EMPTY=111;//如果字符串爲空 返回
    final static int EXIST=200;//是否存在這個記錄
    final static int NOTLEGAL=440;//但輸入字符不合法時返回
    final static int SETTITLE=500;//設置標題
    final static int NOLOGIN=12;//
    final static int LOGIN=80;
    final static int SETTEACHER=13;//当选择教学班 返回老师
    final static int SETTEACHER_2=130;//当选择教学班 返回老师
    final static int LOADING=26;//加载中
    final static int SHOWTABLE=27;//顯示課表
    final static int SHOWTABLE_ERR=270;//顯示課表出錯

    static boolean ISREGISTER=false;//是否注冊
    static String CURRENTSTUNUMBER="";//返回當前學號
    static String CURRENTNAME="";//返回當前學生名字

    static ArrayList<String> CURRENTCLASSES_NUMBER;//返回當前學生擁有的教學班集合
//    static ArrayList<String> CURRENTCLASSES_TEACHERS;//返回當前學生所有教學班老師的集合
    static ArrayList<String> CURRENTSHOWTABLE;//返回當前學生所有課程 用來顯示課表的集合
    static ArrayList<String> CURRENTSCHOOLTIME_INDEX;//返回當前學生所有課程的上課時間的座標 X座標
    static ArrayList<String> CURRENTWEEK_INDEX;//返回當前學生所有課程的星期的座標 Y座標
    static String CURRENTTEACHER_NUM;//返回当前选择老师工号
    static String CURRENTTEACHER_NAME;//返回当前选择老师名字
    static String CURRENTTEACHER_PHONE;//返回当前选择老师电话

    //課表是否導入過
    static boolean classes_isImport=false;

    final static int SETBACKGROUND_RED=56;//设置签到状态  red/green
    final static int SETBACKGROUND_GREEN=57;//设置签到状态  red/green
    final static int CHEAK_ERR=574;//簽到失敗
    final static int SETCOURNAME=58;//设置当前的课程
    final static int SHOWCOURSETABLE=59;//顯示課程表
    final static int WEEKEND=666;//假期

    //模式
    final static String STU_NUMBER_PATTERN="[0-9]{10}";

    final static int DEBUG=11;//調試
    final static int REQUEST_PHONE_STATE=1;//手機狀態 動態權限 用
    final static int SP_CLASSED=30;//請假面板 選擇教學辦數據


    //GPS
    static double CURRENT_LATITUDE;//当前学生的经度
    static double CURRENT_LONGITUDE;//当前学生的纬度



    static String week[]={"星期一","星期二","星期三","星期四","星期五"};
    static HashMap<Integer,String> getweek_String=new HashMap<>();
    static HashMap<String,Integer> getweek_Num=new HashMap<>();
    static void map_init()
    {
        for(int i=1;i<6;i++)
        {
            getweek_String.put(i,week[i-1]);
        }

        for(int j=1;j<6;j++)
        {
            getweek_Num.put(week[j-1],j);
        }

    }




    static HashMap<String,String> c2j=new HashMap<>();//教學班 對應 教師


    static String get_week;//獲得星期
    static String get_schooltime;//上課時間
    static String get_jobnumber;//教師工號
    static String get_teachername;//教師名字
    static String get_coursecode;//課程代碼
    static String get_coursename;//課程名稱


}
