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
import com.rupayan_housing.serverResponseModel.TopTenMillerList;
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.replace.KgToTon;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TopTenMillerAdapter extends RecyclerView.Adapter<TopTenMillerAdapter.ViewHolder> {
    FragmentActivity context;
    List<TopTenMillerList> licenceExpires;

    public TopTenMillerAdapter(FragmentActivity context, List<TopTenMillerList> licenceExpires) {
        this.context = context;
        this.licenceExpires = licenceExpires;
    }

    @NonNull
    @NotNull
    @Override
    public TopTenMillerAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        IodizationWiseMillDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.iodization_wise_mill_dashboard, parent, false);
        return new TopTenMillerAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TopTenMillerAdapter.ViewHolder holder, int position) {
        TopTenMillerList currentProduct = licenceExpires.get(position);
        holder.binding.zoneTvLevel.setText("Mill Name");
        holder.binding.totalMillLevel.setText("Zone Name");
        holder.binding.perTvLevel.setText("Sale Qty");
        try {
            holder.binding.zone.setText(":  " + currentProduct.getFullName());
            holder.binding.mill.setText(":  " + currentProduct.getZoneName());
            holder.binding.perchant.setText(":  " + KgToTon.kgToTon(currentProduct.getTotalSold()) + MtUtils.metricTon);
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
