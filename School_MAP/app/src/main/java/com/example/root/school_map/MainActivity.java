package com.example.root.school_map;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;

import java.net.NoRouteToHostException;
import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.Vector;
import java.util.jar.Manifest;

/**
 * Created by root on 2018/10/23.
 */

public class MainActivity extends AppCompatActivity implements AMapLocationListener {




    Button my_button,leave_button;//按鈕 我的 請假

    //闹铃
    TimePicker timepicer;
    Calendar calendar;
    Button timebutton;

    //SQL
    JDBC_MySQL_Connect db=null;
    String re_stu_number;//通過SIM返回的學號
    boolean re_stu_number_bool;//判斷是否返回學號
    String re_SIM;//通過學號返回的SIM
    boolean re_stu_number_SIM;//判斷是否返回SIM


    Thread app_start;//我的  判斷是否注冊 顯示相應界面
    Thread ask_start;// 請假  判斷是否注冊顯示數據

    Handler handler;

    long exitTime = 0;//再按一次退出應用 記錄點擊時間


    //需要獲取的權限
//    String[] permissions={android.Manifest.permission.READ_PHONE_STATE};
    String SIM ="";
    //fragment
    android.app.FragmentManager fragmentManager;
    android.app.FragmentTransaction transaction;

    //定位
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;

//本地數據庫
    static DAO dao;

    //返回本地數據庫的isregist字段，判斷是否註冊
    Vector<String> isregistv;


    // 创建一个权限列表，把需要使用而没用授权的的权限存放在这里
    List<String> permissionList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //创建广播
//        InnerRecevier_HOME innerReceiver = new InnerRecevier_HOME();
//        //动态注册广播
//        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        //启动广播
//        registerReceiver(innerReceiver, intentFilter);
//
//        //控件初始化 變量初始化
//        init_view();
//        init_class_variable();
//        listen_view();
//        showAPP_start();

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
        }



        // 如果列表为空，就是全部权限都获取了，不用再次获取了。不为空就去申请权限
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]), 1002);
        } else {
            //创建广播
            InnerRecevier_HOME innerReceiver = new InnerRecevier_HOME();
            //动态注册广播
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            //启动广播
            registerReceiver(innerReceiver, intentFilter);

            //控件初始化 變量初始化
            init_view();
            init_class_variable();
            listen_view();
            showAPP_start();
        }



    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    //控件的初始化工作
    void init_view()
    {
        my_button= (Button) findViewById(R.id.mybutton);
        leave_button= (Button) findViewById(R.id.leavebutton);

        //请假  其他的ACTIVITY
//        LayoutInflater factorys = LayoutInflater.from(MainActivity.this);//获取MainActivity中LayoutInflater （上下文参数）
//        View view= factorys.inflate(R.layout.activity_ask_for_leave, null);//获取View 对象
//        sp_classes= view.findViewById(R.id.select_classes_spinner);
//        sp_teacher= view.findViewById(R.id.teacher_spinner);
//        tip=view.findViewById(R.id.reason_tip);

    }
    //類 變量的初始化
    void init_class_variable()
    {

        dao=new DAO(getApplicationContext());
        isregistv=dao.find("isregist",Phone_Manager_Util.getICC(MainActivity.this));
        if(isregistv.size()==0)//如果有記錄就簿添加了
        {
            dao.add("insert into student(number,name,SIM,isregist) values (?,?,?,?)",
                    new Object[]{Return_V.CURRENTSTUNUMBER, Return_V.CURRENTNAME, Phone_Manager_Util.getICC(MainActivity.this),"N"}
            );
        }else{


        }




        Return_V.CURRENTCLASSES_NUMBER= new ArrayList<>();

//        if(isNetworkAvailable(getApplicationContext()))
//        {
//            Toast.makeText(getApplicationContext(),"无网络！",Toast.LENGTH_LONG).show();
//            return;
//        }

        //軟件開始運行的綫程 用於判斷是否注冊 以顯示注冊還是個人信息



        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            switch (msg.what)
            {
                case Return_V.DEBUG:
//                    Toast.makeText(MainActivity.this,re_stu_number,Toast.LENGTH_LONG).show();

//                    for(String s:Return_V.CURRENTCLASSES_NUMBER)
//                    {
//                        Log.e("wtfing",s+"\n");
//                    }

                    leave_button.setEnabled(true);
                    break;

                case Return_V.SETTITLE:
                    MainActivity.this.setTitle("我的");
                    break;
                case Return_V.NOLOGIN:
                    Fragment_Ask_For_Leave.tip.setText("请先注册登录！");
                    my_button.setEnabled(true);
                    break;
                case Return_V.LOGIN:
                    leave_button.setEnabled(true);
                    break;
                case Return_V.SP_CLASSED:
                    Fragment_Ask_For_Leave.adapter=new ArrayAdapter(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,Return_V.CURRENTCLASSES_NUMBER);
                    Fragment_Ask_For_Leave.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Fragment_Ask_For_Leave.sp_select_class.setAdapter(Fragment_Ask_For_Leave.adapter);
                    Fragment_Ask_For_Leave.tip.setText("加载OK");
                    Fragment_Ask_For_Leave.submit.setEnabled(true);
                    my_button.setEnabled(true);
                    break;


            }

            }

        };




    }

    //控件的監聽系列
    void listen_view()
    {

        //我的按鈕的監聽
        my_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.this.setTitle("我的");


//設置系統亮度 會彈出一個可寫入系統  這裡主要解決一個莫名其妙的錯誤 不然數據庫寫不進去
//                Java.lang.SecurityException:
//                so.wih.android.jjewatch was not granted this
//                permission: android.permission.WRITE_SETTINGS

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(getApplicationContext())) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                    } else {
                        //有了权限，具体的动作
                        app_start=new Thread(new APP_atart_Thread());
                        app_start.start();
                        showLoading();
                    }
                }



            }
        });

        //請假按鈕監聽
        leave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.setTitle("請假");
