package com.winapp.deliverytrackingapp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.winapp.deliverytrackingapp.ui.adapter.FilterCustomerAdapter;
import com.winapp.deliverytrackingapp.ui.model.CustomerDetails;
import com.winapp.deliverytrackingapp.ui.model.CustomerGroupModel;
import com.winapp.deliverytrackingapp.ui.model.CustomerModel;
import com.winapp.deliverytrackingapp.ui.utils.Constants;
import com.winapp.deliverytrackingapp.ui.utils.SessionManager;
import com.winapp.deliverytrackingapp.ui.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.winapp.deliverytrackingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FilterCustomerListActivity extends NavigationActivity {
    private RecyclerView customerView;
    ArrayList<CustomerModel> customerList;
    FilterCustomerAdapter customerNameAdapter;
    SweetAlertDialog pDialog;
    SessionManager session;
    private HashMap<String,String > user;
    private String companyId;
    EditText customerNameEdittext;
    TextView totalCustomers;
    BottomSheetBehavior behavior;
    LinearLayout transLayout;
    TextView optionCancel;
    TextView customerName;
    TextView customerId;
    FloatingActionButton createSalesOrder;
    FloatingActionButton createInvoice;
    FloatingActionButton deleteCustomer;
    FloatingActionButton showLocation;
   // DBHelper dbHelper;
    int RESULT_CODE=12;
    LinearLayout createInvoiceLayout;
    LinearLayout createOrderLayout;
    LinearLayout deleteLayout;
    LinearLayout showLocationLayout;
    ProgressDialog dialog;
    String message;
    private ArrayList<CustomerDetails> allCustomersList;
    private String outstandingAmount;
    private String username;

    public String createInvoiceSetting="false";
    private Spinner customerGroupSpinner;
    private ArrayList<CustomerGroupModel> customersGroupList;
    private int FILTER_CUSTOMER_CODE=134;

    private static String FILE = Environment.getExternalStorageDirectory()
            + "/HelloWorld.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_customer_list, contentFrameLayout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filter Customer");
        customerView=findViewById(R.id.customerList);
        session=new SessionManager(this);
        user=session.getUserDetails();
        companyId=user.get(SessionManager.KEY_COMPANY_CODE);
        username=user.get(SessionManager.KEY_USER_NAME);
        customerNameEdittext=findViewById(R.id.customer_search);
        totalCustomers=findViewById(R.id.total_customers);
        transLayout=findViewById(R.id.trans_layout);
        optionCancel=findViewById(R.id.option_cancel);
        customerName=findViewById(R.id.name);
        customerId=findViewById(R.id.cus_id);
        createSalesOrder=findViewById(R.id.create_sales);
        createInvoice=findViewById(R.id.create_invoice);
        deleteCustomer=findViewById(R.id.delete_customer);
        createInvoiceLayout=findViewById(R.id.create_invoice_layout);
        createOrderLayout=findViewById(R.id.create_order_layout);
        deleteLayout=findViewById(R.id.delete_layout);
        showLocationLayout=findViewById(R.id.location_layout);
        showLocation=findViewById(R.id.show_location);
        customerGroupSpinner=findViewById(R.id.customer_group);
      //  dbHelper=new DBHelper(this);

      /*  if (getIntent()!=null){
            message=getIntent().getStringExtra("Message");
            assert message != null;
            if (message.equals("newCustomer")){
                getCustomers();
            }else {
                customerList=new ArrayList<>();
                customerList=dbHelper.getAllCustomers();
                if (customerList.size()>0){
                    setAdapter(customerList);
                }else {
                    getCustomers();
                }
            }
        }else {
            customerList=new ArrayList<>();
            customerList=dbHelper.getAllCustomers();
            if (customerList.size()>0){
                setAdapter(customerList);
            }else {
                getCustomers();
            }
        }*/

        try {
            getCustomersGroups(username);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        ArrayList<SettingsModel> settings=dbHelper.getSettings();
//        if (settings!=null) {
//            if (settings.size() > 0) {
//                for (SettingsModel model : settings) {
//                    if (model.getSettingName().equals("create_invoice_switch")) {
//                        Log.w("SettingName:", model.getSettingName());
//                        Log.w("SettingValue:", model.getSettingValue());
//                        if (model.getSettingValue().equals("1")) {
//                            createInvoiceSetting="true";
//                        }else {
//                            createInvoiceSetting="false";
//                        }
//                    }
//                }
//            }
//        }

        customerNameEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String cusname=editable.toString();
                if (!cusname.isEmpty()){
                    filter(cusname);
                }
            }
        });

        View bottomSheet = findViewById(R.id.design_bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        getSupportActionBar().setTitle("Select Option");
                        transLayout.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");
                        getSupportActionBar().setTitle("Customers");
                        transLayout.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });

        optionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCloseBottomSheet();
            }
        });

        createOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCloseBottomSheet();
               // dbHelper.removeAllItems();
               // AddInvoiceActivityOld.customerId=customerId.getText().toString();
                Utils.setCustomerSession(FilterCustomerListActivity.this,customerId.getText().toString());
