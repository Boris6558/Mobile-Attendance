package com.example.root.school_map;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

/**
 * Created by root on 2018/10/26.
 */

public class Phone_Manager_Util{

    //創建電話管理 鏈接手機
    static TelephonyManager tm;

    static void init_tm(Activity activity)
    {
        tm=(TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 獲得手機的 ICCID卡信息 固化在手機SIM卡
     * 共有20位數字組成，其編碼格式為：XXXXXX 0MFSS YYGXX XXXXX
     * 前六位運營商代碼：中國移動的为：898600；中國聯通的为：898601
     *
     */
    static String getICC(AppCompatActivity activity)
    {
        init_tm(activity);
        return tm.getSimSerialNumber();

    }

    static String getPhoneNumber(AppCompatActivity activity)
    {
        init_tm(activity);
        return tm.getLine1Number();

    }

}
