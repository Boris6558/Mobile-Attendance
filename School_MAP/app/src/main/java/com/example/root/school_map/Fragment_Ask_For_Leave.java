package com.example.root.school_map;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by root on 2018/10/27.
 */

public class Fragment_Ask_For_Leave extends Fragment {


    static Spinner sp_select_class;//當前學生所有教學班編號
    TextView teacher_tv;
    static Button submit,hassubmit;
    EditText editText;
    static TextView tip;


    //SQL
    JDBC_MySQL_Connect db;

    Handler handler=null;

//    String arr[]={"gegege","121215363"};
    static ArrayAdapter<String> adapter;
    Thread select_thread;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.activity_ask_for_leave,null);

        init_view(v);
        init_vlatile();
        view_listenner();


        return v;
    }


    //控件初始化
    void init_view(View v)
    {

        sp_select_class=v.findViewById(R.id.select_classes_spinner);
        teacher_tv=v.findViewById(R.id.teacher_textv);

        submit=v.findViewById(R.id.reason_button);
        submit.setEnabled(false);
        hassubmit=v.findViewById(R.id.hasreason);
        hassubmit.setEnabled(false);

        editText=v.findViewById(R.id.reason_edittext);
        tip=v.findViewById(R.id.reason_tip);
        tip.setText("加载中......");
    }

    //變量 類的初始化工作
    void  init_vlatile()
    {





        handler=new Handler(){

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch( msg.what )
                {
                    case Return_V.DEBUG:
//                        if(Return_V.ISREGISTER)//測試成功
//                        {
//                            tip.setText("true");
//                        }else{
//                            tip.setText("false");
//                        }
//                        Toast.makeText(getContext(),Return_V.CURRENTCLASSES_NUMBER.size()+"",Toast.LENGTH_LONG).show();
//                      tip.setText("");
//                       for(int i=0;i<Return_V.CURRENTCLASSES_NUMBER.size();i++)
//                       {
//                           tip.append(Return_V.CURRENTCLASSES_NUMBER.get(i)+",");
//                       }
//                        adapter=new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,Return_V.CURRENTCLASSES_NUMBER);
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        sp_select_class.setAdapter(adapter);
                        tip.setText(sp_select_class.getSelectedItem().toString());
                        break;
                    case Return_V.DATA_SUBMING:
                        tip.setText("正在确认身份并提交....");
                        submit.setEnabled(false);

                        break;
                    case Return_V.DATA_SUCCEED:
                        submit.setEnabled(true);
                        hassubmit.setEnabled(true);
                        break;
                    case Return_V.NOLOGIN:
                        tip.setText("no login");
                        break;
                    case Return_V.SETTEACHER://設置顯示的老師工號  未加載

                        if(Return_V.CURRENTTEACHER_NUM.length()<4)
                        {
                            Return_V.CURRENTTEACHER_NUM="0"+Return_V.CURRENTTEACHER_NUM;
                        }
                        teacher_tv.setText(Return_V.CURRENTTEACHER_NUM);
//                        Return_V.CURRENTTEACHER_NUM=Return_V.c2j.get(sp_select_class.getSelectedItem().toString());
//                        teacher_tv.setText(Return_V.CURRENTTEACHER_NUM);
                        tip.setText("一切OK，请选择开始请假");

//                        tip.setText(Return_V.classes_isImport+"");
                        hassubmit.setEnabled(false);
                        submit.setEnabled(true);
                        break;

                    case Return_V.LOADING:
                        teacher_tv.setText("......");
                        tip.setText("正在导入课程信息......");
                        submit.setEnabled(false);
                        break;

                }
            }


        };

    }

    //監聽  各個控件
    void view_listenner()
    {
                //提交按钮
                submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                handler.sendEmptyMessage(Return_V.DATA_SUBMING);
                Thread t=new Thread(new DB_add_data_thread());
                t.start();

                //选择教学办
//                Toast.makeText(getContext(),adapterView.getSelectedItem().toString(),Toast.LENGTH_LONG)
//                        .show();




            }
        });

        //單擊  我已提交 按鈕
        hassubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        sp_select_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                handler.sendEmptyMessage(Return_V.DEBUG);

                handler.sendEmptyMessage(Return_V.LOADING);
                select_thread=new Thread(new DB_select_classes_Thread());
                select_thread.start();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //选择教学班  触发 线程
    class DB_select_classes_Thread implements Runnable
    {

        @Override
        public void run() {
            db = new JDBC_MySQL_Connect();


                Return_V.CURRENTTEACHER_NUM = db.DB_getSome_by_other("job_number", sp_select_class.getSelectedItem().toString(),
                        "select * from classes where classes_number=?");
//
            if(Return_V.classes_isImport==false)
            {
                String str;
                Return_V.CURRENTSHOWTABLE = new ArrayList<>();//課表信息  用來顯示
                Return_V.CURRENTSCHOOLTIME_INDEX = new ArrayList<>();//上課時間 X座標
                Return_V.CURRENTWEEK_INDEX = new ArrayList<>();//上課的星期 Y座標
//                Return_V.CURRENTCLASSES_TEACHERS= new ArrayList<>();//自己課程教學班所有老師

                for (int i = 0; i < Return_V.CURRENTCLASSES_NUMBER.size(); i++) {


                    Return_V.get_week = db.DB_getSome_by_other("week", Return_V.CURRENTCLASSES_NUMBER.get(i),
                            "select * from classes where classes_number=?");
                    Return_V.get_schooltime = db.DB_getSome_by_other("schooltime", Return_V.CURRENTCLASSES_NUMBER.get(i),
                            "select * from classes where classes_number=?");
                    Return_V.get_jobnumber = db.DB_getSome_by_other("job_number", Return_V.CURRENTCLASSES_NUMBER.get(i),
                            "select * from classes where classes_number=?");
                    Return_V.get_teachername = db.DB_getSome_by_other("name", Return_V.get_jobnumber,
                            "select * from teachers where job_number=?");
                    Return_V.get_coursecode = db.DB_getSome_by_other("course_code", Return_V.CURRENTCLASSES_NUMBER.get(i),
                            "select * from classes where classes_number=?");
                    Return_V.get_coursename = db.DB_getSome_by_other("course_name", Return_V.get_coursecode,
                            "select * from course where course_code=?");
                    if (Return_V.get_jobnumber.length() < 4) {
                        Return_V.get_jobnumber = "0" + Return_V.get_jobnumber;
                    }

//                        Return_V.c2j.put(Return_V.CURRENTCLASSES_NUMBER.get(i),Return_V.get_jobnumber);



                    Return_V.CURRENTSCHOOLTIME_INDEX.add(Return_V.get_schooltime);
                    Return_V.CURRENTWEEK_INDEX.add(Return_V.get_week);
                    str = Return_V.get_coursename + "\n" + "(" + Return_V.get_jobnumber + "_" + Return_V.get_teachername + ")\n" + Return_V.CURRENTCLASSES_NUMBER.get(i) + "教學班\n" +
                            "課程代碼：\n" + Return_V.get_coursecode;
                    Return_V.CURRENTSHOWTABLE.add(str);
                }
            }




                Return_V.classes_isImport=true;
                handler.sendEmptyMessage(Return_V.SETTEACHER);

        }
    }


    //提交按鈕點擊后 啓動此綫程 寫入數據庫
    class DB_add_data_thread implements Runnable
    {


        @Override
        public void run() {


            db=new JDBC_MySQL_Connect();

            Return_V.CURRENTSTUNUMBER=db.DB_getSome_by_other("stu_number",Phone_Manager_Util.getICC((AppCompatActivity) getActivity()),
                    "select * from students where SIM=?");
            Return_V.CURRENTNAME=db.DB_getSome_by_other("name",Return_V.CURRENTSTUNUMBER
                    ,"select * from students where stu_number=?");
            Return_V.CURRENTCLASSES_NUMBER=db.DB_part_of_table("classes_number",Return_V.CURRENTSTUNUMBER,
                    "select * from stu_timetable where stu_number=?");
            Return_V.CURRENTTEACHER_NAME=db.DB_getSome_by_other("name",Return_V.CURRENTTEACHER_NUM
                    ,"select * from teachers where job_number=?");
            Return_V.CURRENTTEACHER_PHONE=db.DB_getSome_by_other("phone",Return_V.CURRENTTEACHER_NUM
                    ,"select * from teachers where job_number=?");


            if(Return_V.ISREGISTER)//如果這臺手機注冊了,就加載該學生的教學班 任課老師
            {
//                handler.sendEmptyMessage(Return_V.DEBUG);
                sendSMS(Return_V.CURRENTTEACHER_NAME+"老师您好！ 我的学号是"+Return_V.CURRENTSTUNUMBER+
                " 教学班为"+sp_select_class.getSelectedItem().toString()+" 我因 "+editText.getText().toString()
                        +" 故请假。"
                );

            }else{
                handler.sendEmptyMessage(Return_V.NOLOGIN);
            }

            db.close_all();
            handler.sendEmptyMessage(Return_V.DATA_SUCCEED);
            }

        }

    //發訊息
    void sendSMS(String smsBody)

    {

        Uri smsToUri = Uri.parse("smsto:");

        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.setData(Uri.parse("smsto:"+Return_V.CURRENTTEACHER_PHONE));
        intent.putExtra("sms_body", smsBody);

        startActivity(intent);
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
