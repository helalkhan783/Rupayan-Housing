package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.PacketingListLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.PacketingProductionList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class   PacketingListAdapter extends RecyclerView.Adapter<PacketingListAdapter.Myholder> {
    private FragmentActivity context;
    private List<PacketingProductionList> lists;


    @NonNull
    @NotNull
    @Override
    public PacketingListAdapter.Myholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PacketingListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.packeting_list_layout, parent, false);
        return  new PacketingListAdapter.Myholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PacketingListAdapter.Myholder holder, int position) {
     /*   if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1560)) {
            holder.itembinding.details.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1331)) {
            holder.itembinding.edit.setVisibility(View.GONE);
        }
*/
        PacketingProductionList currentItem = lists.get(position);
        if (currentItem.getEntryDate() == null){
            holder.binding.date.setText(":");
        }else {
            holder.binding.date.setText(":  "+   new DateFormatRight(context,currentItem.getEntryDate()).dateFormatForWashing());
        }
        if (currentItem.getProductTitle() == null){
            holder.binding.itemName.setText(":");
        }else {
            holder.binding.itemName.setText(":  "+currentItem.getProductTitle());
        }
        if (currentItem.getQuantity() == null){
            holder.binding.totalQuantity.setText(":");
        }else {
            holder.binding.totalQuantity.setText(":  "+currentItem.getQuantity());
        }
        if (currentItem.getStoreName() == null) {
            holder.binding.store.setText(":");
        }   else {
            holder.binding.store.setText(":  "+currentItem.getStoreName());
        }
        if (currentItem.getEnterpriseName() == null){
            holder.binding.enterpriseName.setText(":");
        }else {
            holder.binding.enterpriseName.setText(":  "+currentItem.getEnterpriseName());
        }
        holder.binding.referrerName.setText(":  "+currentItem.getCustomerFname());

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        private  PacketingListLayoutBinding binding;
        public Myholder(@NonNull @NotNull PacketingListLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}