//                if (createInvoiceSetting.equals("true")){
//
//                    Intent intent=new Intent(getApplicationContext(),CreateNewInvoiceActivity.class);
//                    intent.putExtra("customerName",customerName.getText().toString());
//                    intent.putExtra("customerCode",customerId.getText().toString());
//                    intent.putExtra("activityFrom","SalesOrder");
//                    startActivity(intent);
//
//                }else {
//                    Intent intent=new Intent(FilterCustomerListActivity.this, AddInvoiceActivityOld.class);
//                    intent.putExtra("customerId",customerId.getText().toString());
//                    intent.putExtra("activityFrom","SalesOrder");
//                    startActivity(intent);
//                    viewCloseBottomSheet();
//                }

            }
        });


//        createSalesOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewCloseBottomSheet();
//                dbHelper.removeAllItems();
//                AddInvoiceActivityOld.customerId=customerId.getText().toString();
//                Utils.setCustomerSession(FilterCustomerListActivity.this,customerId.getText().toString());
//                if (createInvoiceSetting.equals("true")){
//
//                    Intent intent=new Intent(getApplicationContext(),CreateNewInvoiceActivity.class);
//                    intent.putExtra("customerName",customerName.getText().toString());
//                    intent.putExtra("customerCode",customerId.getText().toString());
//                    intent.putExtra("activityFrom","SalesOrder");
//                    startActivity(intent);
//
//                }else {
//                    Intent intent = new Intent(FilterCustomerListActivity.this, AddInvoiceActivityOld.class);
//                    intent.putExtra("customerId", customerId.getText().toString());
//                    intent.putExtra("activityFrom", "SalesOrder");
//                    startActivity(intent);
//                    viewCloseBottomSheet();
//                }
//            }
//        });

//        createInvoiceLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewCloseBottomSheet();
//                dbHelper.removeAllItems();
//                AddInvoiceActivityOld.customerId=customerId.getText().toString();
//                Utils.setCustomerSession(FilterCustomerListActivity.this,customerId.getText().toString());
//                if (createInvoiceSetting.equals("true")){
//
//                    Intent intent=new Intent(getApplicationContext(),CreateNewInvoiceActivity.class);
//                    intent.putExtra("customerName",customerName.getText().toString());
//                    intent.putExtra("customerCode",customerId.getText().toString());
//                    intent.putExtra("activityFrom","Invoice");
//                    startActivity(intent);
//
//                }else {
//                    Intent intent = new Intent(FilterCustomerListActivity.this, AddInvoiceActivityOld.class);
//                    intent.putExtra("customerId", customerId.getText().toString());
//                    intent.putExtra("activityFrom", "Invoice");
//                    startActivity(intent);
//                    viewCloseBottomSheet();
//                }
//            }
//        });
//
//        createInvoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewCloseBottomSheet();
//                dbHelper.removeAllItems();
//                AddInvoiceActivityOld.customerId=customerId.getText().toString();
//                Utils.setCustomerSession(FilterCustomerListActivity.this,customerId.getText().toString());
//                if (createInvoiceSetting.equals("true")){
//
//                    Intent intent=new Intent(getApplicationContext(),CreateNewInvoiceActivity.class);
//                    intent.putExtra("customerName",customerName.getText().toString());
//                    intent.putExtra("customerCode",customerId.getText().toString());
//                    intent.putExtra("activityFrom","Invoice");
//                    startActivity(intent);
//
//                }else {
//                    Intent intent = new Intent(FilterCustomerListActivity.this, AddInvoiceActivityOld.class);
//                    intent.putExtra("customerId", customerId.getText().toString());
//                    intent.putExtra("activityFrom", "Invoice");
//                    startActivity(intent);
//                    viewCloseBottomSheet();
//                }
//            }
//        });

