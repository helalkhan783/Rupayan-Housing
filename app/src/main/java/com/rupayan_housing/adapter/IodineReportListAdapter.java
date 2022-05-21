package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.PackegingReportListLayoutBinding;
import com.rupayan_housing.view.fragment.all_report.iodine_used_report.list.IodineReportList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IodineReportListAdapter extends RecyclerView.Adapter<IodineReportListAdapter.viewHolder> {
    private FragmentActivity activity;
    private List<IodineReportList> list;

    @NonNull
    @NotNull
    @Override
    public IodineReportListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PackegingReportListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.packeging_report_list_layout, parent, false);
        return new IodineReportListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull IodineReportListAdapter.viewHolder holder, int position) {
        IodineReportList currentitem = list.get(position);
        holder.itembinding.date.setText(":  " + currentitem.getEntryDate());
        holder.itembinding.product.setText(":  " + currentitem.getProductTitle());

        if (currentitem.getEnterpriseName() != null) {
            holder.itembinding.miller.setText(":  " + currentitem.getEnterpriseName());

        }
        if (currentitem.getCustomerFname() != null) {
            holder.itembinding.referrer.setText(":  " + currentitem.getCustomerFname());

        }
        holder.itembinding.store.setText(":  " + currentitem.getStoreName());
        holder.itembinding.quantity.setText(":  " + currentitem.getQuantity() + " " + currentitem.getName());
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
