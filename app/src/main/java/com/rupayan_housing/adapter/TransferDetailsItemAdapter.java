package com.rupayan_housing.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.serverResponseModel.PendingTransferItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TransferDetailsItemAdapter extends RecyclerView.Adapter<TransferDetailsItemAdapter.MyHolder> {
    FragmentActivity context;
    List<PendingTransferItem> itemList;

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transfer_details_item_model, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        PendingTransferItem currentItem = itemList.get(position);
        try {
            holder.itemName.setText(":  " + currentItem.getProductTitle());
             holder.quantity.setText(":  " + currentItem.getQuantity() + " " + currentItem.getName());

        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemNameModel)
        TextView itemName;
//        @BindView(R.id.priceModel)
//        TextView price;
        @BindView(R.id.quantity)
        TextView quantity;
     /*   @BindView(R.id.total)
        TextView total;*/

        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
