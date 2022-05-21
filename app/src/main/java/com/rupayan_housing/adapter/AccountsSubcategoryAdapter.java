package com.rupayan_housing.adapter;

import android.os.Bundle;
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
import com.rupayan_housing.utils.AccountsUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountsSubcategoryAdapter extends RecyclerView.Adapter<AccountsSubcategoryAdapter.MyViewholder> {

    FragmentActivity context;
    List<Integer> accountsChildImageList;
    List<String> accountsChildNameList;


    public AccountsSubcategoryAdapter(FragmentActivity context, List<Integer> accountsChildImageList, List<String> accountsChildNameList) {
        this.context = context;
        this.accountsChildImageList = accountsChildImageList;
        this.accountsChildNameList = accountsChildNameList;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.management_fragment_model, parent, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

        Integer currentImage = accountsChildImageList.get(position);
        String currentTitle = accountsChildNameList.get(position);


        holder.itemImage.setImageDrawable(context.getDrawable(currentImage));
        holder.itemText.setText(currentTitle);


        holder.itemView.setOnClickListener(v -> {
            /**
             * if user click the receive due fragment then user can go the DueCollectionFragment
             */
            if (accountsChildNameList.get(position).equals(AccountsUtil.receiveDue)) {
                Bundle bundle = new Bundle();
                bundle.putString("name", AccountsUtil.receiveDue);
                Navigation.createNavigateOnClickListener(R.id.action_managementFragment_to_dueCollectionFragment, bundle).onClick(v);
            }
            /**
             * if user click pay due option goto the PayDueSupplierSearchFragment
             */
            if (accountsChildNameList.get(position).equals(AccountsUtil.payDue)) {
                Navigation.createNavigateOnClickListener(R.id.action_managementFragment_to_payDueToSupplier).onClick(v);
            }
            /**
             * if user click pay due expense option goto the expense search fragment
             */
            if (accountsChildNameList.get(position).equals(AccountsUtil.payDueExpense)) {
                Navigation.createNavigateOnClickListener(R.id.action_managementFragment_to_searchExpenseDue).onClick(v);
            }

            /**
             * if user click payment instruction option goto the payment instruction fragment
             */
            if (accountsChildNameList.get(position).equals(AccountsUtil.payInstruction)) {
                Bundle bundle = new Bundle();
                bundle.putString("name", AccountsUtil.payInstruction);
                Navigation.createNavigateOnClickListener(R.id.action_managementFragment_to_paymentInstruction).onClick(v);
            }
            /**
             * if user click payment instruction list option goto the payment instruction list fragment
             */
            if (accountsChildNameList.get(position).equals(AccountsUtil.payInstructionList)) {
                Bundle bundle = new Bundle();
                bundle.putString("name", AccountsUtil.payInstructionList);
                Navigation.createNavigateOnClickListener(R.id.action_managementFragment_to_paymentInstructionList).onClick(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountsChildImageList.size();
    }

    class MyViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_home_item)
        ImageView itemImage;
        @BindView(R.id.text_home_item)
        TextView itemText;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
