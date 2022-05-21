package com.rupayan_housing.view.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.LastMonthZoneWiseMillDashboardBinding;
import com.rupayan_housing.serverResponseModel.LastIodineStockList;
import com.rupayan_housing.serverResponseModel.LastMontQcQaList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LastMontQcQaAdapter extends RecyclerView.Adapter<LastMontQcQaAdapter.ViewHolder> {
    FragmentActivity context;
    List<LastMontQcQaList> licenceExpires;

    public LastMontQcQaAdapter(FragmentActivity context, List<LastMontQcQaList> licenceExpires) {
        this.context = context;
        this.licenceExpires = licenceExpires;
    }

    @NonNull
    @NotNull
    @Override
    public LastMontQcQaAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LastMonthZoneWiseMillDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.last_month_zone_wise_mill_dashboard, parent, false);
        return new LastMontQcQaAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LastMontQcQaAdapter.ViewHolder holder, int position) {
        LastMontQcQaList currentProduct = licenceExpires.get(position);

        try {
            int position1 = position + 1;
            holder.binding.sl.setText("" + position1);
            holder.binding.zone.setText("" + currentProduct.getZoneName());
            if (currentProduct.getTotalMill() != null) {
                holder.binding.mill.setText("" + currentProduct.getTotalMill());
            }

            holder.binding.perchant.setText("" + currentProduct.getPercentage()  );

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
