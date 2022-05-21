package com.rupayan_housing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.MillEditHistoryLayoutBinding;
import com.rupayan_housing.databinding.MillerDeclineListLayoutBinding;
import com.rupayan_housing.databinding.MillerHistoryListLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.DeclineList;
import com.rupayan_housing.utils.PermissionUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeclineListAdapter extends RecyclerView.Adapter<DeclineListAdapter.viewHolder> {
    private Context context;
    List<DeclineList> lists;

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MillerHistoryListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R
                .layout.miller_history_list_layout, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        DeclineList currentList = lists.get(position);
        holder.itembinding.edit.setVisibility(View.GONE);
        holder.itembinding.viewDetails.setVisibility(View.GONE);
        holder.itembinding.history.setVisibility(View.GONE);
        holder.itembinding.switchStatus.setVisibility(View.GONE);


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

        holder.itembinding.misId.setText(":  " + currentList.getMillId());
        if (currentList.getLoginId() !=null){
            holder.itembinding.loginId.setText(":  " + currentList.getLoginId());
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private MillerHistoryListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull MillerHistoryListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
