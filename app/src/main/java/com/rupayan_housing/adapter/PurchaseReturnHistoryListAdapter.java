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
import com.rupayan_housing.databinding.PurchaseReturnHistoryListLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.PurchaseReturnHistoryList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.purchase.AllPurchaseListFragment;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PurchaseReturnHistoryListAdapter extends RecyclerView.Adapter<PurchaseReturnHistoryListAdapter.viewHolder> {
    FragmentActivity activity;
    List<PurchaseReturnHistoryList> lists;

    @NonNull
    @NotNull
    @Override
    public PurchaseReturnHistoryListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PurchaseReturnHistoryListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.purchase_return_history_list_layout, parent, false);
        return new PurchaseReturnHistoryListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PurchaseReturnHistoryListAdapter.viewHolder holder, int position) {
        PurchaseReturnHistoryList currentList = lists.get(position);

        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1501)) {
            holder.itembinding.view.setVisibility(View.GONE);

        }


        holder.itembinding.date.setText(":  " + new DateFormatRight(activity,currentList.getEntryDate()).yearMothDayToDayYearMonth());


        holder.itembinding.enterpriseName.setText(":  " + currentList.getStoreName());
        holder.itembinding.companyName.setText(":  " + currentList.getCompanyName());
        holder.itembinding.ownerName.setText(":  " + currentList.getCustomerFname());
        holder.itembinding.orderSerial.setText(":  " + currentList.getOrderID());
        holder.itembinding.uniqueOrderID.setText(":  " + currentList.getOrderSerial());

        holder.itembinding.view.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("RefOrderId", currentList.getOrderID());
            bundle.putString("orderVendorId", lists.get(holder.getAdapterPosition()).getOrder_vendorID());
            bundle.putString("portion", "PENDING_PURCHASE");
            bundle.putString("pageName", "Purchase Return History Details");
            AllPurchaseListFragment.pageNumber = 1;
            bundle.putString("forHistoryLayout", "1");
            Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_allPurchaseListFragment_to_purchaseReturnPendingDetailsFragment, bundle);
            /*
            Bundle bundle = new Bundle();
            bundle.putString("RefOrderId", currentList.getOrderID());
            bundle.putString("orderVendorId", currentList.getOrder_vendorID());
            bundle.putString("portion", "PurchaseReturnDetails");
            bundle.putString("enterprise", currentList.getStoreName());
            AllPurchaseListFragment.pageNumber = 1;
            Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_allPurchaseListFragment_to_purchaseAndSaleReturnDetailsFragment2, bundle);
           */
        });


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private PurchaseReturnHistoryListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull PurchaseReturnHistoryListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}