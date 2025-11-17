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
import com.winapp.deliverytrackingapp.ui.utils.Utils;

import java.util.ArrayList;

public class TrackingInvoiceDetailAdapter extends RecyclerView.Adapter<TrackingInvoiceDetailAdapter.ViewHolder> {

    private ArrayList<TrackingInvoiceModel.InvoiceList> invoiceLists;
    private Context context;
    View view;
    private String printView;
    public TrackingInvoiceDetailAdapter(Context context, ArrayList<TrackingInvoiceModel.InvoiceList> invoices) {
        this.context=context;
        this.invoiceLists = invoices;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tracking_invoice_details_items, viewGroup, false);
//        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_details_view_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        TrackingInvoiceModel.InvoiceList invoiceList=invoiceLists.get(position);

        viewHolder.sno.setText(String.valueOf(position+1));
        viewHolder.pdtcode.setText(invoiceList.getProductCode());
        viewHolder.pdtName.setText(invoiceList.getProductName());
        viewHolder.qty.setText(Utils.twoDecimalPoint(Double.parseDouble(invoiceList.getNetQty())));
    }

    @Override
    public int getItemCount() {
        return invoiceLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView pdtcode;
        private TextView pdtName;
        private TextView sno,qty;

        public ViewHolder(View view) {
            super(view);
            sno=view.findViewById(R.id.sl_no_inv);
            qty=view.findViewById(R.id.item_qty_inv);
            pdtcode=view.findViewById(R.id.item_code_inv);
            pdtName=view.findViewById(R.id.item_productName_inv);
        }
    }

}