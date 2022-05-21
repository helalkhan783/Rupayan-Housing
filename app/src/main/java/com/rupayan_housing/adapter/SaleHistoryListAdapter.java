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
import com.rupayan_housing.clickHandle.SaleHistoryClickHandle;
import com.rupayan_housing.databinding.PurchaseHistoryListLayoutBinding;
import com.rupayan_housing.databinding.SaleHistoryListLayoutBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.HistoryList;
import com.rupayan_housing.serverResponseModel.PurchaseHistoryList;
import com.rupayan_housing.serverResponseModel.SaleHistoryList;
import com.rupayan_housing.utils.ImageBaseUrl;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.sale.SaleAllListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;


public class SaleHistoryListAdapter extends RecyclerView.Adapter<SaleHistoryListAdapter.viewHolder> {
    private FragmentActivity context;
    List<SaleHistoryList> lists;
    private MyDatabaseHelper myDatabaseHelper;

    public SaleHistoryListAdapter(FragmentActivity context, List<SaleHistoryList> lists) {
        this.context = context;
        this.lists = lists;
        myDatabaseHelper = new MyDatabaseHelper(context);
    }

    @NonNull
    @NotNull
    @Override
    public SaleHistoryListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SaleHistoryListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.sale_history_list_layout, parent, false);
        return new SaleHistoryListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SaleHistoryListAdapter.viewHolder holder, int position) {
        SaleHistoryList currentList = lists.get(position);

        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1534)) {
            holder.itembinding.view.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1533)) {
            holder.itembinding.delete.setVisibility(View.GONE);
        }
        if (currentList.getDate() == null) {
            holder.itembinding.date.setText(":  ");
        } else {
            holder.itembinding.date.setText(":  " + new DateFormatRight(context, currentList.getDate()).dateFormatForPm());


        }
        if (currentList.getEnterpriseName() == null) {
            holder.itembinding.enterpriseName.setText(":  ");
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

        if (currentList.getCustomerFname() == null) {
            holder.itembinding.processdBy.setText("");
        } else {
            holder.itembinding.processdBy.setText(" " + currentList.getFullName());

        }
        if (currentList.getOrderSerial() == null) {

            holder.itembinding.orderSerial.setText(":");
        } else {
            holder.itembinding.orderSerial.setText(":  " + currentList.getId());

        }
        holder.itembinding.saleOrderId.setText(":  " + currentList.getOrderSerial());

        try {
            Glide.with(context).load(ImageBaseUrl.image_base_url + currentList.getProfilePhoto()).centerCrop().
                    error(R.drawable.erro_logo).placeholder(R.drawable.erro_logo).
                    into(holder.itembinding.purchaseImage);

        } catch (NullPointerException e) {
            Log.d("ERROR", e.getMessage());
            Glide.with(context).load(R.drawable.erro_logo).into(holder.itembinding.purchaseImage);
        }

        holder.itembinding.view.setOnClickListener(v -> {
            goToView(holder, currentList.getId(), currentList.getOrderVendorID(), currentList.getId());
        });

        holder.itemView.setOnClickListener(v -> {
            goToView(holder, currentList.getId(), currentList.getOrderVendorID(), currentList.getId());
        });
        holder.itembinding.setClickHandle(new SaleHistoryClickHandle() {
            @Override
            public void edit() {
                try {
                    myDatabaseHelper.deleteAllData();
                } catch (Exception e) {
                    Log.d("ERROR", e.getMessage());
                }

                String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
             /*   if (currentProfileTypeId != null) {
                    if (!currentProfileTypeId.equals("7")) {
                        Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                        return;
                    }
                }
*/

                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1533)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("sid", lists.get(holder.getAdapterPosition()).getSerialID());
                    bundle.putString("pageName", "Edit Sale History Order Id ");
                    bundle.putString("orderId", currentList.getId());
                    SaleAllListFragment.pageNumber = 1;
                    Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_saleAllListFragment_to_editSaleData, bundle);

                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void view() {

            }
        });
    }

    private void goToView(viewHolder holder, String refId, String vId, String odId) {
        if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1534)) {
            Bundle bundle = new Bundle();
            bundle.putString("RefOrderId", refId);
            bundle.putString("orderVendorId", vId);
            bundle.putString("portion", "PENDING_SALE");
            bundle.putString("porson", "SaleHistoryDetails");
            bundle.putString("pageName", "Sale History Details Order Id " + odId + "");
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
        private SaleHistoryListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull SaleHistoryListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
