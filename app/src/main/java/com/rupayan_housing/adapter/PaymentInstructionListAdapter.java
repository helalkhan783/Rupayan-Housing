package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.serverResponseModel.PaymentInstructionListResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentInstructionListAdapter extends RecyclerView.Adapter<PaymentInstructionListAdapter.MyHolder> {

    FragmentActivity context;
    List<PaymentInstructionListResponse> paymentInstructionListResponseList;

    public PaymentInstructionListAdapter(FragmentActivity activity, List<PaymentInstructionListResponse> paymentInstructionListResponseList) {
        this.context = activity;
        this.paymentInstructionListResponseList = paymentInstructionListResponseList;
    }

    @NonNull
    @Override
    public PaymentInstructionListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.payment_instruction_list_item_model, parent, false);
        return new PaymentInstructionListAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentInstructionListAdapter.MyHolder holder, int position) {

        holder.companyName.setText(paymentInstructionListResponseList.get(position).getCompanyName());
        holder.ownerName.setText(paymentInstructionListResponseList.get(position).getCustomer_fname());
        holder.date.setText(paymentInstructionListResponseList.get(position).getDate());
        holder.totalAmount.setText(paymentInstructionListResponseList.get(position).getTotalAmount() + " TK");
        holder.totalPaid.setText(paymentInstructionListResponseList.get(position).getTotalPaid() + " TK");
        holder.totalDue.setText(paymentInstructionListResponseList.get(position).getDue() + " TK");
        holder.totalLimit.setText(paymentInstructionListResponseList.get(position).getPaymentLimit() + " TK");

    }

    @Override
    public int getItemCount() {
        return paymentInstructionListResponseList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_company_name)
        TextView companyName;
        @BindView(R.id.tv_owner_name)
        TextView ownerName;
        @BindView(R.id.tv_total_amount)
        TextView totalAmount;
        @BindView(R.id.tv_total_paid)
        TextView totalPaid;
        @BindView(R.id.tv_total_due)
        TextView totalDue;
        @BindView(R.id.tv_total_limit)
        TextView totalLimit;
        @BindView(R.id.date)
        TextView date;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
