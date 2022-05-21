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
import com.rupayan_housing.utils.ProductionUtils;
import com.rupayan_housing.utils.SaleUtil;
import com.rupayan_housing.view.fragment.store.StoreListFragment;


import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductionAdapter extends RecyclerView.Adapter<ProductionAdapter.MyHolder> {
    private FragmentActivity context;
    private List<String> productionNameList;
    private List<Integer> productionImageList;

    @NonNull
    @NotNull
    @Override
    public ProductionAdapter.MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ManagementFragmentModelBindingBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.management_fragment_model_binding, parent, false);
        return new ProductionAdapter.MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductionAdapter.MyHolder holder, int position) {
        Integer currentImage = productionImageList.get(position);
        String currentName = productionNameList.get(position);
        holder.binding.textHomeItem.setText(currentName);
        holder.binding.imageHomeItem.setImageDrawable(ContextCompat.getDrawable(context, currentImage));

        holder.binding.setClickHandle(() -> {


            if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.washingCrushingList)) {
                /**
                 * washing crushing list
                 */
          /*      if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1075)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("porson", ProductionUtils.washingCrushingList);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_productionAllListFragment, bundle);
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
                return;*/
            }
            if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.pendingWashingCrushing)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1474)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("porson", ProductionUtils.pendingWashingCrushing);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_productionAllListFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.packagingList)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1079)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("porson", ProductionUtils.packagingList);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_productionAllListFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
        /*    if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.packegingPending)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1637)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("porson", ProductionUtils.packegingPending);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_productionAllListFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
        */

          /*  if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.cartooningPending)) {
                Bundle bundle = new Bundle();
                bundle.putString("porson", ProductionUtils.cartooningPending);
                Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_productionAllListFragment, bundle);
            }
*/
            if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.packatingList)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1447)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("porson", ProductionUtils.packatingList);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_productionAllListFragment, bundle);
                    return;
                }
                Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();


            }

            if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.unpackCartoning)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1478)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("porson", ProductionUtils.unpackCartoning);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_unPackCartoningFragment, bundle);
                    return;
                }
                Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();

            }

            if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.iodizationList)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1077)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("porson", ProductionUtils.iodizationList);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_productionAllListFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            }
            if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.pendingIodization)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1077)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("porson", ProductionUtils.pendingIodization);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_productionAllListFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            }
            /**
             * for add new Washing & Crushing
             */

            if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.washingCrushing)) {
               /* try {
                    String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1074)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("portion", ProductionUtils.washingCrushing);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_washingAndCrushing, bundle);

                        return;
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }*/
            }
            /**
             * for handle iodization
             */
            if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.iodization)) {
                try {
                    String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1076)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("portion", ProductionUtils.washingCrushing);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_iodizationFragment, bundle);
                        return;
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            }
            /**
             * For handle Packaging
             */
            if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.packaging)) {
                try {

                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1078)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("portion", ProductionUtils.packaging);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_addNewPackaging, bundle);
                        return;
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            }

            /**
             * For handle Packating
             */
            if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.packating)) {
                try {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1475)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("portion", ProductionUtils.packating);
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_addNewPackating, bundle);
                        return;
                    } else {
                        Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            }


            if (productionNameList.get(holder.getAdapterPosition()).equals(ProductionUtils.stockInfo)) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("porson", ProductionUtils.stockInfo);
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
        return productionNameList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        private ManagementFragmentModelBindingBinding binding;

        public MyHolder(ManagementFragmentModelBindingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
