package com.winapp.deliverytrackingapp.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import com.winapp.deliverytrackingapp.ui.adapter.PickDeliveryPrintPreviewAdapter
import com.winapp.deliverytrackingapp.ui.model.PicklistDeliveryPrintPreviewModel
import com.winapp.deliverytrackingapp.ui.utils.CaptureSignatureView
import com.winapp.deliverytrackingapp.ui.utils.Constants
import com.winapp.deliverytrackingapp.ui.utils.FileCompressor
import com.winapp.deliverytrackingapp.ui.utils.ImageUtil
import com.winapp.deliverytrackingapp.ui.utils.LocationTrack
import com.winapp.deliverytrackingapp.ui.utils.SessionManager
import com.winapp.deliverytrackingapp.ui.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.winapp.KHDelivery.model.PickIistDeliveryListingModel
import com.winapp.deliverytrackingapp.BuildConfig
import com.winapp.deliverytrackingapp.R
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects


class PickListDeliveryPrintPreviewActivity : AppCompatActivity() {
    private var companyId: String? = null
    private var locationCode: String? = null
    private var pDialog: SweetAlertDialog? = null
    private var invoiceNumber: String? = null
    private var custCode: String? = null
    private var session: SessionManager? = null
    private var user: HashMap<String, String>? = null
    private var invoiceHeaderDetails: ArrayList<PicklistDeliveryPrintPreviewModel>? = null
    private var invoiceList: ArrayList<PicklistDeliveryPrintPreviewModel.InvoiceList>? = null
    private var invoiceListView: RecyclerView? = null
    private var adapter: PickDeliveryPrintPreviewAdapter? = null
    private var invoiceNumberText: TextView? = null
    private var invoiceDateText: TextView? = null
    private var customerCodetext: TextView? = null
    private var customerNameText: TextView? = null
    private val addressText: TextView? = null
    private var billAddressText: TextView? = null
    private var shipAddressText: TextView? = null
    //    private var linetxt: TextView? = null
    private var phoneNo_previewl: TextView? = null
    private var deliveryAddr_print_txtl: TextView? = null
    private var deliveryAddr_print_layl: LinearLayout? = null
    private var companyNametext: TextView? = null
    private var companyAddress1Text: TextView? = null
    private var companyAddress2Text: TextView? = null
    private var companyAddress3Text: TextView? = null
    private var companyPhoneText: TextView? = null
    private var companyGstText: TextView? = null
    private var userTxt: TextView? = null
    private var dateTimeTxt: TextView? = null
    private var company_name: String? = null
    private var company_address1: String? = null
    private var company_address2: String? = null
    private var company_address3: String? = null
    private var rootLayout: RelativeLayout? = null
    private var addressLayout: LinearLayout? = null
    var sharedPreferences: SharedPreferences? = null
    var printerMacId: String? = null
    var printerType: String? = null
    var alert11: AlertDialog? = null
    var alertSave: AlertDialog? = null
    var alertInterface: DialogInterface? = null
    var boolean_permission = false
    var boolean_save = false
    var bitmap: Bitmap? = null
    var progressDialog: ProgressDialog? = null
    var pageNumber = 0
    var pdfFileName: String? = null
    var shareLayout: LinearLayout? = null
    var pdfFile: File? = null
//    var printLayout: LinearLayout? = null
    var cancelButton: Button? = null
    var outstanding_amount: String? = "0.0"
    var delDateStr: String? = ""
    var delStatusStr: String? = ""
    lateinit var pickModel: PickIistDeliveryListingModel
    var switchPickStr = ""
    var packStatusStr = ""
    var uploadImgDialog_txt: TextView? = null
    var uploadImgDialogLay: LinearLayout? = null
    var addSignat_Imgl: ImageView? = null
    var alertUpload: AlertDialog? = null
    var alert: AlertDialog? = null

    var signatureString = ""
    var spinnertxt_dialog: String? = "";
    var mPhotoFile: File? = null
    val REQUEST_TAKE_PHOTO = 1
    val REQUEST_GALLERY_PHOTO = 2
    var imageString: String? = ""
    var mCompressor: FileCompressor? = null
    var signatureCapture: ImageView? = null
    var remark_picklistl: EditText? = null
    private var spinner_pickStatus: Spinner? = null
    var alertUploadView: AlertDialog? = null
//    var address1Layout: LinearLayout? = null
    var address2Layout: LinearLayout? = null
    var address3Layout: LinearLayout? = null
    var address4Layout: LinearLayout? = null
    var customerAddress1: TextView? = null
    var customerAddress2: TextView? = null
    var customerAddress3: TextView? = null
    var customerAddress4: TextView? = null
    var company_phone: String? = null
    var company_gst: String? = null
    var username: String? = null
    var currentSaveDateTime: String? = ""
    var current_latitude = "0.00"
    var current_longitude = "0.00"
    var current_addr = ""
    var locationTrack: LocationTrack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val builder = VmPolicy.Builder()
//        StrictMode.setVmPolicy(builder.build())
        setContentView(R.layout.activity_picklist_delivery_print_preview)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) // This initializes supportActionBar
        supportActionBar!!.title = "Delivery PickList"
