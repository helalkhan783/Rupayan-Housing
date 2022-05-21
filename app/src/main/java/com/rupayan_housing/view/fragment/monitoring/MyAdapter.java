package com.rupayan_housing.view.fragment.monitoring;

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

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.CustomerListBindingBinding;
import com.rupayan_housing.databinding.MonitorListBindingBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.ListMonitorModel;
import com.rupayan_housing.serverResponseModel.MonitoringType;
import com.rupayan_housing.utils.PermissionUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    FragmentActivity activity;
    List<ListMonitorModel> monitorLists;
    List<MonitoringType> monitoringType;
    View view;

    public MyAdapter(FragmentActivity activity, List<ListMonitorModel> monitorLists, List<MonitoringType> monitoringType, View view) {
        this.activity = activity;
        this.monitorLists = monitorLists;
        this.monitoringType = monitoringType;
        this.view = view;
    }

    @NonNull
    @NotNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MonitorListBindingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.
                getContext()), R.layout.monitor_list_binding, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyAdapter.ViewHolder holder, int position) {
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1450)) {
            holder.binding.editBtn.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1451)) {
            holder.binding.view.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1548)) {
            holder.binding.history.setVisibility(View.GONE);
        }
        ListMonitorModel listMonitorModel = monitorLists.get(position);
        holder.binding.date.setText(":  " + listMonitorModel.getMonitoringDate());
        holder.binding.zone.setText(":  " + listMonitorModel.getZoneName());
      //  holder.binding.type.setText(":  " + listMonitorModel.getMonitoringType());
        holder.binding.publishedDate.setText(":  " + listMonitorModel.getPublishDate());
        holder.binding.moniotorBy.setText(":  " + listMonitorModel.getMonitorBy());
        if (listMonitorModel.getMillname() !=null){
            holder.binding.millName.setText(":  " + listMonitorModel.getMillname());
        }

        try {
            for (int i = 0; i < monitorLists.size(); i++) {
                if (listMonitorModel.getMonitoringType().equals(monitoringType.get(i).getTypeID())) {
                    holder.binding.type.setText(":  " + monitoringType.get(i).getMonitoringTypeName());
                }
            }

        }catch (Exception e){}

        holder.binding.editBtn.setOnClickListener(v -> {
            String currentProfileTypeId = PreferenceManager.getInstance(activity).getUserCredentials().getProfileTypeId();

            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1450)) {
                String selectedId = null;
                try {
                    selectedId = monitorLists.get(holder.getAdapterPosition()).getSlID();
                } catch (Exception e) {
                    Log.d("ERROR", "ERROR");
                }
                if (monitorLists.get(holder.getAdapterPosition()).getMonitorId() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", selectedId);
                    MonitoringListFragment.pageNumber = 1;
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_monitoringListFragment_to_editMonitoringFragment, bundle);
                }
                return;
            } else {
                Toasty.info(activity, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
            }
        });

        holder.binding.view.setOnClickListener(v -> {
            try {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1451)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("slId", listMonitorModel.getSlID());
                    MonitoringListFragment.pageNumber = 1;
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_monitoringListFragment_to_monitoringViewFragment, bundle);
                } else {
                    Toasty.info(activity, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
        });

        holder.binding.history.setOnClickListener(v -> {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1548)) {
                Bundle bundle = new Bundle();
                bundle.putString("id", listMonitorModel.getMonitorId());
                bundle.putString("pageName", "Monitoring Edit History");
                Navigation.findNavController(view).navigate(R.id.action_monitoringListFragment_to_allSubHistoryListFragmet, bundle);
            } else {
                Toasty.info(activity, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return monitorLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MonitorListBindingBinding binding;

        public ViewHolder(MonitorListBindingBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
