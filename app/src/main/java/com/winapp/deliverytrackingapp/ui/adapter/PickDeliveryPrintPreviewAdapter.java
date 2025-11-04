package com.winapp.deliverytrackingapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.winapp.deliverytrackingapp.ui.activity.NewDeliveryPickListActivity;
import com.winapp.deliverytrackingapp.ui.model.PicklistDeliveryPrintPreviewModel;
import com.winapp.deliverytrackingapp.R;

import java.util.ArrayList;

public class PickDeliveryPrintPreviewAdapter extends RecyclerView.Adapter<PickDeliveryPrintPreviewAdapter.ViewHolder> {

    private ArrayList<PicklistDeliveryPrintPreviewModel.InvoiceList> invoiceLists;
    private Context context;
    View view;
    private String printView;
    public PickDeliveryPrintPreviewAdapter(Context context, ArrayList<PicklistDeliveryPrintPreviewModel.InvoiceList> invoices) {
        this.context=context;
        this.invoiceLists = invoices;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
         if (context instanceof NewDeliveryPickListActivity){
             view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pick_delivery_details_view_items, viewGroup, false);
         }else {
             view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pick_delivery_print_preview_item, viewGroup, false);
         }
//        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_details_view_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        PicklistDeliveryPrintPreviewModel.InvoiceList invoiceList=invoiceLists.get(position);
        viewHolder.slNo.setText(String.valueOf(position+1));
        viewHolder.code.setText(String.valueOf(invoiceList.getProductCode()));

//        if (invoiceList.getUomCode()!=null && !invoiceList.getUomCode().equals("null") && !invoiceList.getUomCode().isEmpty()){
//            viewHolder.description.setText(invoiceList.getDescription()+" ("+invoiceList.getUomCode()+")");
//        }else {
//            viewHolder.description.setText(invoiceList.getDescription());
//        }
        viewHolder.description.setText(invoiceList.getDescription());
        viewHolder.uomtxt.setText(invoiceList.getUomCode());
       // viewHolder.linetxt.setText(invoiceList.getl());
        if(!invoiceList.getNetQuantity().isEmpty()) {
            viewHolder.qtyValue.setText((int) Double.parseDouble(invoiceList.getNetQuantity()) + "");
        }

//        if (invoiceList.getSaleType() != null &&  !invoiceList.getSaleType().equals("null")) {
//
//            if (invoiceList.getSaleType().equals("Return")) {
//                viewHolder.qtyValue.setText((int) Double.parseDouble(invoiceList.getNetQuantity()) + " (as Return)");
//            } else if (invoiceList.getSaleType().equals("FOC")) {
//                viewHolder.qtyValue.setText((int) Double.parseDouble(invoiceList.getNetQuantity()) + " ( as FOC)");
//            } else if (invoiceList.getSaleType().equals("Exchange")) {
//                viewHolder.qtyValue.setText((int) Double.parseDouble(invoiceList.getNetQuantity()) + " ( as Exch)");
//            } else {
//                viewHolder.qtyValue.setText((int) Double.parseDouble(invoiceList.getNetQuantity()) + "");
//            }
//        }
//        else{
//            viewHolder.qtyValue.setText((int) Double.parseDouble(invoiceList.getNetQuantity()) + "");
//        }
//        viewHolder.price.setText(Utils.fourDecimalPoint(Double.parseDouble((invoiceList.getPricevalue()))));
//        viewHolder.total.setText(Utils.fourDecimalPoint(Double.parseDouble(invoiceList.getTotal())));
    }

    @Override
    public int getItemCount() {
        return invoiceLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView slNo;
        private TextView code;
        private TextView description,uomtxt;
        private TextView qtyValue;
        private TextView price;
        private LinearLayout linetxtLay;
        private TextView total,linetxt;
        public ViewHolder(View view) {
            super(view);
            slNo=view.findViewById(R.id.sl_no_pickDel);
            description=view.findViewById(R.id.description_pickDel);
            uomtxt=view.findViewById(R.id.uom_pickDel);
            code=view.findViewById(R.id.inv_code_item_pickDel);
            qtyValue=view.findViewById(R.id.qty_pickDel);
            price=view.findViewById(R.id.price_pickDel);
            total=view.findViewById(R.id.total_pickDel);
            linetxt = view.findViewById(R.id.linetxt_pickDel);
            linetxtLay = view.findViewById(R.id.lineTxt_lay);

        }
    }

}