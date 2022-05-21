package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.IodizationWiseMillDashboardBinding;
import com.rupayan_housing.serverResponseModel.IssueMonitoringList;
import com.rupayan_housing.serverResponseModel.JaninaList;
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.replace.KgToTon;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ZoneListHierachyAdapter extends RecyclerView.Adapter<ZoneListHierachyAdapter.ViewHolder> {
    FragmentActivity context;
    List<JaninaList> licenceExpires;

    public ZoneListHierachyAdapter(FragmentActivity context, List<JaninaList> licenceExpires) {
        this.context = context;
        this.licenceExpires = licenceExpires;
    }

    @NonNull
    @NotNull
    @Override
    public ZoneListHierachyAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        IodizationWiseMillDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.iodization_wise_mill_dashboard, parent, false);
        return new ZoneListHierachyAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ZoneListHierachyAdapter.ViewHolder holder, int position) {
        JaninaList currentProduct = licenceExpires.get(position);
        holder.binding.percentLayout.setVisibility(View.GONE);
        holder.binding.zoneTvLevel.setText("Zone Name");
        holder.binding.totalMillLevel.setText("Sale Qty");

        try {
            holder.binding.zone.setText(":  " + currentProduct.getZoneName());
            holder.binding.mill.setText(":  " + KgToTon.kgToTon(currentProduct.getTotalSold()) + MtUtils.metricTon);

        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return licenceExpires.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final IodizationWiseMillDashboardBinding binding;

        public ViewHolder(final IodizationWiseMillDashboardBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
