package com.winapp.deliverytrackingapp.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.winapp.deliverytrackingapp.ui.utils.Constants
import com.winapp.deliverytrackingapp.ui.utils.SharedPreferenceUtil
import com.winapp.deliverytrackingapp.R

class SplashScreenActivity : AppCompatActivity() {
    var imageView: ImageView? = null
    var textView1: TextView? = null
    var textView2: TextView? = null
    var version_txtl: TextView? = null
    var top: Animation? = null
    var bottom: Animation? = null
    private var sharedPreferenceUtil: SharedPreferenceUtil? = null
    private var loginPreferences: SharedPreferences? = null
    private var loginPrefsEditor: SharedPreferences.Editor? = null
    private var saveLogin: Boolean = false
    private var loginPref: Boolean = false

    private var registerPreferences: SharedPreferences? = null
    private var registerPrefsEditor: SharedPreferences.Editor? = null
    private var saveRegister : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen_new)
        sharedPreferenceUtil = SharedPreferenceUtil(this)

        imageView = findViewById(R.id.imageView)
        textView1 = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)
        top = AnimationUtils.loadAnimation(this, R.anim.top)
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom)
        imageView!!.setAnimation(top)
        textView1!!.setAnimation(bottom)
        textView2!!.setAnimation(bottom)
        version_txtl = findViewById<TextView>(R.id.version_txt)
      //  version_txtl!!.setText(Constants.VERSION_CODE)

        // Store the Remember me to Session..
        // Set the Preference value in edittext for Remembering the values
//        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
//        loginPrefsEditor = loginPreferences!!.edit()
//        saveLogin = loginPreferences!!.getBoolean("saveLogin", false)

         loginPref = sharedPreferenceUtil!!.getBooleanPreference(Constants.KEY_ISLOGIN,false)

        registerPreferences = getSharedPreferences("registerPrefs", MODE_PRIVATE)
        registerPrefsEditor = registerPreferences!!.edit()
        saveRegister = registerPreferences!!.getBoolean("saveRegister", false)

//        sharedPreferenceUtil!!.setStringPreference(
//            Constants.KEY_SELECT_FROMDATE, ""
//        )
//        sharedPreferenceUtil!!.setStringPreference(
//            Constants.KEY_SELECT_FROMDATE_DISPLAY, ""
//        )
//        sharedPreferenceUtil!!.setStringPreference(
//            Constants.KEY_SELECT_TODATE, ""
//        )
//        sharedPreferenceUtil!!.setStringPreference(
//            Constants.KEY_SELECTDATE_TODISPLAY, ""
//        )

        Log.w("savlofff",""+loginPref)

        //todo with license

//        Handler().postDelayed({
//            val device_id = registerPreferences!!.getString("reg_deviceId", "")
//                              getRegisterData("22",device_id!!)
//
//            Log.e("devSplash","$device_id")
//        }, SPLASH_SCREEN.toLong())

        //todo old code test
        Handler().postDelayed({
            if (loginPref) {
                val intent1 = Intent(this@SplashScreenActivity, NewDeliveryPickListActivity::class.java)
                startActivity(intent1)
                finish()
            }
            else
            {
                val intent1 = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent1)
                finish()
            }

        }, SPLASH_SCREEN.toLong())


          //  startActivity(Intent(this, PoScanAddNewActivity::class.java))

//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            if (Helper.isLoggedIn(sharedPreferenceUtil!!)) {
//                val intent1 = Intent(this, MainActivity::class.java)
//                startActivity(intent1)
//                finish()
//            }
//            else{
//                val intent2 = Intent(this, LoginActivity::class.java)
//                startActivity(intent2)
//                finish()
//            }

    }

//    @Throws(JSONException::class)
//    private fun getRegisterData(
//        appCode: String,
//        deviceId: String
//    ) {
//        //     commonMethodKotl.showProgressDialog(this)
//
////        val jsonObject = JSONObject()
////        jsonObject.put("AppCode", appCode)
////        jsonObject.put("DeviceId", deviceId)
//
//        val requestQueue = Volley.newRequestQueue(this)
//        val url = "http://3.85.9.22/Licence/api/LicenceApi/CheckDevice"
//        Log.w("url_checkdevic:", url)
//
//
//        val jsonObjectRequest: StringRequest =
//            object : StringRequest(Method.POST, url, Response.Listener { response: String ->
//
//                try {
//                    Log.w("Response_register:", response.toString())
//
//                    if (response.isNotEmpty()) {
//                        GlobalScope.launch {
//
//                            Log.e("urlCheckDevice:", response)
//
//                            val obj = JSONObject(response)
//                            val urlStr = obj.optString("URL")
//                            val msg = obj.optString("Msg")
//                            val status = obj.optString("Status")
//
//                            withContext(Dispatchers.Main) {
//
//                                if (status.equals("1")) {
//                                    if (loginPref!!) {
//                                        val intent1 = Intent(this@SplashScreenActivity, MainActivity::class.java)
//                                        startActivity(intent1)
//                                        finish()
//                                    }
//                                    else
//                                    {
//                                        val intent1 = Intent(this@SplashScreenActivity, LoginActivity::class.java)
//                                        startActivity(intent1)
//                                        finish()
//                                    }
////                                    if (!urlStr.isEmpty()) {
////                                        urlApi = urlStr
////                                        registerPrefsEditor!!.putBoolean("saveRegister", true)
////                                        registerPrefsEditor!!.putString("reg_url", urlApi)
////
////                                        Constants.BASEURL = urlApi + "/es/data/api/"
////                                        registerPrefsEditor!!.commit()
////                                    }
//                                } else {
//                                    val intent1 = Intent(this@SplashScreenActivity, RegisterDashboardActivity::class.java)
//                                    startActivity(intent1)
//                                    finish()
//
////                                    registerPrefsEditor!!.clear()
////                                    registerPrefsEditor!!.commit()
//                                }
//
//                            }
//                        }
//                        //      commonMethodKotl.cancelProgressDialog()
//
//                    }
//                }
//
//                catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//
//
//            }, Response.ErrorListener { error: VolleyError ->
//                // Do something when error occurred
//                //   commonMethodKotl.cancelProgressDialog()
//
//                Log.w("Error_throwing:", error.toString())
//            }) {
//                override fun getHeaders(): Map<String, String> {
//                    val params = HashMap<String, String>()
//                    val creds =
//                        String.format("%s:%s", Constants.API_SECRET_CODE, Constants.API_SECRET_PASSWORD)
//                    val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.DEFAULT)
//                    params["Authorization"] = auth
//                    return params
//                }
//                override fun getParams(): Map<String, String>? {
//                    val params: MutableMap<String, String> = java.util.HashMap()
//                    params["AppCode"] = appCode
//                    params["DeviceId"] = deviceId
//
//                    return params
//                }
//
//            }
//        jsonObjectRequest.retryPolicy = object : RetryPolicy {
//            override fun getCurrentTimeout(): Int {
//                return 50000
//            }
//
//            override fun getCurrentRetryCount(): Int {
//                return 50000
//            }
//
//            @Throws(VolleyError::class)
//            override fun retry(error: VolleyError) {
//            }
//        }
//        // Add JsonArrayRequest to the RequestQueue
//        requestQueue.add(jsonObjectRequest)
//    }

    companion object {
        private const val SPLASH_SCREEN = 2500
    }
}