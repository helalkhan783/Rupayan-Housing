package com.rupayan_housing.view.fragment.sale;

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
import com.rupayan_housing.adapter.SaleHistoryListAdapter;
import com.rupayan_housing.clickHandle.SaleHistoryClickHandle;
import com.rupayan_housing.databinding.SaleHistoryListLayoutBinding;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.SaleHistoryList;
import com.rupayan_housing.serverResponseModel.SaleReturnHistoryList;
import com.rupayan_housing.utils.ImageBaseUrl;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class SaleReturnHistoryAdapter extends RecyclerView.Adapter<SaleReturnHistoryAdapter.viewHolder> {
    private FragmentActivity context;
    List<SaleReturnHistoryList> lists;
    //private MyDatabaseHelper myDatabaseHelper;

    public SaleReturnHistoryAdapter(FragmentActivity context, List<SaleReturnHistoryList> lists) {
        this.context = context;
        this.lists = lists;
    }
    @NonNull
    @NotNull
    @Override
    public SaleReturnHistoryAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SaleHistoryListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.sale_history_list_layout, parent, false);
        return new SaleReturnHistoryAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SaleReturnHistoryAdapter.viewHolder holder, int position) {
        SaleReturnHistoryList currentList = lists.get(position);
        holder.itembinding.layout.setVisibility(View.GONE);
        holder.itembinding.delete.setVisibility(View.GONE);
        if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1534)) {
            holder.itembinding.view.setVisibility(View.GONE);
        }

        if (currentList.getEntryDate() == null) {
            holder.itembinding.date.setText(":  ");


        } else {

            holder.itembinding.date.setText(":  " + new DateFormatRight(context,currentList.getEntryDate()).yearMothDayToDayYearMonth());

        }
        if (currentList.getStoreName() == null) {
            holder.itembinding.enterpriseName.setText(":  ");
        } else {
            holder.itembinding.enterpriseName.setText(":  " + currentList.getStoreName());

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

        holder.itembinding.saleOrderId.setText(":  "+ currentList.getOrderID());


        if (currentList.getOrderSerial() == null) {
            holder.itembinding.orderSerial.setText(":");
        } else {
            holder.itembinding.orderSerial.setText(":  " + currentList.getOrderSerial());

        }
        holder.itembinding.view.setOnClickListener(v -> {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1533)) {
                Bundle bundle = new Bundle();
                bundle.putString("RefOrderId",lists.get(holder.getAdapterPosition()).getOrderID());
                bundle.putString("portion", "SALES_RETURNS_DETAILS");
                bundle.putString("orderVendorId", lists.get(holder.getAdapterPosition()).getVendorID());
                // bundle.putString("portion", "PENDING_SALE");
                // bundle.putString("portion", "Sale_Return_Pending");
                bundle.putString("pageName", "Sales Return Details");
                bundle.putString("forHistoryLayout","1");
                // bundle.putString("status", "2");
                SaleAllListFragment.pageNumber = 1;
                Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_saleAllListFragment_to_purchaseReturnPendingDetailsFragment, bundle);
                return;

            } else {
                Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
            }




     /*       Bundle bundle = new Bundle();
            bundle.putString("TypeKey", String.valueOf(currentList.getOrderType()));
            bundle.putString("RefOrderId", currentList.getOrderID());
            bundle.putString("orderVendorId", currentList.getVendorID());
            bundle.putString("portion", "SALES_RETURNS_DETAILS");
            bundle.putString("pageName", "Sales Return Details");
            Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_saleAllListFragment_to_pendingSalesReturnDetails,bundle);
     */   });

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
