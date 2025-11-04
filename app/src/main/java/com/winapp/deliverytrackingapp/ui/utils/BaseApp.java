package com.winapp.deliverytrackingapp.ui.utils;

import android.app.Application;


public class BaseApp extends Application {
    private SharedPreferenceUtil sharedPreferenceUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        sharedPreferenceUtil =new SharedPreferenceUtil(this);
    }

    /**
     * Connect print service through interface library
     */
    private void init(){

      //  SunmiPrintHelper.getInstance().initSunmiPrinterService(this);
    }
}
