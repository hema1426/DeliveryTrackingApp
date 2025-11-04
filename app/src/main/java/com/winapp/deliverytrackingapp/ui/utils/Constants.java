package com.winapp.deliverytrackingapp.ui.utils;

import android.content.Context;


public class Constants extends BaseApp {
    String MONTH_FORMAT1 = "MM";
    String MONTH_FORMAT = "MMM";
    String YEAR_FORMAT = "yyyy";

    private SharedPreferenceUtil sharedPreferenceUtil;
    String baseURLStr = "http://18.138.84.16:316/es/data/api/";
    String DATE_FORMAT1 = YEAR_FORMAT + "-" + MONTH_FORMAT1+ "-" + DAY_FORMAT;
    public static String DAY_FORMAT = "dd" ;
    public static String DEFAULT_STRING = "";
    public static String KEY_ADDRESS_ZONE_CODE = "addressZoneCode" ;
    public static String KEY_ADDRESS_ZONE_NAME = "addressZoneName";
    public static String KEY_SELECT_FROMDATE_DISPLAY = "selectFromDateDisplay";
    public static String KEY_SELECTDATE_TODISPLAY = "selectToDateDisplay" ;
    public static String KEY_SELECT_TODATE = "selectToDate" ;
    public static String KEY_SELECT_FROMDATE = "selectFromDate";

// live dashboard  //url 18.138.84.16:235
    // test dashboard url http://18.138.84.16:316/es/data/api/"

//    public static String BASEURL = "http://ezysales.sg:477/es/data/api/";

//    public static String BASEURL = "http://18.138.84.16:235/es/data/api/";

   public static String BASEURL = "http://136.243.60.223:8090/api/";
   // WMS_KH_CYCLE_DELIVERY_DEMO ="http://136.243.60.223:8090/api/";
 //  public static String WMS_SJLITE_SINGAPORE_Test ="http://103.166.144.45:92/api/";  //singapore - 18.8.25

    public static String KEY_ISLOGIN = "islogin";
    public static String KEY_USERNAME = "Username";
    public static String KEY_PASSWORD = "password";

   // public static String BASEURL = "http://54.179.67.55:156/es/data/api/";
    public static String BASEURL_POS = "http://54.242.250.229/WinappDemo/api-v1/";
    public static String API_SECRET_CODE = "winapp";
    public static String API_SECRET_PASSWORD = "admin";
    public static String API_KEY = "43F247EFD7FC6FE78B486DE4FEC6B";

    //FNB-POS
//    public static String BASE_URL = "http://54.242.250.229/WinappDemo/api-v1/POSv1/";
    
    public static String getSignatureFolderPath(Context context) {
        return "0";
    }
}
