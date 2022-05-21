package com.rupayan_housing.adapter;

import android.content.Context;
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
import com.rupayan_housing.databinding.SaleDeclinedListLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.SaleDeclinedList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.sale.SaleAllListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SaleDeclinedListAdapter extends RecyclerView.Adapter<SaleDeclinedListAdapter.viewHolder> {
    private FragmentActivity context;
    List<SaleDeclinedList> lists;

    @NonNull
    @NotNull
    @Override
    public SaleDeclinedListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SaleDeclinedListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.sale_declined_list_layout, parent, false);
        return new SaleDeclinedListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SaleDeclinedListAdapter.viewHolder holder, int position) {
        SaleDeclinedList currentList = lists.get(position);
        if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1534)) {
            holder.itembinding.view.setVisibility(View.GONE);
        }

        holder.itembinding.date.setText(":  " +new DateFormatRight(context,currentList.getDate()).dateFormatForPm());


        holder.itembinding.enterpriseName.setText(":  " + currentList.getEnterpriseName());
        holder.itembinding.companyName.setText(":  " + currentList.getCompanyName());
        holder.itembinding.ownerName.setText(":  " + currentList.getCustomerFname());
        if (currentList.getOrderSerial() == null) {
            holder.itembinding.orderSerial.setText(":  ");
        } else {
            holder.itembinding.orderSerial.setText(":  " + currentList.getOrderSerial());
        }


        holder.itembinding.view.setOnClickListener(v -> {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1533)) {
                Bundle bundle = new Bundle();
                bundle.putString("RefOrderId", currentList.getId());
                bundle.putString("orderVendorId", currentList.getOrderVendorID());
                bundle.putString("portion", "PENDING_SALE");
                bundle.putString("porson", "Sale_Declined_Details");
                bundle.putString("orderId",  currentList.getId());
                SaleAllListFragment.pageNumber = 1;
                Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_saleAllListFragment_to_pendingPurchaseDetailsFragment, bundle);
                return;
            } else {
                Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
            }



          });


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private SaleDeclinedListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull SaleDeclinedListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
