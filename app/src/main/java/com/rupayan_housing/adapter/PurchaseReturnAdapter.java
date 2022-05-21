package com.rupayan_housing.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.PurchaseReturnBinding;
import com.rupayan_housing.serverResponseModel.PurchaseItems;
import com.rupayan_housing.view.fragment.purchase.purchaseReturn.PurchaseReturnItemClick;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PurchaseReturnAdapter extends RecyclerView.Adapter<PurchaseReturnAdapter.MyHolder> {
    private FragmentActivity activity;
    private List<PurchaseItems> items;
    private PurchaseReturnItemClick itemClick;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PurchaseReturnBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.purchase_return, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        try {
            PurchaseItems currentItem = items.get(position);
            holder.binding.itemName.setText("" + currentItem.getItem());
            holder.binding.quantity.setText("" + currentItem.getQuantity());
        } catch (Exception e) {
        }
        holder.binding.returnQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String currentReturnQuantity = holder.binding.returnQuantity.getText().toString();
                    double actualQuantity = Double.parseDouble(items.get(holder.getAdapterPosition()).getQuantity());
                    if (currentReturnQuantity.isEmpty()) {
                        currentReturnQuantity = String.valueOf(0);
                        itemClick.insertQuantity(holder.getAdapterPosition(), currentReturnQuantity, items.get(holder.getAdapterPosition()));
                        return;
                    }
                    if (Double.parseDouble(currentReturnQuantity) > actualQuantity) {
                        holder.binding.returnQuantity.setText(String.valueOf(actualQuantity));
                        currentReturnQuantity = String.valueOf(actualQuantity);
                        itemClick.insertQuantity(holder.getAdapterPosition(), currentReturnQuantity, items.get(holder.getAdapterPosition()));
                        return;
                    }
                    if (Double.parseDouble(currentReturnQuantity) < 0) {
                        holder.binding.returnQuantity.setText("0");
                        currentReturnQuantity = "0";
                        itemClick.insertQuantity(holder.getAdapterPosition(), currentReturnQuantity, items.get(holder.getAdapterPosition()));
                        return;
                    }

//                    holder.binding.returnQuantity.setText(currentReturnQuantity);
                    itemClick.insertQuantity(holder.getAdapterPosition(), currentReturnQuantity, items.get(holder.getAdapterPosition()));
                } catch (Exception e) {
                    Log.d("ERROR", "" + e.getMessage());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        PurchaseReturnBinding binding;

        public MyHolder(PurchaseReturnBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
