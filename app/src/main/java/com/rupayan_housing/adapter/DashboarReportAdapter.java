package com.rupayan_housing.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.ManagementFragmentModelBindingBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.utils.DashBoardReportUtils;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.ReportUtils;
import com.rupayan_housing.utils.SupplierUtils;
import com.rupayan_housing.view.fragment.monitoring.MonitoringListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.NonNull;

public class DashboarReportAdapter extends RecyclerView.Adapter<DashboarReportAdapter.MyHolder> {
    FragmentActivity context;
    List<String> nameList;
    List<Integer> integers;
    View view;

    public DashboarReportAdapter(FragmentActivity context, List<String> nameList, List<Integer> integers, View view) {
        this.context = context;
        this.nameList = nameList;
        this.integers = integers;
        this.view = view;
    }

    @NonNull
    @NotNull
    @Override
    public DashboarReportAdapter.MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ManagementFragmentModelBindingBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.management_fragment_model_binding, parent, false);
        return new DashboarReportAdapter.MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DashboarReportAdapter.MyHolder holder, int position) {
        Integer currentImage = integers.get(position);
        String currentName = nameList.get(position);
        holder.binding.textHomeItem.setText(currentName);

        holder.binding.imageHomeItem.setImageDrawable(ContextCompat.getDrawable(context, currentImage));

        holder.binding.setClickHandle(() -> {
            Bundle bundle = new Bundle();
            if (nameList.get(position).equals(DashBoardReportUtils.iodization)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1608)) {
                    bundle.putString("pageName", "Iodization % (Based on Zone wise Total Mill)");
                    bundle.putString("portion", DashBoardReportUtils.iodization);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(DashBoardReportUtils.iodineStock)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1609)) {
                    bundle.putString("pageName", "Iodine Stock (Based on Zone)");
                    bundle.putString("portion", DashBoardReportUtils.iodineStock);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(DashBoardReportUtils.acQa)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1610)) {
                    bundle.putString("pageName", "QC/QA % (Based on Zone wise Total Mill");
                    bundle.putString("portion", DashBoardReportUtils.acQa);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(DashBoardReportUtils.monitoring)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1611)) {
                    bundle.putString("pageName", "Monitoring (Based on Zone wise)");
                    bundle.putString("portion", DashBoardReportUtils.monitoring);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(DashBoardReportUtils.agencyMonitoring)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1612)) {
                    bundle.putString("pageName", "Monitoring (Based on Surveillance Agency)");
                    bundle.putString("portion", DashBoardReportUtils.agencyMonitoring);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(DashBoardReportUtils.issueMonitoring)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1613)) {
                    bundle.putString("pageName", "Monitoring (Based on Monitoring Issue)");
                    bundle.putString("portion", DashBoardReportUtils.issueMonitoring);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(DashBoardReportUtils.sale)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1633)) {
                    bundle.putString("pageName", "Sale (Based on Slat Type)");
                    bundle.putString("portion", DashBoardReportUtils.sale);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(DashBoardReportUtils.production)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1634)) {
                    bundle.putString("pageName", "Production (Based on Slat Type)");
                    bundle.putString("portion", DashBoardReportUtils.production);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(DashBoardReportUtils.purchase)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1635)) {
                    bundle.putString("pageName", "Purchase (Based on Slat Type)");
                    bundle.putString("portion", DashBoardReportUtils.purchase);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(DashBoardReportUtils.topTenMiller)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1618)) {
                    bundle.putString("pageName", "Top 10 Miller (Based on Iodized Salt Sale)");
                    bundle.putString("portion", DashBoardReportUtils.topTenMiller);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(DashBoardReportUtils.zoneList)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1619)) {
                    bundle.putString("pageName", "Zone List Hierarchy (Based on Iodized Salt Sale)");
                    bundle.putString("portion", DashBoardReportUtils.zoneList);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }

        });
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private ManagementFragmentModelBindingBinding binding;

        public MyHolder(ManagementFragmentModelBindingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

