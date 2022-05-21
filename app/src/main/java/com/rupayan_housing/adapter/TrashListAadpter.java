package com.rupayan_housing.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.TrashItemListLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.TrashList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.items.ItemDelete;
import com.rupayan_housing.view.fragment.items.ItemListFragment;
import com.rupayan_housing.viewModel.ItemsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TrashListAadpter extends RecyclerView.Adapter<TrashListAadpter.viewHolder> {
    private Context context;
    List<TrashList> lists;
    ItemDelete itemDelete;


    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        TrashItemListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.trash_item_list_layout, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        TrashList currentList = lists.get(position);
        holder.itembinding.categoryTrashName.setText(":  " + currentList.getCategory());
        holder.itembinding.itemTrashName.setText(":  " + currentList.getProductTitle());
        if (currentList.getBrandName() !=null){
            holder.itembinding.trashBrandName.setText(":  " + currentList.getBrandName());

        }
        holder.itembinding.trashUnit.setText(":  " + currentList.getName());


        holder.itembinding.delete.setOnClickListener(v -> {
            try {
                /**
                 * check permission first
                 */
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1452) ||
                        PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1)) {
                    /**
                     * now call api for delete this item
                     */
                    itemDelete.delete(
                            lists.get(holder.getAdapterPosition()).getVendorID(),
                            lists.get(holder.getAdapterPosition()).getStoreID(),
                            lists.get(holder.getAdapterPosition()).getProductID()
                    );
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
        });


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TrashItemListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull TrashItemListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }

}
