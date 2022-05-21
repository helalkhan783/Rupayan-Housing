package com.rupayan_housing.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.CustomerListBindingBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.CustomerListModel;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.monitoring.MonitoringListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;


public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {
    private FragmentActivity context;
    private List<CustomerListModel> customerLists;
    private View view;
    private MonitoringListFragment click;

    public CustomerListAdapter(FragmentActivity context, List<CustomerListModel> customerLists, View view, MonitoringListFragment click) {
        this.context = context;
        this.customerLists = customerLists;
        this.view = view;
        this.click = click;
    }

    @NonNull
    @NotNull
    @Override
    public CustomerListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CustomerListBindingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.
                getContext()), R.layout.customer_list_binding, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CustomerListAdapter.ViewHolder holder, int position) {
        CustomerListModel customerList = customerLists.get(position);
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(28)) {
            holder.binding.editBtn.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(29)) {
            holder.binding.delete.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1293)) {
            holder.binding.history.setVisibility(View.GONE);
        }
        try {
            holder.binding.comapanyName.setText(":  " + customerList.getCompanyName());
            holder.binding.ownerName.setText(":  " + customerList.getCustomerFname());
            holder.binding.phone.setText(":  " + customerList.getPhone());
            holder.binding.address.setText(":  " + customerList.getThana() + "; " + customerList.getDistrict() + "; \n   " + customerList.getDivision());

            holder.binding.editBtn.setOnClickListener(v -> {
                String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(28)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("customerId", customerList.getCustomerID());
                    Navigation.findNavController(view).navigate(R.id.action_monitoringListFragment_to_editCustomerFragment, bundle);
                    return;
                } else {
                    Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                }
            });
            holder.binding.history.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("id", customerList.getCustomerID());
                bundle.putString("pageName", "Customer Edit History");
                Navigation.findNavController(view).navigate(R.id.action_monitoringListFragment_to_allSubHistoryListFragmet, bundle);
            });

            //for delete customer
            holder.binding.delete.setOnClickListener(v -> {
                try {
                    click.getPosition(holder.getAdapterPosition(), customerList.getCustomerID());
                } catch (Exception e) {
                }
            });

        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return customerLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CustomerListBindingBinding binding;

        public ViewHolder(CustomerListBindingBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
