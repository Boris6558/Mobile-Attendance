package com.example.root.school_map_teacher;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    EditText gonghao,passwd;
    Button login;

    String input_gonghao="";//當前輸入工號
    String input_passwd="";//當前輸入密碼

    //SQL
    JDBC_MySQL_Connect db=null;


    Thread longinThread=null;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //控件初始化 變量初始化
        init_view();
        init_class_variable();
        listen_view();
    }

    //控件的初始化工作
    void init_view()
    {
        gonghao= (EditText) findViewById(R.id.editText_gonghao);
        passwd= (EditText) findViewById(R.id.editText2_passwd);
        login= (Button) findViewById(R.id.button_login);

    }

    //類 變量的初始化
    void init_class_variable()
    {

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case Return_V.LOGIN_ERROR:
                        Toast.makeText(getApplicationContext(),"登入失敗！",Toast.LENGTH_LONG).show();
                        login.setEnabled(true);
                        break;
                    case Return_V.LOGIN_SECC:
                        Toast.makeText(getApplicationContext(),"登入成功！",Toast.LENGTH_LONG).show();
                        login.setEnabled(true);
                        break;
                    case Return_V.STARTACTIVITY:
                        Intent intent=new Intent();
                        intent.setClass(MainActivity.this,ManagerStudent.class);
                        startActivity(intent);
                        break;

                }

            }

        };
    }


    //控件的監聽系列
    void listen_view()
    {

        //登錄
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login();
            }
        });

    }


    void login()
    {
        login.setEnabled(false);
        if(gonghao.getText().toString().equals("")||passwd.getText().toString().equals(""))
        {
            handler.sendEmptyMessage(Return_V.LOGIN_ERROR);
        }else{
            longinThread=new Thread(new Login_Thread());
            longinThread.start();
        }

    }

//登錄時  啟動線程
    class Login_Thread implements  Runnable
    {

        @Override
        public void run() {
            db= new JDBC_MySQL_Connect();
            input_gonghao=gonghao.getText().toString();
            input_passwd=db.DB_getSome_by_other("phone",input_gonghao,"select * from teachers where job_number=?");
            if(input_passwd.toString().length()>0&&input_passwd.toString().equals(passwd.getText().toString()))
            {
                handler.sendEmptyMessage(Return_V.LOGIN_SECC);
                Return_V.CURRENT_GONGHAO=gonghao.getText().toString();
                handler.sendEmptyMessage(Return_V.STARTACTIVITY);
            }else{
                handler.sendEmptyMessage(Return_V.LOGIN_ERROR);
            }

        }
    }

}
