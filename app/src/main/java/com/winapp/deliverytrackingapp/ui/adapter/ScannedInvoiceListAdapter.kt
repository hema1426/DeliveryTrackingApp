package com.winapp.deliverytrackingapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.winapp.deliverytrackingapp.ui.utils.Constants
import com.winapp.deliverytrackingapp.ui.activity.PickListDeliveryPrintPreviewActivity
import com.winapp.deliverytrackingapp.ui.model.PicklistDeliveryPrintPreviewModel
import com.winapp.KHDelivery.model.PickIistDeliveryListingModel
import com.winapp.deliverytrackingapp.R
import org.json.JSONException
import org.json.JSONObject

class ScannedInvoiceListAdapter(
    private val context: Context,
    private var dataList: MutableList<PickIistDeliveryListingModel>,
    private val pickListUploadClickListener: PickListUploadClickListener

) : RecyclerView.Adapter<ScannedInvoiceListAdapter.MyViewHolder>() {
    private val pickListInvoiceClickListener: PickListInvoiceClickListener? = null
    private var islongPress: Boolean? = null
    var invoiceshowList: ArrayList<PicklistDeliveryPrintPreviewModel.InvoiceList>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.scanned_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(dataList[position],holder)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class MyViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var mainLayout: LinearLayout? = null
        var progressLayout: LinearLayout? = null
        var pidatetxt: TextView
        var piinvoicenotxt: TextView
        var pinoofitemtxt: TextView
//        var custaddrtxt: TextView
        var phonetxt: TextView
        var pistatustxt: TextView
        var usernametxt : TextView
        var picustnametxt: TextView
        var picklistinvoicelay: CardView
        var bottomLayout: CardView
        var three_dot_pickl: ImageView
        var downarrow_pickDelm: ImageView
        var picklist_deli_Lay : LinearLayout
        var three_dot_picklay: LinearLayout
        var status_layoutl: LinearLayout
        var rv_pick_downlist: RecyclerView
        var shippingAddr_pickD: TextView? = null
        var username_list_txt: TextView? = null
        var remark_list_txt: TextView? = null
        var person_list_txt: TextView? = null
        var mobUser_list_txt: TextView? = null
        var mobUserLay: LinearLayout? = null
        var progressBar: ProgressBar? = null
        @SuppressLint("ClickableViewAccessibility", "SuspiciousIndentation")
        fun setData(pickItem: PickIistDeliveryListingModel, holder: MyViewHolder) {
            picustnametxt.text = pickItem.customerName+" - "+pickItem.customerCode
            pidatetxt.text = pickItem.docDate
            piinvoicenotxt.text = pickItem.code
            pinoofitemtxt.text = pickItem.noOfItem
            pistatustxt.text = pickItem.pickListStatus
//            custaddrtxt.text = pickItem.customerAddress
            shippingAddr_pickD!!.text = pickItem.shipAddress
            phonetxt!!.text = pickItem.phoneNo
            username_list_txt!!.text = pickItem.user
            remark_list_txt!!.text = pickItem.remark
            person_list_txt!!.text = pickItem.contactName

            Log.w("shiaddreee1aa",""+pickItem.shipAddress)
            if(pickItem.mobileUser != null && pickItem.mobileUser!!.isNotEmpty()) {
                mobUserLay!!.visibility = View.VISIBLE
                mobUser_list_txt!!.text = pickItem.mobileUser
            }else{
                mobUserLay!!.visibility = View.GONE
            }

            Log.e("cust_nameaa", ".." + pickItem.customerName+" .."+pickItem.noOfItem)
            //   schedule_date.setText(scheduledatel.getDate());
            if (pickItem.pickListStatus.equals(
                    "O",
                ) || pickItem.pickListStatus.equals("Open")
                || pickItem.pickListStatus!!.isEmpty() ) {
                pistatustxt.setText(" Pending ")
                islongPress = true
                status_layoutl.setBackgroundResource(R.drawable.invoice_status_paid)

                //  pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.white))
//             pistatustxt.setBackgroundResource(R.drawable.round_accent_rad3_blue)
              //  picklistinvoicelay.setBackgroundResource(R.color.white)
            } else if (pickItem.pickListStatus.equals(
                    "C") || pickItem.pickListStatus.equals("Close")) {
                pistatustxt.setText(" Delivered ")
                islongPress = true
                status_layoutl.setBackgroundResource(R.drawable.invoice_closed)

                //  pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.white))
           //     picklistinvoicelay.setBackgroundResource(R.color.colorPrimary)
            }
            else if (pickItem.pickListStatus.equals("OC")) {
                pistatustxt.setText(" Packed ")
                islongPress = false
                pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                status_layoutl.setBackgroundResource(R.drawable.invoice_status_partial)
                picklistinvoicelay.setBackgroundResource(R.color.white)
            }
            else if (pickItem.pickListStatus.equals("R")) {
                pistatustxt.setText(R.string.release)
                islongPress = true
                status_layoutl.setBackgroundResource(R.drawable.corner_picklist_orange_text)

                //pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.black))
          //      picklistinvoicelay.setBackgroundResource(R.color.white)
            } else if (pickItem.pickListStatus.equals("OW")) {
                islongPress = false
                pistatustxt.setText("Open Weight")
                pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                status_layoutl.setBackgroundResource(R.drawable.round_accent_rad3_green)
                picklistinvoicelay.setBackgroundResource(R.color.white)
            }

            else if (pickItem.invoiceStatus.equals("P")) {
                islongPress = false
                pistatustxt.setText("Open Printed")
                pistatustxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                status_layoutl.setBackgroundResource(R.drawable.round_accent_rad3_green)
                picklistinvoicelay.setBackgroundResource(R.color.white)
            }

//            three_dot_picklay.setOnClickListener{ view ->
//                pickListUploadClickListener!!.pickListUploadSelected(pickItem,view)
//            }

//            picklist_deli_Lay.setOnClickListener {
//                    val pos = adapterPosition
//              //  if(!pickItem.pickListStatus.equals("C")) {
//
//                    if (pos != -1) {
//                        //card_bg.setBackgroundResource(R.color.colorPrimaryLight);
//                        // packageCategoryClickListener.packageCategorySelected(dataList.get(pos));
//
////                        for (i in dataList.indices) {
////                            val respItem = dataList[i]
////                            respItem.isItemSelected = i == pos
////                            dataList[i] = respItem
////                            notifyItemChanged(i)
////                        }
//                     val intent = Intent(context, PickListDeliveryPrintPreviewActivity::class.java)
//
//                        intent.putExtra("salesCodeDel", pickItem.code)
//                        intent.putExtra("custCodePickDel", pickItem.customerCode)
//                        intent.putExtra("pick_itemDel", pickItem.noOfItem)
//                        intent.putExtra("pick_InvDateDel", pickItem.docDate)
//                        intent.putExtra("pick_statusDel", pickItem.pickListStatus)
//                        intent.putExtra("pick_DocNumDel", pickItem.invNumber)
//                        intent.putExtra("pick_DatetimeDel", pickItem.dateTime)
//                      //  intent.putExtra("pick_model_Del" ,pickItem as Serializable)
//
//                        context.startActivity(intent)
//
//                     //   Log.w("delDateStr1a:",  pickItem.dateTime!!);
//                        //  pickListInvoiceClickListener.pickListInvoiceSelected(pos)
//                    }
////                }else{
////
////                    // Device does not support Bluetooth
////                    Toast.makeText(
////                        context,
////                        "Already Completed",
////                        Toast.LENGTH_SHORT
////                    ).show()
////
////                }
//            }
            downarrow_pickDelm.setOnClickListener {
                if (downarrow_pickDelm.tag == "hide") {
                   bottomLayout.visibility = View.VISIBLE
                    downarrow_pickDelm.tag = "show"
                    downarrow_pickDelm.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_baseline_keyboard_arrow_up_24)
                    )
                    try {
                        if (pickItem.invoiceList!!.size > 0) {
                            setInvoiceAdapter(
                                holder,
                                position,
                                pickItem.invoiceList!!
                            )
                           progressLayout!!.visibility = View.GONE
                           mainLayout!!.visibility = View.VISIBLE
                        } else {
                            getInvoiceDetails(
                                pickItem.code!!,
                                holder,
                                position,
                                pickItem,
                            )
                            pickItem.isShow =true
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    pickItem.isShow = false
                    bottomLayout.visibility = View.GONE
                    downarrow_pickDelm.tag = "hide"
                    downarrow_pickDelm.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_baseline_keyboard_arrow_down_24)
                    )
                }
            }


