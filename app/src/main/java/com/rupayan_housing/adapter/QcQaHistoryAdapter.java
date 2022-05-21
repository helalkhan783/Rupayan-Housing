package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.MonitoringHistoryAdapter;
import com.rupayan_housing.R;
import com.rupayan_housing.databinding.MonitoringHistoryListModelBinding;
import com.rupayan_housing.databinding.QcqaHistoryListModelBinding;
import com.rupayan_housing.serverResponseModel.Enterprize;
import com.rupayan_housing.serverResponseModel.MonitoringHisList;
import com.rupayan_housing.serverResponseModel.QcqaList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class QcQaHistoryAdapter extends RecyclerView.Adapter<QcQaHistoryAdapter.ViewHolder> {
    private FragmentActivity context;
    private List<QcqaList> monitoringLists;
    private List<Enterprize> enterprizes;

    @NonNull
    @NotNull
    @Override
    public QcQaHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        QcqaHistoryListModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.qcqa_history_list_model, parent, false);
        return new QcQaHistoryAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull QcQaHistoryAdapter.ViewHolder holder, int position) {
        QcqaList currentMonitorList = monitoringLists.get(position);
        Enterprize currentEnterprise = enterprizes.get(position);

        if (currentEnterprise.getStoreID().equals(currentMonitorList.getStoreID())) {
            holder.binding.enterpriseName.setText(":  " + currentEnterprise.getFullName());

        }
        holder.binding.monitoringDate.setText(":  " + currentMonitorList.getEntryDateTime());

        holder.binding.sampleName.setText(":  " + currentMonitorList.getModel());
    }

    @Override
    public int getItemCount() {
        return monitoringLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final QcqaHistoryListModelBinding binding;

        public ViewHolder(final QcqaHistoryListModelBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
