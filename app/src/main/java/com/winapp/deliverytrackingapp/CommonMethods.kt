package com.winapp.deliverytrackingapp

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.winapp.deliverytrackingapp.R
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


object CommonMethods {
    var mDialog: Dialog? = null
    fun cancelProgressDialog() {
        if (mDialog != null
            && mDialog!!.isShowing
        ) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }

    const val open_status = "Open"
    const val partial_status = "Partial"
    const val close_status = "Close"

    const val open_statusNo = 0
    const val partial_statusNo = 2
    const val close_statusNo = 1
    var mAnim: Animation? = null

    fun getCurrentDateFile(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("ddMMyyyyHHmmss")
        val date = Date()
        return dateFormat.format(date)
    }

    fun getCurrentDateApiNOSpace(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
        val date = Date()
        return dateFormat.format(date)
    }
    fun getFirstDayApiNOSpace(): String? {
        val cal = Calendar.getInstance()
        val date =
            cal.get(Calendar.YEAR).toString()+""+ (cal.get(Calendar.MONTH) + 1).toString() +""+cal.getActualMinimum(Calendar.DAY_OF_MONTH).toString()

//        val cc = Calendar.getInstance()
//        val year = cc[Calendar.YEAR]
//        val month = cc[Calendar.MONTH]
//        val mDay = cc.getActualMinimum(Calendar.DAY_OF_MONTH)

        return (date)
     return date
    }
    fun  StartMonthDate(): String? {
        val c = Calendar.getInstance()
        //ensure the method works within current month
        c[Calendar.DAY_OF_MONTH] = c.getActualMinimum(Calendar.DAY_OF_MONTH)
       // println("Before Start Date " + c.time)
        val date = c.time
        val dfDate = SimpleDateFormat("yyyyMMdd")
        val CurrentDate = dfDate.format(date)

        Log.w("startmonth11", "" +  CurrentDate)
        return CurrentDate
    }

    fun  StartMonthDate1(): String? {
        val c = Calendar.getInstance()
        //ensure the method works within current month
        c[Calendar.DAY_OF_MONTH] = c.getActualMinimum(Calendar.DAY_OF_MONTH)
        // println("Before Start Date " + c.time)
        val date = c.time
        val dfDate = SimpleDateFormat("dd/MM/yyyy")
        val CurrentDate = dfDate.format(date)

        Log.w("startmonth", "" +  CurrentDate)
        return CurrentDate
    }
    fun setBlinkingText(textView: TextView) {
        mAnim = AlphaAnimation(0.0f, 1.0f)
        mAnim!!.setDuration(1000) // Time of the blink
        mAnim!!.setStartOffset(20)
        mAnim!!.setRepeatMode(Animation.REVERSE)
        mAnim!!.setRepeatCount(Animation.INFINITE)
        textView.startAnimation(mAnim)
    }
    fun setBlinkingLay(linearLayout : LinearLayout) {
        mAnim = AlphaAnimation(0.0f, 1.0f)
        mAnim!!.setDuration(1000) // Time of the blink
        mAnim!!.setStartOffset(20)
        mAnim!!.setRepeatMode(Animation.REVERSE)
        mAnim!!.setRepeatCount(Animation.INFINITE)
        linearLayout.startAnimation(mAnim)
    }

    fun getCurrentDate(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()
        return dateFormat.format(date)
    }

    fun getCurrentDateApiD(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        return dateFormat.format(date)
    }

    fun getApiCurrentDate(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        val date = Date()
        return dateFormat.format(date)
    }

    fun twoDecimalPoint(d: Double): String? {
        val df = DecimalFormat("#.##")
        df.minimumFractionDigits = 2
        return df.format(d)
    }

    fun dateConvert(date: String): String? {

        val curFormater = SimpleDateFormat("dd/MM/yyyy")
        val dateObj = curFormater.parse(date)
        val postFormater = SimpleDateFormat("yyyyMMdd")

        return postFormater.format(dateObj)
    }

    fun getCurrentTime(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()
        return dateFormat.format(date)
    }
    fun getCurrentTime1(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        return dateFormat.format(date)
    }

    fun getCurrentdateapi(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM_dd")
        val date = Date()
        return dateFormat.format(date)
    }

    fun showProgressDialog(activity: Activity?) {
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
            if (mDialog != null)
                mDialog!!.show()
        }
    }

    // navigating user to app settings
    private fun openSettings(activity: Activity, permissionsRequest: Int) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivityForResult(intent, permissionsRequest)
    }

    fun isNetworkConnected(activtiy: Context): Boolean {
        val cmss = activtiy
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val niss = cmss.activeNetworkInfo
        return niss != null
    }

//    fun showConnectionAlert(context: Context) {
//        val alertDialogBuilder = AlertDialog.Builder(
//            Objects.requireNonNull(context), R.style.CustomDialogTheme
//        )
//        alertDialogBuilder.setTitle(context.getString(R.string.alert_title_connectionError))
//        alertDialogBuilder.setMessage(context.getString(R.string.alert_msg_connectionError))
//            .setPositiveButton(context.getString(R.string.action_ok), null)
//        val alertDialog = alertDialogBuilder.create()
//        alertDialog.setOnShowListener {
//            val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
//            btnPositive.setOnClickListener { alertDialog.dismiss() }
//        }
//        alertDialog.setCancelable(false)
//        alertDialog.setCanceledOnTouchOutside(false)
//        alertDialog.show()
//    }

}