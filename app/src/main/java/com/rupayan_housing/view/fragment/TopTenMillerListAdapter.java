package com.rupayan_housing.view.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.LastMonthSaleLayoutDashboardBinding;
import com.rupayan_housing.databinding.ToptenMillerLayoutDashboardBinding;
import com.rupayan_housing.serverResponseModel.LastMontSaleList;
import com.rupayan_housing.serverResponseModel.TopTenMillerList;
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.replace.KgToTon;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TopTenMillerListAdapter extends RecyclerView.Adapter<TopTenMillerListAdapter.ViewHolder> {
    FragmentActivity context;
    List<TopTenMillerList> licenceExpires;
    DashboardFragment access;

    public TopTenMillerListAdapter(FragmentActivity context, List<TopTenMillerList> licenceExpires,DashboardFragment access) {
        this.context = context;
        this.licenceExpires = licenceExpires;
        this.access =access;
    }

    @NonNull
    @NotNull
    @Override
    public TopTenMillerListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ToptenMillerLayoutDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.topten_miller_layout_dashboard, parent, false);
        return new TopTenMillerListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TopTenMillerListAdapter.ViewHolder holder, int position) {
        TopTenMillerList currentProduct = licenceExpires.get(position);
        try {
            int pos = position+1;
            holder.binding.sl.setText("" + pos);
            holder.binding.millName.setText("" + currentProduct.getFullName());
            holder.binding.zoneName.setText("" + currentProduct.getZoneName());
            holder.binding.saleQty.setText("" + KgToTon.kgToTon(currentProduct.getTotalSold()));

        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return licenceExpires.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ToptenMillerLayoutDashboardBinding binding;

        public ViewHolder(final ToptenMillerLayoutDashboardBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}

