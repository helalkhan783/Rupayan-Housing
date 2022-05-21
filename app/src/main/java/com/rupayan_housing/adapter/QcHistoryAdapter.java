package com.rupayan_housing.adapter;

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
import com.rupayan_housing.databinding.QcHistoryListModelBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.QcHistoryList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class QcHistoryAdapter extends RecyclerView.Adapter<QcHistoryAdapter.ViewHolder> {
    FragmentActivity activity;
    List<QcHistoryList> lists;

    @NonNull
    @NotNull
    @Override
    public QcHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        QcHistoryListModelBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.qc_history_list_model, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull QcHistoryAdapter.ViewHolder holder, int position) {
        QcHistoryList currentList = lists.get(position);
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1338)) {
          holder.binding.edit.setVisibility(View.GONE);
        }

        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1339)) {
          holder.binding.view.setVisibility(View.GONE);
        }

        try {
            holder.binding.date.setText(":  " + new DateFormatRight(activity, currentList.getEntryDateTime()).dateFormatForWashing());
            holder.binding.sampleName.setText(String.valueOf(":  " + currentList.getModel()));
            if (currentList.getStoreName() != null) {
                holder.binding.enterpriseName.setText(":  " + currentList.getStoreName());
            }
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }

        holder.binding.edit.setOnClickListener(v -> {
            try {

                String currentProfileTypeId = PreferenceManager.getInstance(activity).getUserCredentials().getProfileTypeId();
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1338)) {
                    Log.d("QC_ID", "" + lists.get(holder.getAdapterPosition()).getQcID());
                    Bundle bundle = new Bundle();
                    bundle.putString("id", lists.get(holder.getAdapterPosition()).getQcID());
                    bundle.putString("SL_ID", lists.get(holder.getAdapterPosition()).getSlID());
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_qcQaPendingFragment_to_edit_QCQAFragment, bundle);
                    return;
                } else {
                    Toasty.info(activity, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
        });
        holder.binding.view.setOnClickListener(v -> {
            try {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1339)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", lists.get(holder.getAdapterPosition()).getQcID());
                    bundle.putString("SL_ID", lists.get(holder.getAdapterPosition()).getSlID());
                    bundle.putString("pageName", "QA/QA Details");
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_qcQaPendingFragment_to_qcqaDetailsFragment, bundle);
                    return;
                } else {
                    Toasty.info(activity, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                    return;
                }
            } catch (Exception e) {
                Log.d("ERROR", "" + e.getMessage());
            }
        });
        holder.binding.history.setOnClickListener(v -> {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1343)) {
                Bundle bundle = new Bundle();
                bundle.putString("pageName", "QcQa Edit History");
                bundle.putString("id", currentList.getQcID());
                Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_qcQaPendingFragment_to_allSubHistoryListFragmet, bundle);
                return;
            } else {
                Toasty.info(activity, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                return;
            }
         });

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        QcHistoryListModelBinding binding;

        public ViewHolder(@NonNull @NotNull QcHistoryListModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
