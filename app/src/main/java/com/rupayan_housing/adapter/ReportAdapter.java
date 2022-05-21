package com.rupayan_housing.adapter;

import android.os.Bundle;
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
import com.rupayan_housing.utils.ReportUtils;

import org.jetbrains.annotations.NotNull;


import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyHolder> {
    private FragmentActivity context;
    private List<String> nameList;
    private List<Integer> imageList;

    @NonNull
    @NotNull
    @Override
    public ReportAdapter.MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ManagementFragmentModelBindingBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.management_fragment_model_binding, parent, false);
        return new ReportAdapter.MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        String currentName = nameList.get(position);
        Integer CurrentImage = imageList.get(position);

        holder.binding.textHomeItem.setText((CharSequence) currentName);
        holder.binding.imageHomeItem.setImageDrawable(ContextCompat.getDrawable(context, CurrentImage));

        Bundle bundle = new Bundle();

        holder.binding.setClickHandle(() -> {
//
            if (nameList.get(position).equals(ReportUtils.saleDetailsReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1278)) {
                    bundle.putString("pageName", ReportUtils.saleDetailsReport);
                    bundle.putString("portion", ReportUtils.saleDetailsReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportListFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }    if (nameList.get(position).equals(ReportUtils.salesSummeryReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1278)) {
                    bundle.putString("pageName", ReportUtils.salesSummeryReport);
                    bundle.putString("portion", ReportUtils.salesSummeryReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportListFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(ReportUtils.noteSheetGenerateReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1549)) {
                    bundle.putString("pageName", ReportUtils.noteSheetGenerateReport);
                    bundle.putString("portion", ReportUtils.noteSheetGenerateReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportListFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(ReportUtils.processingReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1277)) {
                    bundle.putString("portion", ReportUtils.processingReport);
                    bundle.putString("pageName", ReportUtils.processingReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportListFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(ReportUtils.availAbleReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1543)) {
                    bundle.putString("portion", ReportUtils.availAbleReport);
                    bundle.putString("pageName",  ReportUtils.availAbleReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportListFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();

                }

            }   if (nameList.get(position).equals(ReportUtils.projectWiseItemReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1543)) {
                    bundle.putString("portion", ReportUtils.projectWiseItemReport);
                    bundle.putString("pageName",  ReportUtils.projectWiseItemReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportListFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();

                }

            }
            if (nameList.get(position).equals(ReportUtils.inventoryReport)) {

                bundle.putString("portion", ReportUtils.stockInOutReport);
                bundle.putString("pageName", ReportUtils.stockInOutReport);
                Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reconciliationReportFragment, bundle);


            }
            if (nameList.get(position).equals(ReportUtils.PacketingReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1556)) {
                    bundle.putString("pageName", "Packaging Report");
                    bundle.putString("portion", ReportUtils.PacketingReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_packetingReportFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(ReportUtils.packagingReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1557)) {
                    bundle.putString("pageName", "Cartoning Report");
                    bundle.putString("portion", ReportUtils.packagingReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_packetingReportFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            }
            if (nameList.get(position).equals(ReportUtils.productionReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1535)) {
                    bundle.putString("pageName", "Production Report");
                    bundle.putString("portion", ReportUtils.productionReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_packetingReportFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            }
            if (nameList.get(position).equals(ReportUtils.iodineUsedReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1541)) {
                    bundle.putString("portion", ReportUtils.iodineUsedReport);
                    bundle.putString("pageName", "Iodine Used Report");
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_packetingReportFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            }

            if (nameList.get(position).equals(ReportUtils.reconciliation)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1539)) {
                    bundle.putString("portion", ReportUtils.reconciliation);
                    bundle.putString("pageName", "Reconciliation Report");
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reconciliationReportFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }

            if (nameList.get(position).equals(ReportUtils.stockInOutReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1550)) {
                    bundle.putString("portion", ReportUtils.stockInOutReport);
                    bundle.putString("pageName", "Inventory Report");
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reconciliationReportFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }

            if (nameList.get(position).equals(ReportUtils.transferReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1537)) {
                    bundle.putString("portion", ReportUtils.transferReport);
                    bundle.putString("pageName", "Transfer Report");
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reconciliationReportFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }

            if (nameList.get(position).equals(ReportUtils.customerReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1554)) {
                    bundle.putString("portion", ReportUtils.customerReport);
                    bundle.putString("pageName",ReportUtils.customerReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(ReportUtils.projectReport)) {


                bundle.putString("portion", ReportUtils.projectReport);
                bundle.putString("pageName",ReportUtils.projectReport);
                Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);


            }
            if (nameList.get(position).equals(ReportUtils.userReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1555)) {
                    bundle.putString("pageName",ReportUtils.userReport);
                    bundle.putString("portion", ReportUtils.userReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }

            if (nameList.get(position).equals(ReportUtils.qcqaReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1536)) {
                    bundle.putString("pageName", "QC/QA Report");
                    bundle.putString("portion", ReportUtils.qcqaReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }

            if (nameList.get(position).equals(ReportUtils.monitoringReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1540)) {
                    bundle.putString("pageName", "Monitoring Report");
                    bundle.putString("portion", ReportUtils.monitoringReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }
            if (nameList.get(position).equals(ReportUtils.employeeReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1553)) {
                    bundle.putString("pageName", "Employee Report");
                    bundle.putString("portion", ReportUtils.employeeReport);
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_to_reportLicenceFragment2t, bundle);
                    return;
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            }

            if (nameList.get(position).equals(ReportUtils.dashBoardReport)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1631)) {
                    bundle.putString("Item", ReportUtils.dashBoardReport);
                    bundle.putString("pageName", "Report");
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_managementFragment_self, bundle);
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

    class MyHolder extends RecyclerView.ViewHolder {
        private ManagementFragmentModelBindingBinding binding;

        public MyHolder(ManagementFragmentModelBindingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