//        showLocationLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(FilterCustomerListActivity.this, MapsActivity.class);
//                intent.putExtra("customerName",customerName.getText().toString());
//                intent.putExtra("customerId",customerId.getText().toString());
//                intent.putExtra("outstanding",outstandingAmount);
//                intent.putExtra("address","13,North Street , T.Nagar, Chennai..");
//                intent.putExtra("latitude","9.939093");
//                intent.putExtra("longitude","78.121719");
//                startActivity(intent);
//                viewCloseBottomSheet();
//            }
//        });
//
//        showLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(FilterCustomerListActivity.this, MapsActivity.class);
//                intent.putExtra("customerName",customerName.getText().toString());
//                intent.putExtra("customerId",customerId.getText().toString());
//                intent.putExtra("outstanding",outstandingAmount);
//                intent.putExtra("address","13,North Street , T.Nagar, Chennai..");
//                intent.putExtra("latitude","9.939093");
//                intent.putExtra("longitude","78.121719");
//                startActivity(intent);
//                viewCloseBottomSheet();
//            }
//        });

        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRemoveAlert(customerId.getText().toString());
            }
        });

        deleteCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRemoveAlert(customerId.getText().toString());
            }
        });

    }

    public void setCustomerGroupSpinner(ArrayList<CustomerGroupModel> customersGroupList){
        ArrayAdapter<CustomerGroupModel> adapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,customersGroupList);
        customerGroupSpinner.setAdapter(adapter);
        customerGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String groupCode=customersGroupList.get(position).getCustomerGroupCode();
                String groupName=customersGroupList.get(position).getCustomerGroupName();
                getCustomers(groupCode);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void showRemoveAlert(String customerId){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                // .setTitleText("Are you sure?")
                .setContentText("Are you sure want delete Customer ?")
                .setConfirmText("YES")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        viewCloseBottomSheet();
//                        try {
//                            sDialog.dismiss();
//                            viewCloseBottomSheet();
//                            setDeleteCustomer(customerId);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                }).showCancelButton(true)
                .setCancelText("No")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                }).show();
    }


    // http://ezysales.sg:457/es/data/api/MasterApi/DeleteCustomer?Requestdata= {"CompanyCode:"1","CustomerCode":"18455"}
