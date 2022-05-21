package com.rupayan_housing.view.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.LastMonthSaleLayoutDashboardBinding;
import com.rupayan_housing.databinding.MonitoringLayoutDashboardBinding;
import com.rupayan_housing.serverResponseModel.IssueMonitoringList;
import com.rupayan_housing.serverResponseModel.LastMontSaleList;
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.replace.KgToTon;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LastMonthMonitoringSaleAdapter extends RecyclerView.Adapter<LastMonthMonitoringSaleAdapter.ViewHolder> {

    FragmentActivity context;
    List<LastMontSaleList> licenceExpires;
    DashboardFragment access;

    public LastMonthMonitoringSaleAdapter(FragmentActivity context, List<LastMontSaleList> licenceExpires,DashboardFragment access) {
        this.context = context;
        this.licenceExpires = licenceExpires;
        this.access = access;
    }
    @NonNull
    @NotNull
    @Override
    public LastMonthMonitoringSaleAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LastMonthSaleLayoutDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.last_month_sale_layout_dashboard, parent, false);
        return new LastMonthMonitoringSaleAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LastMonthMonitoringSaleAdapter.ViewHolder holder, int position) {
        LastMontSaleList currentProduct = licenceExpires.get(position);

        try {

            holder.binding.month.setText("" + currentProduct.getEntryDate());
            holder.binding.saleType.setText("" + currentProduct.getCategory());
            holder.binding.qty.setText("" + KgToTon.kgToTon(currentProduct.getQuantity()));

        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return licenceExpires.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LastMonthSaleLayoutDashboardBinding binding;

        public ViewHolder(final LastMonthSaleLayoutDashboardBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}

