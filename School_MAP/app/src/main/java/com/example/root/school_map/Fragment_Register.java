package com.example.root.school_map;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.RunnableFuture;

/**
 * Created by root on 2018/10/26.
 */

public class Fragment_Register extends android.app.Fragment {




    EditText stu_number,stu_SIM;
    Button submit_button;//提交按鈕
    TextView tip_textview,xing_textView;

    Handler handler=null;//數據提交成功后 顯示綠色提示

    //SQL
    JDBC_MySQL_Connect db;

    String re_SIM="";//通過學號從數據庫中返回的值
    String re_stunumber="";//返回的學號


    //fragment
    android.app.FragmentManager fragmentManager;
    android.app.FragmentTransaction transaction;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.activity_register,null);

        //初始化工作
        init_view(v);

        return v;
    }


    //控件初始化
    void init_view(View v)
    {

        stu_number=v.findViewById(R.id.stu_number_editText);

        stu_SIM=v.findViewById(R.id.SIM_editText);
        stu_SIM.setText(Phone_Manager_Util.getICC((AppCompatActivity) getActivity()));
        stu_SIM.setFocusableInTouchMode(false);

        tip_textview=v.findViewById(R.id.tip_textView);
        xing_textView=v.findViewById(R.id.xing_textView);
        xing_textView.setVisibility(View.INVISIBLE);

        submit_button=v.findViewById(R.id.regist_button);

        //註冊 提交按鈕
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),str_level+"  "+str_department,Toast.LENGTH_LONG).show();

                //tianjia添加數據
                handler.sendEmptyMessage(Return_V.DATA_SUBMING);
                Thread thread=new Thread(new DB_add_data_thread());
                thread.start();




            }
        });

        handler=new Handler(){

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch( msg.what )
                {
                    case Return_V.DATA_SUBMING:
                        tip_textview.setText("数据正在提交......");
                        tip_textview.setTextColor(Color.RED);
                        submit_button.setEnabled(false);
                        break;
                    case Return_V.DATA_SUCCEED:
                        tip_textview.setText("提交成功！");
                        tip_textview.setTextColor(Color.GREEN);
                        xing_textView.setVisibility(View.VISIBLE);
                        xing_textView.setTextColor(Color.GREEN);
                        submit_button.setEnabled(true);

                        break;
                    case Return_V.STRING_EMPTY:
                        tip_textview.setText("输入为空,请填写完整。");
                        tip_textview.setTextColor(Color.RED);
                        stu_number.setFocusable(true);
                        xing_textView.setVisibility(View.VISIBLE);
                        xing_textView.setTextColor(Color.RED);
                        submit_button.setEnabled(true);
                        break;
                    case Return_V.NOTLEGAL:
                        tip_textview.setText("学号不合法!");
                        tip_textview.setTextColor(Color.RED);
                        stu_number.setFocusable(true);
                        xing_textView.setVisibility(View.VISIBLE);
                        xing_textView.setTextColor(Color.RED);
                        submit_button.setEnabled(true);
                        break;
                    case Return_V.EXIST:
                        tip_textview.setText("学号不存在!");
                        tip_textview.setTextColor(Color.RED);
                        stu_number.setFocusable(true);
                        xing_textView.setVisibility(View.VISIBLE);
                        xing_textView.setTextColor(Color.RED);
                        submit_button.setEnabled(true);
                        break;
                    case Return_V.DEBUG:
//                        Toast.makeText(getActivity(),re_SIM,Toast.LENGTH_LONG).show();
//                        Toast.makeText(getActivity(),"12166156",Toast.LENGTH_LONG).show();
                        break;
                }
            }


        };




    }







    @Override
    public void onPause() {
        super.onPause();
    }


    /**
     * 獲得控件的值  以下的方法
     *
     */
        String get_stu_number_string()
        {
            return stu_number.getText().toString();
        }

        String get_SIM_string()
        {
            return stu_SIM.getText().toString();
        }




    //註冊界面 點擊提交的線程 寫入數據庫
    class DB_add_data_thread implements Runnable
    {


        @Override
        public void run() {


            db=new JDBC_MySQL_Connect();

            //判斷輸入學號的合法性
            if(get_stu_number_string().equals(""))
            {
                handler.sendEmptyMessage(Return_V.STRING_EMPTY);
                Return_V.ISREGISTER=false;
//                handler.sendEmptyMessage(Return_V.DEBUG);
            }else if(!stunumber_is_true(get_stu_number_string()))
            {
                handler.sendEmptyMessage(Return_V.NOTLEGAL);
                Return_V.ISREGISTER=false;
            }else if(!stunumber_is_exist(get_stu_number_string()))
            {
                handler.sendEmptyMessage(Return_V.EXIST);
                Return_V.ISREGISTER=false;
            }
            else{

                //提交數據
                db.DB_register_studen_update(String.format("update students set SIM='%s' where stu_number='%s'"
                        ,get_SIM_string()
                        ,get_stu_number_string()
                ));




                Return_V.ISREGISTER=true;


                //如果該手機註冊過就進入個人信息 否則就註冊
                if(Return_V.ISREGISTER)
                {
                    Return_V.CURRENTSTUNUMBER=db.DB_getSome_by_other("stu_number",Phone_Manager_Util.getICC((AppCompatActivity) getActivity()),
                            "select * from students where SIM=?");
                    Return_V.CURRENTNAME=db.DB_getSome_by_other("name",Return_V.CURRENTSTUNUMBER
                            ,"select * from students where stu_number=?");
//添加姓名 學號，是否註冊
                    MainActivity.dao.update("student","number",Return_V.CURRENTSTUNUMBER,"SIM=?",
                            new String[]{Phone_Manager_Util.getICC((AppCompatActivity) getActivity())});
                    MainActivity.dao.update("student","name",Return_V.CURRENTNAME,"SIM=?",
                            new String[]{Phone_Manager_Util.getICC((AppCompatActivity) getActivity())});
                    MainActivity.dao.update("student","isregist","Y","SIM=?",
                            new String[]{Phone_Manager_Util.getICC((AppCompatActivity) getActivity())});
                    showOwn_info_view();
                    handler.sendEmptyMessage(Return_V.DATA_SUCCEED);

                }else{
                    Return_V.ISREGISTER=false;
                    Return_V.CURRENTSTUNUMBER="";
                    Return_V.CURRENTNAME="";

                }

                db.close_all();




            }

        }
    }

    void getFragmen_Transaction_Instance()
    {
        fragmentManager=getFragmentManager();
        transaction=fragmentManager.beginTransaction();

    }
    void Fragment_Replace(int layoutid,Fragment f)
    {
        transaction.replace(layoutid, f);
        transaction.commit();
    }
    //顯示個人界面
    void showOwn_info_view()
    {
        getFragmen_Transaction_Instance();
        Fragment_Own_Info info=new Fragment_Own_Info();
        Fragment_Replace(R.id.frame, info);

    }

    /**
     * 以下方法
     * 通過學號查找SIM
     * 判斷是否注冊
     *
     */
    boolean SIM_is_register(String stu_number)
    {
        re_SIM=db.DB_getSome_by_other("SIM",stu_number,"select * from students where stu_number=?");
        return re_SIM.equals("")?false:true;

    }

    /**
     * 以下方法
     * 通過SIM查找學號
     * 判斷是否注冊
     */
    boolean stunumber_is_register(String SIM)
    {
        re_stunumber=db.DB_getSome_by_other("stu_number",SIM,"select * from students where SIM=?");
        return re_stunumber.equals("")?false:true;

    }

    /**
     * 判斷學號是否合法
     */
    boolean stunumber_is_true(String stu_nubmer)
    {

        return (stu_nubmer.trim().matches(Return_V.STU_NUMBER_PATTERN)?true:false);
    }

    /**
     * 判斷學號是否存在
     */
    boolean stunumber_is_exist(String stu_nubmer)
    {
        re_stunumber=db.DB_getSome_by_other("stu_number",stu_nubmer,"select * from students where stu_number=?");
        return re_stunumber.equals("")?false:true;

    }
}
