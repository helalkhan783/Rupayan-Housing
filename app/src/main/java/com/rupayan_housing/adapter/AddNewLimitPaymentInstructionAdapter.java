package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.serverResponseModel.AddNewLimitInstructionResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewLimitPaymentInstructionAdapter extends RecyclerView.Adapter<AddNewLimitPaymentInstructionAdapter.MyHolder> {

    FragmentActivity context;
    List<AddNewLimitInstructionResponse> addNewLimitPaymentInstructionResponseList;
    public static List<String> customerIdArray, paymentArray;



    public AddNewLimitPaymentInstructionAdapter(FragmentActivity activity, List<AddNewLimitInstructionResponse> addNewLimitPaymentInstructionResponseList) {
        this.context = activity;
        this.addNewLimitPaymentInstructionResponseList = addNewLimitPaymentInstructionResponseList;

        customerIdArray = new ArrayList<>();
        paymentArray = new ArrayList<>();
    }

    @NonNull
    @Override
    public AddNewLimitPaymentInstructionAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_new_limit_item_layout, parent, false);
        return new AddNewLimitPaymentInstructionAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddNewLimitPaymentInstructionAdapter.MyHolder holder, int position) {

        holder.companyName.setText(addNewLimitPaymentInstructionResponseList.get(position).getCompanyName());
        holder.ownerName.setText(addNewLimitPaymentInstructionResponseList.get(position).getCustomer_fname());
        holder.totalPaid.setText(addNewLimitPaymentInstructionResponseList.get(position).getTotalPaid() + " TK");
        holder.totalDue.setText(addNewLimitPaymentInstructionResponseList.get(position).getDue() + " TK");
        holder.lastReceivedAmount.setText(addNewLimitPaymentInstructionResponseList.get(position).getLastPaidAmount() + " TK");
        holder.lastReceivedDate.setText(addNewLimitPaymentInstructionResponseList.get(position).getLastReceivedDate());
        customerIdArray.add(addNewLimitPaymentInstructionResponseList.get(position).getCustomerID());

    }

    @Override
    public int getItemCount() {
        return addNewLimitPaymentInstructionResponseList.size();
    }




    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_company_name)
        TextView companyName;
        @BindView(R.id.tv_owner_name)
        TextView ownerName;
        @BindView(R.id.tv_total_paid)
        TextView totalPaid;
        @BindView(R.id.tv_total_due)
        TextView totalDue;
        @BindView(R.id.last_received_amount)
        TextView lastReceivedAmount;
        @BindView(R.id.last_received_date)
        TextView lastReceivedDate;
        @BindView(R.id.payment_limit_edittext)
        EditText paymentLimitEditText;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
