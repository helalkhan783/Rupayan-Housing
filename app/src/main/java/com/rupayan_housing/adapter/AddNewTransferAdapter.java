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
import com.rupayan_housing.view.fragment.transfer.addNew.AddNewTransfer;
import com.rupayan_housing.view.fragment.transfer.addNew.AddNewTransferItemClickHandle;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.AllArgsConstructor;


public class AddNewTransferAdapter extends RecyclerView.Adapter<AddNewTransferAdapter.MyHolder> {
    private FragmentActivity context;
    private List<SalesRequisitionItems> list;
    private List<ProductStockListResponse> productStockResponse;
    private AddNewTransferItemClickHandle itemClick;
   private List<ProductStockListResponse> stockListResponses;

    public AddNewTransferAdapter(FragmentActivity context, List<SalesRequisitionItems> list, List<ProductStockListResponse> productStockResponse,
                                 AddNewTransferItemClickHandle itemClick,List<ProductStockListResponse> stockListResponses) {
        this.context = context;
        this.list = list;
        this.productStockResponse = productStockResponse;
        this.itemClick = itemClick;
        this.stockListResponses = stockListResponses;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        NewSaleProductListModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.new_sale_product_list_model, parent, false);
        return new AddNewTransferAdapter.MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        SalesRequisitionItems currentItem = list.get(holder.getAdapterPosition());
        ProductStockListResponse      listResponse = stockListResponses  .get(holder.getAdapterPosition());
        holder.binding.productName.setText(currentItem.getProductTitle());
        holder.binding.unit.setText(currentItem.getUnit_name());
        holder.binding.quantityEt.setText(currentItem.getQuantity());
        holder.binding.stock.setText(""+listResponse.getStockQty());
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
        AddNewTransfer.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(totalQuantity));


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


                List<String> quantityList = AddNewTransfer.getAllQuantity();
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
                    AddNewTransfer.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(total));
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
                List<String> quantityList = AddNewTransfer.getAllQuantity();
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
                AddNewTransfer.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(total));
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


                List<String> quantityList = AddNewTransfer.getAllQuantity();
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
                AddNewTransfer.binding.totalQuantity.setText("Total Quantity: " + String.valueOf(total));


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
