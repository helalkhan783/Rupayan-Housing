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
import com.rupayan_housing.serverResponseModel.LastMontQcQaList;
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.replace.KgToTon;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LastMonthQcAaAdapter extends RecyclerView.Adapter<LastMonthQcAaAdapter.ViewHolder> {
    FragmentActivity context;
    List<LastMontQcQaList> licenceExpires;

    public LastMonthQcAaAdapter(FragmentActivity context, List<LastMontQcQaList> licenceExpires) {
        this.context = context;
        this.licenceExpires = licenceExpires;
    }

    @NonNull
    @NotNull
    @Override
    public LastMonthQcAaAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        IodizationWiseMillDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.iodization_wise_mill_dashboard, parent, false);
        return new LastMonthQcAaAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LastMonthQcAaAdapter.ViewHolder holder, int position) {
        LastMontQcQaList currentProduct = licenceExpires.get(position);
        holder.binding.perTvLevel.setText("Stock Qty");
        try {
            holder.binding.zone.setText(":  " + currentProduct.getZoneName());
            if (currentProduct.getTotalMill() != null) {
                holder.binding.mill.setText(":  " + currentProduct.getTotalMill());
            }

            holder.binding.perchant.setText(":  " + KgToTon.kgToTon(currentProduct.getTotalQc()) + MtUtils.metricTon);

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

