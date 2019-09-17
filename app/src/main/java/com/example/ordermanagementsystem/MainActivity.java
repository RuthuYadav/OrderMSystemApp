package com.example.ordermanagementsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity implements ItemClickListener{


    private static final String TAG = SplashActivity.class.getSimpleName();

    LinearLayoutManager layoutManager;
    RecycleAdapter recycleAdapter;
    ArrayList<Order> orderArrayList = new ArrayList<>();
    Order order;
    AlertDialog.Builder dialogBuilder;
    LayoutInflater alertInflater;
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText OrderNo,OrderDuaDate,OrderTotal,CustomerAddress,CustomerName,CustomerPhone;
    TextView addButton,cancelButton;
    AlertDialog dalertDialog;
    Boolean CheckEditTextEmpty ;
    double latitude,longitude;
    GPSTracker gps;
    String countryName,city;


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.add_order)
    ImageView addORder;

    @OnClick(R.id.add_order)
    public void addOrderCLick(){
        openAlertBoxPicker();
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
         database = FirebaseDatabase.getInstance();
         myRef = database.getReference("Order");
        getLatLng();
        dialogBuilder = new AlertDialog.Builder(this);
        alertInflater = getLayoutInflater();
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Write a message to the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderArrayList.clear();
                // This method is called once with the initial value an again
                // whenever data at this location is updated.
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post

                    Order order1= postSnapshot.getValue(Order.class);
                    Log.d(TAG, "Value is: " + order1);
                    orderArrayList.add(order1);
                    Log.d(TAG, "Valuessss: " + orderArrayList.size());
                }
                // specify an adapter (see also next example)
                recycleAdapter = new RecycleAdapter(getApplicationContext(),orderArrayList);
                recyclerView.setAdapter(recycleAdapter);
                recycleAdapter.setClickListener(MainActivity.this);
                Log.d(TAG, "Valuessss:1 " + orderArrayList.size());

//                orderArrayList.clear();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void openAlertBoxPicker() {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = li.inflate(R.layout.new_order_dialog, null, false);
         addButton = (TextView) view.findViewById(R.id.add_button);
         cancelButton = (TextView) view.findViewById(R.id.cancel_button);
        OrderNo = (EditText) view.findViewById(R.id.orderNo);
        OrderDuaDate = (EditText) view.findViewById(R.id.orderDuedate);
        OrderTotal = (EditText) view.findViewById(R.id.orderTotal);
        CustomerAddress = (EditText) view.findViewById(R.id.customer_address);
        CustomerName = (EditText) view.findViewById(R.id.customer_name);
        CustomerPhone =(EditText) view.findViewById(R.id.customer_phone);
        final TextView title = (TextView)view.findViewById(R.id.title);



