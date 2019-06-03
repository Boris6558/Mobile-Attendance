package com.example.root.school_map;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

/**
 * Created by root on 2018/7/25.
 */

public class StatusService extends IntentService {

    final String TAG="StatusService";
    static final int KUKA=0;

    Intent intent;
    PendingIntent pendingIntent;
    Notification.Builder builder;
    Notification notification;
    NotificationManager manager;

    public StatusService() {
        super("StatusService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e(TAG,"當前課程：");
        showNotification(false);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG,Fragment_Own_Info.show_course.toString());
        showNotification(true);
    }

    void showNotification(boolean finish)
    {



        if(!finish)
        {
//            intent=new Intent(StatusService.this,MainActivity.class);
//            pendingIntent=PendingIntent.getActivities(StatusService.this,0, new Intent[]{intent},PendingIntent.FLAG_UPDATE_CURRENT);
//            builder=new Notification.Builder(StatusService.this);
//            //設置狀態信息
//            builder.setSmallIcon(R.drawable.kaji);
//            builder.setContentTitle("通知"); //设置标题
//            builder.setContentText("正在下載"); //消息内容
//            builder.setContentIntent(pendingIntent);
//            notification=builder.build();
//            manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            manager.notify(KUKA,notification);
            Notification_T("通知","正在返回",false);

        }else{
//            intent=new Intent(StatusService.this,MainActivity.class);
//            pendingIntent=PendingIntent.getActivities(StatusService.this,0, new Intent[]{intent},PendingIntent.FLAG_UPDATE_CURRENT);
//            builder=new Notification.Builder(StatusService.this);
//            //設置狀態信息
//            builder.setSmallIcon(R.drawable.kaji);
//            builder.setContentTitle("通知"); //设置标题
//            builder.setContentText("下載OK"); //消息内容
//            builder.setDefaults(Notification.DEFAULT_ALL);
//            builder.setContentIntent(pendingIntent);
//            notification=builder.build();
//            manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            manager.notify(KUKA,notification);
            Notification_T("當前課程",Fragment_Own_Info.show_course.toString(),true);


        }






    }

    void Notification_T(String titile,String text,boolean isTip)
    {
        intent=new Intent(StatusService.this,MainActivity.class);
        pendingIntent=PendingIntent.getActivities(StatusService.this,0, new Intent[]{intent},PendingIntent.FLAG_UPDATE_CURRENT);
        builder=new Notification.Builder(StatusService.this);
        //設置狀態信息
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle(titile); //设置标题
        builder.setContentText(text); //消息内容
        builder.setDefaults(isTip?Notification.DEFAULT_ALL:Notification.DEFAULT_SOUND);
        builder.setContentIntent(pendingIntent);
        notification=builder.build();
        manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(KUKA,notification);
    }
}
