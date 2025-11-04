package com.winapp.deliverytrackingapp.ui.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.winapp.deliverytrackingapp.R


object CommonMethodKotl {
    var mDialog: Dialog? = null


    fun cancelProgressDialog() {
        if (mDialog != null
            && mDialog!!.isShowing
        ) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }
    fun showError_dialog(activity: Activity,msg: String) {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle("Error")
        alertDialogBuilder.setMessage(msg)
        alertDialogBuilder.setPositiveButton(
            "Ok"
        ) { dialog, which ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }
    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
//    fun toast( msg: String) {
//        Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT).show()
//    }

    fun showProgressDialog(activity: Activity) {
        if (activity != null
            && mDialog == null
        ) {
            mDialog = Dialog(activity)
            mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mDialog!!.setCancelable(false)
            mDialog!!.setCanceledOnTouchOutside(false)
            mDialog!!.setContentView(R.layout.dialog)
            mDialog!!.window!!
                .setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity,
                        android.R.color.transparent
                    )
                )
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(mDialog!!.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            mDialog!!.window!!.attributes = lp
            mDialog!!.show()
        }
    }

}
