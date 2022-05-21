package com.rupayan_housing.view.fragment.production;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.adapter.IodizationHistoryAdapter;
import com.rupayan_housing.clickHandle.IodizationHistoryAdapterClick;
import com.rupayan_housing.databinding.IodizationHistoryListLayoutBinding;
import com.rupayan_housing.localDatabase.MyWashingCrushingHelper;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.IodizatinPendingList;
import com.rupayan_housing.serverResponseModel.IodizationHistoryList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class PendingListAdapter extends RecyclerView.Adapter<PendingListAdapter.viewHolder> {
    FragmentActivity activity;
    List<IodizatinPendingList> lists;
    private MyWashingCrushingHelper myWashingCrushingHelper;

    public PendingListAdapter(FragmentActivity activity, List<IodizatinPendingList> lists) {
        this.activity = activity;
        this.lists = lists;
        myWashingCrushingHelper = new MyWashingCrushingHelper(activity);
    }

    @NonNull
    @NotNull
    @Override
    public PendingListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        IodizationHistoryListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R
                .layout.iodization_history_list_layout, parent, false);
        return new PendingListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PendingListAdapter.viewHolder holder, int position) {
        holder.itembinding.edit.setVisibility(View.GONE);
        holder.itembinding.details.setVisibility(View.GONE);
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1560)) {
            holder.itembinding.details.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1331)) {
            holder.itembinding.edit.setVisibility(View.GONE);
        }

        try {
            /**
             * delete all data before go edit iodization history
             */
            myWashingCrushingHelper.deleteAllData();
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getLocalizedMessage());
        }
        IodizatinPendingList currentList = lists.get(position);


        holder.itembinding.date.setText(":  " + new DateFormatRight(activity, currentList.getEntryDate()).dateFormatForWashing());
        holder.itembinding.itemName.setText(":  " + currentList.getStoreName());
        holder.itembinding.totalQuantity.setText(":  " + String.valueOf(currentList.getQuantity()));
        holder.itembinding.store.setText(":  " + currentList.getStoreName());
        holder.itembinding.enterpriseName.setText(":  " + currentList.getEnterpriseName());


        holder.itembinding.setClickHandle(new IodizationHistoryAdapterClick() {
            @Override
            public void view() {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("portion", " Iodization Details");
                    bundle.putString("pageName", "Iodization Details " + " Id " + lists.get(holder.getAdapterPosition()).getOrderID());
                    bundle.putString("RefOrderId", lists.get(holder.getAdapterPosition()).getOrderID());
                   // bundle.putString("vendorId", lists.get(holder.getAdapterPosition()).getOrder_vendorID());
                     bundle.putString("vendorId", lists.get(holder.getAdapterPosition()).getOrderVendorID());
                    ProductionAllListFragment.pageNumber = 1;
                    Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_productionAllListFragment_to_pending_iodizationDetailsFragment, bundle);
                } catch (Exception e) {
                    Log.d("ERROR", e.getMessage());
                }
            }

            @Override
            public void edit() {
                String currentProfileTypeId = PreferenceManager.getInstance(activity).getUserCredentials().getProfileTypeId();

                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1331)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("sid", lists.get(holder.getAdapterPosition()).getSerialID());
                    bundle.putString("orderId", lists.get(holder.getAdapterPosition()).getOrderID());
                    ProductionAllListFragment.pageNumber = 1;
                    Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_productionAllListFragment_to_editIodization, bundle);
                    return;
                } else {
                    Toasty.info(activity, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                }
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("portion", " Iodization Details");
            bundle.putString("pageName", "Iodization Details " + " Id " + lists.get(holder.getAdapterPosition()).getOrderID());
            bundle.putString("RefOrderId", lists.get(holder.getAdapterPosition()).getOrderID());
           // bundle.putString("vendorId", lists.get(holder.getAdapterPosition()).getOrder_vendorID());
             bundle.putString("vendorId", lists.get(holder.getAdapterPosition()).getOrderVendorID());
            ProductionAllListFragment.pageNumber = 1;
            Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_productionAllListFragment_to_pending_iodizationDetailsFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private IodizationHistoryListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull IodizationHistoryListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}

