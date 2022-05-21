package com.rupayan_housing.view.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.LastMonthZoneWiseMillDashboardBinding;
import com.rupayan_housing.databinding.MonitoringLayoutDashboardBinding;
import com.rupayan_housing.serverResponseModel.AgencyMonirtoringList;
import com.rupayan_housing.serverResponseModel.ZoneWiseMonitorignList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AgengyMonitoringAdapter extends RecyclerView.Adapter<AgengyMonitoringAdapter.ViewHolder> {

    FragmentActivity context;
    List<AgencyMonirtoringList> licenceExpires;

    public AgengyMonitoringAdapter(FragmentActivity context, List<AgencyMonirtoringList> licenceExpires) {
        this.context = context;
        this.licenceExpires = licenceExpires;
    }

    @NonNull
    @NotNull
    @Override
    public AgengyMonitoringAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MonitoringLayoutDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.monitoring_layout_dashboard, parent, false);
        return new AgengyMonitoringAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AgengyMonitoringAdapter.ViewHolder holder, int position) {
        AgencyMonirtoringList currentProduct = licenceExpires.get(position);

        try {
            int position1 = position + 1;
            holder.binding.sl.setText("" + position1);
            holder.binding.agencyName.setText("" + currentProduct.getStoreName());
            holder.binding.totalMonitoring.setText("" + currentProduct.getTotalMonitor());

        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return licenceExpires.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MonitoringLayoutDashboardBinding binding;

        public ViewHolder(final MonitoringLayoutDashboardBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}

