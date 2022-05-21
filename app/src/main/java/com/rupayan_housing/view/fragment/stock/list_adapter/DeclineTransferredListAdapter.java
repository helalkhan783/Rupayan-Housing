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
import com.rupayan_housing.databinding.ReconciliationListLayoutBinding;
import com.rupayan_housing.databinding.StockListLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.sale.SaleAllListFragment;
import com.rupayan_housing.view.fragment.stock.StockAllListFragment;
import com.rupayan_housing.view.fragment.stock.all_response.StockDeclineTransferredList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeclineTransferredListAdapter extends RecyclerView.Adapter<DeclineTransferredListAdapter.viewHolder> {
    private FragmentActivity activity;
    private List<StockDeclineTransferredList> list;
    private String manageView;

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ReconciliationListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.reconciliation_list_layout, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        StockDeclineTransferredList currentList = list.get(position);
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1538)) {
          holder.itembinding.view.setVisibility(View.GONE);
        }
        if (manageView.equals("reportList")) {
            holder.itembinding.view.setVisibility(View.GONE);
        }

        holder.itembinding.date.setText(":  " + new DateFormatRight(activity,currentList.getEntryDate()).onlyDayMonthYear() );

       // holder.itembinding.date.setText(":  " + currentList.getEntryDate());
        if (currentList.getFromEnterpriseName() == null) {
            holder.itembinding.fromEnterprise.setText(":  ");
        } else {
            holder.itembinding.fromEnterprise.setText(":  " + currentList.getFromEnterpriseName());

        }

        if (currentList.getToEnterpriseName() != null) {
            holder.itembinding.toEnterPrise.setText(":  " + currentList.getToEnterpriseName());
        }


        if (currentList.getTransferFrom() == null) {
            holder.itembinding.transferFrom.setText(":  ");
        } else {
            holder.itembinding.transferFrom.setText(":  " + currentList.getFromStoreName());

        }


        if (currentList.getTransferTo() != null) {
            holder.itembinding.transferTo.setText(":  " + currentList.getToStoreName());
        }
        holder.itembinding.itemName.setText(":  " + currentList.getProductTitle());
        holder.itembinding.quantity.setText(":  " + currentList.getQuantity() + " " + currentList.getName());

        holder.itembinding.view.setOnClickListener(v -> {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1538)) {
                Bundle bundle = new Bundle();
                bundle.putString("RefOrderId", list.get(holder.getAdapterPosition()).getOrderID());
                bundle.putString("vendorId", list.get(holder.getAdapterPosition()).getOrderVendorID());
                bundle.putString("pageName", "Transfer Details");
                StockAllListFragment.pageNumber = 1;
                Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_stockAllListFragment_to_transferDetailsFragment, bundle);
                return;

            } else {
                Toasty.info(activity, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
            }


        });

//        holder.itembinding.edit.setOnClickListener(v -> {
//            String currentProfileTypeId = PreferenceManager.getInstance(activity).getUserCredentials().getProfileTypeId();
//            if (!currentProfileTypeId.equals("7")) {
//                Toasty.info(activity, "you don't  have permission for access this portion", Toasty.LENGTH_LONG).show();
//                return;
//            }
//            Toasty.info(activity, "will implement it", Toasty.LENGTH_LONG).show();
//        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ReconciliationListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull ReconciliationListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
