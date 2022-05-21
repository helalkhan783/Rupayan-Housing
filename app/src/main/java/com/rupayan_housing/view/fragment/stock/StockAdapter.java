package com.rupayan_housing.view.fragment.stock;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.ManagementFragmentModelBindingBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.permission.HelperClass;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.StockUtils;
import com.rupayan_housing.view.fragment.sale.SaleAllListFragment;
import com.rupayan_housing.view.fragment.store.StoreListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class StockAdapter extends RecyclerView.Adapter<StockAdapter.MyHolder> {
    private FragmentActivity context;
    private List<String> stockItemName;
    private List<Integer> stockItemImage;

    @NonNull
    @NotNull
    @Override
    public StockAdapter.MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ManagementFragmentModelBindingBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.management_fragment_model_binding, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StockAdapter.MyHolder holder, int position) {
        Integer currentImage = stockItemImage.get(position);
        String currentName = stockItemName.get(position);
        holder.binding.textHomeItem.setText(currentName);
        holder.binding.imageHomeItem.setImageDrawable(ContextCompat.getDrawable(context, currentImage));

        holder.binding.setClickHandle(() -> {

            if (stockItemName.get(holder.getAdapterPosition()).equals(StockUtils.addNewTransferred)) {
                /**
                 * transferred History List
                 */
                try {
                    String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1080)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("portion", StockUtils.addNewTransferred);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_addNewTransfer, bundle);
                        return;
                    } else {
                        Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            }

            if (stockItemName.get(holder.getAdapterPosition()).equals(StockUtils.transferredInHistoryList)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1081)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("portion", StockUtils.transferredInHistoryList);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_stockAllListFragment, bundle);
                    return;

                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (stockItemName.get(holder.getAdapterPosition()).equals(StockUtils.transferredOutHistoryList)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1081)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("portion", StockUtils.transferredOutHistoryList);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_stockAllListFragment, bundle);
                    return;

                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
             }

            if (stockItemName.get(holder.getAdapterPosition()).equals(StockUtils.pendingTransferredList)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1082)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("portion", StockUtils.pendingTransferredList);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_stockAllListFragment, bundle);
                    return;
                }
                else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            }


            if (stockItemName.get(holder.getAdapterPosition()).equals(StockUtils.declineTransferredList)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1083)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("portion", StockUtils.declineTransferredList);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_stockAllListFragment, bundle);
                    return;
                }
                else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            }


            if (stockItemName.get(holder.getAdapterPosition()).equals(StockUtils.stockInfo)) {
                try {
                    HelperClass helperClass = new HelperClass(context);
                    helperClass.navigate(StockUtils.stockInfo,holder.binding.getRoot(),R.id.action_managementFragment_to_storeListFragment);
                } catch (Exception e) {
                }
            }


        });
    }

    @Override
    public int getItemCount() {
        return stockItemName.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        private ManagementFragmentModelBindingBinding binding;

        public MyHolder(ManagementFragmentModelBindingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