//        FancyButton dfancyButton = (FancyButton) view.findViewById(R.id.submit);

        dalertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        dalertDialog.setCanceledOnTouchOutside(false);
        dalertDialog.show();
        dalertDialog.setCancelable(false);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mview) {
                Log.e(TAG, "onClick:mview "+OrderNo.getText().toString());
                CheckEditTextIsEmptyOrNot(OrderNo.getText().toString(),OrderDuaDate.getText().toString(),OrderTotal.getText().toString(),CustomerAddress.getText().toString(),CustomerName.getText().toString(),CustomerPhone.getText().toString());
                if(CheckEditTextEmpty == true)
                {
                    getFirebase(OrderNo.getText().toString(),OrderDuaDate.getText().toString(),OrderTotal.getText().toString(),CustomerAddress.getText().toString(),CustomerName.getText().toString(),CustomerPhone.getText().toString());
                    Toast.makeText(getApplicationContext(),"Entry saved", Toast.LENGTH_LONG).show();
                    ClearEditTextData();
                    dalertDialog.dismiss();
                }
                else {

                    Toast.makeText(getApplicationContext(),"Entry not saved as not all data entered. Complete all entries and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View nview) {
                Log.e(TAG, "onClick: nview" );
                dalertDialog.dismiss();
    }});
    }
    public void getFirebase(String order_no,String orderdue_date,String order_total,String customer_address,String customer_name,String customer_phone){
        String id = myRef.push().getKey();
        Order order = new Order(id,order_no,orderdue_date,customer_address,customer_name,customer_phone,order_total,countryName,city);
        myRef.child(id).setValue(order);
    }
    @Override
    public void onClick(View view, int position) {
        openAlertBoxPicker();
        addButton.setText("Update");
        cancelButton.setText("Delete");

        final Order order = orderArrayList.get(position);
        OrderNo.setText(order.getOrderNo());
        OrderDuaDate.setText(order.getOrderDueDate());
        OrderTotal.setText(order.getTotal());
        CustomerAddress.setText(order.getCustomerAddress());
        CustomerName.setText(order.getCustomerName());
        CustomerPhone.setText(order.getCustomerPhone());
        if (addButton.getText().equals("Update")){
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View mview) {
                    Log.e(TAG, "onClick:update "+OrderNo.getText().toString());
                    getLatLng();
                    updateOrder(order.getId(),OrderNo.getText().toString(),OrderDuaDate.getText().toString(),OrderTotal.getText().toString(),CustomerAddress.getText().toString(),CustomerName.getText().toString(),CustomerPhone.getText().toString());
//                    getFirebase(OrderNo.getText().toString(),OrderDuaDate.getText().toString(),OrderTotal.getText().toString(),CustomerAddress.getText().toString(),CustomerName.getText().toString(),CustomerPhone.getText().toString());

                }
            });
        }
        if (cancelButton.getText().equals("Delete")){
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View nview) {
                    Log.e(TAG, "onClick: delete" );
                    confirmDelete(getApplicationContext(),order.getId());
                }});
        }
        Log.e(TAG, "OnItemCLick: "+order.getCustomerName() );


    }
    public boolean updateOrder(final String id,String order_no,String orderdue_date,String order_total,String customer_address,String customer_name,String customer_phone) {
            //getting the specified Order reference
        CheckEditTextIsEmptyOrNot(OrderNo.getText().toString(),OrderDuaDate.getText().toString(),OrderTotal.getText().toString(),CustomerAddress.getText().toString(),CustomerName.getText().toString(),CustomerPhone.getText().toString());
        if(CheckEditTextEmpty == true)
        {
            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Order").child(id);
            //updating Order
            Order order = new Order(id,order_no,orderdue_date,customer_address,customer_name,customer_phone,order_total,countryName,city);
            dR.setValue(order);
            dalertDialog.dismiss();
        }else {

            Toast.makeText(getApplicationContext(),"Entry not saved as not all data entered. Complete all entries and try again", Toast.LENGTH_LONG).show();
        }
        return true;
    }
    public boolean deleteOrder(Context context,String id) {
        //getting the specified Order reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Order").child(id);
        dR.removeValue();
//        //removing all Orders
        Toast.makeText(context, "Order Deleted", Toast.LENGTH_LONG).show();
        return true;
    }
    public void CheckEditTextIsEmptyOrNot(String order_no,String orderdue_date,String order_total,String customer_address,String customer_name,String customer_phone){

        if(TextUtils.isEmpty(order_no) || TextUtils.isEmpty(orderdue_date) || TextUtils.isEmpty(order_total) || TextUtils.isEmpty(customer_address) || TextUtils.isEmpty(customer_name) || TextUtils.isEmpty(customer_phone)){

            CheckEditTextEmpty = false ;

        }
        else {
            CheckEditTextEmpty = true ;
        }
    }

    public void ClearEditTextData(){

        OrderNo.getText().clear();
        OrderDuaDate.getText().clear();
        OrderTotal.getText().clear();
        CustomerAddress.getText().clear();
        CustomerName.getText().clear();
        CustomerPhone.getText().clear();
//        OrderTotal.getText().clear();

    }
    public void getLatLng(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, 10);
        }
        else {
            gps = new GPSTracker(getApplicationContext(),this);
            if (gps.canGetLocation())
            {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                Log.e("lat...",latitude+",."+longitude);
            }
            else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();

            }
        }
        Geocoder gcd=new Geocoder(getApplicationContext());
        String LAT = Double.toString(latitude);
//
        try {
            List<Address> addresses=gcd.getFromLocation(latitude,longitude,1);
            Log.v("Address:","address:::::::::::"+addresses);
             countryName = addresses.get(0).getCountryName();
             city = addresses.get(0).getLocality();
            Log.e(TAG, "getLatLng: "+countryName+".."+city );
//            String city = addresses.get(1).getLocality();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
public void confirmDelete(Context context,final String id){
    LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View view = li.inflate(R.layout.confirm_delete_dialog, null, false);
    TextView yes = (TextView) view.findViewById(R.id.yes_button);
    TextView no= (TextView) view.findViewById(R.id.no_button);



//        FancyButton dfancyButton = (FancyButton) view.findViewById(R.id.submit);

    dalertDialog = new AlertDialog.Builder(this)
            .setView(view)
            .create();
    dalertDialog.setCanceledOnTouchOutside(false);
    dalertDialog.show();
    dalertDialog.setCancelable(false);
    yes.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View mview) {
            deleteOrder(getApplicationContext(),id);
            dalertDialog.dismiss();
            finish();

        }
    });

    no.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View mview) {
            dalertDialog.dismiss();
        }
    });
}
}
