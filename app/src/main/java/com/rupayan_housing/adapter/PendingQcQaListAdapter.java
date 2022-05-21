package com.rupayan_housing.adapter;

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

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.PendingQcQaClickHandle;
import com.rupayan_housing.databinding.PendingQcqalistModelBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.PendingQcQaList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PendingQcQaListAdapter extends RecyclerView.Adapter<PendingQcQaListAdapter.MyHolder> {
    private FragmentActivity activity;
    private List<PendingQcQaList> lists;


    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PendingQcqalistModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.pending_qcqalist_model, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        PendingQcQaList currentQcQa = lists.get(position);
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1338)) {
            holder.binding.edit.setVisibility(View.GONE);
        }

        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1339)) {
            holder.binding.view.setVisibility(View.GONE);
        }

        if (currentQcQa.getModel() == null) {
            holder.binding.sampleName.setText(":  ");
        } else {
            holder.binding.sampleName.setText(":  " + currentQcQa.getModel());

        }
        if (currentQcQa.getTestDate() == null) {
            holder.binding.date.setText(":  ");
        } else {
            holder.binding.date.setText(":  " + new DateFormatRight(activity, currentQcQa.getTestDate()).onlyDayMonthYear());
        }

        if (currentQcQa.getStoreName() == null) {
            holder.binding.enterpriseName.setText(":  ");
        } else {
            holder.binding.enterpriseName.setText(":  " + currentQcQa.getStoreName());

        }

        holder.binding.view.setOnClickListener(v -> {
            try {
                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1339)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", lists.get(holder.getAdapterPosition()).getQcID());
                    bundle.putString("SL_ID", lists.get(holder.getAdapterPosition()).getSlID());
                    bundle.putString("status", "2");
                    bundle.putString("pageName", "Pending Qc-Qa Details");
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


        holder.binding.setClickHandle(() -> {
            try {
                /**
                 * For edit qc qa
                 */
                String currentProfileTypeId = PreferenceManager.getInstance(activity).getUserCredentials().getProfileTypeId();

                if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(activity).getUserCredentials().getPermissions()).contains(1338)) {
                    Log.d("QC_ID", "" + lists.get(holder.getAdapterPosition()).getQcID());
                    Bundle bundle = new Bundle();
                    bundle.putString("id", lists.get(holder.getAdapterPosition()).getQcID());
                    bundle.putString("SL_ID", lists.get(holder.getAdapterPosition()).getSlID());
                    bundle.putString("status", "2");
                    Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_qcQaPendingFragment_to_edit_QCQAFragment, bundle);
                    return;
                } else {
                    Toasty.info(activity, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                    return;
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

    class MyHolder extends RecyclerView.ViewHolder {
        private PendingQcqalistModelBinding binding;

        public MyHolder(PendingQcqalistModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
