package com.winapp.deliverytrackingapp.ui.model

data class TrackingAssignModel(
    val driver: String,
    val invoices: ArrayList<TrackingAssignInvoice>,
    val user: String
)