package com.rupayan_housing.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;

import com.rupayan_housing.clickHandle.WashingCrushingPendingListClickHandle;
import com.rupayan_housing.databinding.WashingListLayoutBinding;
import com.rupayan_housing.localDatabase.MyWashingCrushingHelper;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.WashingList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.production.ProductionAllListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

public class WashingAdapter extends RecyclerView.Adapter<WashingAdapter.viewHolder> {
    private FragmentActivity activity;
    private List<WashingList> lists;
    private MyWashingCrushingHelper helper;

    public WashingAdapter(FragmentActivity activity, List<WashingList> lists) {
        this.activity = activity;
        this.lists = lists;
        helper = new MyWashingCrushingHelper(activity);
    }

    @NonNull
    @NotNull
    @Override
    public WashingAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        WashingListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R
                .layout.washing_list_layout, parent, false);
        return new WashingAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1443)) {
            holder.itembinding.details.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1434)) {
            holder.itembinding.edit.setVisibility(View.GONE);
        }
        try {
            helper.deleteAllData();
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getLocalizedMessage());
        }


        WashingList currentList = lists.get(position);
        try {
            holder.itembinding.date.setText(":  " + new DateFormatRight(activity, currentList.getEntryDate()).dateFormatForWashing());
            holder.itembinding.itemName.setText(":  " + currentList.getProductTitle());
            holder.itembinding.totalQuantity.setText(String.valueOf(":  " + currentList.getQuantity() + " " + currentList.getName()));
            holder.itembinding.store.setText(":  " + currentList.getStoreName());
            holder.itembinding.enterpriseName.setText(":  " + currentList.getEnterpriseName());
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }


        holder.itembinding.setClickHandle(new WashingCrushingPendingListClickHandle() {
            @Override
            public void view() {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("pageName", "Washing & Crushing Details");
                    bundle.putString("portion", "WASHING_&_CRUSHING_DETAILS");
                    bundle.putString("RefOrderId", lists.get(holder.getAdapterPosition()).getOrderID());
                    bundle.putString("vendorId", lists.get(holder.getAdapterPosition()).getOrder_vendorID());
                    ProductionAllListFragment.pageNumber = 1;
                    Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_productionAllListFragment_to_WashingCrushongDetails, bundle);

                    return;
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }

            }

            @Override
            public void edit() {
                String currentProfileTypeId = PreferenceManager.getInstance(activity).getUserCredentials().getProfileTypeId();
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1434)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("sid", lists.get(holder.getAdapterPosition()).getSerialID());
                    bundle.putString("orderId", lists.get(holder.getAdapterPosition()).getOrderID());
                    ProductionAllListFragment.pageNumber = 1;
                    Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_productionAllListFragment_to_editWashingCrushing, bundle);
                    return;
                } else {
                    Toasty.info(activity, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                }
            }
        });

        holder.itemView.setOnClickListener(v -> {
            try {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1443)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pageName", "Washing & Crushing Details");
                    bundle.putString("portion", "WASHING_&_CRUSHING_DETAILS");
                    bundle.putString("RefOrderId", lists.get(holder.getAdapterPosition()).getOrderID());
                    bundle.putString("vendorId", lists.get(holder.getAdapterPosition()).getOrder_vendorID());
                    ProductionAllListFragment.pageNumber = 1;
                    Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_productionAllListFragment_to_WashingCrushongDetails, bundle);

                    return;
                } else {
                    Toasty.info(activity, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }

        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private WashingListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull WashingListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
