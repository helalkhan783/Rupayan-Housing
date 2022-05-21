package com.rupayan_housing.adapter;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.PurchaseEditClickHandle;
import com.rupayan_housing.databinding.PurchaseHistoryListLayoutBinding;
import com.rupayan_housing.databinding.PurchasePendingListLayoutBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.PurchaseHistoryList;
import com.rupayan_housing.serverResponseModel.PurchasePendingList;
import com.rupayan_housing.utils.ImageBaseUrl;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.purchase.AllPurchaseListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

 public class PurchasePendingListAdapter extends RecyclerView.Adapter<PurchasePendingListAdapter.viewHolder> {
    private FragmentActivity context;
    List<PurchasePendingList> lists;
    private MyDatabaseHelper   myDatabaseHelper ;

    public PurchasePendingListAdapter(FragmentActivity context, List<PurchasePendingList> lists) {
        this.context = context;
        this.lists = lists;
        myDatabaseHelper = new MyDatabaseHelper(context);
    }

    @NonNull
    @NotNull
    @Override
    public PurchasePendingListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PurchasePendingListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.purchase_pending_list_layout, parent, false);
        return new PurchasePendingListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PurchasePendingListAdapter.viewHolder holder, int position) {
        PurchasePendingList currentList = lists.get(position);
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1500)) {
            holder.itembinding.edit.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1501)) {
            holder.itembinding.view.setVisibility(View.GONE);
        }
        try {
            holder.itembinding.date.setText(":  "+new DateFormatRight(context,currentList.getDate()).dateFormatForPm());
            holder.itembinding.enterpriseName.setText(":  " + currentList.getEnterpriseName());
            holder.itembinding.companyName.setText(":  " + currentList.getCompanyName());
            holder.itembinding.ownerName.setText(":  " + currentList.getCustomerFname());
            holder.itembinding.subbmittedBy.setText(currentList.getFullName());
            holder.itembinding.orderSerial.setText(":  " + currentList.getId());
            holder.itembinding.purchaseOrderId.setText(":  " + currentList.getOrderSerial());
            try {
                Glide.with(context).load(ImageBaseUrl.image_base_url + currentList.getProfilePhoto()).centerCrop().
                        error(R.drawable.erro_logo).placeholder(R.drawable.erro_logo).
                        into(holder.itembinding.submittedByImnage);

            } catch (NullPointerException e) {
                Log.d("ERROR", e.getMessage());
                Glide.with(context).load(R.drawable.erro_logo).into(holder.itembinding.submittedByImnage);
            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }

        holder.itembinding.edit.setOnClickListener(v -> {
            try {
                myDatabaseHelper.deleteAllData();
            } catch (Exception e) {
                Log.d("ERROR", e.getLocalizedMessage());
            }

            String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
            /*if (currentProfileTypeId != null) {
                if (!currentProfileTypeId.equals("7")) {
                    Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                    return;
                }
            }
*/

            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1500)) {
                Bundle bundle = new Bundle();
                bundle.putString("sid", lists.get(holder.getAdapterPosition()).getSerialID());
                bundle.putString("pageName",  "Edit Purchase Pending Order Id ");
                bundle.putString("orderId",  currentList.getId());
                AllPurchaseListFragment.pageNumber = 1;
                Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_allPurchaseListFragment_to_editPurchaseData, bundle);
                return;
            } else {
                Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
            }


        });

        holder.itembinding.view.setOnClickListener(v -> {
            try {
                goToDetails(holder,currentList.getId(),currentList.getOrderSerial(),currentList.getOrderVendorID());

            } catch (Exception e) {
                Log.d("Error", e.getMessage());
            }
        });
        holder.itemView.setOnClickListener(v -> goToDetails(holder,currentList.getId(),currentList.getOrderSerial(),currentList.getOrderVendorID()));

    }

    private void goToDetails(viewHolder holder,String refId,String orderSerialId,String orderVendorId) {
        Bundle bundle = new Bundle();
        bundle.putString("RefOrderId", refId);
        bundle.putString("OrderSerialId",orderSerialId);
        bundle.putString("orderVendorId", orderVendorId);
        bundle.putString("portion", "PENDING_PURCHASE");
        bundle.putString("pageName", "Pending Purchase Details");
        bundle.putString("status", "2");
        bundle.putString("pageName", "Purchase History Details Order Id "+refId);
        AllPurchaseListFragment.pageNumber = 1;
        Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_allPurchaseListFragment_to_pendingPurchaseDetailsFragment, bundle);

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private PurchasePendingListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull PurchasePendingListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}