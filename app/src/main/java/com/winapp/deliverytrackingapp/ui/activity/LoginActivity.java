package com.winapp.deliverytrackingapp.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.winapp.deliverytrackingapp.ui.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

import com.winapp.deliverytrackingapp.ui.utils.ConnectivityReceiver;
import com.winapp.deliverytrackingapp.ui.utils.SessionManager;
import com.winapp.deliverytrackingapp.ui.utils.SharedPreferenceUtil;
import com.winapp.deliverytrackingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    /**
     *
     * Define the Variables for the Login Activity and Store it to the Session
     * @param savedInstanceState
     */
    public EditText userNameText;
    public EditText passwordText;
    public Button btnLogin;
    public CheckBox rememberMe;
    private SharedPreferences loginPreferences;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private boolean isEmailValid;
    private boolean isPasswordValid;
    private SessionManager session;
    private SweetAlertDialog pDialog;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){
        userNameText=findViewById(R.id.username);
        passwordText=findViewById(R.id.password);
        btnLogin=findViewById(R.id.btn_login);
        rememberMe=findViewById(R.id.remember_me);
        session=new SessionManager(this);

        // Store the Remember me to Session..
        // Set the Preference value in edittext for Remembering the values
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        sharedPreferenceUtil.setBooleanPreference(Constants.KEY_ISLOGIN,false);
//        if (saveLogin) {
//            userNameText.setText(loginPreferences.getString("username", ""));
//            passwordText.setText(loginPreferences.getString("password", ""));
//            rememberMe.setChecked(true);
//        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validateSession();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void validateSession() throws JSONException {
        if (userNameText.getText().toString().isEmpty()) {
//            userNameText.setError("User Id is Empty");
            Toast.makeText(this, "User Name is Empty", Toast.LENGTH_SHORT).show();

            isEmailValid = false;
        } /*else if (!Patterns.EMAIL_ADDRESS.matcher(emailIdText.getText().toString()).matches()) {
            emailIdText.setError(getResources().getString(R.string.invalid_email));
            isEmailValid = false;
        }*/ else {
            isEmailValid = true;
        }
        // Check for a valid password.
        if (passwordText.getText().toString().isEmpty()) {
//            passwordText.setError(getResources().getString(R.string.error_password));
            Toast.makeText(this, R.string.error_password, Toast.LENGTH_SHORT).show();
            isPasswordValid = false;
        } else if (passwordText.getText().length() < 3) {
//            passwordText.setError(getResources().getString(R.string.invalid_password));
            Toast.makeText(this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
        }
        if (isEmailValid && isPasswordValid) {
            setSession(userNameText.getText().toString().trim(),passwordText.getText().toString().trim());
        }
    }

    private void setSession(String userId, String password) throws JSONException {
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("Username",userId);
        jsonObject.put("Password",password);
        // http://172.16.5.60:8345/api/Login
        String url= Constants.BASEURL +"Login";
        // Initialize a new JsonArrayRequest instance
        Log.w("Given_login_URL:",url + jsonObject);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Authenticating...");
        pDialog.setCancelable(false);
        pDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
            try{
                Log.w("Res_SAP_login:",response.toString());
                if (response.length()>0){

                    String statusCode=response.optString("statusCode");
                    if (statusCode.equals("1")){
                        JSONArray userArray=response.optJSONArray("responseData");
                        assert userArray != null;
                        JSONObject object=userArray.optJSONObject(0);
                        String username=object.optString("userName");
                        String rollname=object.optString("roleName");
                        // String locationCode=object.optString("LocationCode");
                        // String isuserpermission=object.optString("IsUserPermission");
                        // String ismainlocation=object.optString("IsMainLocation");

                        String companycode=object.optString("companyCode");
                        String companyname=object.optString("companyName");
                        String address1=object.optString("streetPO");
                        String address2=object.optString("streetNO");
                        String address3=object.optString("countryName")+"-"+object.optString("zipcode");
                        String postalcode=object.optString("zipcode");
                        String country=object.optString("countryName");
                        String phone=object.optString("phone1");
                        String gstNo=object.optString("gstNo");
                        String locationCode=object.optString("warehouse");
                        String ispermission=object.optString("locationAuthorization");
                        String logo=object.optString("logo");
                        String qrcode=object.optString("qrCode");
                        String paid=object.optString("paid");
                        String unpaid=object.optString("unPaid");
                        String paynow=object.optString("payNow");
                        String bank=object.optString("bank");
                        String cheque=object.optString("cheque");
                        String salesManName=object.optString("salesPersonName");
                        String salesManPhone=object.optString("salesPersonMobile");
                        String salesManMail=object.optString("salesPersonEmail");
                        String salesManOffice=object.optString("salesPersonOfficeNo");
                        String negativeStock =object.optString("allowNegativeStock");
                        String userMiddlename =object.optString("userMiddleName");
                        String adminPermission =object.optString("adminPermission");

                        String invUOM =object.optString("invoiceDefaultUOM");
                        String salesUOM =object.optString("salesOrderDefaultUOM");
                        String returnUOM =object.optString("salesRetunDefaultUOM");
                        String settleNextDate =object.optString("haveSettlementByDate");
                        String shortCode =object.optString("shortCode");
                        String lastPrice =object.optString("showlastSalesPrice");
                        String totalSales =object.optString("invoiceListShowBalance");

                        sharedPreferenceUtil.setStringPreference(sharedPreferenceUtil.KEY_SETTING_INV_UOM, invUOM);
                        sharedPreferenceUtil.setStringPreference(sharedPreferenceUtil.KEY_SETTING_SO_UOM, salesUOM);
                        sharedPreferenceUtil.setStringPreference(sharedPreferenceUtil.KEY_SETTING_RETURN_UOM, returnUOM);
                        sharedPreferenceUtil.setStringPreference(sharedPreferenceUtil.KEY_SETTLEMENT_NEXT_DATE, settleNextDate);
                        //mahudoom given "shortCode": "TRAN",
                        sharedPreferenceUtil.setStringPreference(sharedPreferenceUtil.KEY_SHORT_CODE, shortCode);
                        sharedPreferenceUtil.setStringPreference(sharedPreferenceUtil.KEY_LAST_PRICE, lastPrice);
                        sharedPreferenceUtil.setStringPreference(sharedPreferenceUtil.KEY_TOTAL_SALES, totalSales);
                        sharedPreferenceUtil.setStringPreference(sharedPreferenceUtil.KEY_USER_MIDDLE_NAME, userMiddlename);
                        sharedPreferenceUtil.setStringPreference(sharedPreferenceUtil.KEY_ADMIN_PERMISSION, adminPermission);

                        session.createLoginSession(
                                username,password,rollname,locationCode,"1",ispermission,
                                companycode,companyname,address1,address2,address3,country,postalcode,
                                phone,gstNo,logo,qrcode,paid,unpaid,paynow,bank,cheque,
                                salesManName,salesManPhone,salesManMail,salesManOffice,negativeStock);

                        sharedPreferenceUtil.setStringPreference(
                                Constants.KEY_USERNAME,
                                username
                        );
                        sharedPreferenceUtil.setStringPreference(
                                Constants.KEY_PASSWORD,
                                password
                        );
                        sharedPreferenceUtil.setBooleanPreference(Constants.KEY_ISLOGIN,true);

                        Log.w("savlog11",""+loginPreferences.getBoolean("saveLogin", false));

                        pDialog.dismiss();

                        Intent intent=new Intent(LoginActivity.this, NewDeliveryPickListActivity.class);
                        intent.putExtra("isLogin","1");
                        startActivity(intent);
                        finish();
                        //  getCompaniesList();
                        //  getUserRollPermission(companycode,rollname);
                    }else {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Invalid Username or Password",Toast.LENGTH_LONG).show();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }, error -> {
            // Do something when error occurred
            pDialog.dismiss();
            Log.w("Error_throwing:",error.toString());
            Toast.makeText(getApplicationContext(),"Server Error,Please check",Toast.LENGTH_LONG).show();
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                String creds = String.format("%s:%s", Constants.API_SECRET_CODE, Constants.API_SECRET_PASSWORD);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

//    private void setSession1(String userId, String password) throws JSONException {
//        // Initialize a new RequestQueue instance
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("Username",userId);
//        jsonObject.put("Password",password);
//        // http://172.16.5.60:8345/api/Login
//        String url= Constants.BASEURL +"Login";
//        // Initialize a new JsonArrayRequest instance
//        Log.w("Given_login_URL:",url + jsonObject);
//        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Authenticating...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
//            try{
//                Log.w("Res_SAP_login:",response.toString());
//                if (response.length()>0){
//                    //  {"statusCode":1,"statusMessage":"Success",
//                    //  "responseData":[{"userName":"User1","roleName":"DepartmentHead","userID":"1","companyCode":"WINAPP_DEMO",
//                    //  "companyName":"WINAPP_DEMO","address1":"1 XYZ Chennai  IN 600001","address2":"     "}]}
//
//                    String statusCode=response.optString("statusCode");
//                    if (statusCode.equals("1")){
//                        JSONArray userArray=response.optJSONArray("responseData");
//                        assert userArray != null;
//                        JSONObject object=userArray.optJSONObject(0);
//
//                        String username=response.optString("userName");
//                        String rollname=response.optString("roleName");
////                        String locationCode=response.optString("LocationCode");
//                        String isuserpermission=response.optString("IsUserPermission");
//                        String ismainlocation=response.optString("IsMainLocation");
//                        String companycode=response.optString("companyCode");
//                        String companyname=response.optString("companyName");
//                        String address1=response.optString("address1");
//                        String address2=response.optString("streetNO");
//                        String address3=object.optString("countryName")+"-"+object.optString("zipcode");
//                        session.createLoginSession1(username,password,rollname,
//                                "",isuserpermission,ismainlocation,
//                                companycode,companyname,address1,address2,address3);
//
//                        sharedPreferenceUtil.setStringPreference(
//                                Constants.KEY_USERNAME,
//                                username
//                        );
//                        sharedPreferenceUtil.setStringPreference(
//                                Constants.KEY_PASSWORD,
//                                password
//                        );
//                        sharedPreferenceUtil.setBooleanPreference(Constants.KEY_ISLOGIN,true);
//
//                        // Adding the Preference values to the Session to remember the values
////                                if (rememberMe.isChecked()) {
////                                    loginPrefsEditor.putBoolean("saveLogin", true);
////                                    loginPrefsEditor.putString("username", username);
////                                    loginPrefsEditor.putString("password", password);
////                                    loginPrefsEditor.commit();
////                                } else {
////                                    loginPrefsEditor.clear();
////                                    loginPrefsEditor.commit();
////                                }
//                        redirectActivity();
//                    }else {
//                        pDialog.dismiss();
//                        Toast.makeText(getApplicationContext(),"Invalid Username or Password",Toast.LENGTH_LONG).show();
//                    }
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }, error -> {
//            // Do something when error occurred
//            pDialog.dismiss();
//            Log.w("Error_throwing:",error.toString());
//            Toast.makeText(getApplicationContext(),"Server Error,Please check",Toast.LENGTH_LONG).show();
//        }){
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> params = new HashMap<>();
//                String creds = String.format("%s:%s", Constants.API_SECRET_CODE, Constants.API_SECRET_PASSWORD);
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                params.put("Authorization", auth);
//                return params;
//            }
//        };
//        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 50000;
//            }
//            @Override
//            public int getCurrentRetryCount() {
//                return 50000;
//            }
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
//        // Add JsonArrayRequest to the RequestQueue
//        requestQueue.add(jsonObjectRequest);
//    }

    public void redirectActivity(){
        Intent intent=new Intent(getApplicationContext(),NavigationActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            Snackbar snackbar = Snackbar.make(userNameText, "Click BACK again to exit", Snackbar.LENGTH_LONG);
            snackbar.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            showCloseAlert();
        }
    }

    public void showCloseAlert(){
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Warning..!");
        builder.setMessage("Are You sure want to exit the App?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finishAffinity();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
      //  com.winapp.deliverytrackingapp.MyApplication.getInstance().setConnectivityListener(this);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
      //  Utils.showSnack(isConnected,userNameText);
    }
}