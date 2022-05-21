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
import com.rupayan_housing.serverResponseModel.AgencyMonirtoringList;
import com.rupayan_housing.serverResponseModel.ZoneWiseMonitorignList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DashBoardAgencyMonitoringAdapter extends RecyclerView.Adapter<DashBoardAgencyMonitoringAdapter.ViewHolder> {
    FragmentActivity context;
    List<AgencyMonirtoringList> licenceExpires;

    public DashBoardAgencyMonitoringAdapter(FragmentActivity context, List<AgencyMonirtoringList> licenceExpires) {
        this.context = context;
        this.licenceExpires = licenceExpires;
    }

    @NonNull
    @NotNull
    @Override
    public DashBoardAgencyMonitoringAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        IodizationWiseMillDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.iodization_wise_mill_dashboard, parent, false);
        return new DashBoardAgencyMonitoringAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DashBoardAgencyMonitoringAdapter.ViewHolder holder, int position) {
        AgencyMonirtoringList currentProduct = licenceExpires.get(position);
        holder.binding.percentLayout.setVisibility(View.GONE);
         holder.binding.totalMillLevel.setText("Total Monitoring");
        holder.binding.zoneTvLevel.setText("Agency Name");

        try {
          /*  int position1 = position + 1;
            holder.binding.sl.setText("" + position1);*/
            holder.binding.zone.setText(":  " + currentProduct.getStoreName());
            holder.binding.mill.setText(":  " + currentProduct.getTotalMonitor());

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
