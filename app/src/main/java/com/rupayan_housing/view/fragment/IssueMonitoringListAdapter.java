package com.rupayan_housing.view.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.MonitoringLayoutDashboardBinding;
import com.rupayan_housing.serverResponseModel.AgencyMonirtoringList;
import com.rupayan_housing.serverResponseModel.IssueMonitoringList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IssueMonitoringListAdapter extends RecyclerView.Adapter<IssueMonitoringListAdapter.ViewHolder> {

    FragmentActivity context;
    List<IssueMonitoringList> licenceExpires;

    public IssueMonitoringListAdapter(FragmentActivity context, List<IssueMonitoringList> licenceExpires) {
        this.context = context;
        this.licenceExpires = licenceExpires;
    }

    @NonNull
    @NotNull
    @Override
    public IssueMonitoringListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MonitoringLayoutDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.monitoring_layout_dashboard, parent, false);
        return new IssueMonitoringListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull IssueMonitoringListAdapter.ViewHolder holder, int position) {
        IssueMonitoringList currentProduct = licenceExpires.get(position);

        try {
            int position1 = position + 1;
            holder.binding.sl.setText("" + position1);
            holder.binding.agencyName.setText("" + currentProduct.getMonitoringTypeName());
            holder.binding.totalMonitoring.setText("" + currentProduct.getTypeCount());

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

