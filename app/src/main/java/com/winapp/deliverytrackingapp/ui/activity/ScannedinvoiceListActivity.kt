package com.winapp.deliverytrackingapp.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.winapp.deliverytrackingapp.CommonMethods
import com.winapp.deliverytrackingapp.ui.adapter.DeliveryPickListNewAdapter
import com.winapp.deliverytrackingapp.ui.model.PicklistDeliveryPrintPreviewModel
import com.winapp.deliverytrackingapp.ui.multiselectspinner.MultiSelectSpinnerView
import com.winapp.deliverytrackingapp.ui.utils.CaptureSignatureView
import com.winapp.deliverytrackingapp.ui.utils.CommonMethodKotl.toast
import com.winapp.deliverytrackingapp.ui.utils.Constants
import com.winapp.deliverytrackingapp.ui.utils.FileCompressor
import com.winapp.deliverytrackingapp.ui.utils.ImageUtil
import com.winapp.deliverytrackingapp.ui.utils.LocationTrack
import com.winapp.deliverytrackingapp.ui.utils.SessionManager
import com.winapp.deliverytrackingapp.ui.utils.SharedPreferenceUtil
import com.winapp.deliverytrackingapp.ui.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.winapp.KHDelivery.model.PickIistDeliveryListingModel
import com.winapp.deliverytrackingapp.BuildConfig
import com.winapp.deliverytrackingapp.R
import com.winapp.deliverytrackingapp.ui.adapter.ScannedInvoiceListAdapter
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ScannedinvoiceListActivity : NavigationActivity(),
    OnClickListener, ScannedInvoiceListAdapter.PickListUploadClickListener {

    private var rv_scannedListl: RecyclerView? = null
    private var sharedPreferenceUtil: SharedPreferenceUtil? = null

    // var intentIntegrator: IntentIntegrator? = null
    var pickListNewAdapter: ScannedInvoiceListAdapter? = null
    private var barCodelay: LinearLayout? = null
    var session: SessionManager? = null
    var companyId: String? = null
    var locationCode: String? = null
    var select_FromDateStr: String? = ""
    var select_FromDateShowStr: String? = ""
    var select_ToDateStr: String? = ""
    var usernamel = ""
    var select_ToDateShowStr: String? = ""
    var zoneMutipleCode: String? = ""
    var zoneStrName: String? = ""
    var zoneStrCode: String? = ""
    var username: String? = null
    var user: HashMap<String, String>? = null
    var todateShared: String? = ""
    var fromdateShared: String? = ""
    var userPermission = ""

    var DAY_FORMAT = "dd"
    var MONTH_FORMAT = "MM"
    var YEAR_FORMAT = "yyyy"
    var DATE_FORMAT_API1 = "$YEAR_FORMAT$MONTH_FORMAT$DAY_FORMAT"
    private var current_FromDateApi =
        SimpleDateFormat(DATE_FORMAT_API1, Locale.US).format(Calendar.getInstance().time)
    private var current_ToDateApi =
        SimpleDateFormat(DATE_FORMAT_API1, Locale.US).format(Calendar.getInstance().time)
    private var currentDate =
        SimpleDateFormat(Constants.DAY_FORMAT, Locale.US).format(Calendar.getInstance().time)
    private val DATE_FORMAT1: String = DAY_FORMAT + "/" + MONTH_FORMAT + "/" + YEAR_FORMAT

    var isscanpdt: Boolean? = false
    private var spinner_statusl: Spinner? = null
    private var spinner_pickStatus: Spinner? = null
    var alert: AlertDialog? = null
    var alertUpload: AlertDialog? = null
    var alertUploadView: AlertDialog? = null
    var signatureString = ""
    var addSignat_Imgl: ImageView? = null
    var signatureCapture: ImageView? = null
    var captureSignatureView: CaptureSignatureView? = null
    var uploadImgDialog_txt: TextView? = null
    var uploadImgDialogLay: LinearLayout? = null
    var uploadImgLay: LinearLayout? = null
    var mPhotoFile: File? = null
    var spinnertxt_dialog: String? = "";
    var packStatusStr = ""
    val REQUEST_TAKE_PHOTO = 1
    val REQUEST_GALLERY_PHOTO = 2
    var mCompressor: FileCompressor? = null
    var currentSaveDateTime: String? = ""
    var imageString: String? = ""
    var current_latitude = "0.00"
    var current_longitude = "0.00"
    var currentLocationLatitude = 0.0
    var currentLocationLongitude = 0.0
    var locationTrack: LocationTrack? = null
    var current_addr = ""
    var view_signaturel: ImageView? = null
    var view_imgl: ImageView? = null
    var isSearchCustomerNameClicked = false
    // private var SO_sharf: Boolean? = null
//    private var INV_sharf: Boolean? = null
    private var pickifromdatelay: LinearLayout? = null
    private var pickitodatelay: LinearLayout? = null
    private var custLay_pickl: LinearLayout? = null
    private var pickifromdate_txt: TextView? = null
    private var pickitodate_txt: TextView? = null
    private var pickdate_search: Button? = null
    private var btn_cancelm: Button? = null
    private var spinner_layl: RelativeLayout? = null
    private var pick_type_api: String? = null
    private var isspinner: Boolean? = null
    private lateinit var callback: OnBackPressedCallback
    private var filterImg: ImageView? = null
    private var addImg: ImageView? = null
    private var emptytxt: TextView? = null
    var picklistNew: ArrayList<PickIistDeliveryListingModel> = ArrayList()
    var spinnertxt: String? = "";
    var customerStr: String? = "";
    val CUST_RESULT_CODE = 20
    var soNum: String? = ""
    var selectCustomerName: String? = ""
    var selectCustomerCode: String? = ""
    var totalSize: TextView? = null
    var so_number_filter_pickl: TextView? = null
    var searchFilterView: View? = null
    var custFilterAutol: TextView? = null
    var zoneSpinner: MultiSelectSpinnerView? = null
    val zoneSpinnerStr: String? = ""
    private var pDialog: SweetAlertDialog? = null

    var date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    private var search_ed: EditText? = null
    var searchTextWatcher: TextWatcher? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentFrameLayout = findViewById<FrameLayout>(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_scanned_invoice_list, contentFrameLayout)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Scanned Invoice List"

        //  setContentView(R.layout.activity_delivery_picklist_list
//        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        Log.w("activity_cg", javaClass.getSimpleName().toString())

        sharedPreferenceUtil = SharedPreferenceUtil(this)
        //  intentIntegrator = IntentIntegrator.forSupportFragment(this) // use this instead
        session = SessionManager(this)

        rv_scannedListl = findViewById(R.id.rv_scanned_list)
        barCodelay = findViewById(R.id.barcode_lay)
        pickitodatelay = findViewById(R.id.scani_todatelay)
        pickifromdatelay = findViewById(R.id.scani_fromdatelay)
        pickifromdate_txt = findViewById(R.id.scani_fromdate_txt)
        pickitodate_txt = findViewById(R.id.scani_todate_txt)
        pickdate_search = findViewById(R.id.search_dat_scaninv)
        btn_cancelm = findViewById(R.id.btn_cancel_pickDel)
        custLay_pickl  = findViewById(R.id.custLay_scaninv)
//        search_lay =  findViewById(R.id.picklist_search_lay)
        search_ed = findViewById(R.id.searchBar_pick)
        spinner_statusl = findViewById<View>(R.id.spinner_status_pick) as Spinner
//        spinner_layl = findViewById(R.id.spinner_lay)
        emptytxt = findViewById(R.id.empty_txt)
        totalSize = findViewById(R.id.item_size_list)
        so_number_filter_pickl = findViewById(R.id.so_number_filter_pick)
        searchFilterView = findViewById(R.id.search_filter_scanInv)
        custFilterAutol = findViewById(R.id.custFilterAuto)
        zoneSpinner = findViewById(R.id.zoneSpinnerl)
        mCompressor = FileCompressor(this)

        //   val status = arrayOf("All", "Pending", "Packed", "Delivered")
        val status = arrayOf("All", "Pending", "Packed")

        val langAdapter =
            ArrayAdapter<CharSequence>(this, R.layout.cust_spinner_item, status)
        langAdapter.setDropDownViewResource(R.layout.item_grouplist_spinner)
        spinner_statusl!!.setAdapter(langAdapter)

        pickitodatelay!!.setOnClickListener(this)
        pickifromdatelay!!.setOnClickListener(this)
        pickdate_search!!.setOnClickListener(this)
        btn_cancelm!!.setOnClickListener(this)
        // search_ed!!.setOnClickListener(this)
        spinnertxt = ""
        spinnertxt_dialog = ""
        packStatusStr = ""
        imageString = ""
        signatureString = ""

        getCurrentLocation()

        Log.w("selectdateSO", "" + select_FromDateShowStr + ".." + select_FromDateStr)
        Log.w("selectdateSOTo", "" + select_ToDateShowStr + ".." + select_ToDateStr)

        Log.w("startmonth", "" +  CommonMethods.StartMonthDate())

        if (select_FromDateShowStr!!.isNotEmpty()) {
            pickifromdate_txt!!.setText(select_FromDateShowStr)
            fromdateShared = select_FromDateStr
        } else {
            select_FromDateStr = CommonMethods.getCurrentDateApiNOSpace()
            fromdateShared = CommonMethods.StartMonthDate()
            pickifromdate_txt!!.setText(CommonMethods.StartMonthDate1())
        }

        if (select_ToDateShowStr!!.isNotEmpty()) {
            pickitodate_txt!!.setText(select_ToDateShowStr)
            todateShared = select_ToDateStr
        } else {
            select_ToDateStr = CommonMethods.getCurrentDateApiNOSpace()
            todateShared = select_ToDateStr
            pickitodate_txt!!.setText(CommonMethods.getCurrentTime1())
        }

        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (!editable.toString().isEmpty()) {
                    filterProducts(editable.toString())
                } else {
                    setAdapter(picklistNew!!)
                }
            }
        }
        custLay_pickl!!.setOnClickListener {
            val intent = Intent(applicationContext, FilterCustomerListActivity::class.java)
            startActivityForResult(intent, CUST_RESULT_CODE)
        }
        // custFilterAutol!!.clearFocus()

        search_ed!!.addTextChangedListener(searchTextWatcher)

        spinner_statusl!!.setOnTouchListener(View.OnTouchListener { v, event ->

            Log.e("edtouch", "")
            spinner_statusl!!.setOnItemSelectedListener(@SuppressLint("ClickableViewAccessibility")
            object :
                AdapterView.OnItemSelectedListener {
                @SuppressLint("ClickableViewAccessibility")
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    //first,  we have to retrieve the item position as a string
                    // then, we can change string value into integer
                    val item_position = position.toString()
                    val positonInt = Integer.valueOf(item_position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })
            false
        })

        user = session!!.getUserDetails()
        companyId = user!!.get(SessionManager.KEY_COMPANY_CODE)
        locationCode = user!!.get(SessionManager.KEY_LOCATION_CODE)
        username = user!!.get(SessionManager.KEY_USER_NAME)

//        user = Helper.getLoggedInUser(sharedPreferenceUtil!!)
//        if(user!=null && user!!.size>0){
//            userName = user!!.get(0).userName
//        }
        userPermission = sharedPreferenceUtil!!.getStringPreference(
            sharedPreferenceUtil!!.KEY_ADMIN_PERMISSION,
            ""
        )

        zoneStrCode = sharedPreferenceUtil!!.getStringPreference(
            Constants.KEY_ADDRESS_ZONE_CODE,
            Constants.DEFAULT_STRING
        )
        zoneStrName = sharedPreferenceUtil!!.getStringPreference(
            Constants.KEY_ADDRESS_ZONE_NAME,
            Constants.DEFAULT_STRING
        )
        select_FromDateShowStr = sharedPreferenceUtil!!.getStringPreference(
            Constants.KEY_SELECT_FROMDATE_DISPLAY,
            Constants.DEFAULT_STRING
        )
        select_FromDateStr = sharedPreferenceUtil!!.getStringPreference(
            Constants.KEY_SELECT_FROMDATE,
            Constants.DEFAULT_STRING
        )
        select_ToDateShowStr = sharedPreferenceUtil!!.getStringPreference(
            Constants.KEY_SELECTDATE_TODISPLAY,
            Constants.DEFAULT_STRING
        )
        select_ToDateStr = sharedPreferenceUtil!!.getStringPreference(
            Constants.KEY_SELECT_TODATE,
            Constants.DEFAULT_STRING
        )
        if (userPermission.equals("True",true)) {
            usernamel = "All"
        }else {
            usernamel  = username!!
        }
        getpicklist_Detail(usernamel!!,locationCode!!,"", CommonMethods.StartMonthDate()!!,
            CommonMethods.getCurrentDateApiNOSpace()!!, soNum!!, spinnertxt!!)
    }

    private fun setAdapter(arrayList: ArrayList<PickIistDeliveryListingModel>) {
        visibletxt()
        totalSize!!.text = "(" + arrayList.size + ")" + " Scanned Invoice List"

        pickListNewAdapter = ScannedInvoiceListAdapter(this, arrayList,this)
        rv_scannedListl!!.layoutManager =
            LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            ) as RecyclerView.LayoutManager?
        rv_scannedListl!!.adapter = pickListNewAdapter

