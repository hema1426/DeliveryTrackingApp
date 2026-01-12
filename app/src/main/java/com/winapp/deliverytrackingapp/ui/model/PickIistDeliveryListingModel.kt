package com.winapp.KHDelivery.model


import com.winapp.deliverytrackingapp.ui.model.PicklistDeliveryPrintPreviewModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PickIistDeliveryListingModel : java.io.Serializable {
    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("customerCode")
    @Expose
    var customerCode: String? = null
    @SerializedName("customerName")
    @Expose
    var customerName: String? = null
    @SerializedName("docDate")
    @Expose
    var docDate: String? = null
    @SerializedName("docNumber")
    @Expose
    var invNumber: String? = null
    @SerializedName("noOfItem")
    @Expose
    var noOfItem: String? = null
    @SerializedName("Priority")
    @Expose
    var priority: String? = null
    @SerializedName("pickListStatus")
    @Expose
    var invoiceStatus: String? = null
    @SerializedName("DateTime")
    @Expose
    var dateTime: String? = null

    @SerializedName("reason")
    @Expose
    var reason: String? = null

    var pickListStatus: String? = null
    var customerAddress: String? = null
    var shipAddress: String? = null
    var phoneNo: String? = null
    var contactName: String? = null
    var remark: String? = null
    var user: String? = null
    var signatureUrl: String? = null
    var imageUrl: String? = null
    var mobileUser: String? = null
    var isShow: Boolean = false
    var isItemSelected: Boolean = false
    var invoiceList: ArrayList<PicklistDeliveryPrintPreviewModel.InvoiceList>? = null
}
