package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.MillerLicenceExpireReportListLayoutBinding;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.all_report.licence_expire_report.list.MillerLicenceExpireReportList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MillerLicenceExpireReportListAdapter extends RecyclerView.Adapter<MillerLicenceExpireReportListAdapter.viewHolder> {
    private FragmentActivity activity;
    private List<MillerLicenceExpireReportList> profuctList;

    @NonNull
    @NotNull
    @Override
    public MillerLicenceExpireReportListAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MillerLicenceExpireReportListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.miller_licence_expire_report_list_layout, parent, false);
        return new MillerLicenceExpireReportListAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MillerLicenceExpireReportListAdapter.viewHolder holder, int position) {
        MillerLicenceExpireReportList currentitem = profuctList.get(position);
        holder.itembinding.millerName.setText(":  " + currentitem.getMiller());
        if (currentitem.getLicenceName() != null) {
            holder.itembinding.licenceName.setText(":  " + currentitem.getLicenceName());
        }
        if (currentitem.getAssociationName() != null) {
            holder.itembinding.associationName.setText(":  " + currentitem.getAssociationName());
        }
        holder.itembinding.expireDate.setText(":  " + new DateFormatRight(activity, currentitem.getRenewDate()).onlyDayMonthYear());
    }

    @Override
    public int getItemCount() {
        return profuctList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private MillerLicenceExpireReportListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull MillerLicenceExpireReportListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}