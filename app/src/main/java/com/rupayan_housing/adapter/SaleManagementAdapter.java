package com.rupayan_housing.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.ManagementFragmentModelBindingBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.SaleUtil;
import com.rupayan_housing.utils.StockUtils;
import com.rupayan_housing.view.fragment.store.StoreListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SaleManagementAdapter extends RecyclerView.Adapter<SaleManagementAdapter.MyHolder> {
    private FragmentActivity context;
    private List<String> saleNameList;
    private List<Integer> saleImageList;


    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ManagementFragmentModelBindingBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.management_fragment_model_binding, parent, false);

        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        Integer currentImage = saleImageList.get(position);
        String currentName = saleNameList.get(position);
        holder.binding.textHomeItem.setText(currentName);
        holder.binding.imageHomeItem.setImageDrawable(ContextCompat.getDrawable(context, currentImage));

        /**
         * handle item Click
         */
        holder.binding.setClickHandle(() -> {
            try {

                Bundle bundle = new Bundle();
                if (saleNameList.get(holder.getAdapterPosition()).equals(SaleUtil.newSale)) {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(5)) {
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_addNewSale);
                        return;
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }
                    return;
                }
                if (saleNameList.get(holder.getAdapterPosition()).equals(SaleUtil.salePending)) {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(24)) {
                        bundle.putString("porson", SaleUtil.salePending);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_saleAllListFragment, bundle);
                        return;
                    }
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

                if (saleNameList.get(holder.getAdapterPosition()).equals(SaleUtil.saleHistory)) {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(22)) {
                        bundle.putString("porson", SaleUtil.saleHistory);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_saleAllListFragment, bundle);
                        return;
                    }
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();


                }

                if (saleNameList.get(holder.getAdapterPosition()).equals(SaleUtil.declineSaleList)) {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1090)) {
                        bundle.putString("porson", SaleUtil.declineSaleList);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_saleAllListFragment, bundle);
                        return;
                    }
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();


                }
                if (saleNameList.get(holder.getAdapterPosition()).equals(SaleUtil.saleReturnHistory)) {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1092)) {
                        bundle.putString("porson", SaleUtil.saleReturnHistory);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_saleAllListFragment, bundle);
                        return;
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }

                }
                if (saleNameList.get(holder.getAdapterPosition()).equals(SaleUtil.salePendingReturn)) {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1299)) {
                        bundle.putString("porson", SaleUtil.salePendingReturn);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_saleAllListFragment, bundle);
                        return;
                    }
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();

                }

                if (saleNameList.get(holder.getAdapterPosition()).equals(SaleUtil.saleReturn)) {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1091)) {
                        bundle.putString("porson", SaleUtil.saleReturn);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_salesReturnFragment, bundle);
                        return;
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }
                }

                if (saleNameList.get(holder.getAdapterPosition()).equals(SaleUtil.stockInfo)) {

                    bundle.putString("porson", SaleUtil.stockInfo);
                    bundle.putString("pageName", "Stock List");
                    StoreListFragment.manage = 0;
                    StoreListFragment.endScroll = false;
                    StoreListFragment.pageNumber = 1;
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_storeListFragment, bundle);

                }
            } catch (Exception e) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return saleImageList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private ManagementFragmentModelBindingBinding binding;

        public MyHolder(ManagementFragmentModelBindingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