//                my_button.setEnabled(false);


                ask_start=new Thread(new Ask_for_Thread());
                ask_start.start();
                showAsk_for_leave();

            }
        });



    }


    /**
     * 以下方法
     * 通過SIM查找學號
     * 判斷是否注冊
     *
     */
    boolean stunumber_is_register(String SIM)
    {
        re_stu_number=db.DB_getSome_by_other("stu_number",SIM,"select * from students where SIM=?");
        return re_stu_number.equals("")?false:true;

    }
    /**
     * 以下方法
     * 通過學號查找SIM
     * 判斷是否注冊
     */

    boolean SIM_is_register(String stu_number)
    {
        re_SIM=db.DB_getSome_by_other("SIM",stu_number,"select * from students where stu_number=?");
        return re_SIM.equals("")?false:true;

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

    //顯示祖冊界面
    void showRegister_view()
    {
        getFragmen_Transaction_Instance();
        Fragment_Register register=new Fragment_Register();
        Fragment_Replace(R.id.frame, register);

    }
    //顯示個人界面
    void showOwn_info_view()
    {
        getFragmen_Transaction_Instance();
        Fragment_Own_Info info=new Fragment_Own_Info();
        Fragment_Replace(R.id.frame, info);

    }

    //顯示請假頁面
    void showAsk_for_leave()
    {
        getFragmen_Transaction_Instance();
        Fragment_Ask_For_Leave fragment_ask_for_leave=new Fragment_Ask_For_Leave();
        Fragment_Replace(R.id.frame, fragment_ask_for_leave);


    }
    //顯示緩衝頁面
    void showLoading()
    {
        getFragmen_Transaction_Instance();
        Fragment_buffer buffer=new Fragment_buffer();
        Fragment_Replace(R.id.frame, buffer);


    }

    //判斷網絡是否打開
//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (cm == null) {
//        } else {
//
//            NetworkInfo[] info = cm.getAllNetworkInfo();
//            if (info != null) {
//                for (int i = 0; i < info.length; i++) {
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

    //------------------------------------------------------------↓

    //初始化工作
    void Location()
    {
        // TODO Auto-generated method stub
        mlocationClient = new AMapLocationClient(getApplicationContext());
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(0);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);
        mlocationClient.disableBackgroundLocation(false);
        //设置是否只定位一次
        mLocationOption.setOnceLocation(false);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表


                Return_V.CURRENT_LATITUDE= aMapLocation.getLatitude();//获取纬度
                Return_V.CURRENT_LONGITUDE=aMapLocation.getLongitude();//获取经度
//                haiba=aMapLocation.getAltitude();//海拔
//                address=aMapLocation.getAddress();//地址
//                street=aMapLocation.getStreet();//街道



            } else {

            }
        }
    }


    //------------------------------------------------------------↑

