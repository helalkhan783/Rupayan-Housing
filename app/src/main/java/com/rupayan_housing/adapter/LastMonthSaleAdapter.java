package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.IodizationWiseMillDashboardBinding;
import com.rupayan_housing.serverResponseModel.LastIodineStockList;
import com.rupayan_housing.serverResponseModel.LastMontSaleList;
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.replace.KgToTon;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LastMonthSaleAdapter extends RecyclerView.Adapter<LastMonthSaleAdapter.ViewHolder> {
    FragmentActivity context;
    List<LastMontSaleList> licenceExpires;
    String name;

    public LastMonthSaleAdapter(FragmentActivity context, List<LastMontSaleList> licenceExpires,String name) {
        this.context = context;
        this.licenceExpires = licenceExpires;
        this.name = name;
    }

    @NonNull
    @NotNull
    @Override
    public LastMonthSaleAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        IodizationWiseMillDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.iodization_wise_mill_dashboard, parent, false);
        return new LastMonthSaleAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LastMonthSaleAdapter.ViewHolder holder, int position) {
        LastMontSaleList currentProduct = licenceExpires.get(position);
        holder.binding.zoneTvLevel.setText("Month");
        holder.binding.totalMillLevel.setText("Sale Type");
        holder.binding.perTvLevel.setText(name);
        try {
            holder.binding.zone.setText(":  " + currentProduct.getEntryDate());
            holder.binding.mill.setText(":  " + currentProduct.getCategory());
            holder.binding.perchant.setText(":  " + KgToTon.kgToTon(currentProduct.getQuantity()) + MtUtils.metricTon);
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

