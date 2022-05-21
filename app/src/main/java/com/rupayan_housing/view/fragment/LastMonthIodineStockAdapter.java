package com.rupayan_housing.view.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.LastMontIodizationAdapter;
import com.rupayan_housing.databinding.LastMonthZoneWiseMillDashboardBinding;
import com.rupayan_housing.serverResponseModel.LastIodineStockList;
import com.rupayan_housing.serverResponseModel.LastMonthIodizationList;
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.replace.KgToTon;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LastMonthIodineStockAdapter extends RecyclerView.Adapter<LastMonthIodineStockAdapter.ViewHolder> {
    FragmentActivity context;
    List<LastIodineStockList> licenceExpires;
    DashboardFragment acces;
    public LastMonthIodineStockAdapter(FragmentActivity context, List<LastIodineStockList> licenceExpires,DashboardFragment acces) {
        this.context = context;
        this.licenceExpires = licenceExpires;
          this.acces =acces;
    }

    @NonNull
    @NotNull
    @Override
    public LastMonthIodineStockAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LastMonthZoneWiseMillDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.last_month_zone_wise_mill_dashboard, parent, false);
        return new LastMonthIodineStockAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LastMonthIodineStockAdapter.ViewHolder holder, int position) {
        LastIodineStockList currentProduct = licenceExpires.get(position);

        try {
            int position1 = position + 1;
            holder.binding.sl.setText("" + position1);
            holder.binding.zone.setText("" + currentProduct.getZoneName());
            if (currentProduct.getTotalMill() != null) {
                holder.binding.mill.setText("" + currentProduct.getTotalMill());
            }
            if (currentProduct.getQuantity() != null) {

                holder.binding.perchant.setText("" + KgToTon.kgToTon(String.valueOf(currentProduct.getQuantity())) );
            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return licenceExpires.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LastMonthZoneWiseMillDashboardBinding binding;

        public ViewHolder(final LastMonthZoneWiseMillDashboardBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
