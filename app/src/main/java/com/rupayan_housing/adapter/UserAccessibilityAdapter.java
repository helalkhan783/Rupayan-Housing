package com.rupayan_housing.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.UserAccesabilityClickHandle;
import com.rupayan_housing.databinding.UserAccessabilityModelBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.ManageAccessibility;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.utils.UserUtil;
import com.rupayan_housing.view.fragment.user.UserAllListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserAccessibilityAdapter extends RecyclerView.Adapter<UserAccessibilityAdapter.MyHolder> {
    private FragmentActivity activity;
    private List<ManageAccessibility> lists;


    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        UserAccessabilityModelBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.user_accessability_model, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        ManageAccessibility currentData = lists.get(position);
        holder.binding.name.setText(currentData.getFullName());
        holder.binding.userName.setText(currentData.getUserName());

        holder.binding.setClickHandle(() -> {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getUserName()).equals(currentData.getUserName())) {
                Toasty.info(activity, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                return;
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("portion", "Manage User Permissions");
                bundle.putString("id", lists.get(holder.getAdapterPosition()).getUserId());
                bundle.putString("profileId", lists.get(holder.getAdapterPosition()).getProfileId());
                Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_userAllListFragment_self, bundle);

            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private UserAccessabilityModelBinding binding;

        public MyHolder(UserAccessabilityModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
