package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.CheckLicenceModelLayoutBinding;
import com.rupayan_housing.databinding.LastMonthZoneWiseMillDashboardBinding;
import com.rupayan_housing.serverResponseModel.LastMonthIodizationList;
import com.rupayan_housing.serverResponseModel.LicenceExpire;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LastMontIodizationAdapter extends RecyclerView.Adapter<LastMontIodizationAdapter.ViewHolder> {
    FragmentActivity context;
    List<LastMonthIodizationList> licenceExpires;

    public LastMontIodizationAdapter(FragmentActivity context, List<LastMonthIodizationList> licenceExpires) {
        this.context = context;
        this.licenceExpires = licenceExpires;
    }

    @NonNull
    @NotNull
    @Override
    public LastMontIodizationAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LastMonthZoneWiseMillDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.last_month_zone_wise_mill_dashboard, parent, false);
        return new LastMontIodizationAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LastMontIodizationAdapter.ViewHolder holder, int position) {
        LastMonthIodizationList currentProduct = licenceExpires.get(position);

        try {
            int position1 = position + 1;
            holder.binding.sl.setText("" + position1);
            holder.binding.zone.setText("" + currentProduct.getZoneName());
            if (currentProduct.getTotalMill() != null) {
                holder.binding.mill.setText("" + currentProduct.getTotalMill());
            }

            holder.binding.perchant.setText("" + currentProduct.getPercentage() );

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
