package com.rupayan_housing.adapter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.SupplierListBindingBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.SupplierList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.monitoring.MonitoringListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;


public class SupplierListAdapter extends RecyclerView.Adapter<SupplierListAdapter.ViewHolder> {
    private FragmentActivity context;
    private List<SupplierList> supplierLists;
    View view;
    MonitoringListFragment click;

    public SupplierListAdapter(FragmentActivity context, List<SupplierList> supplierLists, View view, MonitoringListFragment click) {
        this.context = context;
        this.supplierLists = supplierLists;
        this.view = view;
        this.click = click;
    }

    @NonNull
    @NotNull
    @Override
    public SupplierListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SupplierListBindingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.
                getContext()), R.layout.supplier_list_binding, parent, false);
        return new SupplierListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SupplierListAdapter.ViewHolder holder, int position) {
        SupplierList customerList = supplierLists.get(position);
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1461)) {
            holder.binding.edit.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1462)) {
            holder.binding.delete.setVisibility(View.GONE);
        }

        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1294)) {
            holder.binding.history.setVisibility(View.GONE);
        }

        holder.binding.companyName.setText(":  " + customerList.getCompanyName());
        holder.binding.ownerName.setText(":  " + customerList.getCustomerFname());
        holder.binding.phoneNumber.setText(":  " + customerList.getPhone());

        Log.d("INFO",""+customerList.getCountry());

        if (customerList.getCountry() != null) {
            holder.binding.country.setText(":  " + customerList.getCountry());
        }

        String typeId = customerList.getTypeID();
        if (typeId.equals("1")) {
            holder.binding.address.setText(":  " + customerList.getThana() + ", " + customerList.getDistrict() + " \n" + "\t" + customerList.getDivision());
            holder.binding.type.setText(":  Local");
        }
        if (typeId.equals("5")) {
            holder.binding.type.setText(":  Foreign");
            holder.binding.address.setText(":  " + customerList.getAddress());
        }
        holder.binding.edit.setOnClickListener(v -> {
            String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
            /*if (!currentProfileTypeId.equals("7")) {
                Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                return;
            }
*/
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1461)) {

                Bundle bundle = new Bundle();
                bundle.putString("customerId", customerList.getCustomerID());
                if (customerList.getTypeID().equals("1")) {
                    MonitoringListFragment.pageNumber = 1;
                    Navigation.findNavController(view).navigate(R.id.action_monitoringListFragment_to_editLocalSupplierFragment, bundle);
                    return;
                }
                if (customerList.getTypeID().equals("5")) {
                    MonitoringListFragment.pageNumber = 1;
                    Navigation.findNavController(view).navigate(R.id.action_monitoringListFragment_to_editForeignSupplierFragment, bundle);
                }
            }


        });

        holder.binding.history.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", customerList.getCustomerID());
            bundle.putString("pageName", "Supplier Edit History");
            Navigation.findNavController(view).navigate(R.id.action_monitoringListFragment_to_allSubHistoryListFragmet, bundle);
        });

        holder.binding.delete.setOnClickListener(v -> {
            try {
                click.getSupplierPosition(holder.getAdapterPosition(), customerList.getCustomerID());
            } catch (Exception e) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return supplierLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final SupplierListBindingBinding binding;

        public ViewHolder(SupplierListBindingBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
