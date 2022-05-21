package com.rupayan_housing.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.PackagingListLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.PackagingList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PackagingListAdapter extends RecyclerView.Adapter<PackagingListAdapter.MyHolder> {
    private FragmentActivity context;
    private List<PackagingList> lists;
    private View view;

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PackagingListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(parent.getContext()), R.layout.packaging_list_layout, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1561)) {
            holder.binding.view.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1455)) {
            holder.binding.edit.setVisibility(View.GONE);
        }

        PackagingList currentItem = lists.get(position);
        if (currentItem.getEntryDateTime() == null) {
            holder.binding.date.setText(":");
        } else {

            holder.binding.date.setText(":  " + new DateFormatRight(context, currentItem.getEntryDateTime()).dateFormatForWashing());

        }
        if (currentItem.getProductTitle() == null) {
            holder.binding.itemName.setText(":");
        } else {
            holder.binding.itemName.setText(":  " + currentItem.getProductTitle());

        }
        if (currentItem.getPackedName() == null) {
            holder.binding.packateName.setText(":");
        } else {
            holder.binding.packateName.setText(":  " + currentItem.getPackedName());

        }
        if (currentItem.getProductDimensions() == null) {
            holder.binding.weight.setText(":");
        } else {
            holder.binding.weight.setText(":  " + currentItem.getProductDimensions());

        }
        if (currentItem.getOriginItemQty() == null) {
            holder.binding.totalQuantity.setText(":");
        } else {
            holder.binding.totalQuantity.setText(":  " + currentItem.getOriginItemQty());

        }
        if (currentItem.getUnit() == null) {
            holder.binding.totalWeight.setText(":");
        } else {
            holder.binding.totalWeight.setText(":  " + String.valueOf(Double.parseDouble(currentItem.getOriginItemQty())*Double.parseDouble(currentItem.getProductDimensions())));

        }
        if (currentItem.getStore() == null) {
            holder.binding.store.setText(":");

        } else {
            holder.binding.store.setText(":  " + currentItem.getStore());

        }
        if (currentItem.getEnterprise() == null) {
            holder.binding.enterpriseName.setText(":");
        } else {
            holder.binding.enterpriseName.setText(":  " + currentItem.getEnterprise());

        }

        holder.binding.edit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("packagingSLID", currentItem.getPackagingSLID());
            Navigation.findNavController(view).navigate(R.id.action_productionAllListFragment_to_editPackegingData, bundle);
        });

        holder.binding.view.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("packagingVendorId", currentItem.getPackagingVendorID());
            bundle.putString("PackagingSID", currentItem.getPackagingSLID());
            Navigation.findNavController(view).navigate(R.id.action_productionAllListFragment_to_packagingDetails, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private PackagingListLayoutBinding binding;
        public MyHolder(@NonNull @NotNull PackagingListLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
