package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.MonitoringReportListLayoutBinding;
import com.rupayan_housing.databinding.QcqaReportListLayoutBinding;
import com.rupayan_housing.serverResponseModel.MonitoringReportList;
import com.rupayan_housing.serverResponseModel.QcqaReportList;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MonitoringReportAdapter extends RecyclerView.Adapter<MonitoringReportAdapter.ViewHolder> {
    FragmentActivity context;
    List<MonitoringReportList> list;

    @NonNull
    @Override
    public MonitoringReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MonitoringReportListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.monitoring_report_list_layout, parent, false);

        return new MonitoringReportAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MonitoringReportAdapter.ViewHolder holder, int position) {
        MonitoringReportList currentPosition = list.get(position);
        holder.binding.date.setText(":  " + currentPosition.getMonitoringDate());
        holder.binding.publishedDate.setText(":  " + currentPosition.getPublishDate());

        holder.binding.zone.setText(":  " + currentPosition.getZoneName());

        if (currentPosition.getType() != null) {
            holder.binding.type.setText(":  " + currentPosition.getType());

        }
        if (currentPosition.getMillerName() != null) {
            holder.binding.miller.setText(":  " + currentPosition.getMillerName());

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MonitoringReportListLayoutBinding binding;

        public ViewHolder(@NonNull MonitoringReportListLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}