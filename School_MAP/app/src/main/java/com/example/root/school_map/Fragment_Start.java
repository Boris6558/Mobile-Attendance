package com.example.root.school_map;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by root on 2018/10/27.
 * //進入手機時顯示的動態圖
 *
 */

public class Fragment_Start extends Fragment {

    Button importb;//導入課程

    Button weeks[]=new Button[5];//星期
    int weeks_[]={R.id.b_Monday,R.id.b_Tuesday,R.id.b_Wednesday,R.id.b_Thursday,R.id.b_Friday};

    static Button time[]=new Button[8];//時間段
    int time_[]={
            R.id.b_09_00T10_20,
            R.id.b_10_40T12_00,
            R.id.b_12_30T13_50,
            R.id.b_14_00T15_20,
            R.id.b_15_30T16_50,
            R.id.b_17_00T18_20,
            R.id.b_19_00_20_20,
            R.id.b_20_30_21_50,
    };

    static Button course[][]=new Button[8][5];
    int course_[][]={
                {R.id.b_Monday_1_2,R.id.b_Tuesday_1_2,R.id.b_Wednesday_1_2,R.id.b_Thursday_1_2,R.id.b_Friday_1_2},
                {R.id.b_Monday_3_4,R.id.b_Tuesday_3_4,R.id.b_Wednesday_3_4,R.id.b_Thursday_3_4,R.id.b_Friday_3_4},
                {R.id.b_Monday_5_6,R.id.b_Tuesday_5_6,R.id.b_Wednesday_5_6,R.id.b_Thursday_5_6,R.id.b_Friday_5_6},
                {R.id.b_Monday_7_8,R.id.b_Tuesday_7_8,R.id.b_Wednesday_7_8,R.id.b_Thursday_7_8,R.id.b_Friday_7_8},
                {R.id.b_Monday_9_10,R.id.b_Tuesday_9_10,R.id.b_Wednesday_9_10,R.id.b_Thursday_9_10,R.id.b_Friday_9_10},
                {R.id.b_Monday_11_12,R.id.b_Tuesday_11_12,R.id.b_Wednesday_11_12,R.id.b_Thursday_11_12,R.id.b_Friday_11_12},
                {R.id.b_Monday_13_14,R.id.b_Tuesday_13_14,R.id.b_Wednesday_13_14,R.id.b_Thursday_13_14,R.id.b_Friday_13_14},
                {R.id.b_Monday_15_16,R.id.b_Tuesday_15_16,R.id.b_Wednesday_15_16,R.id.b_Thursday_15_16,R.id.b_Friday_15_16}

    };

    //SQL
    JDBC_MySQL_Connect db;

    Handler handler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.activity_start,null);
        class_vlatile_init();
        init_view(v,savedInstanceState);
        litenner_();


//        course[0][1].setText(time[1].getText()+"");//測試

        if(Return_V.classes_isImport)
        {
            handler.sendEmptyMessage(Return_V.SHOWTABLE);
        }





        return v;
    }


    //控件初始化
    void init_view(View v,Bundle savedInstanceState)
    {
        //WEEK
        for(int i=0;i<weeks.length;i++)
        {
            weeks[i]=v.findViewById(weeks_[i]);
        }
        //TIME
        for(int i=0;i<time.length;i++)
        {
            time[i]=v.findViewById(time_[i]);
        }
        //course
        for(int i=0;i<course.length;i++)
        {
            for(int j=0;j<course[i].length;j++)
            {
                course[i][j]=v.findViewById(course_[i][j]);
            }
        }

        importb=v.findViewById(R.id.set);



    }


    //類 變量的初始化
    void class_vlatile_init() {



        handler = new Handler() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case Return_V.SHOWTABLE:
                        if (Return_V.CURRENTSHOWTABLE.size() != 0) {
                            show_table(Return_V.CURRENTSHOWTABLE);
                        }
                        break;
                    case Return_V.SHOWTABLE_ERR:
                        Toast.makeText(getContext(),"請先進入請假頁面，導入成功後才可以導入課表",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };
    }
    //控件的監聽
    void litenner_()
    {
        importb.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if(Return_V.CURRENTCLASSES_NUMBER==null ||
                        Return_V.CURRENTSHOWTABLE==null ||
                        Return_V.CURRENTSCHOOLTIME_INDEX==null)
                {
                    handler.sendEmptyMessage(Return_V.SHOWTABLE_ERR);
                }else{
                    handler.sendEmptyMessage(Return_V.SHOWTABLE);
                }


            }
        });
    }


    //顯示課表
    void show_table(ArrayList<String> arr)
    {
        db=new JDBC_MySQL_Connect();


        for(int i=0;i<arr.size();i++)
        {

            if(course[is_havetime(Return_V.CURRENTSCHOOLTIME_INDEX.get(i))][Integer.valueOf(Return_V.CURRENTWEEK_INDEX.get(i))-1].getText()
                    .equals(""))
            {
                course[is_havetime(Return_V.CURRENTSCHOOLTIME_INDEX.get(i))][Integer.valueOf(Return_V.CURRENTWEEK_INDEX.get(i))-1].
                        setText(arr.get(i));
            }else{
                course[is_havetime(Return_V.CURRENTSCHOOLTIME_INDEX.get(i))][Integer.valueOf(Return_V.CURRENTWEEK_INDEX.get(i))-1].
                        setText(
                                course[is_havetime(Return_V.CURRENTSCHOOLTIME_INDEX.get(i))][Integer.valueOf(Return_V.CURRENTWEEK_INDEX.get(i))-1].
                                        getText()+"\n\n"+arr.get(i));
            }





//            is_havetime(Return_V.CURRENTSCHOOLTIME_INDEX.get(i))
//            Integer.valueOf(Return_V.CURRENTWEEK_INDEX.get(i))-1

        }


    }
    //            int time_index=is_havetime(get_schooltime);
//            int week_index=is_whereweek(get_week);+"-"

    //再time數組裡是否包含時間段  如第1节-第2节\n09:00-10:20  是否存在09：00-10：20這個字符串
    //判斷str裡是否存在str1  返回包含的那個字符串的下標  輸入參數 查找到的上課時間
    int is_havetime(String str1)
    {
        int index=-1;
        for(int i=0;i<time.length;i++)
        {
            if(time[i].getText().toString().contains(str1))
            {
                index=i;
                break;
            }
        }
        return index;
    }


    //判斷返回的星期是星期幾 併返回下標 輸入參數 查找到的日期  為阿拉伯數字的字符串形
    int is_whereweek(String str)
    {
        int whereweek_index=-1;
        Return_V.map_init();
        String week_char=Return_V.getweek_String.get(str);
        for(int i=0;i<weeks.length;i++)
        {
            if(weeks[i].getText().toString().equals(week_char))
            {
                whereweek_index=i;
                break;
            }
        }
        return whereweek_index;
    }






    @Override
    public void onPause() {
        super.onPause();

    }



}
