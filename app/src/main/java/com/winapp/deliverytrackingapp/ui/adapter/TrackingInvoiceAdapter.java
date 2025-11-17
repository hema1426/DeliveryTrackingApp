package com.winapp.deliverytrackingapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.winapp.deliverytrackingapp.R;
import com.winapp.deliverytrackingapp.ui.model.TrackingInvoiceModel;

import java.util.ArrayList;

public class TrackingInvoiceAdapter extends RecyclerView.Adapter<TrackingInvoiceAdapter.ViewHolder> {

    private ArrayList<TrackingInvoiceModel> invoiceLists;
    private Context context;
    private TrackingAssignClickListener trackingAssignClickListener ;
    private TrackingInvoiceDetailAdapter trackingInvoiceDetailAdapter ;
    private  ArrayList<TrackingInvoiceModel.InvoiceList> invoiceDetailsList ;
    View view;
    private String printView;
    public TrackingInvoiceAdapter(Context context, ArrayList<TrackingInvoiceModel> invoices ,
                                  ArrayList<TrackingInvoiceModel.InvoiceList> invoiceDetails  , TrackingAssignClickListener trackingAssignClickListener) {
        this.context=context;
        this.invoiceLists = invoices;
        this.invoiceDetailsList = invoiceDetails;
        this.trackingAssignClickListener = trackingAssignClickListener;
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
        TrackingInvoiceModel invoiceList=invoiceLists.get(position);
        viewHolder.custName.setText(invoiceList.getCustomerName());
        viewHolder.invNo.setText(invoiceList.getInvoiceNumber());
        viewHolder.date.setText(invoiceList.getInvoiceDate());
        viewHolder.driverName.setText(invoiceList.getDriverName());
        viewHolder.assignLayl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackingAssignClickListener.trackingAssignSelected(invoiceList);
            }
        });
        if (invoiceList.getInvoiceList()!= null && !invoiceList.getInvoiceList().isEmpty()
                && invoiceList.getInvoiceList().size()>0) {
            setInvoiceAdapter(viewHolder, position, invoiceList.getInvoiceList());
            viewHolder.rv_pdtList.setVisibility(View.VISIBLE);
            viewHolder.emptytxt.setVisibility(View.GONE);
        }else{
            viewHolder.emptytxt.setVisibility(View.VISIBLE);
            viewHolder.rv_pdtList.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return invoiceLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView invNo,driverName;
        private TextView custName,date , emptytxt;
        private Spinner driverSpinner;
        private LinearLayout assignLayl;
        private RecyclerView rv_pdtList;

        public ViewHolder(View view) {
            super(view);
           // date=view.findViewById(R.id.date_track);
            invNo=view.findViewById(R.id.invNo_track);
            custName=view.findViewById(R.id.custname_track);
            date=view.findViewById(R.id.date_track);
            driverName=view.findViewById(R.id.driverName_track);
            driverSpinner =view.findViewById(R.id.driver_spinner);
            assignLayl =view.findViewById(R.id.assignLay);
            emptytxt =view.findViewById(R.id.emptytxt_item);
            rv_pdtList =view.findViewById(R.id.rv_invoiceDetailList);
        }
    }
    public void setInvoiceAdapter(@NonNull RecyclerView.ViewHolder  viewHolder, int position,
                                  ArrayList<TrackingInvoiceModel.InvoiceList> invoiceList){
        ((ViewHolder) viewHolder).rv_pdtList.setHasFixedSize(true);
        ((ViewHolder) viewHolder).rv_pdtList.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        TrackingInvoiceDetailAdapter adapter=new TrackingInvoiceDetailAdapter(context,invoiceList);
        ((ViewHolder) viewHolder).rv_pdtList.setAdapter(adapter);
        // notifyDataSetChanged();
    }

   public interface TrackingAssignClickListener {
         void trackingAssignSelected(TrackingInvoiceModel pickModel);
    }

}