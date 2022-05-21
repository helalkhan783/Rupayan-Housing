package com.rupayan_housing.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.MillerHistoryListClickHandle;
import com.rupayan_housing.databinding.MillerHistoryListLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.HistoryList;
import com.rupayan_housing.utils.MillerUtils;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.miller.MillerAllListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MillerHistoryListAdapter extends RecyclerView.Adapter<MillerHistoryListAdapter.viewHolder> {
    private Context context;
    List<HistoryList> lists;

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


        HistoryList currentList = lists.get(position);
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1348)) {
            holder.itembinding.edit.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1568)) {
            holder.itembinding.viewDetails.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1638)) {
            holder.itembinding.history.setVisibility(View.GONE);
        }
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
        holder.itembinding.loginId.setText(":  " + currentList.getLoginId());

        holder.itembinding.switchStatus.setSelected(false);
        holder.itembinding.switchStatus.setChecked(false);
        if (currentList.getStatus().equals("1")) {
            holder.itembinding.switchStatus.setSelected(true);
            holder.itembinding.switchStatus.setChecked(true);
        }


        holder.itembinding.switchStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });

        /**
         * Now Handle onclick on edit miller
         */
        holder.itembinding.setClickHandle(new MillerHistoryListClickHandle() {
            @Override
            public void editMiller() {
                Bundle bundle = new Bundle();
                bundle.putString("slID", lists.get(holder.getAdapterPosition()).getSl());
                bundle.putString("portion", MillerUtils.millerHistoryList);
                MillerAllListFragment.pageNumber=1;
                MillerAllListFragment.isFirstLoad=0;
                 Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_millerAllListFragment_to_editMillerFragment, bundle);
            }
        });
        holder.itembinding.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString("slId", lists.get(holder.getAdapterPosition()).getSl());
                    bundle.putString("pageName", "Mill History Details");
                    MillerAllListFragment.pageNumber=1;
                    MillerAllListFragment.isFirstLoad=0;
                    Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_millerAllListFragment_to_millerDetailsViewFragment, bundle);
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            }
        });

        holder.itembinding.history.setOnClickListener(v -> {
            try {
                Bundle bundle = new Bundle();
                bundle.putString("id", currentList.getProfileId());
                bundle.putString("pageName", "Mill Edit History");
                MillerAllListFragment.pageNumber=1;
                MillerAllListFragment.isFirstLoad=0;
                Navigation.findNavController(holder.itembinding.getRoot()).navigate(R.id.action_millerAllListFragment_to_allSubHistoryListFragmet, bundle);
            } catch (Exception e) {
            }

        });

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
