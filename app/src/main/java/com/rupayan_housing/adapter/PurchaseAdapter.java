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
import com.rupayan_housing.clickHandle.QcQaAdapterClickHandle;
import com.rupayan_housing.databinding.ManagementFragmentModelBindingBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.PurchaseUtill;
import com.rupayan_housing.utils.ReportUtils;
import com.rupayan_housing.utils.SaleUtil;
import com.rupayan_housing.view.fragment.purchase.AllPurchaseListFragment;
import com.rupayan_housing.view.fragment.store.StoreListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.MyHolder> {
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

        holder.binding.setClickHandle(() -> {

            if (saleNameList.get(holder.getAdapterPosition()).equals(PurchaseUtill.purchaseHistory)) {

                Bundle bundle = new Bundle();
                bundle.putString("portion", ReportUtils.stockInOutReport);
                bundle.putString("pageName", "Check Inventory");
                Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reconciliationReportFragment, bundle);
                return;
            }
            /**
             * for purchase pending list
             */
            if (saleNameList.get(holder.getAdapterPosition()).equals(PurchaseUtill.pendingPurchaseList)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1498)) {
                    Bundle bundle = new Bundle();
                    AllPurchaseListFragment.isFirstLoad = 0;
                    bundle.putString("porson", PurchaseUtill.pendingPurchaseList);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_allPurchaseListFragment, bundle);
                    return;
                }

                Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();


            }


            if (saleNameList.get(holder.getAdapterPosition()).equals(PurchaseUtill.pendingPurchaseReturn)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1052)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("porson", PurchaseUtill.pendingPurchaseReturn);
                    AllPurchaseListFragment.isFirstLoad = 0;
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_allPurchaseListFragment, bundle);
                    return;
                }
                Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();


            }

            if (saleNameList.get(holder.getAdapterPosition()).equals(PurchaseUtill.purchaseReturnHistory)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1057)) {
                    Bundle bundle = new Bundle();
                    AllPurchaseListFragment.isFirstLoad = 0;
                    bundle.putString("porson", PurchaseUtill.purchaseReturnHistory);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_allPurchaseListFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }


            if (saleNameList.get(holder.getAdapterPosition()).equals(PurchaseUtill.declinePurchaseList)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1055)) {
                    Bundle bundle = new Bundle();
                    AllPurchaseListFragment.isFirstLoad = 0;
                    bundle.putString("porson", PurchaseUtill.declinePurchaseList);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_allPurchaseListFragment, bundle);
                    return;
                }
                Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();


            }

            if (saleNameList.get(holder.getAdapterPosition()).equals(PurchaseUtill.addNewPurchase)) {
                /**
                 * go to add new purchase fragment
                 */
                try {
                    String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();

                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(6)) {
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_addNewPurchase2);
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
                return;
            }
            if (saleNameList.get(holder.getAdapterPosition()).equals(PurchaseUtill.purchaseReturn)) {
                try {
                    String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();

                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1056)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("portion", PurchaseUtill.purchaseReturn);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_purchaseReturnFragment, bundle);
                        return;
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            }

            if (saleNameList.get(holder.getAdapterPosition()).equals(SaleUtil.stockInfo)) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("porson", SaleUtil.stockInfo);
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
        return saleNameList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private ManagementFragmentModelBindingBinding binding;

        public MyHolder(ManagementFragmentModelBindingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
