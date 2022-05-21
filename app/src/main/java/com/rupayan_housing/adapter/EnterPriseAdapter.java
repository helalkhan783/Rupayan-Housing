package com.rupayan_housing.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.PermissionListBinding;
import com.rupayan_housing.serverResponseModel.Enterprise;
import com.rupayan_housing.serverResponseModel.UserEnterpriseAccess;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.user.UserAllListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EnterPriseAdapter extends RecyclerView.Adapter<EnterPriseAdapter.MyHolder> {
    private FragmentActivity context;
    private List<Enterprise> enterprisesList;
    private UserEnterpriseAccess userEnterpriseAccess;
    private UserAllListFragment enterPriseCheckedHandle;


    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PermissionListBinding binding
                = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.permission_list, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        Enterprise currentEnterprise = enterprisesList.get(position);
        String currentEnterpriseName = currentEnterprise.getStoreName();
        String name = currentEnterpriseName.substring(0, 1).toUpperCase() + currentEnterpriseName.substring(1);
        holder.binding.checkboxItem.setText("" + name);

        try {
            List<Integer> enterprisePermissionList = PermissionUtil.currentUserPermissionList(userEnterpriseAccess.getStoreAccess());
            if (enterprisePermissionList.contains(Integer.parseInt(currentEnterprise.getStoreID()))) {
                holder.binding.checkboxItem.setChecked(true);
            }
        } catch (Exception e) {
            Log.d("ERROR", e.getLocalizedMessage());
        }


        /**
         * enterprise checked handle from here
         */
        holder.binding.checkboxItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String currentEnterPriseId = enterprisesList.get(holder.getAdapterPosition()).getStoreID();
            if (isChecked) {
                if (currentEnterPriseId != null) {
                    enterPriseCheckedHandle.checkedEnterPrise(Integer.parseInt(currentEnterPriseId));
                }
                return;
            }
            if (currentEnterPriseId != null) {
                enterPriseCheckedHandle.unCheckedEnterPrise(Integer.parseInt(currentEnterPriseId));
            }
        });


    }

    @Override
    public int getItemCount() {
        return enterprisesList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private PermissionListBinding binding;

        public MyHolder(PermissionListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
