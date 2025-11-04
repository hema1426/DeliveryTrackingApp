package com.winapp.deliverytrackingapp.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.winapp.deliverytrackingapp.ui.model.CustomerModel;
import com.winapp.deliverytrackingapp.ui.utils.Utils;
import com.winapp.deliverytrackingapp.R;

import java.util.ArrayList;

public class FilterCustomerAdapter extends RecyclerView.Adapter<FilterCustomerAdapter.ViewHolder> {
    private ArrayList<CustomerModel> customers;
    public CallBack callBack;
    public Context context;
    public OnMoreButtonClicked onMoreButtonClicked;
    public FilterCustomerAdapter(Context context, ArrayList<CustomerModel> customers, CallBack callBack) {
        this.customers = customers;
        this.callBack=callBack;
        this.context=context;
    }

    public FilterCustomerAdapter(Context context, ArrayList<CustomerModel> customers, OnMoreButtonClicked onMoreButtonClicked) {
        this.customers = customers;
        this.callBack=callBack;
        this.context=context;
        this.onMoreButtonClicked=onMoreButtonClicked;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.select_customer_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        CustomerModel model=customers.get(i);
        viewHolder.customerName.setText(model.getCustomerName());
        viewHolder.customerCode.setText(model.getCustomerCode());
        viewHolder.outstandingAmount.setText(Utils.twoDecimalPoint(Double.parseDouble(model.getOutstandingAmount())));

        if (model.getAddress1()!=null && !model.getAddress1().isEmpty()){
            viewHolder.locationIcon.setVisibility(View.VISIBLE);
            viewHolder.customerAddress.setText(model.getAddress1());
            viewHolder.customerAddress.setVisibility(View.VISIBLE);
        }else {
            viewHolder.locationIcon.setVisibility(View.VISIBLE);
            viewHolder.customerAddress.setText("No Address");
            viewHolder.customerAddress.setVisibility(View.VISIBLE);
        }

//        if (context instanceof CustomerListActivity){
//            viewHolder.moreIcon.setVisibility(View.VISIBLE);
//        }else {
//            viewHolder.moreIcon.setVisibility(View.GONE);
//        }
       /* if (context instanceof SalesOrderListActivity
                || context instanceof InvoiceListActivity
                || context instanceof NewInvoiceListActivity
                || context instanceof SalesReturnActivity
                || context instanceof AddInvoiceActivity) {
            viewHolder.customerCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.searchCustomer(model.getCustomerCode(), model.getCustomerName(),i);
                }
            });
        }
        if (context instanceof SalesOrderListActivity
                || context instanceof InvoiceListActivity
                || context instanceof NewInvoiceListActivity
                || context instanceof AddInvoiceActivity
                || context instanceof SalesReturnActivity){
            viewHolder.customerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.searchCustomer(model.getCustomerCode(),model.getCustomerName(),i);
                }
            });
        }*/

        viewHolder.moreIcon.setVisibility(View.GONE);
        viewHolder.customerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMoreButtonClicked.createInvoice(model.getCustomerCode(),model.getCustomerName(),model.getTaxCode(),model.getTaxPerc(),model.getTaxType());
            }
        });

        viewHolder.moreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMoreButtonClicked.moreOption(model.getCustomerName(),model.getCustomerCode(),model.getOutstandingAmount());
            }
        });

        if (i % 2==0){
            viewHolder.customerLayout.setBackgroundColor(Color.parseColor("#F8F9F9"));
        }else {
            viewHolder.customerLayout.setBackgroundColor(Color.parseColor("#EAEDED"));
        }
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView customerName;
        private TextView customerAddress;
        private TextView customerCode;
        private CardView customerCard;
        private LinearLayout customerLayout;
        private ImageView moreIcon;
        private ImageView locationIcon;
        private TextView outstandingAmount;
        public ViewHolder(View view) {
            super(view);
            customerName = view.findViewById(R.id.name);
            customerCode=view.findViewById(R.id.customer_code);
            customerAddress=view.findViewById(R.id.customer_address);
            customerCard=view.findViewById(R.id.cardlist_item);
            customerLayout=view.findViewById(R.id.root_layout);
            moreIcon=view.findViewById(R.id.more_icon);
            locationIcon=view.findViewById(R.id.location_icon);
            outstandingAmount=view.findViewById(R.id.outstanding_amount);
        }
    }

    public interface CallBack{
        void searchCustomer(String letter,String customerName, int pos);
    }

    public interface OnMoreButtonClicked{
        void moreOption(String customename,String id,String outstanding);
        void createInvoice(String customerCode,String customerName,String taxcode,String taxperc,String taxtype);
    }

    public void filterList(ArrayList<CustomerModel> filterdNames) {
        this.customers = filterdNames;
        notifyDataSetChanged();
    }

}