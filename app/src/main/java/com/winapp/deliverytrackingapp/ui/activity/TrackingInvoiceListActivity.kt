package com.winapp.deliverytrackingapp.ui.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.android.volley.DefaultRetryPolicy
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
import com.google.gson.Gson
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.winapp.deliverytrackingapp.BuildConfig
import com.winapp.deliverytrackingapp.R
import com.winapp.deliverytrackingapp.ui.adapter.TrackingInvoiceAdapter
import com.winapp.deliverytrackingapp.ui.model.TrackingAssignInvoice
import com.winapp.deliverytrackingapp.ui.model.TrackingAssignModel
import com.winapp.deliverytrackingapp.ui.model.TrackingInvoiceModel
import com.winapp.deliverytrackingapp.ui.utils.CaptureSignatureView
import com.winapp.deliverytrackingapp.ui.utils.CommonMethodKotl
import com.winapp.deliverytrackingapp.ui.utils.Constants
import com.winapp.deliverytrackingapp.ui.utils.FileCompressor
import com.winapp.deliverytrackingapp.ui.utils.ImageUtil
import com.winapp.deliverytrackingapp.ui.utils.LocationTrack
import com.winapp.deliverytrackingapp.ui.utils.SessionManager
import com.winapp.deliverytrackingapp.ui.utils.Utils
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

class TrackingInvoiceListActivity  : NavigationActivity() ,TrackingInvoiceAdapter.TrackingAssignClickListener{
    var scannedBarcode: String? = ""
    var rv_trackList: RecyclerView? = null
    var barcodeText: TextView? = null
    private var invoiceHeaderDetails: ArrayList<TrackingInvoiceModel>? = null
    private var invoiceTrackList: ArrayList<TrackingInvoiceModel.InvoiceList>? = null
    var locationCode: String? = null
    var userName: String? = null
    var emptytxt: TextView? = null
    var trackNotxt: TextView? = null
    var trackNoLay: LinearLayout? = null
    private var trackInvAdapter: TrackingInvoiceAdapter? = null
    var switchPickStr = ""
    var packStatusStr = ""
    var currentSaveDateTime: String? = ""
    var current_latitude = "0.00"
    var current_longitude = "0.00"
    var current_addr = ""
    var locationTrack: LocationTrack? = null
    var alertSave: AlertDialog? = null
    var invoiceNo : String? = ""
    var custCode : String? = ""
    private var pDialog: SweetAlertDialog? = null
    var uploadImgDialog_txt: TextView? = null
    var uploadImgDialogLay: LinearLayout? = null
    var addSignat_Imgl: ImageView? = null
    var signatureString = ""
    var mPhotoFile: File? = null
    val REQUEST_TAKE_PHOTO = 1
    val REQUEST_GALLERY_PHOTO = 2
    var imageString: String? = ""
    var mCompressor: FileCompressor? = null
    var signatureCapture: ImageView? = null
    var alertUploadView: AlertDialog? = null
    private var spinner_pickStatus: Spinner? = null
    var alertUpload: AlertDialog? = null
    var alert: AlertDialog? = null
    var spinnertxt_dialog: String? = "";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val contentFrameLayout = findViewById<FrameLayout>(R.id.content_frame)
        layoutInflater.inflate(R.layout.activity_tracking_invoice_list, contentFrameLayout)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Tracking Invoice"

        Log.w("activity_cg", javaClass.getSimpleName().toString())

        getCurrentLocation()
        locationCode = user1!![SessionManager.KEY_LOCATION_CODE]
        userName = user1!![SessionManager.KEY_USER_NAME]
        mCompressor = FileCompressor(this)

        rv_trackList = findViewById(R.id.rv_trackList)
        barcodeText = findViewById(R.id.barcodetxt)
        emptytxt = findViewById(R.id.empty_txt_track)
        trackNotxt = findViewById(R.id.trackingNo_txt)
        trackNoLay = findViewById(R.id.trackingNo_lay)

        spinnertxt_dialog = ""
        packStatusStr = ""
        imageString = ""
        signatureString = ""

