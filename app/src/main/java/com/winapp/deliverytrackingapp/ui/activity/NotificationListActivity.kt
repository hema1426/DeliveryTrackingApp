package com.winapp.deliverytrackingapp.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.winapp.KHDelivery.model.PickIistDeliveryListingModel
import com.winapp.deliverytrackingapp.R
import com.winapp.deliverytrackingapp.ui.adapter.NotificationListAdapter
import com.winapp.deliverytrackingapp.ui.adapter.ScannedInvoiceListAdapter
import com.winapp.deliverytrackingapp.ui.model.NotificationModel
import com.winapp.deliverytrackingapp.ui.model.PicklistDeliveryPrintPreviewModel
import com.winapp.deliverytrackingapp.ui.utils.CommonMethodKotl.toast
import com.winapp.deliverytrackingapp.ui.utils.Constants
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap
import java.util.Objects

private var notifi_size: TextView? = null
private var emptytxt: TextView? = null
private var rv_notificationList: RecyclerView? = null
private var pDialog: SweetAlertDialog? = null
var notificationList: ArrayList<NotificationModel> = ArrayList()
var notificationListAdapter: NotificationListAdapter? = null


class NotificationListActivity : NavigationActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentFrameLayout = findViewById<FrameLayout>(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_notification_list, contentFrameLayout)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Notification List"

        notifi_size = findViewById(R.id.notif_size_list)
        rv_notificationList = findViewById(R.id.rv_notificat_list)
        emptytxt = findViewById(R.id.notif_empty_txt)

        Log.w("activity_cg", javaClass.getSimpleName().toString())
        getNotificationList()
    }

    @Throws(JSONException::class)
    private fun getNotificationList() {
        val requestQueue = Volley.newRequestQueue(this)
        val jsonObject = JSONObject()

        jsonObject.put("User", username1)

        val url = Constants.BASEURL + "NotificationsList"
        Log.w("url_picklis_deli:", "$url-$jsonObject")
        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"))
        pDialog!!.setTitleText("Getting PickList...")
        pDialog!!.setCancelable(false)
        pDialog!!.show()

        notificationList = ArrayList()

        val jsonArrayRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
            Response.Listener<JSONObject> { response: JSONObject ->
                try {
                    Log.w("notif_ListRes:", response.toString())

                    pDialog!!.dismiss()
                    val statusCode = response.optString("statusCode")
                    val statusMsg = response.optString("statusMessage")

                    if (statusCode == "1") {
                        val responseData = response.optJSONArray("responseData")!!

                        if (responseData!!.length() > 0) {

                            for (i in 0 until responseData.length()) {
                                val obj = responseData.optJSONObject(i)

                                val model = NotificationModel()
                                model.title = obj.optString("title");
                                model.message = obj.optString("message")
                                model.date = obj.optString("createdAt")
                                model.type = obj.optString("docType")

                                notificationList!!.add(model)
                            }
                            if (notificationList!!.size > 0) {
                                rv_notificationList!!.visibility = View.VISIBLE
                                emptytxt!!.visibility = View.GONE
                                setAdapter(notificationList!!)
                            } else {
                                rv_notificationList!!.visibility = View.GONE
                                emptytxt!!.visibility = View.VISIBLE
                                notifi_size!!.visibility = View.GONE
                            }

                        } else {
                            rv_notificationList!!.visibility = View.GONE
                            emptytxt!!.visibility = View.VISIBLE
                            notifi_size!!.visibility = View.GONE
                        }
                    } else {
                        toast(this@NotificationListActivity, statusMsg)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                pDialog!!.dismiss()
                // Do something when error occurred
                Log.w("Error_throwing:", error.toString())
                Toast.makeText(
                    applicationContext,
                    "Server Error,Please try again..",
                    Toast.LENGTH_LONG
                ).show()
            }) {
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                val creds =
                    String.format("%s:%s", Constants.API_SECRET_CODE, Constants.API_SECRET_PASSWORD)
                val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.DEFAULT)
                params["Authorization"] = auth
                return params
            }
        }
        jsonArrayRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        })
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest)
    }

    private fun setAdapter(arrayList: ArrayList<NotificationModel>) {
        notifi_size!!.text = "(" + arrayList.size + ")" + "Notification List"

        notificationListAdapter = NotificationListAdapter(this, arrayList)
        rv_notificationList!!.layoutManager =
            LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            ) as RecyclerView.LayoutManager?
        rv_notificationList!!.adapter = notificationListAdapter

    }
}