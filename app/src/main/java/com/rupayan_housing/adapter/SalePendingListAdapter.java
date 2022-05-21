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
import com.rupayan_housing.clickHandle.SalePendingListEditClickHandle;
import com.rupayan_housing.databinding.CategoryItemListLayoutBinding;
import com.rupayan_housing.databinding.SalePendingListLayoutBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.CategoryList;
import com.rupayan_housing.serverResponseModel.SalePendingList;
import com.rupayan_housing.utils.ImageBaseUrl;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.sale.SaleAllListFragment;
import com.rupayan_housing.view.fragment.sale.SaleViewDetailsTree;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

public class SalePendingListAdapter extends RecyclerView.Adapter<SalePendingListAdapter.viewHolder> {
    private FragmentActivity context;
    List<SalePendingList> lists;
    public static String serialId, VendorId;
    private MyDatabaseHelper myDatabaseHelper;
    View view;
    SaleViewDetailsTree saleViewDetailsTree;

    public SalePendingListAdapter(FragmentActivity context, List<SalePendingList> lists, View view, SaleViewDetailsTree saleViewDetailsTree) {
        this.context = context;
        this.lists = lists;
        myDatabaseHelper = new MyDatabaseHelper(context);
        this.view = view;
        this.saleViewDetailsTree = saleViewDetailsTree;
    }

    @NonNull
    @NotNull
    @Override
    public SalePendingListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SalePendingListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.sale_pending_list_layout, parent, false);
        return new SalePendingListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SalePendingListAdapter.viewHolder holder, int position) {
        SalePendingList currentList = lists.get(position);
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1534)) {
            holder.itembinding.view.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1533)) {
            holder.itembinding.edit.setVisibility(View.GONE);
        }
        try {
            myDatabaseHelper.deleteAllData();
        } catch (Exception e) {

        }
        if (currentList.getDate() == null) {
            holder.itembinding.date.setText(":");

        } else {

            holder.itembinding.date.setText(":  " + new DateFormatRight(context, currentList.getDate()).dateFormatForPm());

        }
        if (currentList.getEnterpriseName() == null) {
            holder.itembinding.enterpriseName.setText(":");

        } else {
            holder.itembinding.enterpriseName.setText(":  " + currentList.getEnterpriseName());
        }
        if (currentList.getCompanyName() == null) {
            holder.itembinding.companyName.setText(":  ");

        } else {
            holder.itembinding.companyName.setText(":  " + currentList.getCompanyName());

        }
        if (currentList.getCustomerFname() == null) {
            holder.itembinding.ownerName.setText(":  ");

        } else {
            holder.itembinding.ownerName.setText(":  " + currentList.getCustomerFname());

        }


        holder.itembinding.processdBy.setText(currentList.getFullName());


        if (currentList.getOrderSerial() == null) {
            holder.itembinding.orderSerial.setText(":");
        } else {
            holder.itembinding.orderSerial.setText(":  " + currentList.getOrderSerial());
        }
        VendorId = String.valueOf(currentList.getId());


        try {
            Glide.with(context).load(ImageBaseUrl.image_base_url + currentList.getProfilePhoto()).centerCrop().
                    error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).
                    into(holder.itembinding.saleImage);

        } catch (NullPointerException e) {
            Log.d("ERROR", e.getMessage());
            Glide.with(context).load(R.mipmap.ic_launcher).into(holder.itembinding.saleImage);
        }

        /**
         * now handle onlClick
         */
        holder.itembinding.setClickHandle(new SalePendingListEditClickHandle() {
            @Override
            public void edit() {
                try {
                    SaleAllListFragment.pageNumber = 1;

                    String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
                    /*if (currentProfileTypeId != null) {
                        if (!currentProfileTypeId.equals("7")) {
                            Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                            return;
                        }
                    }*/

                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1533)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("sid", lists.get(holder.getAdapterPosition()).getSerialID());
                        bundle.putString("pageName", "Edit Sale Pending Order Id ");
                        bundle.putString("orderId", currentList.getId());
                        SaleAllListFragment.pageNumber = 1;
                        Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_saleAllListFragment_to_editSaleData, bundle);
                        return;
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            }

            @Override
            public void view() {
                try {
                    goToView(holder, currentList.getId(), currentList.getOrderVendorID(), currentList.getId());

                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            }


        });

        holder.itemView.setOnClickListener(v -> goToView(holder, currentList.getId(), currentList.getOrderVendorID(), currentList.getId()));

        /**
         * For show pending sale details
         */
        holder.itembinding.view.setOnClickListener(v -> {
            try {
                saleViewDetailsTree.view(lists.get(holder.getAdapterPosition()).getId(), currentList.getOrderVendorID());
            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
        });
    }

    private void goToView(SalePendingListAdapter.viewHolder holder, String refId, String vId, String odId) {
        if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1534)) {
            Bundle bundle = new Bundle();
            bundle.putString("RefOrderId", refId);
            bundle.putString("orderVendorId", vId);
            bundle.putString("portion", "PENDING_SALE");
            bundle.putString("status", "2");
            bundle.putString("porson", "SaleHistoryDetails");
            bundle.putString("pageName", "Sale Pending Details Order Id (" + odId + ")");
            SaleAllListFragment.pageNumber = 1;
            Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_saleAllListFragment_to_pendingPurchaseDetailsFragment, bundle);
            return;

        } else {
            Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private SalePendingListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull SalePendingListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}