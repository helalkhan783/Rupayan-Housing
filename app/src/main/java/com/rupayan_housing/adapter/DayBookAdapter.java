package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.rupayan_housing.R;
import com.rupayan_housing.serverResponseModel.DayBookList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DayBookAdapter extends RecyclerView.Adapter<DayBookAdapter.MyHolder> {

    FragmentActivity context;
    List<DayBookList> list;

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daybook_model, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        DayBookList currentItem = list.get(position);
        holder.date.setText(currentItem.getTransactionData());
        holder.id.setText("#" + currentItem.getReferenceId());
        holder.particulars.setText(currentItem.getParticulars());
        holder.receipt.setText(String.valueOf(currentItem.getReceipt()));
        holder.payment.setText(String.valueOf(currentItem.getPayment()));
        holder.user.setText(currentItem.getProcessedBy().getFullName());
        holder.enterprise.setText(currentItem.getEnterprise());
        holder.viewBtn.setOnClickListener(v -> {
            Toasty.info(context, "will", Toasty.LENGTH_LONG).show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.id)
        TextView id;
        @BindView(R.id.particulars)
        TextView particulars;
        @BindView(R.id.enterprise)
        TextView enterprise;
        @BindView(R.id.receipt)
        TextView receipt;
        @BindView(R.id.payment)
        TextView payment;
        @BindView(R.id.user)
        TextView user;
        @BindView(R.id.viewBtn)
        ImageButton viewBtn;


        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
