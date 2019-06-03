package com.example.root.school_map_teacher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by root on 2019/3/3.
 */

public class ManagerStudent extends AppCompatActivity {

    static Spinner sp_select_class;//老师任教的課程
    TextView tip;
    Button button;

    Handler handler;


    //SQL
    JDBC_MySQL_Connect db;
    Thread select_thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managerstudent);

        //控件初始化 變量初始化
        init_view();
        init_class_variable();
        listen_view();

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



        }
    }

    //控件的初始化工作
    void init_view()
    {
        sp_select_class= (Spinner) findViewById(R.id.select_classes_spinner);
        tip= (TextView) findViewById(R.id.tip);
        button= (Button) findViewById(R.id.button);

    }
    //類 變量的初始化
    void init_class_variable()
    {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case Return_V.LOADING:
                        tip.setText("正在导入任課信息......");
                        break;
                }
            }
        };
    }

    //控件的監聽系列
    void listen_view()
    {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(ManagerStudent.this,LeaveList.class);
                startActivity(intent);
            }
        });

    }

}
