package com.rupayan_housing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.PurchaseReturnReportLayoutBinding;
import com.rupayan_housing.serverResponseModel.PurchaseReturnReportList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PurchaseReturnReportListAdapter extends RecyclerView.Adapter<PurchaseReturnReportListAdapter.viewHolder> {
    private Context context;
    List<PurchaseReturnReportList> profuctList;

    @NonNull
    @NotNull
    @Override
    public PurchaseReturnReportListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PurchaseReturnReportLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.purchase_return_report_layout, parent, false);
        return new PurchaseReturnReportListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PurchaseReturnReportListAdapter.viewHolder holder, int position) {
        PurchaseReturnReportList currentitem = profuctList.get(position);
        holder.itembinding.name.setText(":  "+currentitem.getCategory());
        holder.itembinding.date.setText(":  "+currentitem.getEntryDate());
        holder.itembinding.product.setText(":  "+currentitem.getProductTitle());
        if (currentitem.getBrandName() ==null){
            holder.itembinding.brand.setText( ":  ");
        }else {
            holder.itembinding.brand.setText( ":  "+currentitem.getBrandName());

        }
        holder.itembinding.miller.setText(":  "+currentitem.getEnterprizeName());
        holder.itembinding.store.setText(":  "+currentitem.getStoreName());
        holder.itembinding.supplier.setText(":  "+currentitem.getCustomerFname());
        holder.itembinding.quantity.setText(":  "+currentitem.getQuantity() +" "+currentitem.getName());

    }

    @Override
    public int getItemCount() {
        return profuctList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private PurchaseReturnReportLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull PurchaseReturnReportLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}