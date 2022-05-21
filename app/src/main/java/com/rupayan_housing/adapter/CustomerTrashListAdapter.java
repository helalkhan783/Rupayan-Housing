package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.CustomerListBindingBinding;
import com.rupayan_housing.serverResponseModel.CustoerTrashList;
import com.rupayan_housing.serverResponseModel.CustomerListModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomerTrashListAdapter extends RecyclerView.Adapter<CustomerTrashListAdapter.ViewHolder> {
    private FragmentActivity context;
    private List<CustoerTrashList> customerLists;

    @NonNull
    @NotNull
    @Override
    public CustomerTrashListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CustomerListBindingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.customer_list_binding, parent, false);
        return new CustomerTrashListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CustomerTrashListAdapter.ViewHolder holder, int position) {
        CustoerTrashList customerList = customerLists.get(position);
        holder.binding.comapanyName.setText(":  " + customerList.getCompanyName());
        holder.binding.ownerName.setText(":  " + customerList.getCustomerFname());
        holder.binding.phone.setText(":  " + customerList.getPhone());
        holder.binding.address.setText(":  " + customerList.getAddress());
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
