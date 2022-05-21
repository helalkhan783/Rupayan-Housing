package com.rupayan_housing.view.fragment.all_report.reconcilation_report;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.ReconciliationReportListLayoutBinding;
import com.rupayan_housing.view.fragment.all_report.reconcilation_report.list_response.ReconciliationReportProfuctList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReconciliationReportListAdapter extends RecyclerView.Adapter<ReconciliationReportListAdapter.viewHolder>  {
    private   FragmentActivity activity;
    private   List<ReconciliationReportProfuctList> profuctList;
    @NonNull
    @NotNull
    @Override
    public ReconciliationReportListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ReconciliationReportListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.reconciliation_report_list_layout, parent, false);
        return new ReconciliationReportListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReconciliationReportListAdapter.viewHolder holder, int position) {
        ReconciliationReportProfuctList currentitem = profuctList.get(position);
        holder.itembinding.name.setText(":  "+currentitem.getCategory());
        holder.itembinding.date.setText(":  "+currentitem.getEntryDate());
        holder.itembinding.product.setText(":  "+currentitem.getProductTitle());
        holder.itembinding.type.setText(":  "+currentitem.getReconciliationType());
        if (currentitem.getBrandName() == null){
            holder.itembinding.brand.setText(":  ");
        }
        else {
            holder.itembinding.brand.setText(":  "+ currentitem.getBrandName());

        }
        holder.itembinding.miller.setText(":  "+currentitem.getEnterprizeName());
        holder.itembinding.store.setText(":  "+currentitem.getStoreName());
        if (currentitem.getCustomerFname() != null){
                    holder.itembinding.supplier.setText(":  "+currentitem.getCustomerFname());

        }
        holder.itembinding.quantity.setText(":  "+currentitem.getQuantity());

    }

    @Override
    public int getItemCount() {
        return profuctList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ReconciliationReportListLayoutBinding itembinding;
        public viewHolder(@NonNull @NotNull ReconciliationReportListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}