//        setTitle()

        session = SessionManager(this)
        user = session!!.getUserDetails()
        companyId = user!!.get(SessionManager.KEY_COMPANY_CODE)
        locationCode = user!!.get(SessionManager.KEY_LOCATION_CODE)
        company_name = user!!.get(SessionManager.KEY_COMPANY_NAME)
        company_address1 = user!!.get(SessionManager.KEY_ADDRESS1)
        company_address2 = user!!.get(SessionManager.KEY_ADDRESS2)
        company_address3 = user!!.get(SessionManager.KEY_ADDRESS3)
        company_phone = user!!.get(SessionManager.KEY_PHONE_NO)
        company_gst = user!!.get(SessionManager.KEY_COMPANY_REG_NO)
        username = user!!.get(SessionManager.KEY_USER_NAME)
        mCompressor = FileCompressor(this)

        Log.w("activity_cg", javaClass.getSimpleName().toString())

        getCurrentLocation()
        invoiceListView = findViewById(R.id.invoiceList)
        invoiceNumberText = findViewById(R.id.sr_no)
        invoiceDateText = findViewById(R.id.sr_date)
        customerCodetext = findViewById(R.id.customer_code)
        customerNameText = findViewById(R.id.customer_name_value)
        billAddressText = findViewById(R.id.bill_addr_previewDe)
        shipAddressText = findViewById(R.id.ship_addr_previewDe)
        deliveryAddr_print_txtl = findViewById(R.id.deliveryAddr_print_txt)
        deliveryAddr_print_layl = findViewById(R.id.deliveryAddr_print_lay)
        companyNametext = findViewById(R.id.company_name)
        companyAddress1Text = findViewById(R.id.address1)
        companyAddress2Text = findViewById(R.id.address2)
        companyAddress3Text = findViewById(R.id.address3)
        companyGstText = findViewById(R.id.gst_no)
        companyPhoneText = findViewById(R.id.mobile_no)
        addressLayout = findViewById(R.id.adressLayout)
        rootLayout = findViewById(R.id.rootLayout)
        userTxt = findViewById(R.id.del_preview_user)
        dateTimeTxt = findViewById(R.id.del_preview_date)
        phoneNo_previewl = findViewById(R.id.phoneNo_previewDe)

        sharedPreferences = getSharedPreferences("PrinterPref", MODE_PRIVATE)
        printerType = sharedPreferences!!.getString("printer_type", "")
        printerMacId = sharedPreferences!!.getString("mac_address", "")

