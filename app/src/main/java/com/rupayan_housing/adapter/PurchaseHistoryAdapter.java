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
import com.rupayan_housing.databinding.SalePendingListLayoutBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.PurchaseHistoryList;
import com.rupayan_housing.serverResponseModel.SalePendingList;
import com.rupayan_housing.utils.ImageBaseUrl;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.purchase.AllPurchaseListFragment;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.viewHolder> {
    private FragmentActivity context;
    List<PurchaseHistoryList> lists;
    private MyDatabaseHelper myDatabaseHelper;
    private String datee;

    public PurchaseHistoryAdapter(FragmentActivity context, List<PurchaseHistoryList> lists) {
        this.context = context;
        this.lists = lists;
        myDatabaseHelper = new MyDatabaseHelper(context);
    }

    @NonNull
    @NotNull
    @Override
    public PurchaseHistoryAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PurchaseHistoryListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.purchase_history_list_layout, parent, false);
        return new PurchaseHistoryAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PurchaseHistoryAdapter.viewHolder holder, int position) {
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1500)) {
            holder.itembinding.edit.setVisibility(View.GONE);

        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1501)) {
            holder.itembinding.view.setVisibility(View.GONE);
        }

        PurchaseHistoryList currentList = lists.get(position);
        holder.itembinding.date.setText(":  "+new DateFormatRight(context,currentList.getDate()).dateFormatForPm());
        holder.itembinding.enterpriseName.setText(":  " + currentList.getEnterpriseName());
        holder.itembinding.companyName.setText(":  " + currentList.getCompanyName());
        holder.itembinding.ownerName.setText(":  " + currentList.getCustomerFname());
        holder.itembinding.processdBy.setText(currentList.getFullName());
        holder.itembinding.orderSerial.setText(":  " + currentList.getId());
        holder.itembinding.purchaseOrderId.setText(":  " + currentList.getOrder_serial());
        try {
            Glide.with(context).load(ImageBaseUrl.image_base_url + currentList.getProfilePhoto()).centerCrop().
                    error(R.drawable.erro_logo).placeholder(R.drawable.erro_logo).
                    into(holder.itembinding.purchaseImage);

        } catch (NullPointerException e) {
            Log.d("ERROR", e.getMessage());
            Glide.with(context).load(R.mipmap.ic_launcher).into(holder.itembinding.purchaseImage);
        }
        holder.itembinding.setClickHandle(new PurchaseEditClickHandle() {
            @Override
            public void view() {
                /**
                 * will add view
                 */
            }

            @Override
            public void edit() {
                try {
                    myDatabaseHelper.deleteAllData();
                } catch (Exception e) {
                    Log.d("ERROR", e.getLocalizedMessage());
                }

                String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1500)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("sid", lists.get(holder.getAdapterPosition()).getSerialID());
                    bundle.putString("pageName",  "Update Purchase Order Id "+currentList.getId());
                    bundle.putString("orderId",  currentList.getId());
                    AllPurchaseListFragment.pageNumber = 1;
                    Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_allPurchaseListFragment_to_editPurchaseData, bundle);
                    return;
                } else {
                    Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                }

             }
        });
        /**
         * go to details page
         */
        holder.itembinding.view.setOnClickListener(v -> {
         gotoViewPage(holder,currentList.getId(),currentList.getOrder_vendorID());
        });
        holder.itemView.setOnClickListener(v -> gotoViewPage(holder,currentList.getId(),currentList.getOrder_vendorID()));


    }

    private void gotoViewPage(viewHolder holder,String refId,String vId) {
        Bundle bundle = new Bundle();
        bundle.putString("RefOrderId", refId);
        bundle.putString("orderVendorId", vId);
        bundle.putString("portion", "PENDING_PURCHASE");
        bundle.putString("porson", "PurchaseHistoryDetails");
        bundle.putString("pageName", "Purchase History Details Order Id "+refId);
        AllPurchaseListFragment.pageNumber = 1;
        Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_allPurchaseListFragment_to_pendingPurchaseDetailsFragment, bundle);

    }


    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private PurchaseHistoryListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull PurchaseHistoryListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}