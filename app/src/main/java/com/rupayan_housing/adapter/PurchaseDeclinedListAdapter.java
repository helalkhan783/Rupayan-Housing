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
import com.rupayan_housing.retrofit.PurchaseDeclineList;
import com.rupayan_housing.serverResponseModel.PurchasePendingList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.purchase.AllPurchaseListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PurchaseDeclinedListAdapter extends RecyclerView.Adapter<PurchaseDeclinedListAdapter.viewHolder> {
    FragmentActivity activity;
    List<PurchaseDeclineList> lists;


    @NonNull
    @NotNull
    @Override
    public PurchaseDeclinedListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PurchaseReturnHistoryListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.purchase_return_history_list_layout, parent, false);
        return new PurchaseDeclinedListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PurchaseDeclinedListAdapter.viewHolder holder, int position) {
        PurchaseDeclineList currentList = lists.get(position);
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1501)) {
            holder.itembinding.view.setVisibility(View.GONE);
        }

        holder.itembinding.date.setText(":  "+new DateFormatRight(activity,currentList.getDate()).dateFormatForPm());

      //  holder.itembinding.date.setText(":  " + new DateFormatRight(activity, currentList.getDate()).yearMothDayToDayYearMonth());
        holder.itembinding.enterpriseName.setText(":  " + currentList.getEnterpriseName());
        holder.itembinding.companyName.setText(":  " + currentList.getCompanyName());
        holder.itembinding.ownerName.setText(":  " + currentList.getCustomerFname());
        holder.itembinding.orderSerial.setText(":  " + currentList.getId());
        holder.itembinding.uniqueOrderID.setText(":  " + currentList.getOrderSerial());

        holder.itembinding.view.setOnClickListener(v -> {
            goToDetails(holder,currentList.getId(),currentList.getOrderVendorID());
        });

     //   holder.itemView.setOnClickListener(v -> goToDetails(holder,currentList.getId(),currentList.getOrderVendorID()));
    }

    private void goToDetails(PurchaseDeclinedListAdapter.viewHolder holder, String refId, String orderVendorId) {
        Bundle bundle = new Bundle();
        bundle.putString("RefOrderId",refId );
        bundle.putString("orderVendorId", orderVendorId );
        bundle.putString("portion", "PENDING_PURCHASE");
         bundle.putString("porson", "PurchaseHistoryDetails");
        bundle.putString("pageName", "Purchase Decline Details Order Id "+refId);
        AllPurchaseListFragment.pageNumber = 1;
        Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_allPurchaseListFragment_to_pendingPurchaseDetailsFragment, bundle);

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