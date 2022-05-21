package com.rupayan_housing.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.rupayan_housing.R;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.utils.ManagementUtils;
import com.rupayan_housing.utils.PermissionUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyHolder> {
    FragmentActivity context;
    List<Integer> imageList;
    List<String> itemNameList;

    Bundle bundle = new Bundle();

    public SubCategoryAdapter(FragmentActivity context, List<Integer> imageList, List<String> itemNameList) {
        this.context = context;
        this.imageList = imageList;
        this.itemNameList = itemNameList;
    }

    @NonNull
    @Override
    public SubCategoryAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.management_fragment_model, parent, false);
        return new SubCategoryAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryAdapter.MyHolder holder, int position) {
        Integer currentImage = imageList.get(position);
        String currentTitle = itemNameList.get(position);


        holder.itemImage.setImageDrawable(context.getDrawable(currentImage));
        holder.itemText.setText(currentTitle);


        holder.itemView.setOnClickListener(v -> {

            if (itemNameList.get(holder.getAdapterPosition()).equals(ManagementUtils.addNewItem)) {
                try {
                    String currentProfileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
                  /*  if (!currentProfileTypeId.equals("7")) {
                        Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                        return;
                    }*/
                    if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(2)) {
                        bundle.putString("portion", itemNameList.get(position));
                        Navigation.createNavigateOnClickListener(R.id.action_managementFragment_to_addNewItem, bundle).onClick(v);
                        return;
                    } else {
                        Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                 //   Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                }
            }
            if (itemNameList.get(position).equals(ManagementUtils.itemLists)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1051)) {
                    bundle.putString("porson", itemNameList.get(position));
                    Navigation.createNavigateOnClickListener(R.id.action_managementFragment_to_itemListFragment, bundle).onClick(v);
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
            }
            if (itemNameList.get(position).equals(ManagementUtils.itemCategory)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1473)) {
                    bundle.putString("porson", itemNameList.get(position));
                    Navigation.createNavigateOnClickListener(R.id.action_managementFragment_to_itemListFragment, bundle).onClick(v);
                } else {
                    Toasty.info(context, PermissionUtil.permissionMessage, Toasty.LENGTH_LONG).show();
                }
             }

            if (itemNameList.get(position).equals(ManagementUtils.assignItemPacket)) {
                bundle.putString("porson", itemNameList.get(position));
                Navigation.createNavigateOnClickListener(R.id.action_managementFragment_to_itemListFragment, bundle).onClick(v);

            }

            if (itemNameList.get(position).equals(ManagementUtils.brands)) {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1048)) {
                    bundle.putString("porson", itemNameList.get(position));
                    Navigation.createNavigateOnClickListener(R.id.action_managementFragment_to_itemListFragment, bundle).onClick(v);
                    return;
                } else {
                    Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                }

            }
            if (itemNameList.get(position).equals(ManagementUtils.trash)) {
               try {
                   if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1049)) {
                       bundle.putString("porson", itemNameList.get(position));
                       Navigation.createNavigateOnClickListener(R.id.action_managementFragment_to_itemListFragment, bundle).onClick(v);
                   } else {
                       Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                   }
               }catch (Exception e){
                   Toasty.info(context, "You don't have permission for access this portion e", Toasty.LENGTH_LONG).show();

               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemNameList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_home_item)
        ImageView itemImage;
        @BindView(R.id.text_home_item)
        TextView itemText;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
