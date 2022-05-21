package com.rupayan_housing.adapter;

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
import com.rupayan_housing.databinding.BrandListLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.BrandList;
import com.rupayan_housing.utils.ImageBaseUrl;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.items.ItemListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;


public class BrandListAdapter extends RecyclerView.Adapter<BrandListAdapter.MyHolder> {
    private FragmentActivity context;
    private List<BrandList> brandLists;
    ItemListFragment click;

    public BrandListAdapter(FragmentActivity context, List<BrandList> brandLists, ItemListFragment click) {
        this.context = context;
        this.brandLists = brandLists;
        this.click = click;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        BrandListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.brand_list_layout, parent, false);


        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        BrandList currentPossition = brandLists.get(position);
       /* if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1515)) {
            holder.binding.edit.setVisibility(View.GONE);
        }
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1516)) {
            holder.binding.delete.setVisibility(View.GONE);
        }*/

        holder.binding.brandName.setText(":  " + currentPossition.getBrandName());
        try {
            Glide.with(context).load(ImageBaseUrl.image_base_url + currentPossition.getImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.binding.brandImageView);

        } catch (NullPointerException e) {
            Log.d("ERROR", e.getMessage());
        }
        /** click handle*/
        holder.binding.edit.setOnClickListener(v -> {
            click.getData(holder.getAdapterPosition(), currentPossition.getBrandID(), currentPossition.getBrandName(), currentPossition.getImage());
        });

        holder.binding.delete.setOnClickListener(v -> click.delete(holder.getAdapterPosition(), currentPossition.getBrandID()));

    }

    @Override
    public int getItemCount() {
        return brandLists.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private final BrandListLayoutBinding binding;

        public MyHolder(@NonNull @NotNull BrandListLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
