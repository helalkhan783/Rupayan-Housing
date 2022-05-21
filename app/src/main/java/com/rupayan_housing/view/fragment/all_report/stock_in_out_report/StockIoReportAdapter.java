package com.rupayan_housing.view.fragment.all_report.stock_in_out_report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.SaleReturnReportListLayoutBinding;
import com.rupayan_housing.view.fragment.all_report.stock_in_out_report.list_response.StockIOReportList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StockIoReportAdapter extends RecyclerView.Adapter<StockIoReportAdapter.viewHolder>  {
    private Context context;
    List<StockIOReportList> profuctList;
    @NonNull
    @NotNull
    @Override
    public StockIoReportAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SaleReturnReportListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.sale_return_report_list_layout, parent, false);
        return new StockIoReportAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StockIoReportAdapter.viewHolder holder, int position) {
        StockIOReportList currentitem = profuctList.get(position);
        holder.itembinding.name.setText(":  "+currentitem.getCategory());
        holder.itembinding.date.setText(":  "+currentitem.getEntryDate());
        holder.itembinding.product.setText(":  "+currentitem.getProductTitle());
        holder.itembinding.brand.setText(":  "+String.valueOf(currentitem.getBrandName()));
        holder.itembinding.miller.setText(":  "+currentitem.getEnterprizeName());
        holder.itembinding.store.setText(":  "+currentitem.getStoreName());
        if (currentitem.getCustomerFname() != null){
            holder.itembinding.supplier.setText(":  "+String.valueOf(currentitem.getCustomerFname()));
        }
        holder.itembinding.quantity.setText(":  "+currentitem.getQuantity());

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