package com.rupayan_housing.adapter;

import android.content.Context;
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
import com.rupayan_housing.clickHandle.ItemListClickHandle;
import com.rupayan_housing.databinding.ItemListLayoutBinding;
import com.rupayan_housing.databinding.PacketLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.ItemPacketList;
import com.rupayan_housing.serverResponseModel.Product;
import com.rupayan_housing.utils.ImageBaseUrl;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.items.ItemListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ItemPacketListAdapter extends RecyclerView.Adapter<ItemPacketListAdapter.ViewHolder> {
    private FragmentActivity context;
    private List<ItemPacketList> products;
    private View view;
    private String productId, title;

    @NonNull
    @NotNull
    @Override
    public ItemPacketListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PacketLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.packet_layout, parent, false);
        return new ItemPacketListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemPacketListAdapter.ViewHolder holder, int position) {
        ItemPacketList currentProduct = products.get(position);
        try {
            if (currentProduct.getDateTime() != null) {
                holder.binding.date.setText(":  " + new DateFormatRight(context,currentProduct.getDateTime()).dateFormatForWashing());
            }

            if (currentProduct.getProductTitle() != null) {
                holder.binding.name.setText(":  " + currentProduct.getProductTitle());
            }

            if (currentProduct.getProductDimensions() != null) {
                holder.binding.quantity.setText(":  " + currentProduct.getProductDimensions() + " " + currentProduct.getName());
            }
            String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();

            holder.binding.edit.setOnClickListener(v -> {
                if (!currentProfileTypeId.equals("7")) {
                    Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                    return;
                }
                if (PermissionUtil
                        .currentUserPermissionList(PreferenceManager.getInstance(context)
                                .getUserCredentials().getPermissions()).contains(1) ||
                        PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).
                                getUserCredentials().getPermissions()).contains(3)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", currentProduct.getProductTitle());
                    bundle.putString("quantity", currentProduct.getProductDimensions());
                    bundle.putString("price", currentProduct.getSellingPrice());
                    bundle.putString("productId", currentProduct.getProductID());
                    bundle.putString("unit", currentProduct.getName());
                    bundle.putString("baseUnit", currentProduct.getBaseUnit());
                    bundle.putString("title", title);
                    Navigation.findNavController(view).navigate(R.id.action_addItemPacketFragment_to_editItemPacketFragment, bundle);
                } else {
                    Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                    return;
                }

            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final PacketLayoutBinding binding;

        public ViewHolder(final PacketLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
