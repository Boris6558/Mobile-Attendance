package com.example.root.school_map;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by root on 2018/11/18.
 */

public class Fragment_Own_Info extends Fragment {

    TextView stu_name,stu_number,see_classes,tvdi,tvjie; //姓名 学号 查看课表
    EditText ed_1_2_16;
    TextView signin_bg,current_course;//签到状态 显示当前课程
    Button signin_button;//签到按钮
//    ImageView img;//更換圖片

    //fragment
    android.app.FragmentManager fragmentManager;
    android.app.FragmentTransaction transaction;

    Handler handler=null;

    JDBC_MySQL_Connect db;


    static String show_course="";//显示当前课程
    Thread thread_signin;

    DAO daoname,daonum;
//    boolean isCheck=false;// 當前是否簽到


    String current_week="";//獲取今天星期幾   課程表的行
    String current_time="";//獲取現在是第幾節課  課程表的列
    int current_time_index;//獲取第幾節  座標



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.activity_own_info,null);
        init_view(v);
        view_litenner();



        return v;
    }


    //控件初始化
    void init_view(View v)
    {


        daoname=new DAO(getActivity());
        daonum=new DAO(getActivity());

        stu_name=v.findViewById(R.id.txt_name);
        stu_name.setText(daoname.find("name",Phone_Manager_Util.getICC((AppCompatActivity) getActivity())).get(0));
        stu_number=v.findViewById(R.id.txt_stunumber);
        stu_number.setText(daonum.find("number",Phone_Manager_Util.getICC((AppCompatActivity) getActivity())).get(0));

        see_classes=v.findViewById(R.id.textView_see_classes);
        see_classes.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线

        signin_bg=v.findViewById(R.id.signin_bg);
        current_course=v.findViewById(R.id.text_coursename);

        signin_button=v.findViewById(R.id.signin_button);

        tvdi=v.findViewById(R.id.tv_di);
        tvjie=v.findViewById(R.id.tv_jie);
        ed_1_2_16=v.findViewById(R.id.ed_1to16);



        handler=new Handler(){

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch( msg.what ) {
                    case Return_V.SETBACKGROUND_GREEN:
                        signin_bg.setBackgroundColor(Color.GREEN);
                        break;
                    case Return_V.SETBACKGROUND_RED:
                        signin_bg.setBackgroundColor(Color.RED);

                        break;
                    case Return_V.SETCOURNAME:
//                        show_course=Fragment_Start.course[0][0].getText().toString();
                        current_course.setText(show_course);
//                        current_course.setText("hang"+current_week+" "+"button index："+current_time_index);
//                        current_course.setText(Get_Time.get_time("year"));

//                        current_course.setText(Return_V.CURRENT_LONGITUDE+"");
                        break;
                    case Return_V.DATA_SUCCEED:
                        signin_button.setEnabled(true);
                        Toast.makeText(getContext(),"簽到成功！",Toast.LENGTH_SHORT).show();
                        break;
                    case Return_V.CHEAK_ERR:
                        Toast.makeText(getContext(),"簽到失敗！請進入請假頁面導入課表先！",Toast.LENGTH_LONG).show();
                        break;
                    case Return_V.WEEKEND:
                        Toast.makeText(getContext(),"今天週末哦~~",Toast.LENGTH_LONG).show();

                        break;
                    case Return_V.SHOWCOURSETABLE://測試  (再項目中測試成功)
                        Fragment_Start.course[0][0].setText("[U201]软件工程\n(薛建民_0978)\nHG教学班");
                        Fragment_Start.course[5][1].setText("[U506]软件工程\n" + "(薛建民_0978)\n" + "HG01教学班");
                        Fragment_Start.course[5][2].setText("[U202]编程规范\n" + "(张莉娜_1593)\n" + "IM教学班");
                        Fragment_Start.course[5][3].setText("[S2503]编程规范\n" + "(张莉娜_1593)\n" + "IM01教学班");
                        break;
                }

            }


        };


    }
    //各个控件的监听
    void view_litenner()
    {



        //查看课表 点击 查看开始进入界面的课表
        see_classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                see_classes.setTextColor(Color.RED);
//                    handler.sendEmptyMessage(Return_V.SHOWCOURSETABLE);


                showAPP_start();

            }
        });

        //簽到按鈕
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if(Return_V.CURRENTCLASSES_NUMBER==null ||
                            Return_V.CURRENTSHOWTABLE==null ||
                            Return_V.CURRENTSCHOOLTIME_INDEX==null)
                    {
                        handler.sendEmptyMessage(Return_V.CHEAK_ERR);
                    }else{
                        thread_signin=new Thread(new Thread_Signin());
                        signin_button.setEnabled(false);
                        thread_signin.start();
                    }




            }
        });

    }

    //签到
    class Thread_Signin implements Runnable
    {

        @Override
        public void run() {

            db=new JDBC_MySQL_Connect();

           //顯示當前課程
            show_current_classes();

            //上傳位置信息 ok
            if(current_week.equals("null"))
            {
                handler.sendEmptyMessage(Return_V.WEEKEND);//提示週末
            }else{


//                show_course=Integer.parseInt(current_week)-1+" "+current_time_index;
                db.DB_add(String.format("update stu_timetable set longitude='%s',latitude='%s' where stu_number='%s' and classes_number='%s'",
                        Return_V.CURRENT_LONGITUDE+"",Return_V.CURRENT_LATITUDE+"",Return_V.CURRENTSTUNUMBER,"AJL"));
                handler.sendEmptyMessage(Return_V.SETBACKGROUND_GREEN);//簽到狀態
                handler.sendEmptyMessage(Return_V.SETCOURNAME);//顯示當前課程
//            handler.sendEmptyMessage(Return_V.SHOWCOURSETABLE);//顯示課程表
                show_course=
                        Fragment_Start.course[current_time_index]
                                [Integer.parseInt(current_week)-1].getText().toString();


                //成功
                handler.sendEmptyMessage(Return_V.DATA_SUCCEED);
//            isCheck=true;
                db.close_all();


            }




        }
    }

    //顯示當前課程
    void show_current_classes()
    {
        Return_V.map_init();//初始化
        current_week=Return_V.getweek_Num.get(Get_Time.get_time("week"))+"";//今天星期幾
        //第幾節課
        String tmp=ed_1_2_16.getText().toString();
        for(int i=0;i<Fragment_Start.time.length;i++)
        {
            if(Fragment_Start.time[i].getText().toString().substring(1,2).equals(tmp))
            {
                current_time=tmp;
                current_time_index=i;
            }else if(Fragment_Start.time[i].getText().toString().substring(5,6).equals(tmp))
            {
                current_time=tmp;
                current_time_index=i;
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

    //顯示 進入軟件時候的界面圖
    void showAPP_start()
    {
        getFragmen_Transaction_Instance();
        Fragment_Start start=new Fragment_Start();
        Fragment_Replace(R.id.frame, start);

    }
    @Override
    public void onPause() {
        super.onPause();
    }

}
