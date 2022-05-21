package com.rupayan_housing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.rupayan_housing.R;
import com.rupayan_housing.serverResponseModel.SalesItemResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * for show single salesRequisition product list
 */
public class SalesRequisitionDetailsProductsAdapter extends RecyclerView.Adapter<SalesRequisitionDetailsProductsAdapter.MyHolder> {
    FragmentActivity context;
    List<SalesItemResponse> itemList;

    public SalesRequisitionDetailsProductsAdapter(FragmentActivity context, List<SalesItemResponse> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_list_rv, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        SalesItemResponse currentProduct = itemList.get(position);
        holder.productNameTv.setText(currentProduct.getItem());
        holder.productQuantityTv.setText("X "+currentProduct.getQuantity());
        holder.productPriceTv.setText(currentProduct.getSellingPrice()+" TK");
        holder.totalAmountTv.setText(String.valueOf(currentProduct.getTotalAmount())+" "+"TK");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.productNameTv)
        TextView productNameTv;
        @BindView(R.id.productQuantityTv)
        TextView productQuantityTv;
        @BindView(R.id.productPriceTv)
        TextView productPriceTv;
        @BindView(R.id.totalAmountTv)
        TextView totalAmountTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
