package com.rupayan_housing.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.StockListXmlBinding;
import com.rupayan_housing.serverResponseModel.StockList;
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.replace.KgToTon;
import com.rupayan_housing.view.fragment.store.StoreListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StockInformationListAdapter extends RecyclerView.Adapter<StockInformationListAdapter.viewHolder> {
    private Context context;
    List<StockList> lists;
    View view;

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        StockListXmlBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.stock_list_xml, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        StockList currentList = lists.get(position);
        try {
            holder.itembinding.itemName.setText(":  " + currentList.getProductTitle());
            if (currentList.getBrandName() != null) {
                holder.itembinding.brand.setText(":  " + currentList.getBrandName());
            }
            holder.itembinding.category.setText(":  " + currentList.getCategory());

            if (currentList.getQuantity() != null) {
                holder.itembinding.quality.setText(":  " + currentList.getQuantity()+ " " + MtUtils.kg);
            }
            holder.itembinding.lbp.setText(":  " + currentList.getUnitBuyingPrice());
            holder.itembinding.lsp.setText(":  " + currentList.getAvgPrice());
        } catch (Exception e) {
            Log.e("ERROR", "" + e.getMessage());
        }

        holder.itembinding.view.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("productId", currentList.getProductID());
            bundle.putString("porson", "stockDetails");
            bundle.putString("pageName", "Stock Details");
            if (StoreListFragment.endScroll) {
                StoreListFragment.manage = 1;
            }
            StoreListFragment.pageNumber = +1;
            Navigation.findNavController(view).navigate(R.id.action_storeListFragment_self, bundle);
        });


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private StockListXmlBinding itembinding;

        public viewHolder(@NonNull @NotNull StockListXmlBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }

    public Double kgToTon(String purchaseCm) {
        try {
            double number = 0.00;
            if (purchaseCm != null) {
                number = Double.parseDouble(purchaseCm) / 1000;
            }

            return number;
        } catch (Exception e) {
            return 0.0;
        }
    }

}
