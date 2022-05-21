package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.MillReportListLayoutBinding;
import com.rupayan_housing.serverResponseModel.HistoryList;
import com.rupayan_housing.serverResponseModel.MillReportList;

import java.util.List;

public class MillReportListAdapter extends RecyclerView.Adapter<MillReportListAdapter.Myholder> {
    private FragmentActivity context;
    List<MillReportList> historyLists;

    public MillReportListAdapter(FragmentActivity context, List<MillReportList> historyLists) {
        this.context = context;
        this.historyLists = historyLists;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MillReportListLayoutBinding itembinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.mill_report_list_layout, parent, false);
        return new Myholder(itembinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        MillReportList currentList =historyLists.get(position);
        if (currentList.getEntryTime() == null) {
            holder.itembinding.entryTime.setText(":  ");
        } else {
            holder.itembinding.entryTime.setText(":  " + currentList.getEntryTime());
        }

        if (currentList.getDisplayName() == null) {
            holder.itembinding.displayName.setText(":  ");
        } else {
            holder.itembinding.displayName.setText(":  " + currentList.getDisplayName());
        }
        holder.itembinding.name.setText(":  " + currentList.getFullName());

        if (currentList.getProcessTypeName() == null) {
            holder.itembinding.processType.setText(":  ");
        } else {
            holder.itembinding.processType.setText(":  " + currentList.getProcessTypeName());

        }

        if (currentList.getMillTypeName() == null) {
            holder.itembinding.millType.setText(":  ");
        } else {
            holder.itembinding.millType.setText(":  " + currentList.getMillTypeName());

        }
        if (currentList.getReviewTime() != null) {
            holder.itembinding.reviewDate.setText(":  " + currentList.getReviewTime());//
        } else {

        }
        holder.itembinding.capacity.setText(":  " + currentList.getCapacity());
        if (currentList.getCountryName() == null
        ) {
            holder.itembinding.countryName.setText(":  ");
        } else {
            holder.itembinding.countryName.setText(":  " + currentList.getUpazilaName() + "; " + currentList.getDistrictName() + "; " + currentList.getDivisionName() + "; " + currentList.getCountryName());
        }
        holder.itembinding.zone.setText(":  " + currentList.getZoneName());

        if (currentList.getUpazilaName() == null) {
            holder.itembinding.upazillaName.setText(":  ");
        } else {

            holder.itembinding.upazillaName.setText(":  " + currentList.getUpazilaName());
        }

        holder.itembinding.misId.setText(":  " + currentList.getMillID());
        holder.itembinding.loginId.setText(":  " + currentList.getLoginId());
    }

    @Override
    public int getItemCount() {
        return historyLists.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        MillReportListLayoutBinding itembinding;

        public Myholder(@NonNull MillReportListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
