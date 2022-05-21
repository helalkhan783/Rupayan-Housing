package com.rupayan_housing.adapter;

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
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.ProductionUtils;
import com.rupayan_housing.utils.ReconciliationUtils;
import com.rupayan_housing.view.fragment.store.StoreListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class ReconciliationSubAdapter extends RecyclerView.Adapter<ReconciliationSubAdapter.MyHolder> {
    FragmentActivity context;
    List<String> reconciliationItemName;
    List<Integer> reconciliationItemImage;

    @NonNull
    @NotNull
    @Override
    public ReconciliationSubAdapter.MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ManagementFragmentModelBindingBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.management_fragment_model_binding, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReconciliationSubAdapter.MyHolder holder, int position) {
        Integer currentImage = reconciliationItemImage.get(position);
        String currentName = reconciliationItemName.get(position);
        holder.binding.textHomeItem.setText(currentName);

        holder.binding.imageHomeItem.setImageDrawable(ContextCompat.getDrawable(context, currentImage));

        holder.binding.setClickHandle(() -> {


            if (reconciliationItemName.get(holder.getAdapterPosition()).equals(ReconciliationUtils.addNewReconciliation)) {

                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1084)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("portion", ReconciliationUtils.addNewReconciliation);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_addNewReconcilation, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }


            }


            if (reconciliationItemName.get(holder.getAdapterPosition()).equals(ReconciliationUtils.reconciliationHistoryList)) {

                try {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1085)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("portion", ReconciliationUtils.reconciliationHistoryList);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_stockAllListFragment, bundle);
                        return;
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            }


            if (reconciliationItemName.get(holder.getAdapterPosition()).equals(ReconciliationUtils.pendingReconciliationList)) {
                /**
                 pending Reconciliation List
                 */
                try {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1086)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("portion", ReconciliationUtils.pendingReconciliationList);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_stockAllListFragment, bundle);
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            }


            if (reconciliationItemName.get(holder.getAdapterPosition()).equals(ReconciliationUtils.declinedReconciliationList)) {
                try {

                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1087) ||
                            PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("portion", ReconciliationUtils.declinedReconciliationList);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_stockAllListFragment, bundle);
                        return;
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toasty.info(context, "" + e.getMessage(), Toasty.LENGTH_LONG).show();

                }
            }
            if (reconciliationItemName.get(holder.getAdapterPosition()).equals(ReconciliationUtils.stockInfo)) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("porson", ReconciliationUtils.stockInfo);
                    bundle.putString("pageName", "Stock List");
                    StoreListFragment.manage = 0;
                    StoreListFragment.endScroll = false;
                    StoreListFragment.pageNumber = 1;
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_storeListFragment, bundle);
                } catch (Exception e) {
                }
            }


        });
    }

    @Override
    public int getItemCount() {
        return reconciliationItemName.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private ManagementFragmentModelBindingBinding binding;

        public MyHolder(ManagementFragmentModelBindingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