//    private void setDeleteCustomer(String customerCode) throws JSONException {
//        // Initialize a new RequestQueue instance
//
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("CompanyCode",companyId);
//        jsonObject.put("CustomerCode",customerCode);
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String url= Utils.getBaseUrl(this) +"MasterApi/DeleteCustomer?Requestdata="+jsonObject.toString();
//        // Initialize a new JsonArrayRequest instance
//        Log.w("Given_url:",url);
//        dialog=new ProgressDialog(FilterCustomerListActivity.this);
//        dialog.setMessage("Updating Customer List...");
//        dialog.setCancelable(false);
//        dialog.show();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.DELETE,
//                url,
//                null,
//                response -> {
//                    try{
//                        Log.w("Delete_Response_is:",response.toString());
//                        if (response.length()>0){
//                            boolean issaved=response.optBoolean("IsSaved");
//                            String result=response.optString("Result");
//                            if (issaved && result.equals("pass")){
//                                finish();
//                                startActivity(getIntent());
//                            }else {
//                                Toast.makeText(getApplicationContext(),"Error in Deleting Customer",Toast.LENGTH_LONG).show();
//                            }
//                        }
//                        dialog.dismiss();
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }, error -> {
//            // Do something when error occurred
//            dialog.dismiss();
//            Log.w("Error_throwing:",error.toString());
//        }){
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> params = new HashMap<>();
//                String creds = String.format("%s:%s", Constants.API_SECRET_CODE, Constants.API_SECRET_PASSWORD);
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                params.put("Authorization", auth);
//                return params;
//            }
//        };
//        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 50000;
//            }
//            @Override
//            public int getCurrentRetryCount() {
//                return 50000;
//            }
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
//        // Add JsonArrayRequest to the RequestQueue
//        requestQueue.add(jsonObjectRequest);
//    }


    public void viewCloseBottomSheet(){
        //  hideKeyboard();
        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        // get the Customer name from the local db
    }

    public void getCustomersGroups(String username){
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url= Constants.BASEURL +"BPGroupList";
        customerList=new ArrayList<>();
        allCustomersList=new ArrayList<>();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("User",username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.w("Given_urlcustGroup:",url+jsonObject);
        dialog=new ProgressDialog(FilterCustomerListActivity.this);
        dialog.setMessage("Loading Customers Groups...");
        dialog.setCancelable(false);
        dialog.show();
        customersGroupList=new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url, jsonObject,
                response -> {
                    try {
                        Log.w("SAP_CUSTOMERS_GROUP:", response.toString());
                        String statusCode=response.optString("statusCode");
                        if (statusCode.equals("1")){
                            JSONArray customerDetailArray=response.optJSONArray("responseData");
                            for (int i=0;i<customerDetailArray.length();i++){
                                JSONObject object=customerDetailArray.optJSONObject(i);
                                CustomerGroupModel model = new CustomerGroupModel();
                                model.setCustomerGroupCode(object.optString("groupCode"));
                                model.setCustomerGroupName(object.optString("groupName"));
                                customersGroupList.add(model);
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Error,in getting Customer Group list",Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                        if (customersGroupList.size()>0){
                            setCustomerGroupSpinner(customersGroupList);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    dialog.dismiss();
                    // Do something when error occurred
                    Log.w("Error_throwing:",error.toString());
                }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                String creds = String.format("%s:%s", Constants.API_SECRET_CODE, Constants.API_SECRET_PASSWORD);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }


    public void getCustomers(String groupCode){
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url=Constants.BASEURL +"CustomerList";
        customerList=new ArrayList<>();
        allCustomersList=new ArrayList<>();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("GroupCode",groupCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.w("Given_url:",url);
        dialog=new ProgressDialog(FilterCustomerListActivity.this);
        dialog.setMessage("Loading Customers List...");
        dialog.setCancelable(false);
        dialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    try {
                        Log.w("Response_SAP_CUSTOMERS:", response.toString());

                        // {"statusCode":1,"statusMessage":"Success",
                        // "responseData":[{"customerCode":"CUS\/686","customerName":"VH FACTORY","groupCode":"100",
                        // "contactPerson":"","creditLimit":"150.000000","currencyCode":"SGD","currencyName":"Singapore Dollar",
                        // "taxType":"","taxCode":"SR","taxName":"Sales Standard Rated Supplier SR","taxPercentage":"7.000000",
                        // "balance":"21.600000","outstandingAmount":"128.400000","address":"","street":"","city":"","state":"",
                        // "zipCode":"","country":"","createDate":"13\/07\/2021","updateDate":"30\/07\/2021","active":"N","remark":""},
                        // {"customerCode":"WinApp","customerName":"WinApp","groupCode":"100","contactPerson":"","creditLimit":"0.000000"
                        // ,"currencyCode":"SGD","currencyName":"Singapore Dollar","taxType":"I","taxCode":"","taxName":"","taxPercentage":"",
                        // "balance":"0.000000","outstandingAmount":"0.000000","address":"","street":"","city":"","state":"","zipCode":""
                        // ,"country":"","createDate":"29\/07\/2021","updateDate":"30\/07\/2021","active":"N","remark":""}]}

                        String statusCode=response.optString("statusCode");
                        if (statusCode.equals("1")){
                            JSONArray customerDetailArray=response.optJSONArray("responseData");
                            for (int i=0;i<customerDetailArray.length();i++){
                                JSONObject object=customerDetailArray.optJSONObject(i);
                                //  if (customerObject.optBoolean("IsActive")) {
                                CustomerModel model = new CustomerModel();
                                model.setCustomerCode(object.optString("customerCode"));
                                model.setCustomerName(object.optString("customerName"));
                                model.setAddress1(object.optString("address"));
                                model.setAddress2(object.optString("street"));
                                model.setAddress3(object.optString("city"));
                                model.setCustomerAddress(object.optString("address"));
                                model.setHaveTax(object.optString("HaveTax"));
                                model.setTaxType(object.optString("taxType"));
                                model.setTaxPerc(object.optString("taxPercentage"));
                                model.setTaxCode(object.optString("taxCode"));
                                model.setBillDiscPercentage(object.optString("discountPercentage"));

                                //  model.setCustomerBarcode(object.optString("BarCode"));
                                // model.setCustomerBarcode(String.valueOf(i));
                                if (object.optString("outstandingAmount").equals("null") || object.optString("outstandingAmount").isEmpty()) {
                                    model.setOutstandingAmount("0.00");
                                } else {
                                    model.setOutstandingAmount(object.optString("outstandingAmount"));
                                }
                                customerList.add(model);
                                // }
                            }
                        }else {
                            Toast.makeText(getApplicationContext(),"Error,in getting Customer list",Toast.LENGTH_LONG).show();
                        }

                        dialog.dismiss();
                        if (customerList.size()>0){
                            // new InsertCustomerTask().execute();
                            setAdapter(customerList);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    dialog.dismiss();
                    // Do something when error occurred
                    Log.w("Error_throwing:",error.toString());
                }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                String creds = String.format("%s:%s", Constants.API_SECRET_CODE, Constants.API_SECRET_PASSWORD);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private class InsertCustomerTask extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(Void...arg0) {
//            dbHelper.removeAllCustomers();
//            dbHelper.insertCustomerList(customerList);
            return "You are at PostExecute";
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private void setAdapter(ArrayList<CustomerModel> customerNames) {
        customerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        totalCustomers.setText(customerNames.size()+" Customers");
        customerNameAdapter=new FilterCustomerAdapter(this, customerNames, new FilterCustomerAdapter.OnMoreButtonClicked() {
            @Override
            public void moreOption(String customer,String customer_code,String outstanding) {
            /*    if (getIntent()!=null){
                    String from=getIntent().getStringExtra("from");
                    if (from.equals("do") || from.equals("so") || from.equals("iv")){
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("customerCode",customer_code);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }else {
                        customerName.setText(customer);
                        customerId.setText(customer_code);
                        outstandingAmount=outstanding;
                        viewCloseBottomSheet();
                    }
                }else {
                    customerName.setText(customer);
                    customerId.setText(customer_code);
                    outstandingAmount=outstanding;
                    viewCloseBottomSheet();
                }*/
            }

            @Override
            public void createInvoice(String customerCode, String customerName, String taxcode, String taxperc, String taxtype) {
                Intent intent=new Intent();
                intent.putExtra("customerName",customerName);
                intent.putExtra("customerCode",customerCode);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        customerView.setAdapter(customerNameAdapter);
    }

    private void filter(String text) {
        try {
            //new array list that will hold the filtered data
            ArrayList<CustomerModel> filterdNames = new ArrayList<>();
            //looping through existing elements
            for (CustomerModel s : customerList) {
                //if the existing elements contains the search input
                if (s.getCustomerName().toLowerCase().contains(text.toLowerCase()) || s.getCustomerCode().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    filterdNames.add(s);
                }
            }
            //calling a method of the adapter class and passing the filtered list
            customerNameAdapter.filterList(filterdNames);

        }catch (Exception ex){
            Log.e("Error_in_filter", Objects.requireNonNull(ex.getMessage()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sorting_menu, menu);
//        MenuItem menuItem=menu.findItem(R.id.action_filter);
//        menuItem.setVisible(false);
        MenuItem addcustomer=menu.findItem(R.id.action_add);
        addcustomer.setVisible(false);
//        MenuItem barcode=menu.findItem(R.id.action_barcode);
//        barcode.setVisible(false);
//        ArrayList<UserRoll> userRolls=helper.getUserPermissions();
//        if (userRolls.size()>0) {
//            for (UserRoll roll : userRolls) {
//                if (roll.getFormName().equals("Customer Add")){
//                    if (roll.getHavePermission().equals("true")){
//                        addcustomer.setVisible(true);
//                    }else {
//                        addcustomer.setVisible(false);
//                    }
//                }
          //  }
    //    }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {//finish();
            // onBackPressed();
            finish();
        }
//        else if (item.getItemId() == R.id.action_barcode) {
//            if (customerNameEdittext.getText().toString().length()>0){
//                customerNameEdittext.clearFocus();
//                customerNameEdittext.setText("");
//                setAdapter(customerList);
//            }
//            Intent intent=new Intent(FilterCustomerListActivity.this, BarCodeScanner.class);
//            startActivityForResult(intent,RESULT_CODE);
//        }else if (item.getItemId() == R.id.action_add){
//            if (customerNameEdittext.getText().toString().length()>0){
//                customerNameEdittext.clearFocus();
//                customerNameEdittext.setText("");
//                setAdapter(customerList);
//            }
//            Intent intent=new Intent(getApplicationContext(),AddCustomerActivity.class);
//            startActivity(intent);
//        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_CODE) {
                String barcodeText=data.getExtras().getString("Contents");
                if (customerList.size()>0){
                    for (CustomerModel model:customerList){
                        if (model.getCustomerBarcode().equals(barcodeText)){
                            customerName.setText(model.getCustomerName());
                            customerId.setText(model.getCustomerCode());
                            viewCloseBottomSheet();
                        }
                    }
                }else {
                    Toast.makeText(FilterCustomerListActivity.this,"No Such Customer",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //finish();
        Intent intent=new Intent(FilterCustomerListActivity.this,NavigationActivity.class);
        startActivity(intent);
        finish();
    }

}