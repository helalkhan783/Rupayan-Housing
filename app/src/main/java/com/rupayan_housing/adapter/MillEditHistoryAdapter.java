package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.MillEditHistoryLayoutBinding;
import com.rupayan_housing.databinding.MillerHistoryListLayoutBinding;
import com.rupayan_housing.serverResponseModel.MillEdithistoryList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MillEditHistoryAdapter extends RecyclerView.Adapter<MillEditHistoryAdapter.viewHolder> {
    FragmentActivity context;

    List<MillEdithistoryList> millEdithistoryLists;

    public MillEditHistoryAdapter(FragmentActivity context, List<MillEdithistoryList> millEdithistoryLists) {
        this.context = context;
        this.millEdithistoryLists = millEdithistoryLists;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MillEditHistoryLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R
                .layout.mill_edit_history_layout, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        MillEdithistoryList currentList = millEdithistoryLists.get(position);
        try {
            holder.itembinding.date.setText(":  " + currentList.getEntryTime());
            holder.itembinding.milName.setText(":  " + currentList.getFullName());
            holder.itembinding.shortName.setText(":  " + currentList.getDisplayName());
            holder.itembinding.processType.setText(":  " + currentList.getProcessTypeName());
            holder.itembinding.millType.setText(":  " + currentList.getMillTypeName());
            holder.itembinding.zone.setText(":  " + currentList.getZoneName());
            holder.itembinding.millId.setText(":  " + currentList.getMillId());
            holder.itembinding.logInId.setText(":  " + currentList.getLoginId());
            holder.itembinding.reviewDate.setText(":  " + currentList.getReviewTime());
            holder.itembinding.address.setText(":  " + currentList.getCountryName() + "; " + currentList.getDivisionName() + "; \n   " + currentList.getDistrictName() + "; " + currentList.getUpazilaName());
            if (currentList.getStatus().equals("1")) {
                holder.itembinding.millStatusSwitch.setChecked(true);
                holder.itembinding.millStatusSwitch.setSelected(true);
            }
            if (currentList.getStatus().equals("0")) {
                holder.itembinding.millStatusSwitch.setChecked(false);
                holder.itembinding.millStatusSwitch.setSelected(false);
            }

        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return millEdithistoryLists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private MillEditHistoryLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull MillEditHistoryLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
