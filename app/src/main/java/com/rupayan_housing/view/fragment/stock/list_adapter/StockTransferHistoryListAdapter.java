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
import com.rupayan_housing.databinding.StockTransferListLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.stock.StockAllListFragment;
import com.rupayan_housing.view.fragment.stock.all_response.StockTransferHistoryList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StockTransferHistoryListAdapter extends RecyclerView.Adapter<StockTransferHistoryListAdapter.viewHolder> {
    private FragmentActivity activity;
    private List<StockTransferHistoryList> list;
    private boolean isHistoryIn;


    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        StockTransferListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.stock_transfer_list_layout, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        StockTransferHistoryList currentList = list.get(position);
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1460)) {
          holder.itembinding.edit.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1538)) {
          holder.itembinding.view.setVisibility(View.GONE);
        }
        try {

            holder.itembinding.date.setText(":  " + new DateFormatRight(activity,currentList.getEntryDate()).onlyDayMonthYear() );
            holder.itembinding.fromEnterprise.setText(":  " + currentList.getFromEnterpriseName());
            holder.itembinding.toEnterPrise.setText(":  " + currentList.getToEnterpriseName());
            holder.itembinding.transferFrom.setText(":  " + currentList.getFromStoreName());
            holder.itembinding.transferTo.setText(":  " + currentList.getToStoreName());
            holder.itembinding.itemName.setText(":  " + currentList.getProductTitle());
            holder.itembinding.quantity.setText(":  " + currentList.getQuantity() + " " + currentList.getName());

            holder.itembinding.view.setOnClickListener(v -> {
                 goToDetailsPage(holder);

            });
            holder.itemView.setOnClickListener(v -> {
                goToDetailsPage(holder);
            });
            holder.itembinding.edit.setOnClickListener(v -> {
                try {

                    Bundle bundle = new Bundle();
                    bundle.putString("id", list.get(holder.getAdapterPosition()).getSerialID());
                    StockAllListFragment.pageNumber = 1;
                    Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_stockAllListFragment_to_EditTransferDate, bundle);
                    return;


                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }

    }

    private void goToDetailsPage(viewHolder holder) {
     try {
         if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1538)) {
             Bundle bundle = new Bundle();
             bundle.putString("RefOrderId", list.get(holder.getAdapterPosition()).getOrderID());
             bundle.putString("vendorId", list.get(holder.getAdapterPosition()).getOrder_vendorID());
             bundle.putString("pageName", "Transfer Details");
             StockAllListFragment.pageNumber = 1;
             Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_stockAllListFragment_to_transferDetailsFragment, bundle);
             return;

         } else {
             Toasty.info(activity, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
         }
     }catch (Exception e){}
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private StockTransferListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull StockTransferListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
