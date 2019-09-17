package com.example.ordermanagementsystem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder>{
   private ArrayList<Order> orderArrayList;
   Context context;
   ItemClickListener clickListener;
   MainActivity mainActivity;




    // Provide a suitable constructor (depends on the kind of dataset)
    public RecycleAdapter(Context context,ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        mainActivity=new MainActivity();
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.orderno.setText("Order no : "+orderArrayList.get(position).getOrderNo());
        holder.customerName.setText("name : "+orderArrayList.get(position).getCustomerName());
        holder.orderduedate.setText("date : "+orderArrayList.get(position).getOrderDueDate());
        holder.orderTotal.setText("total : "+orderArrayList.get(position).getTotal());
        holder.customerPhone.setText("phone : "+orderArrayList.get(position).getCustomerPhone());
        holder.customerAddress.setText("address : "+orderArrayList.get(position).getCustomerAddress());
//        holder.edit_IV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                Log.e(TAG, "onClick:edit "+orderArrayList.get(position).getCustomerName());
//        }
//        });

//        holder.bind(orderArrayList.get(position), onItemClickListener);

//        orderArrayList.clear();

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView orderno;

        public TextView orderduedate;

        public TextView customerName;

        public TextView customerPhone;

        public TextView orderTotal;

        public TextView customerAddress;
//        public ImageView edit_IV;
//        public ImageView delete_IV;

        public MyViewHolder(View v) {
            super(v);
            orderno = v.findViewById(R.id.order_no);
            orderTotal = v.findViewById(R.id.order_total);
            orderduedate = v.findViewById(R.id.order_due_date);
            customerAddress = v.findViewById(R.id.customer_address);
            customerPhone = v.findViewById(R.id.customer_phone);
            customerName = v.findViewById(R.id.customer_name);
//            edit_IV = v.findViewById(R.id.edit_order);
//            delete_IV = v.findViewById(R.id.delete_order);

            itemView.setOnClickListener(this);
//            orderno = v.findViewById(R.id.order_no);
//        @Override
        }

        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
