package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.PackegingReportListLayoutBinding;
import com.rupayan_housing.databinding.ProductionReportListLayoutBinding;
import com.rupayan_housing.serverResponseModel.ProductionReportList;
import com.rupayan_housing.view.fragment.all_report.packeting_report.list.ReportPackegingList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductionReportLisAdapter extends RecyclerView.Adapter<ProductionReportLisAdapter.viewHolder> {
    private FragmentActivity activity;
    private List<ProductionReportList> list;

    public ProductionReportLisAdapter(FragmentActivity activity, List<ProductionReportList> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ProductionReportLisAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ProductionReportListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.production_report_list_layout, parent, false);
        return new ProductionReportLisAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductionReportLisAdapter.viewHolder holder, int position) {
        ProductionReportList currentitem = list.get(position);
        holder.itembinding.date.setText(":  "+currentitem.getEntryDate());
        holder.itembinding.product.setText(":  "+currentitem.getProductTitle());
        holder.itembinding.category.setText(":  "+currentitem.getEnterprizeName());
        holder.itembinding.store.setText( ":  "+currentitem.getStoreName());
         holder.itembinding.quantity.setText(":  "+currentitem.getQuantity() +" "+currentitem.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ProductionReportListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull ProductionReportListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
