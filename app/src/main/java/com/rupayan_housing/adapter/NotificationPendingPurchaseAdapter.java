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
import com.rupayan_housing.serverResponseModel.ItemsResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationPendingPurchaseAdapter extends RecyclerView.Adapter<NotificationPendingPurchaseAdapter.MyHolder> {
    FragmentActivity context;
    List<ItemsResponse> itemsResponseList;
    String enterPriseName;
    public static double totalAmount = 0;
    public static double collectedAmount = 0;


    public NotificationPendingPurchaseAdapter(FragmentActivity context, List<ItemsResponse> itemsResponseList, String enterPriseName) {
        this.context = context;
        this.itemsResponseList = itemsResponseList;
        this.enterPriseName = enterPriseName;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_pending_product_model, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ItemsResponse currentItems = itemsResponseList.get(position);
        try {
            holder.itemNameTv.setText(":  " + currentItems.getProductTitle());
            holder.quantityTv.setText(":  " + currentItems.getQuantity() + " " +currentItems.getUnit());
            holder.enterPrisenameTv.setText(":  " + enterPriseName);
            holder.storeTv.setText(":  " + currentItems.getSoldFrom());
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return itemsResponseList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ItemNameTv)
        TextView itemNameTv;
        @BindView(R.id.quantityTv)
        TextView quantityTv;


        @BindView(R.id.enterPrisenameTv)
        TextView enterPrisenameTv;
        @BindView(R.id.storeTv)
        TextView storeTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
