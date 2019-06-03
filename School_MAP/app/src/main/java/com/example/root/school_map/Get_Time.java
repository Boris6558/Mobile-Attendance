package com.example.root.school_map;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by root on 2018/12/4.
 *
 * 获取当前时间
 */

public class Get_Time {




    //获取年份
     static String get_time(String str)
    {

         Calendar c= Calendar.getInstance();
        Date date=new Date();


        if(str.trim().equals("year"))
        {
            SimpleDateFormat df = new SimpleDateFormat("yyyy");//设置日期格式
            return df.format(date);
        }else if(str.trim().equals("month")){
            SimpleDateFormat df = new SimpleDateFormat("MM");//设置日期格式
            return df.format(date);
        }else if(str.trim().equals("hour")){
            SimpleDateFormat df = new SimpleDateFormat("HH");//设置日期格式
            return df.format(date);
        }else if(str.trim().equals("minute")){
            SimpleDateFormat df = new SimpleDateFormat("mm");//设置日期格式
            return df.format(date);
        }else if(str.trim().equals("second")){
            SimpleDateFormat df = new SimpleDateFormat("ss");//设置日期格式
            return df.format(date);
        }else if(str.trim().equals("day")){
            SimpleDateFormat df = new SimpleDateFormat("dd");//设置日期格式
            return df.format(date);
        }else if(str.trim().equals("week")){
            SimpleDateFormat df = new SimpleDateFormat("EEEE");//设置日期格式
            return df.format(date);
        }else{
            return "null";
        }



    }

}