//        shareLayout = findViewById(R.id.share_layout)
//        printLayout = findViewById(R.id.print_layout)
//        cancelButton = findViewById(R.id.cancel)
       // address1Layout = findViewById(R.id.address1Layout)
        address2Layout = findViewById(R.id.address2Layout)
        address3Layout = findViewById(R.id.address3Layout)
        address4Layout = findViewById(R.id.address4Layout)
        customerAddress1 = findViewById(R.id.cus_address1)
        customerAddress2 = findViewById(R.id.cus_address2)
        customerAddress3 = findViewById(R.id.cus_address3)
        customerAddress4 = findViewById(R.id.cus_address4)
    //    pdfView = findViewById(R.id.pdfView)
        val bottomSheet = findViewById<View>(R.id.design_bottom_sheet)
        behavior = BottomSheetBehavior.from(bottomSheet)
        checkPermission()
        requestPermission()
        Log.w("Printer_Mac_Id:", printerMacId!!)
        Log.w("Printer_Type:", printerType!!)

        spinnertxt_dialog = ""
        packStatusStr = ""
        imageString = ""
        signatureString = ""

        userTxt!!.setText(username)
        if (intent != null) {
            invoiceNumber = intent.getStringExtra("salesCodeDel")
            custCode = intent.getStringExtra("custCodePickDel")
            outstanding_amount = intent.getStringExtra("outstandingAmount")
            delDateStr = intent.getStringExtra("pick_DatetimeDel")
            delStatusStr = intent.getStringExtra("pick_statusDel")
//            pickModel = intent.getSerializableExtra("pick_model_Del") as PickIistDeliveryListingModel
            //  pickModel = intent.getParcelableExtra<PickIistDeliveryListingModel>("pick_model_Del")!!

            Log.w("delDateStr1:", delDateStr!!)

            if (invoiceNumber != null) {
                try {
                    getInvoiceDetails(invoiceNumber!!)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            if (!delDateStr!!.isEmpty()) {
                dateFormatHour(delDateStr)
            }
        }
//        printLayout!!.setOnClickListener(View.OnClickListener {
//            if (behavior!!.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
//                behavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
//            } else {
//                behavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
//            }
//            printPreview()
//        })
//        cancelButton!!.setOnClickListener(View.OnClickListener {
//            if (behavior!!.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
//                behavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
//            } else {
//                behavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
//            }
//        })
    }

    private fun dateFormatHour(dateStr: String?) {
        val readFormat: DateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        val writeFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss a")
        var date: Date? = null
        try {
            date = readFormat.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var formattedDate = ""
        if (date != null) {
            formattedDate = writeFormat.format(date)
        }
        dateTimeTxt!!.text = formattedDate
        Log.w("pickdel_date", "" + formattedDate)
    }

    @Throws(JSONException::class)
    private fun getInvoiceDetails(invoiceNumber: String) {
        // Initialize a new RequestQueue instance
        val jsonObject = JSONObject()
        // jsonObject.put("CompanyCode", companyId);
        jsonObject.put("InvoiceNo", invoiceNumber)
        jsonObject.put("LocationCode", locationCode)
        val requestQueue = Volley.newRequestQueue(this)
        val url = Constants.BASEURL + "InvoiceDetails"
        // Initialize a new JsonArrayRequest instance
        Log.w("Given_url:", url + jsonObject.toString())
        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog!!.setTitleText("Generating Print Preview...")
        pDialog!!.setCancelable(false)
        pDialog!!.show()
        invoiceHeaderDetails = ArrayList()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url,
            jsonObject, Response.Listener { response: JSONObject ->
                try {
                    Log.w("picklist_deli_res:", response.toString())

                    //  Log.w("DetailsResponse::", response.toString());
                    val statusCode = response.optString("statusCode")
                    if (statusCode == "1") {
                        val responseData = response.getJSONArray("responseData")
                        val `object` = responseData.optJSONObject(0)
                        //
                        val model = PicklistDeliveryPrintPreviewModel()
                        model.invoiceNumber = `object`.optString("invoiceNumber")
                        model.invoiceDate = `object`.optString("invoiceDate")
                        model.customerCode = `object`.optString("customerCode")
                        model.customerName = `object`.optString("customerName")
                        model.address = (`object`.optString("address1") +
                                `object`.optString("address2") + `object`.optString("address3")
                                + `object`.optString("street") + `object`.optString("countryName") + `object`.optString(
                            "zipcode"
                        ))
                        if(`object`.optString("shipTo") !=null && `object`.optString("shipTo").isNotEmpty()){
                            model.deliveryAddress = `object`.optString("shipTo")
                        } else {
                            model.deliveryAddress =
                                `object`.optString("shippingAddress") + `object`.optString("shipAddress2") + `object`.optString(
                                    "shipAddress3") +
                                        `object`.optString("shipStreet") + `object`.optString("shipCountry") + `object`.optString(
                                    "shipZipCode")
                        }
                        model.fromShipZipcode = `object`.optString("zipcode")
                        model.toShipZipcode = `object`.optString("shipZipCode")
                        val delieryAddr =
                            `object`.optString("shipAddress2") + `object`.optString("shipAddress3") +
                                    `object`.optString("shipStreet")
                        model.subTotal = `object`.optString("subTotal")
                        model.netTax = `object`.optString("taxTotal")
                        model.netTotal = `object`.optString("netTotal")
                        model.taxType = `object`.optString("taxType")
                        model.taxValue = `object`.optString("taxPerc")
                        model.outStandingAmount = `object`.optString("totalOutstandingAmount")
                        model.paymentTerm = `object`.optString("paymentTerm")
                        model.balanceAmount = `object`.optString("balanceAmount")
                        Utils.setInvoiceOutstandingAmount(`object`.optString("balanceAmount"))
                        Utils.setInvoiceMode("Invoice")
                        model.billDiscount = `object`.optString("billDiscount")
                        model.itemDiscount = `object`.optString("totalDiscount")
                        model.address1 = `object`.optString("address1")
                        model.address2 = `object`.optString("address2")
                        model.address3 = `object`.optString("address3")
//                        model.allowDeliveryAddress = `object`.optString("showShippingAddress")
//                        if (`object`.optString("showShippingAddress")
//                                .equals("Yes", ignoreCase = true)
//                        ) {
//                            deliveryAddr_print_layl!!.visibility = View.VISIBLE
//                            deliveryAddr_print_txtl!!.text =
//                                `object`.optString("shipAddress1") + `object`.optString("shipAddress2") + `object`.optString(
//                                    "shipAddress3"
//                                ) + `object`.optString("shipStreet")
//                        } else {
//                            deliveryAddr_print_layl!!.visibility = View.GONE
//                        }
                        model.addressstate = `object`.optString("street") + " " +
                                `object`.optString("block") + " " + `object`.optString("city")
                        model.addresssZipcode =
                            (`object`.optString("countryName") + " " + `object`.optString("state") + " "
                                    + `object`.optString("zipcode"))
                        model.soNumber = `object`.optString("soNumber")
                        model.soDate = `object`.optString("soDate")
                        model.doDate = `object`.optString("doDate")
                        model.doNumber = `object`.optString("doNumber")
                        model.phoneNo = `object`.optString("phoneNo")
                        val signFlag = `object`.optString("signFlag")
                        if (signFlag == "Y") {
                            val signature = `object`.optString("signature")
                            // Utils.setSignature(signature)
                            // createSignature()
                        } else {
                            // Utils.setSignature("")
                        }
                        var lineNo = ""
                      //  val lineArray = `object`.optJSONArray("invTextTypeDetails")
//                        for (i in 0 until lineArray.length()) {
//                            val lineobj = lineArray.optJSONObject(i)
//                            val invoiceListModel = PicklistDeliveryPrintPreviewModel()
//                            lineNo = lineobj.optString("lineText")
//                            // invoiceListModel.setLineNo(lineobj.optString("lineText"));
//                        }
                        val detailsArray = `object`.optJSONArray("invoiceDetails")
                        invoiceList = ArrayList()
                        for (i in 0 until detailsArray.length()) {
                            val detailObject = detailsArray.optJSONObject(i)
                            if (detailObject.optString("quantity").toDouble() > 0) {
                                val invoiceListModel =
                                    PicklistDeliveryPrintPreviewModel.InvoiceList()
                                invoiceListModel.productCode = detailObject.optString("productCode")
                                invoiceListModel.description = detailObject.optString("productName")
                                invoiceListModel.lqty = detailObject.optString("unitQty")
                                invoiceListModel.cqty = detailObject.optString("cartonQty")
                                invoiceListModel.netQty = detailObject.optString("quantity")
                                invoiceListModel.excQty = detailObject.optString("exc_Qty")
                                invoiceListModel.netQuantity = detailObject.optString("netQuantity")
                                invoiceListModel.focQty = detailObject.optString("foc_Qty")
                                invoiceListModel.uomCode = detailObject.optString("uoMName")
                                invoiceListModel.saleType = ""
                                if (detailObject.optString("bP_CatalogNo") != null) {
                                    invoiceListModel.customerItemCode =
                                        detailObject.optString("bP_CatalogNo")
                                }
                                invoiceListModel.returnQty = detailObject.optString("returnQty")
                                invoiceListModel.cartonPrice = detailObject.optString("cartonPrice")
                                invoiceListModel.unitPrice = detailObject.optString("price")
                                val qty = detailObject.optString("quantity").toDouble()
                                val price = detailObject.optString("price").toDouble()
                                val nettotal = qty * price
                                invoiceListModel.total = nettotal.toString()
                                invoiceListModel.pricevalue = price.toString()
                                invoiceListModel.pcsperCarton =
                                    detailObject.optString("pcsPerCarton")
                                invoiceListModel.itemtax = detailObject.optString("totalTax")
                                invoiceListModel.subTotal = detailObject.optString("subTotal")
                                invoiceList!!.add(invoiceListModel)
                                Log.w("invoicSizeEntr1", "")
                            }
                        }
                        Log.w("invoicSize", "" + invoiceList!!.size)
                        model.lineNo = lineNo
                        model.invoiceList = invoiceList
                        invoiceHeaderDetails!!.add(model)
                        if (invoiceList!!.size > 0) {
                            setInvoiceAdapter()
                        }
                        pDialog!!.dismiss()
                    } else {
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error: VolleyError ->
                // Do something when error occurred
                pDialog!!.dismiss()
                Log.w("Error_throwing:", error.toString())
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
        jsonObjectRequest.setRetryPolicy(object : RetryPolicy {
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
        requestQueue.add(jsonObjectRequest)
    }

//    private fun createSignature() {
//        if (Utils.getSignature() != null && !Utils.getSignature().isEmpty()) {
//            try {
//                ImageUtil.saveStamp(this, Utils.getSignature(), "Signature")
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//    }

    fun setInvoiceAdapter() {
        for (model in invoiceHeaderDetails!!) {
            invoiceNumberText!!.text = model.invoiceNumber
            invoiceDateText!!.text = model.invoiceDate
            customerCodetext!!.text = model.customerCode
            customerNameText!!.text = model.customerName
            billAddressText!!.text = model.address
            shipAddressText!!.text = model.deliveryAddress
            //   linetxt!!.text = model.lineNo
            phoneNo_previewl!!.text = model.phoneNo
            Log.w("shiaddreee1detat",""+model.deliveryAddress)
//            if (!model.getAddress1().isEmpty()){
//                address1Layout.setVisibility(View.VISIBLE);
//                customerAddress1.setText(model.getAddress1());
//            }
//            if (!model.getAddress2().isEmpty()){
//                address2Layout.setVisibility(View.VISIBLE);
//                customerAddress2.setText(model.getAddress2());
//            }
//            if (!model.getAddress3().isEmpty()){
//                address3Layout.setVisibility(View.VISIBLE);
//                customerAddress3.setText(model.getAddress3());
//            }
//            if (!model.getAddressstate().isEmpty() ) {
//                if (!model.getAddresssZipcode().isEmpty()) {
//                    address4Layout.setVisibility(View.VISIBLE);
//                    customerAddress4.setText(model.getAddressstate() + " " + model.getAddresssZipcode());
//                } else {
//                    address4Layout.setVisibility(View.VISIBLE);
//                    customerAddress4.setText(model.getAddressstate());
//                }
//            }
//            else{
//                    if (!model.getAddresssZipcode().isEmpty() ) {
//                        address4Layout.setVisibility(View.VISIBLE);
//                        customerAddress4.setText(model.getAddresssZipcode());
//                    }
//                }
//        }
//        companyNametext.setText(company_name);
//
//        if (!company_address1.isEmpty()){
//            companyAddress1Text.setVisibility(View.VISIBLE);
//            companyAddress1Text.setText(company_address1);
//        }
//
//        if (!company_address2.isEmpty()){
//            companyAddress2Text.setVisibility(View.VISIBLE);
//            companyAddress2Text.setText(company_address2);
//        }
//
//        if (!company_address3.isEmpty()){
//            companyAddress3Text.setVisibility(View.VISIBLE);
//            companyAddress3Text.setText(company_address3);
//        }
//
//        if (!company_phone.isEmpty()){
//            companyPhoneText.setText("TEL : "+company_phone);
//            companyPhoneText.setVisibility(View.VISIBLE);
//        }
//
//        if (!company_gst.isEmpty()){
//            companyGstText.setText("CO REG NO : "+company_gst);
//            companyGstText.setVisibility(View.VISIBLE);
//        }
            invoiceListView!!.setHasFixedSize(true)
            // RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            invoiceListView!!.layoutManager = LinearLayoutManager(
                this@PickListDeliveryPrintPreviewActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = PickDeliveryPrintPreviewAdapter(
                this@PickListDeliveryPrintPreviewActivity,
                invoiceList
            )
            invoiceListView!!.adapter = adapter
            rootLayout!!.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.picklist_print_menu, menu)
        val action_print = menu.findItem(R.id.action_print_picklist)
        val menuItem = menu.findItem(R.id.switch_btn_menu)
        val viewImg = menu.findItem(R.id.viewImg_pick_menu)
        val uploadItem = menu.findItem(R.id.upload_pick_menu)
        val saveItem = menu.findItem(R.id.save_pick_menu)
        val mapItem = menu.findItem(R.id.map_pick_menu)

        action_print.setVisible(false)

        val switchPicklist = menuItem.actionView as SwitchCompat?
        switchPicklist!!.text = "   Status : "

        if(delStatusStr.equals("C",true)){
            menuItem.setVisible(false)
        }else{
            menuItem.setVisible(true)
        }
        saveItem.setOnMenuItemClickListener {
            showCompletedAlert()
            true
        }
        mapItem.setOnMenuItemClickListener {
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
                    true
                }
        mapItem.setOnMenuItemClickListener {
            val fromZipcode  = invoiceHeaderDetails?.firstOrNull()?.fromShipZipcode
            val toZipcode = invoiceHeaderDetails?.firstOrNull()?.toShipZipcode

            if (toZipcode != null) {
                val intent = Intent(applicationContext, MapsActivity::class.java).apply {
                    putExtra("from_zipcode", fromZipcode)
                    putExtra("to_zipcode", toZipcode)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Destination zipcode not available", Toast.LENGTH_SHORT).show()
            }
            true
        }
        viewImg.setOnMenuItemClickListener {
            showViewImageAlert(pickModel) // pickModel not enable check
            true
        }
        uploadItem.setOnMenuItemClickListener {
            //  showUploadImageAlert(pickModel)
            showUploadImageAlert()
            true
        }

        switchColor1(switchPicklist,false)

        switchPicklist.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                switchPickStr =  "OC"
                packStatusStr =  "Picked"
                switchColor(switchPicklist,isChecked)
//                  switchPicklist!!.setBackgroundColor(Color.parseColor("#AC655C"));
                showSaveAlert(switchPicklist,switchPickStr , packStatusStr, "")
            } else {
                switchColor1(switchPicklist,isChecked)
                //   switchPicklist!!.setBackgroundColor(Color.parseColor("#F95B24"));
                switchPickStr = "O"
                packStatusStr =  "Pending"
            }
        }
        return true
    }

    private fun showPopupMenu(view: View) {
      //  val menuItemView = findViewById<View>(R.id.fab)
        val popupMenu = PopupMenu(this@PickListDeliveryPrintPreviewActivity, view)
        popupMenu.menuInflater.inflate(R.menu.three_dot_pick_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.upload_pick_menu -> {
                    //  showUploadImageAlert(pickModel)
                    showUploadImageAlert()
                    true
                } R.id.viewImg_pick_menu -> {
                //  showViewImageAlert()
                true
            }
                else -> false
            }
        }
        popupMenu.show()
    }

    fun showUploadImageAlert() {
        val alertDialog = AlertDialog.Builder(this@PickListDeliveryPrintPreviewActivity)
        val customLayout: View = layoutInflater.inflate(R.layout.pick_image_upload_dialog, null)
        alertDialog.setView(customLayout)
        uploadImgDialogLay = customLayout.findViewById<LinearLayout>(R.id.attachement_layout_inv)
        uploadImgDialog_txt = customLayout.findViewById<TextView>(R.id.select_Img_pickdel)
        addSignat_Imgl = customLayout.findViewById<ImageView>(R.id.addSignat_Img)
        signatureCapture = customLayout.findViewById(R.id.signature_capture)
        remark_picklistl = customLayout.findViewById(R.id.remark_picklist)
        val submit_imgl = customLayout.findViewById<TextView>(R.id.submit_img_inv)
        val invNo_txt = customLayout.findViewById<TextView>(R.id.invNo_txt_edit)
        val close_btn_edit_invl = customLayout.findViewById<ImageView>(R.id.close_btn_pickdel)
        spinner_pickStatus = customLayout.findViewById<Spinner>(R.id.spinner_status_pickD)

        val mSig = CaptureSignatureView(this@PickListDeliveryPrintPreviewActivity, null)
        // mContent.addView(mSig, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        invNo_txt.text = invoiceNumber
//        Log.w("pickmodelaa:", pickModel.code!!)

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

            if(signatureString.isNotEmpty() || imageString!!.isNotEmpty()){
                spinnertxt_dialog = "OC"
                packStatusStr = "Picked" // todo

                val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                val currentDateandTime = sdf.format(Date())
                currentSaveDateTime = currentDateandTime

                var remarkStr = "";
                if(remark_picklistl!!.text.isNotEmpty()){
                    remarkStr = remark_picklistl!!.text.toString()
                }else{
                    remarkStr = "";
                }

                try {
                    val obj = JSONObject()
                    obj.put("invoiceNumber", invoiceNumber)
                    obj.put("currentDateTime", currentSaveDateTime)
                    obj.put("customerCode", custCode)
                    obj.put("Username", username)
                    obj.put("status", spinnertxt_dialog)
                    obj.put("PackStatus", packStatusStr)
                    obj.put("Remark", remarkStr)
                    obj.put("latitude", current_latitude)
                    obj.put("longitude", current_longitude)
                    obj.put("CurrentAddress", current_addr)
                    obj.put("SendMail", "")
                    obj.put("image", imageString)
                    obj.put("signature", signatureString)

                    Log.w("imgSign_","$obj")

                    savePicklistDeliveryApi(obj,null,"false")
                } catch (e: JSONException) {
                    throw RuntimeException(e)
                }
            }else{
                Toast.makeText(applicationContext,  "Choose any one of the option !", Toast.LENGTH_SHORT).show()

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
        val mSig = CaptureSignatureView(this@PickListDeliveryPrintPreviewActivity, null) {
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
    fun showImage() {
        val builder = AlertDialog.Builder(this@PickListDeliveryPrintPreviewActivity)
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
        val builder = AlertDialog.Builder(this@PickListDeliveryPrintPreviewActivity)
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
        }
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
                        Log.d("cg_perm", report.deniedPermissionResponses[i].permissionName)
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
    private fun switchColor(switchPicklist: SwitchCompat?,checked: Boolean) {
        switchPicklist!!.getThumbDrawable().setColorFilter(
            if (checked) Color.BLACK
            else Color.parseColor("#F95B24"),
            PorterDuff.Mode.MULTIPLY)
        switchPicklist!!.getTrackDrawable().setColorFilter(
            if (!checked) Color.BLACK
            else Color.parseColor("#F95B24"),
            PorterDuff.Mode.MULTIPLY
        )
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
    private fun switchColor1(switchPicklist: SwitchCompat?,checked: Boolean) {
        switchPicklist!!.getThumbDrawable().setColorFilter(
            if (checked) Color.BLACK
            else Color.WHITE,
            PorterDuff.Mode.MULTIPLY)
        switchPicklist!!.getTrackDrawable().setColorFilter(
            if (!checked) Color.BLACK
            else Color.WHITE,
            PorterDuff.Mode.MULTIPLY
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_print_picklist) {
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth
                Toast.makeText(
                    applicationContext,
                    "This device does not support bluetooth",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!mBluetoothAdapter.isEnabled) {
                // Bluetooth is not enabled :)
                Toast.makeText(
                    applicationContext,
                    "Enable bluetooth and connect the printer",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Bluetooth is enabled
                if (!printerType!!.isEmpty()) {
                    //showPrintAlert();
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please configure Printer",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            return true
        } else if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun savePickListDelivery(switchPicklist: SwitchCompat?,status: String , pickStatus: String, mail: String){
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        currentSaveDateTime = currentDateandTime

        try {
            val obj = JSONObject()
            obj.put("invoiceNumber", invoiceNumber)
            obj.put("currentDateTime", currentSaveDateTime)
            obj.put("customerCode", custCode)
            obj.put("Username", username)
            obj.put("status", status)
            obj.put("PackStatus", pickStatus)
            obj.put("Remark", "")
            obj.put("latitude", current_latitude)
            obj.put("longitude", current_longitude)
            obj.put("CurrentAddress", current_addr)
            obj.put("SendMail", mail)
            obj.put("image", "")
            obj.put("signature", "")

            savePicklistDeliveryApi(obj,switchPicklist,"true")
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
    }

    @SuppressLint("MissingInflatedId")
    fun showViewImageAlert(pickModel: PickIistDeliveryListingModel) {
        val alertDialog = AlertDialog.Builder(this@PickListDeliveryPrintPreviewActivity)
        val customLayout: View = layoutInflater.inflate(R.layout.pick_view_image_dialog, null)
        alertDialog.setView(customLayout)
        var view_imgl = customLayout.findViewById<ImageView>(R.id.view_pick_img)
        var view_signaturel = customLayout.findViewById<ImageView>(R.id.view_pick_signature)

        val view_img_ok = customLayout.findViewById<Button>(R.id.view_pick_ok)
        val invNo_txt_view = customLayout.findViewById<TextView>(R.id.invNo_pick_view)
        invNo_txt_view.setText(invoiceNumber)

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


    fun savePicklistDeliveryApi(jsonBody: JSONObject ,switchPicklist: SwitchCompat?,alert: String) { // todo
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

                        val intent = Intent(applicationContext, NewDeliveryPickListActivity::class.java)
                        startActivity(intent)
                        finish()

                        if (alertUpload != null && alertUpload!!.isShowing) {
                            alertUpload!!.dismiss()
                            alertUpload = null
                        }
                        if(alert.equals("true")) {
                            alertSave!!.dismiss()
                        }
                        if(switchPicklist != null) {
                            switchPicklist.isChecked = false
                        }
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
                        if(alert.equals("true")) {
                            alertSave!!.dismiss()
                        }
                        if(switchPicklist != null) {
                            switchPicklist!!.isChecked = false
                        }
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
                    val params = java.util.HashMap<String, String>()
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
    fun showSaveAlert(switchPicklist: SwitchCompat?,status: String , pickStatus: String , mail: String) {
        val builder1 = AlertDialog.Builder(this@PickListDeliveryPrintPreviewActivity)
        builder1.setTitle("Are you sure want to save picklist?")
        // builder1.setMessage("Products and Customer Details will be erased.");
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "YES"
        ) { dialog, id -> dialog.cancel()
            savePickListDelivery(switchPicklist,status , pickStatus ,mail)
        }
        builder1.setNegativeButton(
            "NO"
        ) { dialog, id -> dialog.cancel()
            if(switchPicklist != null) {
                switchPicklist!!.isChecked = false
            }
            switchPickStr = ""}
        alertSave = builder1.create()
        alertSave!! .show()
    }

    fun showCompletedAlert() {
        val builder1 = AlertDialog.Builder(this@PickListDeliveryPrintPreviewActivity)
        builder1.setTitle("Are you sure update to completed status?")
        // builder1.setMessage("Products and Customer Details will be erased.");
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "YES"
        ) { dialog, id ->
            switchPickStr =  "C"
            packStatusStr =  "Delivered"
            savePickListDelivery(null,switchPickStr , packStatusStr,"Yes")
            dialog.cancel()
        }
        builder1.setNegativeButton(
            "NO"
        ) { dialog, id -> dialog.cancel()
            switchPickStr = ""
        }
        alertSave = builder1.create()
        alertSave!! .show()
    }
    fun getCurrentLocation() {
        locationTrack = LocationTrack(this@PickListDeliveryPrintPreviewActivity)
        if (locationTrack!!.canGetLocation()) {
            val longitude: Double = locationTrack!!.getLongitude()
            val latitude: Double = locationTrack!!.getLatitude()
            current_latitude = latitude.toString()
            current_longitude = longitude.toString()
            val currentAddress = Utils.getCompleteAddress(this@PickListDeliveryPrintPreviewActivity, latitude, longitude)
            if (currentAddress != null && !currentAddress.isEmpty()) {
                //  locationText.setText(currentAddress)
                current_addr = currentAddress
            }
            Log.w("latlongpickDPrev",""+current_latitude)

        } else {
            // locationTrack!!.showSettingsAlert();
        }
    }
    fun printPreview() {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(
                applicationContext,
                "This device does not support bluetooth",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!mBluetoothAdapter.isEnabled) {
            // Bluetooth is not enabled :)
            Toast.makeText(
                applicationContext,
                "Enable bluetooth and connect the printer",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // Bluetooth is enabled
            if (!printerType!!.isEmpty()) {
                //   showPrintAlert();
            } else {
                Toast.makeText(applicationContext, "Please configure Printer", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return if (result == PackageManager.PERMISSION_GRANTED) {
            boolean_permission = true
            true
        } else {
            boolean_permission = false
            false
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                this,
                "Write External Storage permission allows us to save files. Please allow this permission in App Settings.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            boolean_permission = true
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .")
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .")
            }
        }
    }

    fun closeAlert() {
        if (alertInterface != null) {
            alertInterface!!.dismiss()
        }
    }

    fun setTitle() {
        //Customize the ActionBar
        val abar = supportActionBar
        val viewActionBar = layoutInflater.inflate(R.layout.action_bar_title, null)
        val params = ActionBar.LayoutParams( //Center the textview in the ActionBar !
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
        val textviewTitle = viewActionBar.findViewById<TextView>(R.id.actionbar_textview)
        textviewTitle.text = "Delivery PickList"
        Objects.requireNonNull(abar)!!.setCustomView(viewActionBar, params)
        abar!!.setDisplayShowCustomEnabled(true)
        abar.setDisplayShowTitleEnabled(false)
        abar.setDisplayHomeAsUpEnabled(true)
        abar.setHomeButtonEnabled(true)
    }

    companion object {
        var REQUEST_PERMISSIONS = 154
        private val TAG = PickListDeliveryPrintPreviewActivity::class.java.getSimpleName()
        const val SAMPLE_FILE = "android_tutorial.pdf"
        var behavior: BottomSheetBehavior<*>? = null
        private const val PERMISSION_REQUEST_CODE = 100
    }
    override fun onDestroy() {
        super.onDestroy()
        if (locationTrack != null) {
            locationTrack!!.stopListener()
        }
    }
}