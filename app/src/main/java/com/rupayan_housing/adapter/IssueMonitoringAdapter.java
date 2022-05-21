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
import com.rupayan_housing.serverResponseModel.IssueMonitoringList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IssueMonitoringAdapter extends RecyclerView.Adapter<IssueMonitoringAdapter.ViewHolder> {
    FragmentActivity context;
    List<IssueMonitoringList> licenceExpires;

    public IssueMonitoringAdapter(FragmentActivity context, List<IssueMonitoringList> licenceExpires) {
        this.context = context;
        this.licenceExpires = licenceExpires;
    }

    @NonNull
    @NotNull
    @Override
    public IssueMonitoringAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        IodizationWiseMillDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.iodization_wise_mill_dashboard, parent, false);
        return new IssueMonitoringAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull IssueMonitoringAdapter.ViewHolder holder, int position) {
        IssueMonitoringList currentProduct = licenceExpires.get(position);
        holder.binding.percentLayout.setVisibility(View.GONE);
        holder.binding.zoneTvLevel.setText("Monitoring Issue");
        holder.binding.totalMillLevel.setText("Total Monitoring");

        try {
          /*  int position1 = position + 1;
            holder.binding.sl.setText("" + position1);*/
            holder.binding.zone.setText(":  " + currentProduct.getMonitoringTypeName());
            holder.binding.totalMillLevel.setText(":  " + currentProduct.getTypeCount());

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
