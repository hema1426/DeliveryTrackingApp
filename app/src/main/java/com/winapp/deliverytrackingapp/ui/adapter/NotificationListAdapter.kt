package com.winapp.deliverytrackingapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.winapp.deliverytrackingapp.R
import com.winapp.deliverytrackingapp.ui.model.NotificationModel

class NotificationListAdapter(
    private val context: Context,
    private val invoiceLists: ArrayList<NotificationModel>,
) : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.notification_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val invoiceList = invoiceLists[position]
        viewHolder.notifTitle.text = invoiceList.title
        viewHolder.notifMsg.text = invoiceList.message
        viewHolder.notifDate.text = invoiceList.date
        viewHolder.type.text = invoiceList.type
        viewHolder.cardview.setBackgroundResource(
            if (position % 2 == 0)
                R.drawable.bg_row_ripple
            else
                R.drawable.bg_row_ripple_alt
        )
    }

    override fun getItemCount(): Int {
        return invoiceLists.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val notifTitle: TextView = view.findViewById(R.id.notif_title_item)
        val notifMsg: TextView = view.findViewById(R.id.notif_msg_item)
        val notifDate: TextView = view.findViewById(R.id.notif_date_item)
        val type: TextView = view.findViewById(R.id.notif_type_item)
        val cardview: CardView = view.findViewById(R.id.notifiList_card)
    }

//    interface TrackingAssignClickListener {
//        fun trackingAssignSelected(pickModel: NotificationModel)
//    }
}
