package com.rupayan_housing.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.NewSaleProductListClickHandle;
import com.rupayan_housing.databinding.NewSaleProductListModelBinding;
import com.rupayan_housing.serverResponseModel.ProductStockListResponse;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;
import com.rupayan_housing.view.fragment.reconciliation.edit.EditReconcilationData;
import com.rupayan_housing.view.fragment.reconciliation.edit.EditReconciliationItemClick;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EditReconciliationAdapter extends RecyclerView.Adapter<EditReconciliationAdapter.MyHolder>{
    private FragmentActivity context;
    private List<SalesRequisitionItems> list;
    private List<ProductStockListResponse> productStockResponse;
    private EditReconciliationItemClick itemClick;



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewSaleProductListModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.new_sale_product_list_model, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.binding.delete.setVisibility(View.GONE);
        holder.binding.stockLayout.setVisibility(View.GONE);
        SalesRequisitionItems currentItem = list.get(holder.getAdapterPosition());
        holder.binding.productName.setText(currentItem.getProductTitle());
        holder.binding.unit.setText(currentItem.getUnit_name());
        holder.binding.quantityEt.setText(currentItem.getQuantity());
        if (Double.parseDouble(currentItem.getQuantity()) > 0) {
            holder.binding.quantityEt.setVisibility(View.VISIBLE);
            holder.binding.unit.setVisibility(View.VISIBLE);
            holder.binding.removeBtn.setVisibility(View.VISIBLE);
        } else {
            holder.binding.quantityEt.setVisibility(View.VISIBLE);
            holder.binding.unit.setVisibility(View.VISIBLE);
            holder.binding.removeBtn.setVisibility(View.GONE);
        }

        /**
         * now manage loading time total quantity
         */
        double totalQuantity = 0;
        for (int i = 0; i < list.size(); i++) {
            totalQuantity += Double.parseDouble(list.get(i).getQuantity());
        }
        EditReconcilationData.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(totalQuantity));


        if (productStockResponse != null) {
            for (int i = 0; i < productStockResponse.size(); i++) {
                if (currentItem.getProductID().equals(productStockResponse.get(i).getProductID())) {
                    holder.binding.stock.setVisibility(View.VISIBLE);
                    holder.binding.stock.setText("Stock Available:" + productStockResponse.get(i).getStockQty());
                }
            }
        }


        holder.binding.setClickHandle(new NewSaleProductListClickHandle() {
            @Override
            public void addQuantity() {
                if (holder.binding.quantityEt.getText().toString().isEmpty()) {
                    return;
                }
                double quantity = Double.parseDouble(holder.binding.quantityEt.getText().toString());
                if (productStockResponse != null) {
                    if (productStockResponse.get(holder.getAdapterPosition()).getStockQty() == quantity) {//for handle stock Available or not
                        Toasty.info(context, "Stock Out", Toasty.LENGTH_LONG).show();
                        return;
                    }
                }
                quantity += 1;
                if (quantity > 0) {
                    holder.binding.quantityEt.setVisibility(View.VISIBLE);
                    holder.binding.unit.setVisibility(View.VISIBLE);
                    holder.binding.removeBtn.setVisibility(View.VISIBLE);
                }
                holder.binding.quantityEt.setText("" + quantity);
                itemClick.insertQuantity(holder.getAdapterPosition(), String.valueOf(quantity), list.get(holder.getAdapterPosition()));


                List<String> quantityList = EditReconcilationData.getAllQuantity();
                if (quantityList.isEmpty()) {
                    return;
                }
                double total = 0;
                for (int i = 0; i < quantityList.size(); i++) {
                    if (quantityList.get(i).isEmpty()) {
                    } else {
                        total += Double.parseDouble(quantityList.get(i));
                    }

                }
                try {
                    EditReconcilationData.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(total));
                } catch (Exception e) {

                }
            }

            @Override
            public void removeQuantity() {
                if (holder.binding.quantityEt.getText().toString().isEmpty()) {
                    return;
                }
                double quantity = Double.parseDouble(holder.binding.quantityEt.getText().toString());
                if (quantity == 0) {
                    holder.binding.quantityEt.setVisibility(View.GONE);
                    holder.binding.removeBtn.setVisibility(View.GONE);
                    holder.binding.unit.setVisibility(View.GONE);
                    return;
                }
                quantity -= 1;
                holder.binding.quantityEt.setText(String.valueOf(quantity));
                itemClick.minusQuantity(holder.getAdapterPosition(), String.valueOf(quantity), list.get(holder.getAdapterPosition()));
                List<String> quantityList = EditReconcilationData.getAllQuantity();
                if (quantityList.isEmpty()) {
                    return;
                }
                double total = 0;
                for (int i = 0; i < quantityList.size(); i++) {
                    if (quantityList.get(i).isEmpty()) {
                    } else {
                        total += Double.parseDouble(quantityList.get(i));
                    }

                }
                EditReconcilationData.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(total));
            }

            @Override
            public void delete() {
                itemClick.removeBtn(holder.getAdapterPosition());
            }
        });
        /**
         * for check real time
         */
        holder.binding.quantityEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double quantity = 0;
                if (holder.binding.quantityEt.getText().toString().isEmpty()) {

                } else {
                    try {
                        quantity = Double.parseDouble(holder.binding.quantityEt.getText().toString());
                    } catch (Exception e) {
                        Log.d("ERROR", e.getLocalizedMessage());
                    }
                }
                itemClick.insertQuantity(holder.getAdapterPosition(), String.valueOf(quantity), list.get(holder.getAdapterPosition()));


                List<String> quantityList = EditReconcilationData.getAllQuantity();
                if (quantityList.isEmpty()) {
                    return;
                }
                double total = 0.0;
                for (int i = 0; i < quantityList.size(); i++) {
                    if (quantityList.get(i).isEmpty()) {

                    } else {
                        total += Double.parseDouble(quantityList.get(i));
                    }
                }
                EditReconcilationData.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(total));


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private NewSaleProductListModelBinding binding;

        public MyHolder(NewSaleProductListModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
