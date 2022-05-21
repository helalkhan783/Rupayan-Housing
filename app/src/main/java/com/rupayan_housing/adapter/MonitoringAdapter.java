package com.rupayan_housing.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.rupayan_housing.utils.MonitoringUtil;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DashboardFragment;
import com.rupayan_housing.view.fragment.monitoring.MonitoringListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MonitoringAdapter extends RecyclerView.Adapter<MonitoringAdapter.MyHolder> {
    private FragmentActivity context;
    private List<String> monitoringNameList;
    private List<Integer> monitoringImageList;

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ManagementFragmentModelBindingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.management_fragment_model_binding, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        Integer currentImage = monitoringImageList.get(position);
        String currentName = monitoringNameList.get(position);
        holder.binding.textHomeItem.setText(currentName);
        holder.binding.imageHomeItem.setImageDrawable(ContextCompat.getDrawable(context, currentImage));


        /**
         * For handle Monitoring
         */
        holder.binding.setClickHandle(() -> {
            if (monitoringNameList.get(position).equals(MonitoringUtil.addMonitoring)) {
                String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
                if (currentProfileTypeId.equals("4")) {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1426)) {
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_addNewMonitoring);
                        return;
                    } else {
                        Toasty.warning(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                    }
                    return;
                }
                Toasty.warning(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();

            }
            if (monitoringNameList.get(position).equals(MonitoringUtil.monitoringHistory)) {
                try {
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1427)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("portion", MonitoringUtil.monitoringHistory);
                        MonitoringListFragment.isFirstLoad = 0;
                        MonitoringListFragment.pageNumber = 1;
                        DashboardFragment.manageMonitor = false;// ekhan theke gele date set hobe na
                        Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_monitoringListFragment, bundle);
                        return;
                    } else
                        Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();


                } catch (Exception e) {
                }
               /* if (PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId().equals("7")){
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("portion", MonitoringUtil.monitoringHistory);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_monitoringListFragment, bundle);

                }catch (Exception e){}
                return;
                }

                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1427) || PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1)) {
                 try{
                     Bundle bundle = new Bundle();
                     bundle.putString("portion", MonitoringUtil.monitoringHistory);
                     Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_monitoringListFragment, bundle);

                 }
                 catch (Exception e){
                     Log.d("Error",e.getMessage());
                 }

                   return;
                } else {
                    Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                }*/
            }
        });


    }

    @Override
    public int getItemCount() {
        return monitoringNameList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private ManagementFragmentModelBindingBinding binding;

        public MyHolder(ManagementFragmentModelBindingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
