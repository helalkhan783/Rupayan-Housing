package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.PackegingReportListLayoutBinding;
import com.rupayan_housing.view.fragment.all_report.packaging_report.list.ReportPacketingList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PackettingReportListAdapter extends RecyclerView.Adapter<PackettingReportListAdapter.viewHolder> {
    private FragmentActivity activity;
    private List<ReportPacketingList> list;

    @NonNull
    @NotNull
    @Override
    public PackettingReportListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PackegingReportListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.packeging_report_list_layout, parent, false);
        return new PackettingReportListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PackettingReportListAdapter.viewHolder holder, int position) {
        ReportPacketingList currentitem = list.get(position);
        holder.itembinding.date.setText(":  "+currentitem.getEntryDate());
        holder.itembinding.product.setText(":  "+currentitem.getProductTitle());
        holder.itembinding.miller.setText(":  "+currentitem.getEnterpriseName());
        holder.itembinding.store.setText( ":  "+currentitem.getStoreName());
        holder.itembinding.referrer.setText(":  "+currentitem.getCustomerFname());
        holder.itembinding.quantity.setText(":  "+currentitem.getQuantity() +" "+currentitem.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private PackegingReportListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull PackegingReportListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
