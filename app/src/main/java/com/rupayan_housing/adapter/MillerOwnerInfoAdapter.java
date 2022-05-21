package com.rupayan_housing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.MillerOwnerInfoLayoutBinding;
import com.rupayan_housing.serverResponseModel.view_details.MillerOwnerDatum;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MillerOwnerInfoAdapter extends RecyclerView.Adapter<MillerOwnerInfoAdapter.viewHolder> {
    private Context context;
    private List<MillerOwnerDatum> lists;

    @NonNull
    @NotNull
    @Override
    public MillerOwnerInfoAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MillerOwnerInfoLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R
                .layout.miller_owner_info_layout, parent, false);
        return new MillerOwnerInfoAdapter.viewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull MillerOwnerInfoAdapter.viewHolder holder, int position) {
        MillerOwnerDatum currentList =lists.get(position);
        holder.itembinding.ownerName.setText(":  "+currentList.getOwnerName());
        holder.itembinding.division.setText(":  "+currentList.getDivisionName());
        holder.itembinding.district.setText(":  "+currentList.getDistrictName());
        holder.itembinding.uapazilla.setText(":  "+currentList.getUpazilaName());
        holder.itembinding.nid.setText(":  "+currentList.getNid());
        holder.itembinding.mobile.setText(":  "+currentList.getMobileNo());
        holder.itembinding.altMobile.setText(":  "+currentList.getAltMobile());
        holder.itembinding.email.setText(":  "+currentList.getEmail());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private MillerOwnerInfoLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull MillerOwnerInfoLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }}