//            if (pickItem.isItemSelected()) {
//                picklistinvoicelay.setBackgroundResource(R.color.colorPrimaryLight);
//            } else {
//                picklistinvoicelay.setBackgroundResource(R.color.white);
//            }

            //  Glide.with(context).load(packageCategory.getImage_url()).apply(new RequestOptions().placeholder(R.drawable.placeholder_food)).into(image);
            //  Glide.with(context).load(categoryFood.getImage_url()).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(16)).placeholder(R.drawable.placeholder_food)).into(image);
        }
        init {
            pidatetxt = itemView.findViewById<View>(R.id.picki_del_date_item) as TextView
            picustnametxt = itemView.findViewById<View>(R.id.cust_name_picki_del_item) as TextView
            piinvoicenotxt = itemView.findViewById<View>(R.id.picki_del_invoiceno_item) as TextView
            pinoofitemtxt = itemView.findViewById<View>(R.id.picki_del_noitem_item) as TextView
           // custaddrtxt = itemView.findViewById<View>(R.id.cust_addr_pick_deli_item) as TextView
            phonetxt = itemView.findViewById<View>(R.id.phoneno_list_pickD) as TextView
            pistatustxt = itemView.findViewById<View>(R.id.picki_del_status_item) as TextView
            usernametxt = itemView.findViewById<View>(R.id.user_picki_item) as TextView
            picklistinvoicelay = itemView.findViewById<View>(R.id.picklist_deli_card) as CardView
            three_dot_pickl = itemView.findViewById<View>(R.id.three_dot_pickl) as ImageView
            downarrow_pickDelm = itemView.findViewById<View>(R.id.downarrow_pickDel) as ImageView
            three_dot_picklay = itemView.findViewById<View>(R.id.three_dot_pickl_lay) as LinearLayout
            status_layoutl = itemView.findViewById<View>(R.id.status_layout) as LinearLayout
            picklist_deli_Lay = itemView.findViewById<View>(R.id.picklist_deli_lay) as LinearLayout
            rv_pick_downlist = itemView.findViewById<View>(R.id.pickDel_downlist) as RecyclerView
            mainLayout = itemView.findViewById<LinearLayout>(R.id.main_layout_pickD)
            progressLayout = itemView.findViewById<LinearLayout>(R.id.progress_layout_pickD)
            bottomLayout = itemView.findViewById<CardView>(R.id.bottom_layout_pickD)
            shippingAddr_pickD = itemView.findViewById(R.id.shippingAddr_list_pickD)
            person_list_txt = itemView.findViewById(R.id.person_pick_deli_item)
            username_list_txt = itemView.findViewById(R.id.user_pick_deli_item)
            remark_list_txt = itemView.findViewById(R.id.remark_pick_deli_item)
            mobUser_list_txt = itemView.findViewById(R.id.picki_del_mobUser_item)
            mobUserLay = itemView.findViewById(R.id.mobUser_Lay)

        }
    }

    @Throws(JSONException::class)
    private fun getInvoiceDetails(
        invoiceNumber: String,
        viewHolder: RecyclerView.ViewHolder,
        position: Int,
        invoice: PickIistDeliveryListingModel,
    ) {
        // Initialize a new RequestQueue instance
        val jsonObject = JSONObject()
        // jsonObject.put("CompanyCode",companyId);
        jsonObject.put("DeliveryOrderNo", invoiceNumber)
        val requestQueue = Volley.newRequestQueue(context)
        val url = Constants.BASEURL + "DeliveryOrderDetails"
        // Initialize a new JsonArrayRequest instance
        Log.w("url_pickDeliver:", url)

        invoiceshowList = ArrayList()
        val pdfInvoiceList = ArrayList<PicklistDeliveryPrintPreviewModel>()
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url,
            jsonObject,
            Response.Listener<JSONObject> { response: JSONObject ->
                try {
                    Log.w("url_pickDeliver:", response.toString())

                    if (response.length() > 0) {
                        val responseData = response.getJSONArray("responseData")
                        val `object` = responseData.optJSONObject(0)
                        val model = PicklistDeliveryPrintPreviewModel()
                        model.invoiceNumber = `object`.optString("doNumber")
                        model.invoiceDate = `object`.optString("doDate")
                        model.customerCode = `object`.optString("customerCode")
                        model.customerName = `object`.optString("customerName")
                        //  model.setAddress(response.optString("Address1"));
                        // model.setDeliveryAddress(response.optString("Address1"));
                        model.subTotal = `object`.optString("total")
                        model.netTax = `object`.optString("taxTotal")
                        model.netTotal = `object`.optString("netTotal")
                        model.taxType = `object`.optString("TaxType")
                        model.taxValue = `object`.optString("TaxPerc")
                        model.outStandingAmount = `object`.optString("balanceAmount")
                        model.billDiscount = `object`.optString("BillDIscount")
                        model.allowDeliveryAddress = `object`.optString("showShippingAddress")
                        val products = `object`.getJSONArray("deliveryOrderDetails")
                        for (i in 0 until products.length()) {
                            val detailObject = products.getJSONObject(i)
                            val invoiceListModel =
                                PicklistDeliveryPrintPreviewModel.InvoiceList()
                            invoiceListModel.productCode = detailObject.optString("productCode")
                            invoiceListModel.description = detailObject.optString("productName")
                            invoiceListModel.lqty = detailObject.optString("unitQty")
                            invoiceListModel.cqty = detailObject.optString("cartonQty")
                            invoiceListModel.netQty = detailObject.optString("quantity")
                            invoiceListModel.netQuantity = detailObject.optString("netQuantity")
                            invoiceListModel.focQty = detailObject.optString("foc_Qty")
                            invoiceListModel.returnQty = detailObject.optString("returnQty")
                            invoiceListModel.cartonPrice = detailObject.optString("cartonPrice")
                            invoiceListModel.unitPrice = detailObject.optString("price")
                            invoiceListModel.uomCode = detailObject.optString("uoMName")
                            val qty = detailObject.optString("quantity").toDouble()
                            val price = detailObject.optString("price").toDouble()
                            val nettotal = qty * price
                            // invoiceListModel.setTotal(String.valueOf(nettotal));
                            invoiceListModel.total = detailObject.optString("total")
                            invoiceListModel.pricevalue = price.toString()
                            invoiceListModel.pcsperCarton = detailObject.optString("pcsPerCarton")
                            invoiceListModel.itemtax = detailObject.optString("totalTax")
                            invoiceListModel.subTotal = detailObject.optString("subTotal")
                            invoiceshowList!!.add(invoiceListModel)
                        }
                        model.invoiceList = invoiceshowList
                        pdfInvoiceList.add(model)
                    }
                    if (invoiceshowList!!.size > 0) {
                        (viewHolder as MyViewHolder).progressLayout!!.visibility =
                            View.GONE
                        (viewHolder as MyViewHolder).mainLayout!!.visibility =
                            View.VISIBLE
//
                        invoice.invoiceList = invoiceshowList!!
                        setInvoiceAdapter(viewHolder, position, invoiceshowList)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                // Do something when error occurred
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
    fun setInvoiceAdapter(
        viewHolder: RecyclerView.ViewHolder,
        position: Int,
        invoiceList: ArrayList<PicklistDeliveryPrintPreviewModel.InvoiceList>?
    ) {
        (viewHolder as MyViewHolder).rv_pick_downlist.setHasFixedSize(true)
        (viewHolder as MyViewHolder).rv_pick_downlist.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = PickDeliveryPrintPreviewAdapter(context, invoiceList!!)
        (viewHolder as MyViewHolder).rv_pick_downlist.adapter = adapter
        // notifyDataSetChanged();
    }
    fun updateList(list: ArrayList<PickIistDeliveryListingModel>) {
        dataList = list
        notifyDataSetChanged()
    }

    interface PickListInvoiceClickListener {
        fun pickListInvoiceSelected(position: Int?)
    }
    interface PickListUploadClickListener {
        fun pickListUploadSelected(pickModel: PickIistDeliveryListingModel, view: View)
    }

    interface PickListInvLongClickListener {
        fun pickListLongClickSelected(position: Int?)
    }
}