//        search_ed!!.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable) {
//                if (s != null && s.length > 3) {
//                    Log.e("searchgg",""+s.toString())
//                  //  getPicklistapi(fromdateApi, todateApi, s.toString(), pick_type_api)
//                }
//            }
//        })
//        search_ed!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                Log.e("searchff",""+search_ed!!.getText().toString())
//               // getPicklistapi(fromdateApi, todateApi, search_ed!!.getText().toString(), pick_type_api)
//
//            }
//            false
//        })
    }

    fun filterProducts(name: String) {
        try {
            //new array list that will hold the filtered data
            val filterdNames = ArrayList<PickIistDeliveryListingModel>()
            //looping through existing elements
            for (s in picklistNew!!) {
                //if the existing elements contains the search input
                if (s.customerName!!.lowercase(Locale.getDefault())
                        .contains(name.lowercase(Locale.getDefault())) ||
                    s.customerCode!!.lowercase(Locale.getDefault())
                        .contains(name.lowercase(Locale.getDefault()))
                ) {
                    //adding the element to filtered list
                    filterdNames.add(s)
                }
            }
            //calling a method of the adapter class and passing the filtered list
            if (filterdNames.size > 0) {
                visibletxt()
                pickListNewAdapter!!.updateList(filterdNames)
                totalSize!!.text = "(" + filterdNames.size + ")" + "Items"
            } else {
                emptytxt()
                Utils.hideKeyBoard(this, search_ed)
            }
        } catch (ex: Exception) {
            Log.e("Error_in_filter", ex.message!!)
        }
    }

    @Throws(JSONException::class)
    private fun getpicklist_Detail(
        user:String,
        location:String,
        customerCode: String,
        fromdate: String,
        todate: String,
        docNum: String,
        status: String
    ) {
        // Initialize a new RequestQueue instance
        val requestQueue = Volley.newRequestQueue(this)
        // Initialize a new JsonArrayRequest instance
        val jsonObject = JSONObject()
        //        if (selectedUser!=null && !selectedUser.isEmpty()){
//            jsonObject.put("User",selectedUser);
//        }else {
//            jsonObject.put("User",userName);
//        }
//        {"User":"SALES1","LocationCode":"01","CustomerCode":"","FromDate":"20251224","ToDate":"20251224","DocNo":"","DocStatus":"C"}

        jsonObject.put("User", user)
        jsonObject.put("LocationCode", location)
        jsonObject.put("CustomerCode", customerCode)
        jsonObject.put("FromDate", fromdate)
        jsonObject.put("ToDate", todate)
        jsonObject.put("DocNo", docNum)
        jsonObject.put("DocStatus", status)

        val url = Constants.BASEURL + "ScannedInvoiceList"
        Log.w("url_scanned_inv:", "$url-$jsonObject")
        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"))
        pDialog!!.setTitleText("Getting PickList...")
        pDialog!!.setCancelable(false)
        pDialog!!.show()

        picklistNew = ArrayList()

        val jsonArrayRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
            Response.Listener<JSONObject> { response: JSONObject ->
                try {
                    Log.w("picklist_deli_res:", response.toString())

                    pDialog!!.dismiss()
                    val statusCode = response.optString("statusCode")
                    val statusMsg = response.optString("statusMessage")

                    if (statusCode == "1") {
                        val responseData = response.optJSONArray("responseData")!!

                        if (responseData!!.length() > 0) {

                            for (i in 0 until responseData.length()) {
                                val obj = responseData.optJSONObject(i)
                                var invoiceList: ArrayList<PicklistDeliveryPrintPreviewModel.InvoiceList>? = ArrayList()

                                var custAddress =  obj.optString("billingAddress") +obj.optString("billingStreet") +
                                        obj.optString("billingCity") +obj.optString("billingState") +obj.optString("billingCountry")+
                                        obj.optString("billingZipCode")
                                var shipAddress =  ""
                                if(obj.optString("shipTo") !=null && obj.optString("shipTo").isNotEmpty()){
                                    shipAddress =  obj.optString("shipTo")
                                }else{
                                    shipAddress =  obj.optString("shippingAddress2") +obj.optString("shippingStreet") +
                                            obj.optString("shippingCity") +obj.optString("shippingState") +obj.optString("shippingCountry")+
                                            obj.optString("shippingZipCode")
                                }

//                                Log.w("shiaddreee1",""+shipAddress)
//                                Log.w("shiaddreee",""+obj.optString("shippingAddress2") )
                                val model = PickIistDeliveryListingModel()
                                model.code = obj.optString("code");
                                model.customerCode = obj.optString("customerCode")
                                model.customerName = obj.optString("customerName")
                                model.docDate = obj.optString("invoiceDate")
                                model.invNumber = obj.optString("invoiceNumber")
                                model.noOfItem = obj.optString("noOfItemCount")
                                model.invoiceStatus = obj.optString("invoiceStatus")
                                model.dateTime = obj.optString("dateTime")
                                model.pickListStatus = obj.optString("pickingStatus")
                                model.customerAddress = custAddress
                                model.shipAddress = shipAddress
                                model.phoneNo = obj.optString("phone1")
                                model.contactName = obj.optString("deliveryContactPerson")
                                model.remark = obj.optString("remark")
                                model.user = obj.optString("user")
                                model.signatureUrl = obj.optString("signature")
                                model.imageUrl = obj.optString("image")
                                model.mobileUser = obj.optString("mobileUser")
                                model.reason = obj.optString("reason")
                                model.isShow = false
                                model.isItemSelected = false
                                model.invoiceList = invoiceList

                                Log.w("delDateStr1a:",".."+obj.optString("dateTime"));

                                picklistNew!!.add(model)
                            }

                            //   withContext(Dispatchers.Main) {
                            if (picklistNew!!.size > 0) {
                                searchFilterView!!.visibility = View.GONE
                                setAdapter(picklistNew!!)
                                cleartxt()

                            } else {
                                emptytxt()
                                searchFilterView!!.visibility = View.GONE
                            }
                            //   }
                        } else {
                            emptytxt()
                        }
                    } else {
                        toast(this@ScannedinvoiceListActivity, statusMsg)
                        emptytxt()

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
    override fun onClick(view: View) {
        when (view.id) {

            R.id.scani_fromdatelay ->                 // Get Current Date
            {
                val c = Calendar.getInstance()
                val mYear = c[Calendar.YEAR]
                val mMonth = c[Calendar.MONTH]
                val mDay = c[Calendar.DAY_OF_MONTH]
                val datePickerDialog = DatePickerDialog(
                    Objects.requireNonNull(this),
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        var selectedMonthYearDate: Date? = null
                        try {
                            selectedMonthYearDate = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                                .parse(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year.toString())
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                        if (selectedMonthYearDate != null) {
                            currentDate =
                                SimpleDateFormat(DATE_FORMAT1, Locale.US).format(
                                    selectedMonthYearDate
                                )
                            current_FromDateApi =
                                SimpleDateFormat(DATE_FORMAT_API1, Locale.US).format(
                                    selectedMonthYearDate
                                )
                            sharedPreferenceUtil!!.setStringPreference(
                                Constants.KEY_SELECT_FROMDATE, current_FromDateApi
                            )
                            fromdateShared = current_FromDateApi

                            sharedPreferenceUtil!!.setStringPreference(
                                Constants.KEY_SELECT_FROMDATE_DISPLAY, currentDate
                            )
                            pickifromdate_txt!!.setText(currentDate)
                        }
                    }, mYear, mMonth, mDay
                )
                //  datePickerDialog.datePicker.minDate = c.timeInMillis
                //datePickerDialog.getDatePicker().setMaxDate(twoDaysLater.getTimeInMillis());
                datePickerDialog.show()
            }

            R.id.scani_todatelay ->                 // Get Current Date
            {
                val c = Calendar.getInstance()
                val mYear = c[Calendar.YEAR]
                val mMonth = c[Calendar.MONTH]
                val mDay = c[Calendar.DAY_OF_MONTH]
                val datePickerDialog = DatePickerDialog(
                    Objects.requireNonNull(this),
                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        var selectedMonthYearDate: Date? = null
                        try {
                            selectedMonthYearDate = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                                .parse(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year.toString())
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                        if (selectedMonthYearDate != null) {
                            currentDate =
                                SimpleDateFormat(DATE_FORMAT1, Locale.US).format(
                                    selectedMonthYearDate
                                )
                            current_ToDateApi =
                                SimpleDateFormat(DATE_FORMAT_API1, Locale.US).format(
                                    selectedMonthYearDate
                                )
                            sharedPreferenceUtil!!.setStringPreference(
                                Constants.KEY_SELECT_TODATE, current_ToDateApi
                            )
                            todateShared = current_ToDateApi

                            sharedPreferenceUtil!!.setStringPreference(
                                Constants.KEY_SELECTDATE_TODISPLAY, currentDate
                            )
                            pickitodate_txt!!.setText(currentDate)
                        }
                        Log.e("api3", "..")

                    }, mYear, mMonth, mDay
                )
                //   datePickerDialog.datePicker.minDate = c.timeInMillis
                //datePickerDialog.getDatePicker().setMaxDate(twoDaysLater.getTimeInMillis());
                datePickerDialog.show()
            }

            R.id.btn_cancel_pickDel -> {
                cleartxt()
                searchFilterView!!.visibility = View.GONE
            }

            R.id.search_dat_scaninv -> {
                if (pickifromdate_txt!!.text.toString()
                        .isNotEmpty() && pickitodate_txt!!.text.toString()
                        .isNotEmpty()
                ) {
                    val sdformat = SimpleDateFormat("dd/MM/yyyy")
                    var d1: Date? = null
                    var d2: Date? = null
                    try {
                        d1 = sdformat.parse(pickifromdate_txt!!.getText().toString())
                        d2 = sdformat.parse(pickitodate_txt!!.getText().toString())
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    Log.w("datecompa", "" + pickifromdate_txt!!.getText().toString() + "..mm" + d1);
                    Log.w("datecompa11", "" + pickitodate_txt!!.getText().toString() + "..mm" + d2);


                    if (spinner_statusl!!.selectedItem.equals("All")) {
                        spinnertxt = ""
                    } else if (spinner_statusl!!.selectedItem.equals("Pending")) {
                        spinnertxt = "O"
                        Log.w("api2_s1", ".." + spinnertxt)

                    } else if (spinner_statusl!!.selectedItem.equals("Packed")) {
                        spinnertxt = "OC"
                        Log.w("api2_s2", ".." + spinnertxt)

                    } else if (spinner_statusl!!.selectedItem.equals("Delivered")) {
                        spinnertxt = "C"
                        Log.w("api2_s3", ".." + spinnertxt)
                    }

                    if (so_number_filter_pickl!!.text.toString().isNotEmpty()
                    ) {
                        soNum = so_number_filter_pickl!!.text.toString()

                    } else {
                        soNum = "";
                    }
                    if (d1!!.compareTo(d2) > 0) {

                        // if (pickifromdate_txt!!.getText().toString()!!.compareTo(pickitodate_txt!!.getText().toString()) > 0) {
                        Toast.makeText(
                            this,
                            "From date should not be greater than to date",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        isSearchCustomerNameClicked = true
                        Log.w("spinna11", "" + soNum + customerStr)
                        if (custFilterAutol!!.text.isEmpty()) {
                            customerStr = ""
                        }
                        var zoneMutipleCodeStr = ""
                        if (zoneMutipleCode!!.isNotEmpty()) {
                            zoneMutipleCodeStr = zoneMutipleCode!!
                        } else {
                            zoneMutipleCodeStr = zoneStrCode!!
                        }
                        Log.w("selectdateSOToaa", "" + fromdateShared + todateShared)

                        getpicklist_Detail(usernamel!!,locationCode!!,
                            selectCustomerCode!!, fromdateShared!!, todateShared!!, soNum!!,
                            spinnertxt!!
                        )
                    }

//                    if(picklistNew.size > 0){
//                        if(spinnertxt.equals("O")) {
//                            val picklistStatus = picklistNew.filter { it.pickListStatus.equals("O") } as ArrayList<PickIistResponseNew>
//                            pickListNewAdapter!!.updateList(picklistStatus)
//                        }
//                        else if(spinnertxt.equals("C")) {
//                            val picklistStatus = picklistNew.filter { it.pickListStatus.equals("C") } as ArrayList<PickIistResponseNew>
//                            pickListNewAdapter!!.updateList(picklistStatus)
//                        }
//                        else if(spinnertxt.equals("R")) {
//                            val picklistStatus = picklistNew.filter { it.pickListStatus.equals("Y") && it.pickListStatus.equals("P")
//                            } as ArrayList<PickIistResponseNew>
//                            pickListNewAdapter!!.updateList(picklistStatus)
//                        }
//                        else if(spinnertxt.equals("")) {
//                            pickListNewAdapter!!.updateList(picklistNew)
//                        }
//                    }
//                    else {
//                        emptytxt()
//                    }
                } else {
                    emptytxt()
                    Toast.makeText(this, "Select from and to date", Toast.LENGTH_LONG)
                        .show()
                }

            }

//            R.id.searchBar_pick ->
//            {
////                search_ed!!.addTextChangedListener(object : TextWatcher {
////                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
////                        Log.e("seacff",""+s.toString())
////
////                    }
////                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
////                        Log.e("seacii",""+s.toString())
////
////                    }
////                    override fun afterTextChanged(s: Editable) {
////                        if (s != null && s.length > 0) {
////                            Log.e("searchgg",""+s.toString())
////                            getPicklistapi(fromdateApi, todateApi, s.toString(), pick_type_api)
////                        }
////                    }
////                })
////        search_ed!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
////            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
////                Log.e("searchff",""+search_ed!!.getText().toString())
////                getPicklistapi(fromdateApi, todateApi, search_ed!!.getText().toString(), pick_type_api)
////
////            }
////            false
////        })
//                Log.e("searchpi","")
//            }

        }
    }

    fun emptytxt() {
        rv_scannedListl!!.visibility = View.GONE
        totalSize!!.visibility = View.INVISIBLE
        emptytxt!!.visibility = View.VISIBLE
        spinner_statusl!!.setSelection(0)
        searchFilterView!!.visibility = View.GONE
        //  searchProduct!!.isEnabled = false
    }

    fun cleartxt() {
        // pickifromdate_txt!!.setText(CommonMethods.getCurrentTime())
        // pickitodate_txt!!.setText(CommonMethods.getCurrentTime())

        //  current_FromDateApi = CommonMethods.getCurrentDateApiNOSpace()
        // current_ToDateApi = CommonMethods.getCurrentDateApiNOSpace()
        custFilterAutol!!.setText("")
        customerStr = ""
        spinnertxt = ""
        so_number_filter_pickl!!.setText("")
    }

    fun visibletxt() {
        rv_scannedListl!!.visibility = View.VISIBLE
        totalSize!!.visibility = View.VISIBLE
        emptytxt!!.visibility = View.GONE
        // searchProduct!!.isEnabled = true
    }

    //    public void setLocationDataToAdapter(ArrayList<String> arrayList) {
    //        // Creating ArrayAdapter using the string array and default spinner layout
    //        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SalesReturnActivity.this, android.R.layout.simple_spinner_item, arrayList);
    //        // Specify layout to be used when list of choices appears
    //        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //        // Applying the adapter to our spinner
    //        locationSpinner.setAdapter(arrayAdapter);
    //        locationSpinner.setOnItemSelectedListener(this);
    //    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == android.R.id.home) {
//            onBackPressed()
//        }
//        return super.onOptionsItemSelected(item)
//    }
//    override fun onBackPressed() {
//        //Execute your code here
//        val intent = Intent(applicationContext, NavigationActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
override fun onBackPressed() {
    val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
    /* if (drawer.isDrawerOpen(GravityCompat.START)) {
        drawer.closeDrawer(GravityCompat.START);
    } else {
        super.onBackPressed();
    }*/if (lastBackPressTime < System.currentTimeMillis() - 4000) {
        val snackbar = Snackbar
            .make(drawer, "Click BACK again to exit", Snackbar.LENGTH_LONG)
        snackbar.show()
        lastBackPressTime = System.currentTimeMillis()
    } else {
        showCloseAlert()
        // super.onBackPressed();
        /*  Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finishAffinity();
        android.os.Process.killProcess(android.os.Process.myPid());*/
    }
}
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    mPhotoFile = mCompressor!!.compressToFile(mPhotoFile)
                    imageString = ImageUtil.getBase64StringImage(mPhotoFile)
                    //  Log.w("GivenImage1:",imageString);
                    Utils.w("GivenImage1Pick", imageString)
                    showImage()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                /* Glide.with(MainActivity.this)
                        .load(mPhotoFile)
                        .apply(new RequestOptions().centerCrop()
                                .circleCrop()
                                .placeholder(R.drawable.profile_pic_place_holder))
                        .into(imageViewProfilePic);*/
            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                val selectedImage = data!!.data
                try {
                    mPhotoFile =
                        mCompressor!!.compressToFile(File(getRealPathFromUri(selectedImage)))
                    uploadImgDialog_txt!!.setText(selectedImage.toString())
                    imageString = ImageUtil.getBase64StringImage(mPhotoFile)
                    // Log.w("GivenImage2:",imageString);
                    Utils.w("GivenImage2Pick", imageString)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (requestCode == CUST_RESULT_CODE) {
                if(data != null) {
                    val customername = data!!.getStringExtra("customerName")
                    val customercode = data.getStringExtra("customerCode")
                    custFilterAutol!!.setText(customername)
                    selectCustomerCode = customercode
                    selectCustomerName = customername
                }
            }
        }
    }

    object CheckPermission {
        //  CHECK FOR LOCATION PERMISSION
        fun checkPermission(activity: Activity?): Boolean {
            val result = ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            return if (result == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                false
            }
        }

        //REQUEST FOR PERMISSSION
        fun requestPermission(activity: Activity?, code: Int) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Toast.makeText(
                    activity,
                    "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    code
                )
            }
        }
    }

    fun showUploadImageAlert(pickModel: PickIistDeliveryListingModel) {
        val alertDialog = AlertDialog.Builder(this@ScannedinvoiceListActivity)
        val customLayout: View = layoutInflater.inflate(R.layout.pick_image_upload_dialog, null)
        alertDialog.setView(customLayout)
        uploadImgDialogLay = customLayout.findViewById<LinearLayout>(R.id.attachement_layout_inv)
        uploadImgDialog_txt = customLayout.findViewById<TextView>(R.id.select_Img_pickdel)
        addSignat_Imgl = customLayout.findViewById<ImageView>(R.id.addSignat_Img)
        signatureCapture = customLayout.findViewById(R.id.signature_capture)
        val submit_imgl = customLayout.findViewById<TextView>(R.id.submit_img_inv)
        val invNo_txt = customLayout.findViewById<TextView>(R.id.invNo_txt_edit)
        val close_btn_edit_invl = customLayout.findViewById<ImageView>(R.id.close_btn_pickdel)
        spinner_pickStatus = customLayout.findViewById<Spinner>(R.id.spinner_status_pickD)

        val mSig = CaptureSignatureView(this@ScannedinvoiceListActivity, null)
        // mContent.addView(mSig, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        invNo_txt.text = pickModel.invNumber

        uploadImgDialog_txt!!.setOnClickListener {
            if (uploadImgDialog_txt!!.getTag() == "view_image") {
                showImage()
            } else {
                selectImage()
            }
        }

        if (mPhotoFile != null && mPhotoFile!!.length() > 0) {
            uploadImgDialog_txt!!.setText("View Image")
            uploadImgDialog_txt!!.setTag("view_image")
        } else {
            uploadImgDialog_txt!!.setText("Select Image")
            uploadImgDialog_txt!!.setTag("select_image")
        }

        val status = arrayOf("Picked", "Not Picked")

        val langAdapter = ArrayAdapter<CharSequence>(this, R.layout.cust_spinner_item, status)
        langAdapter.setDropDownViewResource(R.layout.item_grouplist_spinner)
        spinner_pickStatus!!.setAdapter(langAdapter)

//        uploadImgDialogLay!!.setOnClickListener(OnClickListener {
//            if (uploadImgDialog_txt!!.getTag() == "view_image") {
//                showImage()
//            } else {
//                selectImage()
//            }
//        })
        addSignat_Imgl!!.setOnClickListener {
            showSignatureAlert(signatureCapture!!)
        }

        close_btn_edit_invl.setOnClickListener {
            imageString = ""
            signatureString = ""
            mPhotoFile = null
            alertUpload!!.dismiss()
        }

        submit_imgl.setOnClickListener {

            if(signatureString.isNotEmpty()){
                spinnertxt_dialog = "C"
                packStatusStr = "Delivered"

                val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                val currentDateandTime = sdf.format(Date())
                currentSaveDateTime = currentDateandTime

                try {
                    val obj = JSONObject()
                    obj.put("invoiceNumber", pickModel.code)
                    obj.put("currentDateTime", currentSaveDateTime)
                    obj.put("customerCode", pickModel.customerCode)
                    obj.put("Username", username)
                    obj.put("status", spinnertxt_dialog)
                    obj.put("PackStatus", packStatusStr)
                    obj.put("latitude", current_latitude)
                    obj.put("longitude", current_longitude)
                    obj.put("CurrentAddress", current_addr)
                    obj.put("image", imageString)
                    obj.put("signature", signatureString)

                    savePicklistDeliveryApi(obj)
                } catch (e: JSONException) {
                    throw RuntimeException(e)
                }
            }else{
                Toast.makeText(applicationContext,  "Signature Should not be empty!", Toast.LENGTH_SHORT).show()

//                if (spinner_pickStatus!!.selectedItem.equals("Picked")) {
//                    spinnertxt_dialog = "OC"
//                } else  {
//                    spinnertxt_dialog = "O"
//                }
                // //  spinnertxt_dialog = "OC"
            }
//            {"invoiceNumber":"18","currentDateTime":"20250616_171118","customerCode":"0005","Username":"ST01",
//            "status":"C",
//                "latitude":"10.96440894","longitude":"78.44143506","image":"","signature":""}

        }
        alertUpload = alertDialog.create()
        alertUpload!!.setCanceledOnTouchOutside(false)
        alertUpload!!.show()
    }
    @SuppressLint("MissingInflatedId")
    fun showViewImageAlert(pickModel: PickIistDeliveryListingModel) {
        val alertDialog = AlertDialog.Builder(this@ScannedinvoiceListActivity)
        val customLayout: View = layoutInflater.inflate(R.layout.pick_view_image_dialog, null)
        alertDialog.setView(customLayout)
        var view_imgl = customLayout.findViewById<ImageView>(R.id.view_pick_img)
        var view_signaturel = customLayout.findViewById<ImageView>(R.id.view_pick_signature)

        val view_img_ok = customLayout.findViewById<Button>(R.id.view_pick_ok)
        val invNo_txt_view = customLayout.findViewById<TextView>(R.id.invNo_pick_view)
        invNo_txt_view.setText(pickModel.invNumber)

        view_img_ok.setOnClickListener {
            alertUploadView!!.dismiss()
        }

        Glide.with(this)
            .load(pickModel.imageUrl)
            .error(R.drawable.no_image_found)
            .into(view_imgl)

        Glide.with(this)
            .load(pickModel.signatureUrl)
            .error(R.drawable.no_image_found)
            .into(view_signaturel)

        alertUploadView = alertDialog.create()
        alertUploadView!!.setCanceledOnTouchOutside(true)
        alertUploadView!!.show()
    }

    fun showImage() {
        val builder = AlertDialog.Builder(this@ScannedinvoiceListActivity)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.image_view_layout, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.invoice_image)
        Glide.with(this)
            .load(mPhotoFile)
            .error(R.drawable.no_image_found)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            }).into(imageView)
        builder.setCancelable(false)
        builder.setTitle("Invoice Image")
        builder.setView(dialogView)
        builder.setNeutralButton(
            "NEW IMAGE"
        ) { dialogInterface, i -> selectImage() }
        builder.setPositiveButton(
            "OK"
        ) { dialog, which ->
            uploadImgDialog_txt!!.setTag("view_image")
            uploadImgDialog_txt!!.setText("View Image")
            dialog.dismiss()
        }.create().show()
    }

    fun selectImage() {
        val items = arrayOf<CharSequence>(
            "Take Photo",  /* "Choose from Library",*/
            "Cancel"
        )
        val builder = AlertDialog.Builder(this@ScannedinvoiceListActivity)
        builder.setItems(
            items
        ) { dialog: DialogInterface, item: Int ->
            if (items[item] == "Take Photo") {
                requestStoragePermission(true)
            } //else if (items[item].equals("Choose from Library")) {
            else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }
    fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(contentUri!!, proj, null, null, null)
            assert(cursor != null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }
    private fun requestStoragePermission(isCamera: Boolean) {
        var permission = arrayOf<String?>(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA
            )
        }
        Dexter.withContext(this)
            .withPermissions(*permission)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        if (isCamera) {
                            dispatchTakePictureIntent()
                        } else {
                            dispatchGalleryIntent()
                        }
                    }
                    for (i in report.deniedPermissionResponses.indices) {
                        Log.d(
                            "cg_perm", report.deniedPermissionResponses[i].permissionName
                        )
                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // show alert dialog navigating to Settings
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .withErrorListener { error: DexterError? ->
                Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT)
                    .show()
            }
            .onSameThread()
            .check()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile
                )
                mPhotoFile = photoFile
                Log.w("uploadImgpic",""+mPhotoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    /**
     * Select image fro gallery
     */
    private fun dispatchGalleryIntent() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO)
    }
    //    private fun showPopupMenu( position: Int) {
