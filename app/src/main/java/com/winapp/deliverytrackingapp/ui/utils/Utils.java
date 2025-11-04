package com.winapp.deliverytrackingapp.ui.utils;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Utils {

    static SweetAlertDialog pDialog;
    public static String outstandingAmount = "0.00";
    public static String invoiceOutstandingAmount = "0.00";
//    public static DBHelper dbHelper;
    public static String signature = "";
    public static String selectImage = "";
    public static String invoiceMode = "";
    public static String receiptMode = "";
    public static String currentDateTime = "";
    public static String logo = "";
    public static String invoiceDate = "";
    public static String qrcode = "";
    public static String unpaid = "";
    public static String paid = "";

    public static String orderDate="";
    public static String orderNo="";

    public static String getOrderDate() {
        return orderDate;
    }

    public static void setOrderDate(String orderDate) {
        Utils.orderDate = orderDate;
    }

    public static String getOrderNo() {
        return orderNo;
    }

    public static void setOrderNo(String orderNo) {
        Utils.orderNo = orderNo;
    }

    public static String getLogo() {
        return logo;
    }

    public static void setLogo(String logo) {
        Utils.logo = logo;
    }

    public static String getQrcode() {
        return qrcode;
    }

    public static void setQrcode(String qrcode) {
        Utils.qrcode = qrcode;
    }

    public static String getUnpaid() {
        return unpaid;
    }

    public static void setUnpaid(String unpaid) {
        Utils.unpaid = unpaid;
    }

    public static String getPaid() {
        return paid;
    }

    public static void setPaid(String paid) {
        Utils.paid = paid;
    }

    public static String getCurrentDateTime() {
        return currentDateTime;
    }

    public static void setCurrentDateTime(String currentDateTime) {
        Utils.currentDateTime = currentDateTime;
    }
    public static String getInvoiceDate() {
        return invoiceDate;
    }

    public static void setInvoiceDate(String invoiceDate) {
        Utils.invoiceDate = invoiceDate;
    }
    public static String getReceiptMode() {
        return receiptMode;
    }

    public static void setReceiptMode(String receiptMode) {
        Utils.receiptMode = receiptMode;
    }

    public static String getInvoiceMode() {
        return invoiceMode;
    }

    public static void setInvoiceMode(String invoiceMode) {
        Utils.invoiceMode = invoiceMode;
    }

    public static String getInvoiceOutstandingAmount() {
        return invoiceOutstandingAmount;
    }

    public static void setInvoiceOutstandingAmount(String invoiceOutstandingAmount) {
        Utils.invoiceOutstandingAmount = invoiceOutstandingAmount;
    }

    public static String getOutstandingAmount() {
        return outstandingAmount;
    }

    public static void setOutstandingAmount(String outstandingAmount) {
        Utils.outstandingAmount = outstandingAmount;
    }

    public static String getSignature() {
        return signature;
    }

    public static void setSignature(String signature) {
        Log.d("cg_setSign",""+signature);
        Utils.signature = signature;
        if (signature.isEmpty()) {
            Utils.setReceiptMode("");
        }
    }

    public static String getSelectImage() {
        return selectImage;
    }

    public static void setSelectImage(String selectImage) {
        Log.d("cg_setImg",""+selectImage);
        Utils.selectImage = selectImage;
//        if (selectImage.isEmpty()) {
//            utils.setReceiptMode("");
//        }
    }
    public static String oneDecimalPoint(double val) {
        DecimalFormat df = new DecimalFormat("#");
        String tot = df.format(val);
        return tot;
    }

    public static String getNextInvoiceDate(String date) throws ParseException {
        String dt = date;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));
        c.add(Calendar.DATE, 1);  // number of days to add
        dt = sdf.format(c.getTime());
        System.out.println("GivenNextDate: "+dt);
        return dt;
    }
    public static String getDayOfTheDate(String givenDate) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(sdf.parse(givenDate));
        System.out.println("Day_Name: "+str);
        return str;
    }
    // email validation
    public static boolean isValidMail(String email) {
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(EMAIL_STRING).matcher(email).matches();
    }

    // empty string checking
    public static boolean isEmpty(String value) {
        return (value == null || value.trim().equals("null") || value.trim().length() <= 0);
    }

    public static String getQtyValue(String val) {
        double data = Double.parseDouble(val);
        int value = (int) data;
        return String.valueOf(value);
    }

    public static void showLoader(Context context, String title) {
        try {
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(title);
            pDialog.setCancelable(false);
            pDialog.show();
        } catch (Exception ex) {

        }

    }
    public static void hideLoader() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
    public static void hideKeyBoard(Context context, View view){
        // Then just use the following:
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    public static ArrayList<String> getSorting() {
        ArrayList<String> letters = new ArrayList<>();
        letters.add("All");
        letters.add("A");
        letters.add("B");
        letters.add("C");
        letters.add("D");
        letters.add("E");
        letters.add("F");
        letters.add("G");
        letters.add("H");
        letters.add("I");
        letters.add("J");
        letters.add("K");
        letters.add("L");
        letters.add("M");
        letters.add("N");
        letters.add("O");
        letters.add("P");
        letters.add("Q");
        letters.add("R");
        letters.add("S");
        letters.add("T");
        letters.add("U");
        letters.add("V");
        letters.add("W");
        letters.add("X");
        letters.add("Y");
        letters.add("Z");
        return letters;
    }

    public static Boolean isTablet(Context context) {
        if ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            return true;
        }
        return false;
    }

    public static String twoDecimalPoint(double d) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMinimumFractionDigits(2);
        String tot = df.format(d);

        return tot;
    }

    public static boolean validatePrinterConfiguration(Activity activity, String printerType, String printerMacId){
        boolean printetCheck=false;
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(activity,"This device does not support bluetooth",Toast.LENGTH_SHORT).show();
        } else if (!mBluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled :)
            Toast.makeText(activity,"Enable bluetooth and connect the printer",Toast.LENGTH_SHORT).show();
        } else {
            // Bluetooth is enabled
            if (!printerType.isEmpty() && !printerMacId.isEmpty()){
                printetCheck=true;
            }else {
                Toast.makeText(activity,"Please configure Printer",Toast.LENGTH_SHORT).show();
            }
        }
        return printetCheck;
    }

    public static String fourDecimalPoint(double d) {
        DecimalFormat df = new DecimalFormat("#.####");
        df.setMinimumFractionDigits(4);
        String tot = df.format(d);

        return tot;
    }

