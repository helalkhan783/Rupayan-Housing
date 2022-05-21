package com.rupayan_housing.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.CustomeEditHistoryLayoutBinding;
import com.rupayan_housing.serverResponseModel.CustomerEditHistoryList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomerEditHistoryAdapter extends RecyclerView.Adapter<CustomerEditHistoryAdapter.ViewHolder> {
    private FragmentActivity context;
    private List<CustomerEditHistoryList> customerEditHistoryList;

    public CustomerEditHistoryAdapter(FragmentActivity context, List<CustomerEditHistoryList> customerEditHistoryList) {
        this.context = context;
        this.customerEditHistoryList = customerEditHistoryList;
    }

    @NonNull
    @NotNull
    @Override
    public CustomerEditHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CustomeEditHistoryLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.custome_edit_history_layout, parent, false);
        return new CustomerEditHistoryAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CustomerEditHistoryAdapter.ViewHolder holder, int position) {
        CustomerEditHistoryList data = customerEditHistoryList.get(position);

        holder.binding.sl.setText(":  " + position);
        holder.binding.companyName.setText(":  " + data.getCompanyName());
        holder.binding.ownerName.setText(":  " + data.getCustomerFname());
        holder.binding.phone.setText(":  " + data.getPhone());
        holder.binding.address.setText(":  " + data.getAddress());
        holder.binding.type.setText(":  " + data.getType());
        holder.binding.editAttemptTime.setText(":  " + data.getEditAttemptTime());
        holder.binding.editBy.setText(":  " + data.getEditAttemptByName());

        if (data.getStatus().equals("3")) {
            holder.binding.editStatus.setText(":  " + "Edit Approved");
        }
        if (data.getStatus().equals("0")) {
            holder.binding.editStatus.setText(":  " + "Old");
            holder.binding.editStatus.setTextColor(Color.RED);
        }


    }

    @Override
    public int getItemCount() {
        return customerEditHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CustomeEditHistoryLayoutBinding binding;

        public ViewHolder(final CustomeEditHistoryLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
