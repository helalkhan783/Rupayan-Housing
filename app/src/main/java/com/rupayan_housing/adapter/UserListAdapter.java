package com.rupayan_housing.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.UserListClickHandle;
import com.rupayan_housing.databinding.UserListModelBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.UserLists;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.viewModel.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyHolder> {
    private FragmentActivity context;
    private List<UserLists> lists;
    private LifecycleOwner lifecycleOwner;
    private boolean isFromDialogOrPermissions = false;

    public UserListAdapter(FragmentActivity context, List<UserLists> lists, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.lists = lists;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        UserListModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R
                .layout.user_list_model, parent, false);

        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        UserLists currentUser = lists.get(position);

        // permission wise manage action button
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1003)) {
            holder.binding.edit.setVisibility(View.GONE);
            holder.binding.userSwitch.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1005)) {
            holder.binding.delete.setVisibility(View.GONE);
        }


        // set data to view
        if (currentUser.getFullName() == null) {
            holder.binding.name.setText(":   ");
        } else {
            holder.binding.name.setText(":   " + currentUser.getFullName());
        }
        if (currentUser.getUserName() == null) {
            holder.binding.userName.setText(":  ");
        } else {
            holder.binding.userName.setText(":  " + currentUser.getUserName());
        }

        String status = currentUser.getUserStatusId();


// set click action to action view

        holder.binding.setClickHandle(new UserListClickHandle() {
            @Override
            public void add() {
            }

            @Override
            public void edit() {
                     Bundle bundle = new Bundle();
                    bundle.putString("id", lists.get(holder.getAdapterPosition()).getProfileId());
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_userAllListFragment_to_editUserFragment, bundle);

            }
        });
        /**
         * status or switch
         */
        if (status.equals("1")) {
            holder.binding.userSwitch.setChecked(true);
        } else {
            holder.binding.userSwitch.setChecked(false);
        }

        holder.binding.userSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            UserViewModel userViewModel = new ViewModelProvider(context).get(UserViewModel.class);
            userViewModel.checkUserActivation(context, currentUser.getProfileId(), status).observe(lifecycleOwner, response -> {
                if (response == null) {
                    Toasty.error(context, "Something Wrong Contact to Support \n", Toasty.LENGTH_LONG).show();
                    return;
                }
                if (response.getStatus() == 400) {
                    Toasty.info(context, "" + response.getMessage(), Toasty.LENGTH_LONG).show();
                    return;
                }
                Toasty.success(context, "" + response.getMessage(), Toasty.LENGTH_LONG).show();
            });
        });

    }

    private void checkStatus(String profileID, String status) {


    }

    private void userActivationDialog(String profileId, String status, String messageEnd, MyHolder holder) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage("Do you want to " + messageEnd + " ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                (dialog, which) -> {
                    dialog.dismiss();
                    checkStatus(profileId, status);
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> {
            dialog.dismiss();
            dialogSensor(holder);
        });
        alertDialog.show();
    }

    private void dialogSensor(MyHolder holder) {
        isFromDialogOrPermissions = true;
        if (holder.binding.userSwitch.isChecked()) {
            holder.binding.userSwitch.setChecked(false);
            return;
        }
        holder.binding.userSwitch.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private UserListModelBinding binding;

        public MyHolder(UserListModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