//    public static void setSessionForLocation(Context context, String locationCode) {
//        try {
//            SharedPreferences sharedPreferences = context.getSharedPreferences(SessionManager.PREF_NAME, MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(SessionManager.KEY_LOCATION_CODE, locationCode);
//            editor.commit();
//            editor.apply();
//            Toast.makeText(context, "Location Changed Successfully...!", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//        }
//    }

//    public static String getBaseUrl(Context context) {
//        dbHelper = new DBHelper(context);
//        return dbHelper.getCustomerUrl();
//    }

    // slide the view from below itself to the current position
    public static void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),          // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        // animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public static void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        //animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public static void refreshActionBarMenu(Activity activity) {
        activity.invalidateOptionsMenu();
    }

    public static String jsonToEscapeString(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }
        char c = 0;
        int i;
        int len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
        String t;

        sb.append('"');
        for (i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '/':
                    //                if (b == '<') {
                    sb.append('\\');
                    //                }
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < ' ') {
                        t = "000" + Integer.toHexString(c);
                        sb.append("\\u" + t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
        return sb.toString();
    }

    public static String getCompleteAddress(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My loction address", strReturnedAddress.toString());
            } else {
                Log.w("My address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My loction address", "Canont get Address!");
        }
        return strAdd;
    }

    public static void setCustomerSession(Context context, String customerId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("customerPref", MODE_PRIVATE);
        SharedPreferences.Editor customerPredEdit = sharedPreferences.edit();
        customerPredEdit.putString("customerId", customerId);
        customerPredEdit.apply();
        Log.w("SetsessionValue:", customerId);
    }

    public static void clearCustomerSession(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("customerPref", MODE_PRIVATE);
        SharedPreferences.Editor customerPredEdit = sharedPreferences.edit();
        customerPredEdit.putString("customerId", "");
        customerPredEdit.apply();
    }
    public static void w(String TAG, String message) {
        int maxLogSize = 2000;
        for (int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            Log.w(TAG, message.substring(start, end));
        }
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }


    public static String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    public static String getCurrentDateWithSlace() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    public static String changeDateFormat(String date) {
        @SuppressLint("SimpleDateFormat")
        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        @SuppressLint("SimpleDateFormat")
        DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
        String resultDate = "";
        try {
            resultDate = outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertDate(String strDate, String oldFormat, String requireFormat) {
        @SuppressLint("SimpleDateFormat")
        DateFormat inputFormat = new SimpleDateFormat(oldFormat);
        @SuppressLint("SimpleDateFormat")
        DateFormat outputFormat = new SimpleDateFormat(requireFormat);
        String resultDate = "";
        try {
            resultDate = outputFormat.format(inputFormat.parse(strDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    public static String getDate(Context context, TextView dateTextView) {
        // Get Current Date
        final String[] dateString = {""};
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateTextView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        dateString[0] = dateTextView.getText().toString();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        return dateString[0];
    }

    public static int getPrintSize(int paperSize, String isLogo, String isQrcode,
                                   String outstandingAmount, String isSignature, String isPaid, String invoiceRemarks ) {
        double height=0;
        try {
            int totalsize=0 ;

            int logoSize = 0 ;
            int qrcodeSize = 0 ;
            int outstandingSize = 0 ;
            int signatureSize = 0 ;
            int paidSize = 0 ;
            int remarks=0;

            if (invoiceRemarks!=null && !invoiceRemarks.isEmpty() && !invoiceRemarks.equals("null")){
                remarks=20;
            }

            if(isLogo.equals("true")){
                logoSize = 30 ;
            }
            if(isQrcode.equals("true")){
                qrcodeSize = 70 ;
            }
            if (outstandingAmount !=null && !outstandingAmount.isEmpty() && !outstandingAmount.equals("null")){
                outstandingSize = 10 ;
            }
            if(isSignature.equals("true")){
                signatureSize = 20 ;
            }
            if(isPaid.equals("true")){
                paidSize = 40 ;
            }

            totalsize = paperSize+logoSize+qrcodeSize+outstandingSize+signatureSize+paidSize+remarks
             - 40;

            height = totalsize / 1.10 ;

            Log.d("cg_print_size::",""+height);
        }catch (Exception er){}
        return (int) height;
    }

    public static int getPrintSize(int size) {
        double height;
        height = size / 1.12;
        return (int) height;
    }
}
