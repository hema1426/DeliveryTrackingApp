package com.winapp.deliverytrackingapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.winapp.deliverytrackingapp.R;
import com.winapp.deliverytrackingapp.ui.model.TrackingInvoiceModel;

import java.util.ArrayList;

public class TrackingInvoice1Adapter extends RecyclerView.Adapter<TrackingInvoice1Adapter.ViewHolder> {

    private ArrayList<TrackingInvoiceModel.InvoiceList> invoiceLists;
    private Context context;
    View view;
    private String printView;
    public TrackingInvoice1Adapter(Context context, ArrayList<TrackingInvoiceModel.InvoiceList> invoices) {
        this.context=context;
        this.invoiceLists = invoices;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tracking_invoice_items, viewGroup, false);
//        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_details_view_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        TrackingInvoiceModel.InvoiceList invoiceList=invoiceLists.get(position);
//        viewHolder.pdtcode.setText(invoiceList.getProductCode());
//        viewHolder.pdtName.setText(invoiceList.getProductName());
//        viewHolder.date.setText(invoiceList.getUomCode());
    }

    @Override
    public int getItemCount() {
        return invoiceLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView pdtcode;
        private TextView pdtName,date;
        private Spinner driverSpinner;

        public ViewHolder(View view) {
            super(view);
           // date=view.findViewById(R.id.date_track);
//            pdtcode=view.findViewById(R.id.pdtCode_track);
//            pdtName=view.findViewById(R.id.pdtname_track);
            driverSpinner =view.findViewById(R.id.driver_spinner);
        }
    }

}