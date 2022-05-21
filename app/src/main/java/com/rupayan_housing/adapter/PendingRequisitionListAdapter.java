package com.rupayan_housing.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.serverResponseModel.PendingRequisitionListResponse;
import com.rupayan_housing.viewModel.PendingRequisitionListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * for show all pending requisition list
 */
public class PendingRequisitionListAdapter extends RecyclerView.Adapter<PendingRequisitionListAdapter.MyHolder> {
    private final PendingRequisitionListViewModel pendingRequisitionListViewModel;
    FragmentActivity context;
    List<PendingRequisitionListResponse> pendingRequisitionListResponseList;

    public PendingRequisitionListAdapter(FragmentActivity context, List<PendingRequisitionListResponse> pendingRequisitionListResponseList) {
        this.context = context;
        this.pendingRequisitionListResponseList = pendingRequisitionListResponseList;
        pendingRequisitionListViewModel = ViewModelProviders.of(context).get(PendingRequisitionListViewModel.class);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_requisition_list_item, parent, false);
        return new MyHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        PendingRequisitionListResponse currentRequisition = pendingRequisitionListResponseList.get(position);
        holder.orderId.setText("#"+currentRequisition.getId());
        holder.ownerName.setText(currentRequisition.getCustomerFname());
        holder.companyName.setText(currentRequisition.getCompanyName());
        holder.enterPriseName.setText(currentRequisition.getEnterpriseName());
        holder.processBy.setText(currentRequisition.getFullName());
        holder.startDate.setText(currentRequisition.getDate());
        holder.endDate.setText(currentRequisition.getEndDate());
        holder.totalAmount.setText(currentRequisition.getGrandTotal() + " " + "TK");

        /**
         * for show pending requisition details by id
         */
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", currentRequisition.getId());
            bundle.putBoolean("isPending",true);
            Navigation.createNavigateOnClickListener(R.id.action_pendingRequisitionListFragment_to_singleSalesRequisitionDetails,bundle).onClick(v);

        });
    }

    @Override
    public int getItemCount() {
        return pendingRequisitionListResponseList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.orderId)
        TextView orderId;
        @BindView(R.id.ownerNameEt)
        TextView ownerName;
        @BindView(R.id.companyName)
        TextView companyName;
        @BindView(R.id.enterPriseName)
        TextView enterPriseName;
        @BindView(R.id.processBy)
        TextView processBy;
        @BindView(R.id.startDate)
        TextView startDate;
        @BindView(R.id.endDate)
        TextView endDate;
        @BindView(R.id.totalAmount)
        TextView totalAmount;
        @BindView(R.id.nextBtn)
        ImageButton nextBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
