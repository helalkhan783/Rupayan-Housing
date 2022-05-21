package com.rupayan_housing.view.fragment;

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

import com.bumptech.glide.Glide;
import com.rupayan_housing.R;
import com.rupayan_housing.databinding.MonitorListBindingBinding;
import com.rupayan_housing.databinding.SliderListBindingBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.ListMonitorModel;
import com.rupayan_housing.serverResponseModel.MonitoringType;
import com.rupayan_housing.serverResponseModel.SliderList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.monitoring.MonitoringListFragment;
import com.rupayan_housing.view.fragment.monitoring.MyAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder> {
    private FragmentActivity activity;
    private List<Integer> sliderLists;

    public SliderAdapter(FragmentActivity activity, List<Integer> sliderLists/*List<SliderList> sliderLists*/) {
        this.activity = activity;
        this.sliderLists = sliderLists;
    }

    @NonNull
    @NotNull
    @Override
    public SliderAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SliderListBindingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.
                getContext()), R.layout.slider_list_binding, parent, false);
        return new SliderAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SliderAdapter.ViewHolder holder, int position) {

            Integer sliderList = sliderLists.get(position);
            try {
                Glide.with(activity)
                        .load(sliderList).into(holder.binding.banner);
            } catch (Exception e) {
            }



    }

    @Override
    public int getItemCount() {
        return sliderLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final SliderListBindingBinding binding;

        public ViewHolder(SliderListBindingBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
