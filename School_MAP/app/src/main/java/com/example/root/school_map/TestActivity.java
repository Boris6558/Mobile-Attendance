package com.example.root.school_map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;

/**
 * 移动终端考勤1.0
 *
 * 团队名称：
 * 组员：郭沣瑶，李铭琪,刘斌
 *
 * 项目文档：郭沣瑶，李铭琪,刘斌
 *
 * APP端：刘斌
 *
 * WEB端：郭沣瑶
 *
 * 服务器配置：刘斌
 *
 */
public class TestActivity extends AppCompatActivity implements AMapLocationListener {


    TextView textView,textView2,textview3,simtv;
    Button button,simbutton;

    //定位
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;


    //SQL
    JDBC_MySQL_Connect jdbc;
    String add_info="";

    String str,error="";
    double weidu = 0.0,jingdu=0.0,haiba=0.0;
    String address="",street="",time="";
    SimpleDateFormat df;//设置日期格式


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final String sss=sHA1(getBaseContext());
        //控件
        textView= (TextView) findViewById(R.id.tv);
        textView2= (TextView) findViewById(R.id.tv2);
        textview3= (TextView) findViewById(R.id.textView2);
        simtv= (TextView) findViewById(R.id.simtextView);

        button= (Button) findViewById(R.id.button);
        simbutton= (Button) findViewById(R.id.simbutton2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                init_TextView3();


                //定位
                Thread thread=new Thread(new MyThread());
                thread.start();
//                textView.setText(sss.toString());
//                Log.e("TAG",sss.toString());





            }
        });

        simbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager te=(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                simtv.setText(te.getSimSerialNumber().toString());
            }
        });




    }



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

    //定位回调监听，当定位完成后调用此方法

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {



        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                weidu= aMapLocation.getLatitude();//获取纬度
                jingdu=aMapLocation.getLongitude();//获取经度
//                haiba=aMapLocation.getAltitude();//海拔
//                address=aMapLocation.getAddress();//地址
//                street=aMapLocation.getStreet();//街道

                df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//实例时间
                time=df.format(new Date()).toString();


                str="当前纬度："+weidu+"\n"+"当前经度："+jingdu+"\n"+
                "时间："+time+"\n";
                textView.setText(str.toString());
                textView2.setText("<定位成功！>");
//                Toast.makeText(getApplicationContext(),"<定位成功！>",Toast.LENGTH_LONG).show();




            } else {
                str="当前纬度："+weidu+"\n"+"当前经度："+jingdu+"\n"+
                        "海拔："+haiba+"米\n"+"地址:"+address+"\n"+"街道:"+street+"\n";


                error="AmapError location Error, ErrCode:"
                + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo()+"\n"+
                "<请打开GPS定位，并且在设置中设置该应用为信任的应用！>";
                textView.setText(str.toString());
                textView2.setText(error.toString());



            }
        }

    }

//获取实际的SHA1 避免更新时签名不同 65
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //
    void init_TextView3()
    {
        textview3.setText("数据正在提交.....\n是否提交成功，得看你网速了！");
        textview3.setTextColor(Color.RED);

    }


    //修改是否已更新数据库
    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if( msg.what==110 )
            {
                textview3.setText(add_info);
                textview3.setTextColor(Color.GREEN);
            }
        }


    };

    //点击签到后 开启定位线程
    class MyThread implements Runnable{

        @Override
        public void run() {
            Location();

            jdbc=new JDBC_MySQL_Connect();

            try {
//                jdbc.test_data_select("select * from jingweidu");
//                Log.e("TAG","查询数据成功");
//                add_info="数据查詢成功";
                jdbc.test_data_add(String.format("insert into jingweidu (jingdu,weidu) values ('%s','%s')",jingdu+"",weidu+""));
                add_info="数据添加成功";
                Log.e("TAG",add_info);


            } catch (SQLException e) {
                e.printStackTrace();
                add_info="提交过程报错！请检查联网状态或GPS是否打开。并在手机设置中把该应用设置为信任此信用！";
            }finally {


                    jdbc.close_all();

            }

            handler.sendEmptyMessage(110);

        }
    }







}
