package com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.sale_return_report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.SaleReturnReportListLayoutBinding;
import com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.sale_return_report.sale_return_report_list.SaleReturnReportProfuctList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SaleReturnReportListAdapter extends RecyclerView.Adapter<SaleReturnReportListAdapter.viewHolder> {
    private Context context;
    List<SaleReturnReportProfuctList> profuctList;

    @NonNull
    @NotNull
    @Override
    public SaleReturnReportListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SaleReturnReportListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.sale_return_report_list_layout, parent, false);
        return new SaleReturnReportListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SaleReturnReportListAdapter.viewHolder holder, int position) {
        SaleReturnReportProfuctList currentitem = profuctList.get(position);
        holder.itembinding.name.setText(":  " + currentitem.getCategory());
        holder.itembinding.date.setText(":  " + currentitem.getEntryDate());
        holder.itembinding.product.setText(":  " + currentitem.getProductTitle());
        if (currentitem.getBrandName() !=null){
            holder.itembinding.brand.setText(String.valueOf(":  " + currentitem.getBrandName()));
        }
        holder.itembinding.miller.setText(":  " + currentitem.getEnterprizeName());
        holder.itembinding.store.setText(":  " + currentitem.getStoreName());
        if (currentitem.getCustomerFname() == null) {
            holder.itembinding.supplier.setText(":  ");
        } else {
            holder.itembinding.supplier.setText(":  " + currentitem.getCustomerFname());

        }
        holder.itembinding.quantity.setText(":  " + currentitem.getQuantity()+" "+currentitem.getName());

    }

    @Override
    public int getItemCount() {
        return profuctList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private SaleReturnReportListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull SaleReturnReportListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}