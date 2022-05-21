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
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.replace.KgToTon;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class IodineStockAdapter extends RecyclerView.Adapter<IodineStockAdapter.ViewHolder> {
    FragmentActivity context;
    List<LastIodineStockList> licenceExpires;
    public IodineStockAdapter(FragmentActivity context, List<LastIodineStockList> licenceExpires) {
        this.context = context;
        this.licenceExpires = licenceExpires;
    }

    @NonNull
    @NotNull
    @Override
    public IodineStockAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        IodizationWiseMillDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.iodization_wise_mill_dashboard, parent, false);
        return new IodineStockAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull IodineStockAdapter.ViewHolder holder, int position) {
        LastIodineStockList currentProduct = licenceExpires.get(position);
        holder.binding.perTvLevel.setText("Stock Qty");
        try {
            holder.binding.zone.setText(":  " + currentProduct.getZoneName());
            if (currentProduct.getTotalMill() != null) {
                holder.binding.mill.setText(":  " + currentProduct.getTotalMill());
            }

            holder.binding.perchant.setText(":  " + KgToTon.kgToTon(String.valueOf( currentProduct.getQuantity())) + MtUtils.metricTon);

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