//        val popup = PopupMenu(this, position, Gravity.END)
//        val inflater = popup.menuInflater
//        inflater.inflate(R.menu.three_dot_pick_menu, popup.menu)
//
//        //set menu item click listener here
//        popup.setOnMenuItemClickListener(MyMenuItemClickListener(position))
//        popup.show()
//    }
    private fun showPopupMenu(pickModel: PickIistDeliveryListingModel, view: View) {
//        val menuItemView = findViewById<View>(R.id.fab)
        val popupMenu = PopupMenu(this@ScannedinvoiceListActivity, view)
        popupMenu.menuInflater.inflate(R.menu.three_dot_pick_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.upload_pick_menu -> {
                    showUploadImageAlert(pickModel)
                    true
                } R.id.viewImg_pick_menu -> {
                showViewImageAlert(pickModel)
                true
            }
                else -> false
            }
        }
        popupMenu.show()
    }
    fun savePicklistDeliveryApi(jsonBody: JSONObject) {
        try {
            pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog!!.setCancelable(false)
            val requestQueue = Volley.newRequestQueue(this)
            Log.w("picklDel_request:", jsonBody.toString())
            var URL = ""
            URL = Constants.BASEURL + "PostingSignImageInvoice"
            Log.w("url_picklDel_save:", URL)
            pDialog!!.setTitleText("Saving Picklist...")
            pDialog!!.show()

            val salesOrderRequest: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, URL, jsonBody,
                Response.Listener { response: JSONObject ->
                    Log.w("picklis_del_sav:", response.toString())
                    pDialog!!.dismiss()
                    val statusCode = response.optString("statusCode")
                    val message = response.optString("statusMessage")

                    var responseData: JSONObject? = null
                    responseData = response.optJSONObject("responseData")
                    if (statusCode == "1") {
                        //  val docNum = responseData.optString("docNum")
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                        val intent = Intent(applicationContext, ScannedinvoiceListActivity::class.java)
                        startActivity(intent)
                        finish()
                        alertUpload!!.dismiss()
                        imageString = ""
                        signatureString = ""
//                        if (StockTakeAddActivity.isPrintEnable) {
//                            intent.putExtra("docNum", docNum)
//                        }
                    } else {
                        /* Intent intent=new Intent(getApplicationContext(),TransferListProductActivity.class);
                      if (isPrintEnable) {
                          intent.putExtra("docNum","22010004");
                          intent.putExtra("transferType",transferType);
                      }
                      startActivity(intent);
                      finish();*/
                        if (responseData != null) {
                            Toast.makeText(
                                applicationContext,
                                responseData.optString("error"),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Error in Saving Data...",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                Response.ErrorListener { error: VolleyError ->
                    Log.w("SalesOrder_Response:", error.toString())
                    pDialog!!.dismiss()
                }) {
                /* @Override
                 public byte[] getBody() {
                     return jsonBody.toString().getBytes();
                 }*/
                override fun getBodyContentType(): String {
                    return "application/json"
                }

                override fun getHeaders(): Map<String, String> {
                    val params = HashMap<String, String>()
                    val creds = String.format(
                        "%s:%s",
                        Constants.API_SECRET_CODE,
                        Constants.API_SECRET_PASSWORD
                    )
                    val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.DEFAULT)
                    params["Authorization"] = auth
                    return params
                }
            }
            salesOrderRequest.setRetryPolicy(object : RetryPolicy {
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
            requestQueue.add(salesOrderRequest)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.sorting_menu, menu)

        val addInvoice = menu.findItem(R.id.action_add)
        addInvoice.setVisible(false)
        val filter = menu.findItem(R.id.action_filter)
        filter.setVisible(true)
//        if (NewInvoiceListActivity.visibleFragment == "invoices") {
//            filter.setVisible(true)
//            //filter.setVisible(false);
//        } else {
//            filter.setVisible(false)
//        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) { //finish();
            onBackPressed()

            /*   case R.id.action_remove:
                showRemoveAlert();
                break;*/
        } else if (item.itemId == R.id.action_filter) {
            if (searchFilterView!!.visibility == View.VISIBLE) {
                searchFilterView!!.visibility = View.GONE
                custFilterAutol!!.setText("")
                selectCustomerCode = ""
                isSearchCustomerNameClicked = false
//                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
//                }
                //slideUp(searchFilterView);
            } else {
                custFilterAutol!!.setText("")
                selectCustomerCode = ""
                isSearchCustomerNameClicked = false
                searchFilterView!!.visibility = View.VISIBLE
//                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
//                }
                // slideDown(searchFilterView);
            }
        }
        return true
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need Permissions")
        builder.setMessage(
            "This app needs permission to use this feature. You can grant them in app settings."
        )
        builder.setPositiveButton("GOTO SETTINGS") { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        builder.show()
    }
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivityForResult(intent, 101)
    }
    fun showSignatureAlert(signatureCaptu:ImageView) {
        val alertDialog = AlertDialog.Builder(this)
        val customLayout = layoutInflater.inflate(R.layout.signature_layout, null)
        alertDialog.setView(customLayout)
        val acceptButton = customLayout.findViewById<Button>(R.id.buttonYes)
        val cancelButton = customLayout.findViewById<Button>(R.id.buttonNo)
        val clearButton = customLayout.findViewById<Button>(R.id.buttonClear)

        val mContent = customLayout.findViewById<LinearLayout>(R.id.signature_layout)
        acceptButton.setEnabled(false)
        acceptButton.setAlpha(0.4f)
        val mSig = CaptureSignatureView(this@ScannedinvoiceListActivity, null) {
            acceptButton.setEnabled(true)
            acceptButton.setAlpha(1f)
        }
        mContent.addView(
            mSig,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        acceptButton.setOnClickListener { // byte[] signature = captureSignatureView.getBytes();
            val signature = mSig.getBitmap()
            signatureCaptu!!.setImageBitmap(signature)
            signatureString = ImageUtil.convertBimaptoBase64(signature)
            //  Utils.setSignature(signatureString)
            alert!!.dismiss()
            Log.w("SignatureString:", signatureString)
        }
        cancelButton.setOnClickListener { alert!!.dismiss() }
        clearButton.setOnClickListener { mSig.ClearCanvas() }
        alert = alertDialog.create()
        alert!!.setCanceledOnTouchOutside(false)
        alert!!.show()
    }
    override fun pickListUploadSelected(pickModel: PickIistDeliveryListingModel, view: View) {
        // showUploadImageAlert(pickModel)
        showPopupMenu(pickModel,view)
    }
    fun getCurrentLocation() {
        locationTrack = LocationTrack(this@ScannedinvoiceListActivity)
        if (locationTrack!!.canGetLocation()) {
            val longitude: Double = locationTrack!!.getLongitude()
            val latitude: Double = locationTrack!!.getLatitude()
            current_latitude = latitude.toString()
            current_longitude = longitude.toString()
            val currentAddress = Utils.getCompleteAddress(this@ScannedinvoiceListActivity, latitude, longitude)
            if (currentAddress != null && !currentAddress.isEmpty()) {
                //  locationText.setText(currentAddress)
                current_addr = currentAddress
            }
            Log.w("latlongpickD",""+current_latitude)

        } else {
            //   locationTrack!!.showSettingsAlert();
        }
    }
    //    fun setCurrentLocation(latitude: Double, longitude: Double) {
//        currentLocationLatitude = latitude
//        currentLocationLongitude = longitude
//        val currentAddress = Utils.getCompleteAddress(this@NewDeliveryPickListActivity, latitude, longitude)
//        if (currentAddress != null && !currentAddress.isEmpty()) {
//          //  locationText.setText(currentAddress)
//        }
//        Log.w("latlongpickD11",""+current_latitude)
//    }
    override fun onDestroy() {
        super.onDestroy()
        if (locationTrack != null) {
            locationTrack!!.stopListener()
        }
    }
}
