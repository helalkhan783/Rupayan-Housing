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
import com.rupayan_housing.view.fragment.reconciliation.addNew.AddNewReconcilation;
import com.rupayan_housing.view.fragment.reconciliation.addNew.AddNewReconcilationItemClick;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;

 public class AddNewReconcilationAdapter extends RecyclerView.Adapter<AddNewReconcilationAdapter.MyHolder> {
    private FragmentActivity context;
    private List<SalesRequisitionItems> list;
    private List<ProductStockListResponse> productStockResponse;
    private AddNewReconcilationItemClick itemClick;
    private  List<ProductStockListResponse> stockList;

     public AddNewReconcilationAdapter(FragmentActivity context, List<SalesRequisitionItems> list, List<ProductStockListResponse> productStockResponse,
                                       AddNewReconcilationItemClick itemClick, List<ProductStockListResponse> stockList) {
         this.context = context;
         this.list = list;
         this.productStockResponse = productStockResponse;
         this.itemClick = itemClick;
         this.stockList = stockList;
     }

     @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        NewSaleProductListModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.new_sale_product_list_model, parent, false);
        return new AddNewReconcilationAdapter.MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        SalesRequisitionItems currentItem = list.get(holder.getAdapterPosition());
        ProductStockListResponse currentStock = stockList.get(position);

        holder.binding.stock.setText(""+currentStock.getStockQty());
        holder.binding.productName.setText(currentItem.getProductTitle());
        holder.binding.unit.setText(currentItem.getUnit_name());
        holder.binding.quantityEt.setText(currentItem.getQuantity());
        if (Integer.parseInt(currentItem.getQuantity()) > 0) {
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
        int totalQuantity = 0;
        for (int i = 0; i < list.size(); i++) {
            totalQuantity += Integer.parseInt(list.get(i).getQuantity());
        }
        AddNewReconcilation.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(totalQuantity));


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
                int quantity = Integer.parseInt(holder.binding.quantityEt.getText().toString());
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


                List<String> quantityList = AddNewReconcilation.getAllQuantity();
                if (quantityList.isEmpty()) {
                    return;
                }
                int total = 0;
                for (int i = 0; i < quantityList.size(); i++) {
                    if (quantityList.get(i).isEmpty()) {
                    } else {
                        total += Integer.parseInt(quantityList.get(i));
                    }

                }
                try {
                    AddNewReconcilation.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(total));
                } catch (Exception e) {

                }
            }

            @Override
            public void removeQuantity() {
                if (holder.binding.quantityEt.getText().toString().isEmpty()) {
                    return;
                }
                int quantity = Integer.parseInt(holder.binding.quantityEt.getText().toString());
                if (quantity == 0) {
                    holder.binding.quantityEt.setVisibility(View.GONE);
                    holder.binding.removeBtn.setVisibility(View.GONE);
                    holder.binding.unit.setVisibility(View.GONE);
                    return;
                }
                quantity -= 1;
                holder.binding.quantityEt.setText(String.valueOf(quantity));
                itemClick.minusQuantity(holder.getAdapterPosition(), String.valueOf(quantity), list.get(holder.getAdapterPosition()));
                List<String> quantityList = AddNewReconcilation.getAllQuantity();
                if (quantityList.isEmpty()) {
                    return;
                }
                int total = 0;
                for (int i = 0; i < quantityList.size(); i++) {
                    if (quantityList.get(i).isEmpty()) {
                    } else {
                        total += Integer.parseInt(quantityList.get(i));
                    }

                }
                AddNewReconcilation.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(total));
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
                int quantity = 0;
                if (holder.binding.quantityEt.getText().toString().isEmpty()) {

                } else {
                    try {
                        quantity = Integer.parseInt(holder.binding.quantityEt.getText().toString());
                    } catch (Exception e) {
                        Log.d("ERROR", e.getLocalizedMessage());
                    }
                }
                itemClick.insertQuantity(holder.getAdapterPosition(), String.valueOf(quantity), list.get(holder.getAdapterPosition()));


                List<String> quantityList = AddNewReconcilation.getAllQuantity();
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
                AddNewReconcilation.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(total));


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

    public class MyHolder extends RecyclerView.ViewHolder {
        NewSaleProductListModelBinding binding;

        public MyHolder(NewSaleProductListModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
