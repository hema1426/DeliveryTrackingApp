package com.winapp.deliverytrackingapp.ui.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NetworkChangeReceiver extends BroadcastReceiver {

    SweetAlertDialog pDialog;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        int status = NetworkUtil.getConnectivityStatusString(context);
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                //write code for internet is disconnected...
                showAlert(context);
            } else {
                //write code for internet is connected...
               // Toast.makeText(context,"Internet Connected",Toast.LENGTH_SHORT).show();
               pDialog.dismiss();
            }
        }
    }

    public void showAlert(Context applicationContext){
        pDialog.setTitleText("No Internet Connection!");
        pDialog.setContentText("Please Check Internet Connection.");
        // .setCancelText("It's Okay")
        pDialog.setCancelable(false);
        pDialog.setConfirmText("Okay");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                //  context.startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
                sDialog.dismiss();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(a);
                ((Activity) applicationContext).finishAffinity();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }).show();
    }
    public void closeDialog(){
        if (pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
}