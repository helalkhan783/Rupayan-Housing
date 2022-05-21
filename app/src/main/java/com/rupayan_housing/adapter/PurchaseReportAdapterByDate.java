package com.rupayan_housing.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rupayan_housing.R;
import com.rupayan_housing.databinding.PurchaseHistoryListLayoutBinding;
import com.rupayan_housing.databinding.PurchaseReportByDateLayoutBinding;
import com.rupayan_housing.serverResponseModel.PurchaseHistoryList;
import com.rupayan_housing.serverResponseModel.PurchaseReportProductList;
import com.rupayan_housing.utils.ImageBaseUrl;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PurchaseReportAdapterByDate extends RecyclerView.Adapter<PurchaseReportAdapterByDate.viewHolder>  {
    private Context context;
    List<PurchaseReportProductList> lists;

    @NonNull
    @NotNull
    @Override
    public PurchaseReportAdapterByDate.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PurchaseReportByDateLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.purchase_report_by_date_layout, parent, false);
        return new PurchaseReportAdapterByDate.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PurchaseReportAdapterByDate.viewHolder holder, int position) {
        PurchaseReportProductList currentitem = lists.get(position);
        holder.itembinding.name.setText(":  "+currentitem.getCategory());
        holder.itembinding.date.setText(":  "+currentitem.getEntryDate());
        holder.itembinding.product.setText(":  "+currentitem.getProductTitle());

        if(currentitem.getBrandName() == null){
            holder.itembinding.brand.setText(":  ");
        }else {
            holder.itembinding.brand.setText(":  "+currentitem.getBrandName());

        }
        holder.itembinding.miller.setText(":  "+currentitem.getEnterprizeName());
        holder.itembinding.store.setText(":  "+currentitem.getStoreName());
        holder.itembinding.supplier.setText(":  "+currentitem.getCustomerFname());
        String unit = "";
        if (currentitem.getName() !=null){
            unit =currentitem.getName();
        }
        holder.itembinding.quantity.setText(":  "+currentitem.getQuantity() +" "+unit);

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private PurchaseReportByDateLayoutBinding itembinding;
        public viewHolder(@NonNull @NotNull PurchaseReportByDateLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}