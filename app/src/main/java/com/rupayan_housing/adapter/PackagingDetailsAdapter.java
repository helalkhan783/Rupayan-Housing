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
import com.rupayan_housing.databinding.PackagingDetailsListLayoutBinding;
import com.rupayan_housing.serverResponseModel.PackagingDetail;


import org.jetbrains.annotations.NotNull;

import java.util.List;



public class PackagingDetailsAdapter extends RecyclerView.Adapter<PackagingDetailsAdapter.MyHolder> {
    private FragmentActivity context;
    private List<PackagingDetail> lists;

    public PackagingDetailsAdapter(FragmentActivity context, List<PackagingDetail> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @NotNull
    @Override
    public PackagingDetailsAdapter.MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PackagingDetailsListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(parent.getContext()), R.layout.packaging_details_list_layout, parent, false);
        return new PackagingDetailsAdapter.MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PackagingDetailsAdapter.MyHolder holder, int position) {
        PackagingDetail currentItem= lists.get(position);
 try {
     holder.binding.convertedItem.setText(":  ");
     holder.binding.originItem.setText(":  ");
     holder.binding.originQty.setText(":  "+currentItem.getOriginItemQty()+" "+currentItem.getOriginUnit());
     holder.binding.convertedQty.setText(":  "+currentItem.getConvertedItemQty()+" "+currentItem.getConUnit());

 }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private PackagingDetailsListLayoutBinding binding;
        public MyHolder(@NonNull @NotNull PackagingDetailsListLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
