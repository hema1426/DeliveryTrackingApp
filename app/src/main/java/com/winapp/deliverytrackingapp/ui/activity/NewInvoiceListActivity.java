package com.winapp.deliverytrackingapp.ui.activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.FrameLayout;

import com.winapp.deliverytrackingapp.R;

import java.util.Objects;

public class NewInvoiceListActivity extends NavigationActivity{
@Override
protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        FrameLayout contentFrameLayout=findViewById(R.id.content_frame);
       // getLayoutInflater().inflate(R.layout.activity_new_invoice_list,contentFrameLayout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Invoices");
        }
}
