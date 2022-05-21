package com.rupayan_housing.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rupayan_housing.R;
import com.rupayan_housing.serverResponseModel.Order;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerOrderAdapter extends RecyclerView.Adapter<CustomerOrderAdapter.MyHolder> {
    public static Set<String> selectedOrderList = new HashSet<>();//store the selected order list for send server

    FragmentActivity context;
    List<Order> ordersList;

    public CustomerOrderAdapter(FragmentActivity activity, List<Order> orders) {
        this.context = activity;
        this.ordersList = orders;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.receive_due_rv_model, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Order currentOrder = ordersList.get(position);
        holder.orderId.setText("#" + currentOrder.getOrderID());
        holder.totalPaid.setText(currentOrder.getPaidAmount() + " TK");
        holder.totalAmount.setText(currentOrder.getTotalAmount() + " TK");
        holder.orderDue.setText(currentOrder.getDue() + " TK");


        holder.selectedOrder.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                selectedOrderList.add(String.valueOf(ordersList.get(position).getOrderID()));
            } else {
                selectedOrderList.remove(String.valueOf(ordersList.get(position).getOrderID()));
            }
            Log.d("ok", "ok");
            Log.d("OK", new Gson().toJson(ordersList));
            Log.d("OK", String.valueOf(selectedOrderList.size()));
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.orderIdTv)
        TextView orderId;
        @BindView(R.id.totalPaidTv)
        TextView totalPaid;
        @BindView(R.id.totalAmountTv)
        TextView totalAmount;
        @BindView(R.id.orderDueTv)
        TextView orderDue;
        @BindView(R.id.selectedOrder)
        CheckBox selectedOrder;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
