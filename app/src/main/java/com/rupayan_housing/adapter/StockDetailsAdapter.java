package com.rupayan_housing.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.StockLayoutBinding;
import com.rupayan_housing.serverResponseModel.StockDetail;
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.replace.KgToTon;


import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StockDetailsAdapter extends RecyclerView.Adapter<StockDetailsAdapter.viewHolder> {
   private Context context;
   List<StockDetail> lists;

   public StockDetailsAdapter(Context context, List<StockDetail> lists) {
       this.context = context;
       this.lists = lists;
   }

   @NonNull
   @NotNull
   @Override
   public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       StockLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
               from(parent.getContext()), R.layout.stock_layout, parent, false);
       return new viewHolder(binding);
   }

   @Override
   public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
       try {
           StockDetail currentList = lists.get(position);
           holder.itembinding.storeName.setText(":  " + currentList.getName());
           holder.itembinding.quantity.setText(":  " + currentList.getQuantity() + MtUtils.kg);
       } catch (Exception e) {
           Log.e("ERROR", "" + e.getMessage());
       }
   }

   @Override
   public int getItemCount() {
       return lists.size();
   }

   public class viewHolder extends RecyclerView.ViewHolder {
       private StockLayoutBinding itembinding;
       public viewHolder(@NonNull @NotNull StockLayoutBinding itembinding) {
           super(itembinding.getRoot());
           this.itembinding = itembinding;
       }
   }
}
