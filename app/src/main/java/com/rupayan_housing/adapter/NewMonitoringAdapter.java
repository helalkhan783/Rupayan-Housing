package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.rupayan_housing.R;
import com.rupayan_housing.databinding.NewMonitoringListBindingBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.ListMonitorModel;
import com.rupayan_housing.serverResponseModel.MonitoringType;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class NewMonitoringAdapter extends RecyclerView.Adapter<NewMonitoringAdapter.ViewHolder> {
    private FragmentActivity context;
    private List<ListMonitorModel> monitoringLists;
    private List<MonitoringType> monitoringModelObject;
    private View view;

    public NewMonitoringAdapter(FragmentActivity context, List<ListMonitorModel> monitoringLists, List<MonitoringType> monitoringModelObject, View view) {
        this.context = context;
        this.monitoringLists = monitoringLists;
        this.monitoringModelObject = monitoringModelObject;
        this.view = view;
    }

    @NonNull
    @NotNull
    @Override
    public NewMonitoringAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        NewMonitoringListBindingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.
                getContext()), R.layout.new_monitoring_list_binding, parent, false);
        return new NewMonitoringAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewMonitoringAdapter.ViewHolder holder, int position) {
        ListMonitorModel currentmonitorList = monitoringLists.get(position);
        MonitoringType type = monitoringModelObject.get(position);
        try {
            if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1450)) {
                holder.binding.monitoringEdit.setVisibility(View.GONE);
            }
            if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1451)) {
                holder.binding.monitoringView.setVisibility(View.GONE);
            }
            if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1548)) {
                holder.binding.history.setVisibility(View.GONE);
            }
            try {
                for (int i = 0; i < monitoringModelObject.size(); i++) {
                    if (currentmonitorList.getMonitoringType().equals(monitoringModelObject.get(i).getTypeID())) {
                        holder.binding.monitoringType.setText(":  " + monitoringModelObject.get(i).getMonitoringTypeName());
                    }
                }

            } catch (Exception e) {
            }
            if (currentmonitorList.getMonitoringDate() == null) {
                holder.binding.monitoringDate.setText(":");
            } else {
                holder.binding.monitoringDate.setText(":  " + new DateFormatRight(context, currentmonitorList.getMonitoringDate()).onlyDayMonthYear());
            }
            if (holder.binding.monitorBy != null) {
                holder.binding.monitorBy.setText(":  " + currentmonitorList.getMonitorBy());
            }

            if (currentmonitorList.getZoneName() == null) {
                holder.binding.zoneName.setText(":");
            } else {
                holder.binding.zoneName.setText(":  " + currentmonitorList.getZoneName());
            }


            if (currentmonitorList.getPublishDate() == null) {
                holder.binding.publishedDate.setText(":");

            } else {
                holder.binding.publishedDate.setText(":  " + currentmonitorList.getPublishDate());
            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return monitoringLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final NewMonitoringListBindingBinding binding;

        public ViewHolder(NewMonitoringListBindingBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
