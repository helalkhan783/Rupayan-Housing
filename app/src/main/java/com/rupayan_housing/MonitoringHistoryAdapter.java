package com.rupayan_housing;

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

 import com.rupayan_housing.databinding.MonitoringHistoryListModelBinding;
import com.rupayan_housing.databinding.MonitoringListModelBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.ListMonitorModel;
import com.rupayan_housing.serverResponseModel.MonitoringHisList;
import com.rupayan_housing.serverResponseModel.MonitoringType;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.monitoring.MonitoringListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

 public class MonitoringHistoryAdapter extends RecyclerView.Adapter<MonitoringHistoryAdapter.ViewHolder> {
    private FragmentActivity context;
    private List<MonitoringHisList> monitoringLists;
    private List<MonitoringType> monitoringTypes;

    public MonitoringHistoryAdapter(FragmentActivity context, List<MonitoringHisList> monitoringLists, List<MonitoringType> monitoringTypes) {
        this.context = context;
        this.monitoringLists = monitoringLists;
        this.monitoringTypes = monitoringTypes;
    }

    @NonNull
    @NotNull
    @Override
    public MonitoringHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MonitoringHistoryListModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.monitoring_history_list_model, parent, false);
        return new MonitoringHistoryAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MonitoringHistoryAdapter.ViewHolder holder, int position) {
        MonitoringHisList currentmonitorList = monitoringLists.get(position);
        holder.binding.monitoringDate.setText(":  " + currentmonitorList.getMonitoringDate());
        if (currentmonitorList.getAgencyName() !=null){
            holder.binding.agency.setText(":  " + currentmonitorList.getAgencyName());
        }
        holder.binding.monitoringType.setText(":  " + currentmonitorList.getMonitoringType());
        holder.binding.publishedDate.setText(":  " + currentmonitorList.getPublishDate());
        holder.binding.monitorBy.setText(":  " + currentmonitorList.getMonitorBy());
        try {
            for (int i = 0; i < monitoringLists.size(); i++) {
                if (currentmonitorList.getMonitoringType().equals(monitoringTypes.get(i).getTypeID())) {
                    holder.binding.monitoringType.setText(":  " + monitoringTypes.get(i).getMonitoringTypeName());
                }
            }

        }catch (Exception e){}


    }

    @Override
    public int getItemCount() {
        return monitoringLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MonitoringHistoryListModelBinding binding;

        public ViewHolder(final MonitoringHistoryListModelBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
