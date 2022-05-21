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
import com.rupayan_housing.databinding.QcqaDeclinedModelBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.DeclineQcQaList;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.DateFormatRight;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeclinedQcQaAdapter extends RecyclerView.Adapter<DeclinedQcQaAdapter.MyHolder> {
    private FragmentActivity context;
    private List<DeclineQcQaList> lists;

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        QcqaDeclinedModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.qcqa_declined_model, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        if (!PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1339)) {
            holder.binding.viewBtn.setVisibility(View.GONE);
        }

        try {
            DeclineQcQaList currentQcQa = lists.get(position);
            holder.binding.sampleName.setText(":  " + currentQcQa.getModel());

            holder.binding.date.setText(":  " +new DateFormatRight(context,currentQcQa.getTestDate()).onlyDayMonthYear());
            holder.binding.enterpriseName.setText(":  " + currentQcQa.getStoreName());
        } catch (Exception e) {
            Log.d("ERROR", "" + e.getMessage());
        }
        /**
         * For show details
         */
        holder.binding.viewBtn.setOnClickListener(v -> {
            if (PermissionUtil.currentUserPermissionList(PreferenceManager.getInstance(context).getUserCredentials().getPermissions()).contains(1339)) {
                Bundle bundle = new Bundle();
                bundle.putString("id", lists.get(holder.getAdapterPosition()).getQcID());
                bundle.putString("SL_ID", lists.get(holder.getAdapterPosition()).getSlID());
                bundle.putString("pageName", "QC/QA Details");
                Navigation.findNavController(holder.binding.getRoot()).navigate(R.id.action_qcQaPendingFragment_to_qcqaDetailsFragment, bundle);
                return;
            } else {
                Toasty.info(context, "You don't have permission for access this portion", Toasty.LENGTH_LONG).show();
                return;
            }


        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        QcqaDeclinedModelBinding binding;

        public MyHolder(QcqaDeclinedModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
