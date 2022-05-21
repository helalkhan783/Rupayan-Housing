package com.rupayan_housing.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.SelectedNewConfirmSaleModelBinding;
import com.rupayan_housing.serverResponseModel.SalesRequisitionItems;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConfirmNewSaleSelectedProductListAdapter extends RecyclerView.Adapter<ConfirmNewSaleSelectedProductListAdapter.MyHolder> {
    private FragmentActivity context;
    private List<SalesRequisitionItems> list;


    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        SelectedNewConfirmSaleModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.selected_new_confirm_sale_model, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        SalesRequisitionItems currentItem = list.get(position);
        holder.binding.productName.setText(":  "+currentItem.getProductTitle());
       String unit = currentItem.getUnit_name();
       if (unit == null){
           unit="";
       }
        try {
            holder.binding.quantity.setText(":  "+currentItem.getQuantity()+" "+unit);
         } catch (Exception e) {
            Log.d("ERROR", "ERROR");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        private SelectedNewConfirmSaleModelBinding binding;

        public MyHolder(SelectedNewConfirmSaleModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
