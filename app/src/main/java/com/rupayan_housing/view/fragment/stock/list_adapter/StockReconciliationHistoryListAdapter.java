package com.rupayan_housing.view.fragment.stock.list_adapter;

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
import com.rupayan_housing.databinding.StockListLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.stock.StockAllListFragment;
import com.rupayan_housing.view.fragment.stock.all_response.StockDeclineTransferredList;
import com.rupayan_housing.view.fragment.stock.all_response.StockReconciliationHistoryList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StockReconciliationHistoryListAdapter extends RecyclerView.Adapter<StockReconciliationHistoryListAdapter.viewHolder> {
    private FragmentActivity activity;
    private List<StockReconciliationHistoryList> list;
    private  View view;


    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        StockListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.stock_list_layout, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        StockReconciliationHistoryList currentList = list.get(position);
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1437)) {
            holder.itembinding.edit.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1545)) {
            holder.itembinding.view.setVisibility(View.GONE);
        }

        try {
            if (currentList.getEnterpriseName() == null) {
                holder.itembinding.enterpriseName.setText(":  ");
            } else {
                holder.itembinding.enterpriseName.setText(":  " + currentList.getEnterpriseName());
            }
            if (currentList.getStoreName() == null) {
                holder.itembinding.store.setText(":  ");
            } else {
                holder.itembinding.store.setText(":  " + currentList.getStoreName());
            }
            if (currentList.getProductTitle() == null) {
                holder.itembinding.itemName.setText(":  ");
            } else {
                holder.itembinding.itemName.setText(":  " + currentList.getProductTitle());
            }
            if (currentList.getReconciliationType() == null) {
            } else {
                holder.itembinding.type.setText(":  " + currentList.getReconciliationType());

            }
            if (currentList.getQuantity() == null) {
                holder.itembinding.quantity.setText(":  ");
            } else {
                holder.itembinding.quantity.setText(":  " + currentList.getQuantity() + " " + currentList.getUnitName());

            }

            holder.itembinding.unit.setText(":  " + currentList.getUnitName());
            holder.itembinding.view.setOnClickListener(v -> {
                gotoDetailsPage(holder);
            });
            holder.itemView.setOnClickListener(v -> {
                gotoDetailsPage(holder);
            });


        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
        holder.itembinding.edit.setOnClickListener(v -> {
            try {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", list.get(holder.getAdapterPosition()).getSerialID());
                    Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_stockAllListFragment_to_editReconciliation, bundle);

            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
        });


    }

    private void gotoDetailsPage(viewHolder holder) {
        try {
                Bundle bundle = new Bundle();
                bundle.putString("RefOrderId", list.get(holder.getAdapterPosition()).getOrderID());
                bundle.putString("vendorId", list.get(holder.getAdapterPosition()).getOrder_vendorID());
                bundle.putString("pageName", "Reconciliation History Details");
                bundle.putString("portion", "PENDING_RECONCILIATION_DETAILS");
                StockAllListFragment.pageNumber = 1;
                Navigation.findNavController(view).navigate(R.id.action_stockAllListFragment_to_reconciliationDetailsFragment, bundle);

        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private StockListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull StockListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
