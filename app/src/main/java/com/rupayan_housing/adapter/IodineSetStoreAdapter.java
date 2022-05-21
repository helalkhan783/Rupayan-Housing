package com.rupayan_housing.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.IodinesetFragment;
import com.rupayan_housing.R;
import com.rupayan_housing.databinding.ManagementFragmentModelBindingBinding;
import com.rupayan_housing.databinding.PermissionListBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.SetIodineStoreList;
import com.rupayan_housing.utils.DashBoardReportUtils;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.SettingsUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lombok.NonNull;

public class IodineSetStoreAdapter extends RecyclerView.Adapter<IodineSetStoreAdapter.MyHolder> {
    FragmentActivity context;
    List<SetIodineStoreList> setIodineStoreLists;
    View view;
    IodinesetFragment click;

    public IodineSetStoreAdapter(FragmentActivity context, List<SetIodineStoreList> setIodineStoreLists, View view, IodinesetFragment click) {
        this.context = context;
        this.setIodineStoreLists = setIodineStoreLists;
        this.view = view;
        this.click = click;
    }

    @lombok.NonNull
    @NotNull
    @Override
    public IodineSetStoreAdapter.MyHolder onCreateViewHolder(@lombok.NonNull @NotNull ViewGroup parent, int viewType) {
        PermissionListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.permission_list, parent, false);
        return new IodineSetStoreAdapter.MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull IodineSetStoreAdapter.MyHolder holder, int position) {
        SetIodineStoreList currentImage = setIodineStoreLists.get(position);
        holder.binding.checkboxItem.setText("" + currentImage.getStoreName());
        if (currentImage.getIodineStore().equals("1")) {
            holder.binding.checkboxItem.setChecked(true);
            holder.binding.checkboxItem.setSelected(true);
        }
        holder.binding.checkboxItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                click.getData(holder.getAdapterPosition(), currentImage.getStoreID());
            }
        });





    }

    @Override
    public int getItemCount() {
        return setIodineStoreLists.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private PermissionListBinding binding;

        public MyHolder(PermissionListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}

