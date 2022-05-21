package com.rupayan_housing.view.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.JaninaLayoutDashboardBinding;
import com.rupayan_housing.databinding.ToptenMillerLayoutDashboardBinding;
import com.rupayan_housing.serverResponseModel.JaninaList;
import com.rupayan_housing.serverResponseModel.TopTenMillerList;
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.replace.KgToTon;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JaninaAdapter extends RecyclerView.Adapter<JaninaAdapter.ViewHolder> {
    FragmentActivity context;
    List<JaninaList> licenceExpires;
    DashboardFragment access;

    public JaninaAdapter(FragmentActivity context, List<JaninaList> licenceExpires,DashboardFragment access) {
        this.context = context;
        this.licenceExpires = licenceExpires;
        this.access = access;
    }

    @NonNull
    @NotNull
    @Override
    public JaninaAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        JaninaLayoutDashboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.janina_layout_dashboard, parent, false);
        return new JaninaAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull JaninaAdapter.ViewHolder holder, int position) {
        JaninaList currentProduct = licenceExpires.get(position);
        try {
            int position1 = position + 1;
            holder.binding.sl.setText("" + position1);
            holder.binding.zone.setText("" + currentProduct.getZoneName());
            holder.binding.qty.setText("" + KgToTon.kgToTon(currentProduct.getTotalSold()) );

        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return licenceExpires.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final JaninaLayoutDashboardBinding binding;

        public ViewHolder(final JaninaLayoutDashboardBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}

