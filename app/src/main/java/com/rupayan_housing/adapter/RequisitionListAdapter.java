package com.rupayan_housing.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.serverResponseModel.SalesRequisitionListResponse;
import com.rupayan_housing.viewModel.SalesRequisitionListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * for show all sales requisition list
 */
public class RequisitionListAdapter extends RecyclerView.Adapter<RequisitionListAdapter.MyHolder> {
    private SalesRequisitionListViewModel salesRequisitionListViewModel;
    FragmentActivity context;
    List<SalesRequisitionListResponse> salesRequisitionList;

    public RequisitionListAdapter(FragmentActivity context, List<SalesRequisitionListResponse> salesRequisitionList) {
        this.context = context;
        this.salesRequisitionList = salesRequisitionList;
        salesRequisitionListViewModel = ViewModelProviders.of(context).get(SalesRequisitionListViewModel.class);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.requisition_list, parent, false);
        return new MyHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        SalesRequisitionListResponse currentRequisition = salesRequisitionList.get(position);
        holder.ownerName.setText(currentRequisition.getCustomerFname());
        holder.companyName.setText(currentRequisition.getCompanyName());
        holder.enterPriseName.setText(currentRequisition.getEnterpriseName());
        holder.processBy.setText(currentRequisition.getFullName());
        holder.startDate.setText(currentRequisition.getDate());
        holder.endDate.setText(currentRequisition.getEndDate());
        holder.totalAmount.setText(currentRequisition.getGrandTotal() + " " + "TK");

        /***
         * For get single requisition details
         */
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", currentRequisition.getId());
            Navigation.createNavigateOnClickListener(R.id.action_salesRequisitionList_to_singleSalesRequisitionDetails, bundle).onClick(v);
            return;
        });

    }

    @Override
    public int getItemCount() {
        return salesRequisitionList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
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

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