      //  scanFromFragment()
    }
    fun scanFromFragment() {
        fragmentLauncher.launch(ScanOptions())
    }
    private val fragmentLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract()
    ) { result ->
        if (result.contents == null) {
            Toast.makeText(this@TrackingInvoiceListActivity, "No Product Found", Toast.LENGTH_LONG).show()
        } else {
          //  barCodeLayl!!.visibility = View.VISIBLE
            val barcodeTxt = result.contents
           barcodeText!!.setText(barcodeTxt)
            getInvoiceDetails(barcodeTxt)
//            val integrator = IntentIntegrator(this)
//            integrator.setPrompt("Scan a barcode")
//            integrator.setCameraId(0) // Use a specific camera of the device
//            integrator.setOrientationLocked(false)
//            integrator.setBeepEnabled(true)
//            integrator.initiateScan()

            // Log.w("BarcodeTextInv:", barcodeTxt!!)
            val mp = MediaPlayer.create(this, R.raw.beep) // sound is inside res/raw/mysound
            mp.start()
            scannedBarcode = barcodeTxt
           // searchAndSendActivity(barcodeTxt)
//            if (scanType == "number") {
//                scanBarTxt(result.contents)
//            } else if (scanType == "item") {
//                itemCodeEd!!.removeTextChangedListener(itemTextWatcher)
//
//                scanItemCodeTxt(result.contents)
//
//            } else if (scanType == "bin") {
//
//                binEd!!.setText(result.contents)
//            }
//            binEd!!.clearFocus()
//            itemCodeEd!!.clearFocus()
//            poNumberEd!!.clearFocus()

            Log.e("scan_barcode.. ", "${result.contents}")

//            Toast.makeText(this@PoScanAddNewActivity, "Scanned from fragment: " + result.getContents(),
//                Toast.LENGTH_LONG).show()
        }
    }

    private fun driverNotification(invoiceNo : String) {

        val requestQueue = Volley.newRequestQueue(this)

        val jsonObject = JSONObject()

        jsonObject.put("InvoiceNo", invoiceNo)
        val url = Constants.BASEURL + "DriverAssignmentNotification"

        Log.w("notifica_driverURL",  "$url.. $invoiceNo")

        invoiceHeaderDetails = ArrayList()
        invoiceTrackList = ArrayList()
        CommonMethodKotl.showProgressDialog(this)

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            null,
            Response.Listener { response: JSONObject ->
                try {
                    Log.w("assignRes:: ", response.toString())
                    val statusCode = response.optString("statusCode")
                    val statusMsg = response.optString("statusMessage")
                    val responseData = response.getJSONObject("responseData")
                    if (statusCode == "1") {
                        //    val obj = responseData.optJSONObject()
                           // val statusMsg1 = obj.optString("fromDate")

                            Toast.makeText(applicationContext, statusMsg, Toast.LENGTH_SHORT).show()
                            rv_trackList!!.removeAllViews()
                            invoiceHeaderDetails = ArrayList()
                            invoiceTrackList = ArrayList()
                            if (trackInvAdapter != null) {
                                trackInvAdapter!!.notifyDataSetChanged()
                            }
                    }
                    CommonMethodKotl.cancelProgressDialog()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.w("Error3:", Objects.requireNonNull(e.message!!))
                }
            },
            Response.ErrorListener { error: VolleyError ->
                // Do something when error occurred
                //  pDialog.dismiss();
                CommonMethodKotl.cancelProgressDialog()
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
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest)
    }
    private fun saveDriverAssign(jsonObj:TrackingAssignModel, invoiceNo: String) {

        val requestQueue = Volley.newRequestQueue(this)
        val url = Constants.BASEURL + "DriverAssignment"

        Log.w("Given_urlsave", "$url ..${Gson().toJson(jsonObj)}")
        //        {
//            "driver": "SALES1",
//            "invoices": [
//            {
//                "invoiceNo": 53
//            }
//            ],"user": "SALES1"
//        }
        Log.w("sampldriverjson"," ")
        invoiceHeaderDetails = ArrayList()
        invoiceTrackList = ArrayList()
        CommonMethodKotl.showProgressDialog(this)

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            null,
            Response.Listener { response: JSONObject ->
                try {
                    Log.w("assignRes:: ", response.toString())
                    val statusCode = response.optString("statusCode")
                    val statusMsg = response.optString("statusMessage")
                    val responseData = response.getJSONObject("responseData")
                    if (statusCode == "1") {
                        if (responseData.length()>0) {
                        //    val obj = responseData.optJSONObject()
                           // val statusMsg1 = obj.optString("fromDate")

                            Toast.makeText(applicationContext, statusMsg, Toast.LENGTH_SHORT).show()
                            rv_trackList!!.removeAllViews()
                            invoiceHeaderDetails = ArrayList()
                            invoiceTrackList = ArrayList()
                            if (trackInvAdapter != null) {
                                trackInvAdapter!!.notifyDataSetChanged()
                            }

                            emptytxt!!.visibility = View.VISIBLE
                            rv_trackList!!.visibility = View.GONE
                            trackNoLay!!.visibility = View.GONE
                            getInvoiceDetails("")
                            driverNotification(invoiceNo)
                        } else {
                            emptytxt!!.visibility = View.GONE
                            trackNoLay!!.visibility = View.VISIBLE
                            rv_trackList!!.visibility = View.VISIBLE
                            Toast.makeText(applicationContext, "Error in getting data", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        emptytxt!!.visibility = View.GONE
                        trackNoLay!!.visibility = View.VISIBLE
                        rv_trackList!!.visibility = View.VISIBLE
                        Toast.makeText(applicationContext, statusMsg, Toast.LENGTH_SHORT).show()
                    }
                    CommonMethodKotl.cancelProgressDialog()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.w("Error3:", Objects.requireNonNull(e.message!!))
                }
            },
            Response.ErrorListener { error: VolleyError ->
                // Do something when error occurred
                //  pDialog.dismiss();
                CommonMethodKotl.cancelProgressDialog()
                Log.w("Error_throwing:", error.toString())
            }) {
            override fun getBody(): ByteArray {
                return Gson().toJson(jsonObj).toString().toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                val creds =
                    String.format("%s:%s", Constants.API_SECRET_CODE, Constants.API_SECRET_PASSWORD)
                val auth = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.DEFAULT)
                params["Authorization"] = auth
                return params
            }
        }
        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest)
    }

    private fun getInvoiceDetails(trackingNumber: String) {
        // Initialize a new RequestQueue instance
        val jsonObject = JSONObject()
        // jsonObject.put("CompanyCode", companyId);
        jsonObject.put("TrackingNo", trackingNumber)
        //jsonObject.put("LocationCode", locationCode)

        val requestQueue = Volley.newRequestQueue(this)
        val url = Constants.BASEURL + "InvoiceByTracking"

        Log.w("Given_urlInv:", "$url ..$jsonObject")
        invoiceHeaderDetails = ArrayList()
        invoiceTrackList = ArrayList()

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            url,
            jsonObject,
            Response.Listener { response: JSONObject ->
                try {
                    Log.w("trackDetailsRes:: ", response.toString())
                    val statusCode = response.optString("statusCode")
                    val responseData = response.getJSONArray("responseData")
                    if (statusCode == "1") {
                        if (responseData.length()>0) {
                        val `object` = responseData.optJSONObject(0)
                        val model = TrackingInvoiceModel()
                        model.invoiceNumber = `object`.optString("invoiceNumber")
                        model.invoiceDate = `object`.optString("invoiceDate")
                        model.customerCode = `object`.optString("customerCode")
                        model.customerName = `object`.optString("customerName")
                        model.driverName = userName
                        model.invoiceCode = `object`.optString("invoiceNumber")
                            invoiceNo = `object`.optString("invoiceNumber")
                                custCode = `object`.optString("customerCode")
//                        model.overAllTotal = `object`.optString("overAllTotal")
//                        response
//                        model.address =
//                            `object`.optString("address1") + `object`.optString("address2") + `object`.optString(
//                                "address3"
//                            )
//                        model.address1 = `object`.optString("address1")
//                        model.address2 = `object`.optString("address2")
//                        model.address3 = `object`.optString("address3")
//                        model.addressstate =
//                            (`object`.optString("street") + " "
//                                    + `object`.optString("block") + " " +`object`.optString("city"))
//                        model.addresssZipcode =
//                            (`object`.optString("countryName") + " " + `object`.optString("state") + " "
//                                    + `object`.optString("zipcode"))
//
//                        // model.setDeliveryAddress(model.getAddress());
//                        model.subTotal = `object`.optString("subTotal")
//                        model.netTax = `object`.optString("taxTotal")
//                        model.netTotal = `object`.optString("netTotal")
//                        model.paymentTerm = `object`.optString("paymentTerm")
//                        model.taxType = `object`.optString("taxType")
//                        model.taxValue = `object`.optString("taxPerc")
//                        model.outStandingAmount = `object`.optString("totalOutstandingAmount")
//                        model.balanceAmount = `object`.optString("balanceAmount")
//                        model.billDiscount = `object`.optString("billDiscount")
//                        model.itemDiscount = `object`.optString("totalDiscount")
//                        model.soNumber = `object`.optString("soNumber")
//                        model.soDate = `object`.optString("soDate")
//                        model.doDate = `object`.optString("doDate")
//                        model.doNumber = `object`.optString("doNumber")
//                        model.currentAddress = `object`.optString("CurrentAddress")
//                        model.deliveryAddress =
//                            `object`.optString("shipAddress2") + `object`.optString("shipAddress3") + `object`.optString(
//                                "shipStreet"
//                            )
//
//                        model.allowDeliveryAddress = `object`.optString("showShippingAddress")
                        val signFlag = `object`.optString("signFlag")
//                        if (signFlag == "Y") {
//                            val signature = `object`.optString("signature")
//                            Utils.setSignature(signature)
//                            createSignature()
//                        } else {
//                            Utils.setSignature("")
//                        }
                        val detailsArray = `object`.optJSONArray("invoiceDetails")
                        for (i in 0 until detailsArray.length()) {
                            val detailObject = detailsArray.optJSONObject(i)

                            if (detailObject.optString("quantity").toDouble() > 0) {
                                val invoiceListModel = TrackingInvoiceModel.InvoiceList()
                                invoiceListModel.productCode = detailObject.optString("productCode")
                                invoiceListModel.productName = detailObject.optString("productName")
                                invoiceListModel.lqty = detailObject.optString("unitQty")
                                invoiceListModel.cqty = detailObject.optString("cartonQty")
                                invoiceListModel.netQty = detailObject.optString("quantity")
                                invoiceListModel.netQuantity = detailObject.optString("netQuantity")
                                invoiceListModel.focQty = detailObject.optString("foc_Qty")
                                invoiceListModel.returnQty = detailObject.optString("returnQty")
                                invoiceListModel.cartonPrice = detailObject.optString("cartonPrice")
                                invoiceListModel.unitPrice = detailObject.optString("price")
//                                invoiceListModel.excQty = detailObject.optString("exc_Qty")
//                                invoiceListModel.stockQty = detailObject.optString("stockInHand")
                                invoiceListModel.saleType =""
                                if (detailObject.optString("bP_CatalogNo") != null) {
                                    invoiceListModel.customerItemCode = detailObject.optString("bP_CatalogNo")
                                }
//                                val qty1 = detailObject.optString("quantity").toDouble()
//                                val price1 = detailObject.optString("price").toDouble()
//                                val nettotal1 = qty1 * price1
//                                invoiceListModel.total = detailObject.optString("total")
//                                invoiceListModel.pricevalue = price1.toString()
//                                invoiceListModel.uomCode = detailObject.optString("uomCode")
//                                invoiceListModel.pcsperCarton = detailObject.optString("pcsPerCarton")
//                                invoiceListModel.itemtax = detailObject.optString("totalTax")
//                                invoiceListModel.subTotal = detailObject.optString("subTotal")
                                invoiceTrackList!!.add(invoiceListModel)
                            }
                            model.invoiceList = invoiceTrackList
                        }
                        invoiceHeaderDetails!!.add(model)

                        if(invoiceHeaderDetails!!.size > 0){
                            Log.w("detailssize","${invoiceHeaderDetails!!.size}");
                            setTrackingdapter(trackingNumber,invoiceHeaderDetails!!, invoiceTrackList!!)
                            setRefreshEnabled(true)

                            emptytxt!!.visibility = View.GONE
                            trackNoLay!!.visibility = View.VISIBLE
                            rv_trackList!!.visibility = View.VISIBLE
                            Toast.makeText(this@TrackingInvoiceListActivity, "Product "+"$trackingNumber", Toast.LENGTH_SHORT).show()
                        }else{
                            emptytxt!!.visibility = View.VISIBLE
                            rv_trackList!!.visibility = View.GONE
                            trackNoLay!!.visibility = View.GONE
                            setRefreshEnabled(false)

                            Toast.makeText(this@TrackingInvoiceListActivity, "Product "+"$trackingNumber  Not Found", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        emptytxt!!.visibility = View.VISIBLE
                        rv_trackList!!.visibility = View.GONE
                        trackNoLay!!.visibility = View.GONE
                            setRefreshEnabled(false)

                        Toast.makeText(this@TrackingInvoiceListActivity, "Product "+"$trackingNumber  Not Found", Toast.LENGTH_SHORT).show()
                    }
                      //  printInvoice(copy,isDoPrint)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Error in printing Data...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.w("Error3:", Objects.requireNonNull(e.message!!))
                }
            },
            Response.ErrorListener { error: VolleyError ->
                // Do something when error occurred
                //  pDialog.dismiss();
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

    fun setTrackingdapter(trackingNumber: String,trackInvoiceLists: ArrayList<TrackingInvoiceModel> ,
                          trackInvoiceDetailList: ArrayList<TrackingInvoiceModel.InvoiceList>) {
        trackNotxt!!.setText(trackingNumber)
        rv_trackList!!.setHasFixedSize(true)
        rv_trackList!!.setLayoutManager(
            LinearLayoutManager(
                this@TrackingInvoiceListActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        trackInvAdapter =
            TrackingInvoiceAdapter(
                this@TrackingInvoiceListActivity,
                trackInvoiceLists,trackInvoiceDetailList,this
            )
        rv_trackList!!.setAdapter(trackInvAdapter)
    }
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        if (event.keyCode == KeyEvent.KEYCODE_ENTER) {
            if (event.action == KeyEvent.ACTION_UP) {
                var scanningTxt = barcodeText!!.text.toString()

               // Toast.makeText(this, "BarCode Value... + "+barcodeText!!.text.toString(), Toast.LENGTH_SHORT).show()
                scannedBarcode = scanningTxt
           //     searchAndSendActivity(scanningTxt)
                // getBarcodeValues(barcodeEdittext!!.text.toString().trim { it <= ' ' })
                return true
            }
        }
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            // false;
        }
        return super.dispatchKeyEvent(event)
    }
//    fun searchAndSendActivity(barcode: String?) {
//        try {
//            val model = getProductData(barcode)
//            if (model != null) {
//                if (productSummaryList != null && productSummaryList.size > 0) {
//                    if (!isAlreadyExist(barcode)) {
//                        Toast.makeText(applicationContext, "Product Found...", Toast.LENGTH_SHORT)
//                            .show()
//                        Log.w("entypdt",""+model.barcode)
//                        setProductDetails(model)
//                        //  addItem(model);
//
//                    }
//                } else {
//                    Toast.makeText(applicationContext, "Product Found...", Toast.LENGTH_SHORT)
//                        .show()
//                    Log.w("entypdt2",""+model.barcode)
//                    //addItem(model);
//                    setProductDetails(model)
//                    // setProductDetails(model)
//                }
//            } else {
//                //  showBarcodeAlert(barcode);
//                Toast.makeText(applicationContext, "No Product found", Toast.LENGTH_SHORT).show()
//            }
//        } catch (e: Exception) { }
//    }
    override fun trackingAssignSelected(invoiceModel: TrackingInvoiceModel?) {
    var trackAssignList: ArrayList<TrackingAssignInvoice>? = ArrayList()

    var trackingAssignModel = TrackingAssignInvoice(invoiceModel!!.invoiceCode )
    trackAssignList!!.add(trackingAssignModel)

    var trackingAssignInvModel = TrackingAssignModel(userName!! ,trackAssignList!!,userName!! )

    saveDriverAssign(trackingAssignInvModel, invoiceModel!!.invoiceCode)
}
override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.barcode_menu, menu)
    val switchmenu = menu.findItem(R.id.switch_track_menu)
    switchmenu.setVisible(false)
    val uploadItem = menu.findItem(R.id.upload_track_menu)
    uploadItem.setVisible(false)

    val switchPicklist = switchmenu.actionView as SwitchCompat?
    switchPicklist!!.text = "   Status : "

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
    fun showUploadImageAlert() {
        val alertDialog = AlertDialog.Builder(this@TrackingInvoiceListActivity)
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
        val remarkLay = customLayout.findViewById<LinearLayout>(R.id.remarkLayl)
        remarkLay.visibility = View.GONE

        val mSig = CaptureSignatureView(this@TrackingInvoiceListActivity, null)
        // mContent.addView(mSig, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        invNo_txt.text = invoiceNo
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

                try {
                    val obj = JSONObject()
                    obj.put("invoiceNumber", invoiceNo)
                    obj.put("currentDateTime", currentSaveDateTime)
                    obj.put("customerCode", custCode)
                    obj.put("Username", userName)
                    obj.put("status", spinnertxt_dialog)
                    obj.put("PackStatus", packStatusStr)
                    obj.put("Remark", "")
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
        val mSig = CaptureSignatureView(this@TrackingInvoiceListActivity, null) {
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
        val builder = AlertDialog.Builder(this@TrackingInvoiceListActivity)
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
        val builder = AlertDialog.Builder(this@TrackingInvoiceListActivity)
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
                    Utils.w("GivenImageTrack", imageString)
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

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
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
    private fun setRefreshEnabled(enabled: Boolean) {
        toolbar!!.menu.findItem(R.id.switch_track_menu).setVisible(enabled)
        toolbar!!.menu.findItem(R.id.upload_track_menu).setVisible(enabled)
    }
    fun showSaveAlert(switchPicklist: SwitchCompat?,status: String , pickStatus: String , mail: String) {
        val builder1 = AlertDialog.Builder(this@TrackingInvoiceListActivity)
        builder1.setTitle("Are you sure want to update status ?")
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
    fun savePickListDelivery(switchPicklist: SwitchCompat?,status: String , pickStatus: String, mail: String){
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        currentSaveDateTime = currentDateandTime

        try {
            val obj = JSONObject()
            obj.put("invoiceNumber", invoiceNo)
            obj.put("currentDateTime", currentSaveDateTime)
            obj.put("customerCode", custCode)
            obj.put("Username", userName)
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
    fun savePicklistDeliveryApi(jsonBody: JSONObject ,switchPicklist: SwitchCompat?,alert: String) { // todo
        try {
            pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
            pDialog!!.setCancelable(false)

            val requestQueue = Volley.newRequestQueue(this)
            Log.w("track_request:", jsonBody.toString())
            var URL = ""
            URL = Constants.BASEURL + "PostingSignImageInvoice"
            Log.w("url_picklDel_save:", URL)
            pDialog!!.setTitleText("Saving Status...")
            pDialog!!.show()

            val salesOrderRequest: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, URL, jsonBody,
                Response.Listener { response: JSONObject ->
                    Log.w("track_status_sav:", response.toString())
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


                        if(alert.equals("true")) {
                            alertSave!!.dismiss()
                        }
                        if(switchPicklist != null) {
                            switchPicklist.isChecked = false
                        }

                    } else {
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
                    Log.w("track_error:", error.toString())
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

    fun getCurrentLocation() {
        locationTrack = LocationTrack(this@TrackingInvoiceListActivity)
        if (locationTrack!!.canGetLocation()) {
            val longitude: Double = locationTrack!!.getLongitude()
            val latitude: Double = locationTrack!!.getLatitude()
            current_latitude = latitude.toString()
            current_longitude = longitude.toString()
            val currentAddress = Utils.getCompleteAddress(this@TrackingInvoiceListActivity, latitude, longitude)
            if (currentAddress != null && !currentAddress.isEmpty()) {
                //  locationText.setText(currentAddress)
                current_addr = currentAddress
            }
            Log.w("latlongpickDPrev",""+current_latitude)

        } else {
            // locationTrack!!.showSettingsAlert();
        }
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
    if (item.itemId == android.R.id.home) {
      //  finish();
        val intent = Intent(this@TrackingInvoiceListActivity, NewDeliveryPickListActivity::class.java)
        startActivity(intent)
       finish();

    } else if (item.itemId == R.id.action_barcode) {
        scanFromFragment()
    }
    return true
}
}