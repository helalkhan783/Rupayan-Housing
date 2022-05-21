package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.serverResponseModel.ExpenseOrdersResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpenseDueOrdersAdapter extends RecyclerView.Adapter<ExpenseDueOrdersAdapter.MyHolder> {
    public static Set<String> selectedOrderList = new HashSet<>();//store the selected order list for send server

    FragmentActivity context;
    List<ExpenseOrdersResponse> ordersList;

    public ExpenseDueOrdersAdapter(FragmentActivity context, List<ExpenseOrdersResponse> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public ExpenseDueOrdersAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.receive_due_rv_model, parent, false);
        return new ExpenseDueOrdersAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseDueOrdersAdapter.MyHolder holder, int position) {
        ExpenseOrdersResponse currentOrder = ordersList.get(position);
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

       /*     if (isChecked) {
                selectedOrderList.add(String.valueOf(ordersList.get(position).getOrderID()));
            } else {
                if (selectedOrderList.isEmpty()){
                    return;
                }else {
                    if (selectedOrderList.contains(String.valueOf(ordersList.get(position).getOrderID()))) {
                       try {
                           selectedOrderList.remove(position);
                       }catch (Exception e){
                           Log.d("EXCEPTION",e.getLocalizedMessage());
                       }
                    }
                }
            }*/
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