//請假 綫程
        class Ask_for_Thread implements  Runnable
    {

        @Override
        public void run() {

            db= new JDBC_MySQL_Connect();


            re_stu_number_bool=stunumber_is_register(Phone_Manager_Util.getICC(MainActivity.this));


            //如果該手機註冊過就顯示數據
            if(re_stu_number_bool)
            {
                if(!Return_V.classes_isImport)
                {

                    Return_V.ISREGISTER=true;
                    Return_V.CURRENTSTUNUMBER=db.DB_getSome_by_other("stu_number",Phone_Manager_Util.getICC(MainActivity.this),
                            "select * from students where SIM=?");
                    Return_V.CURRENTNAME=db.DB_getSome_by_other("name",Return_V.CURRENTSTUNUMBER
                            ,"select * from students where stu_number=?");
                    Return_V.CURRENTCLASSES_NUMBER=db.DB_part_of_table("classes_number",Return_V.CURRENTSTUNUMBER,
                            "select * from stu_timetable where stu_number=?");
                }



                handler.sendEmptyMessage(Return_V.SP_CLASSED);


            }else{
                Return_V.ISREGISTER=false;
                Return_V.CURRENTSTUNUMBER="";
                Return_V.CURRENTNAME="";
                handler.sendEmptyMessage(Return_V.NOLOGIN);

            }

            db.close_all();
        }
    }

//我的  綫程
    class APP_atart_Thread implements Runnable
    {

        @Override
        public void run() {
            Location();

//如果該手機註冊過就進入個人信息 否則就註冊
                if(isregistv.get(0).equals("N"))
                {
                    //沒註冊
                    if(Return_V.ISREGISTER==true)
                    {
                        showOwn_info_view();
                    }else{
                        Return_V.ISREGISTER=false;
                        Return_V.CURRENTSTUNUMBER="";
                        Return_V.CURRENTNAME="";
                        showRegister_view();
                        handler.sendEmptyMessage(Return_V.LOGIN);
                    }

                }else{
                    //註冊了

                    //這條重置本地數據庫  測試用
//                    dao.update("student","isregist","N","SIM=?",new String[]{Phone_Manager_Util.getICC(MainActivity.this)});
//                    Return_V.ISREGISTER=false;
//                    dao.deleteall("student",null,null);
//                    Log.e("gggggggge",isregistv.size()+"");


                    showOwn_info_view();



                }







        }
    }


    //按下返回鍵回到初始點  再按一次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {



        if (keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN) {

            showAPP_start();
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {

                finish();
                System.exit(0);
            }



            return true;
        }



        return super.onKeyDown(keyCode, event);
    }

    //這個類監聽HOME鍵
    public class InnerRecevier_HOME extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";

        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {//按下home鍵
//                        Toast.makeText(MainActivity.this, "Home键被监听", Toast.LENGTH_SHORT).show();
                        Intent intent_home=new Intent(MainActivity.this,StatusService.class);
                        startService(intent_home);
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
//                        Toast.makeText(MainActivity.this, "多任务键被监听", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }


}

