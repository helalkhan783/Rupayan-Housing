package com.rupayan_housing.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.rupayan_housing.R;
import com.rupayan_housing.utils.SalesRequisitionUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesRequisitionAdapter extends RecyclerView.Adapter<SalesRequisitionAdapter.MyHolder> {
    FragmentActivity context;
    List<String> allSaleRequisitionTitle;
    List<Integer> allSaleRequisitionImage;

    public SalesRequisitionAdapter(FragmentActivity activity, List<String> allSaleRequisitionTitle, List<Integer> allSaleRequisitionImage) {
        this.context = activity;
        this.allSaleRequisitionTitle = allSaleRequisitionTitle;
        this.allSaleRequisitionImage = allSaleRequisitionImage;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.management_fragment_model, parent, false);
        return new MyHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Integer currentImage = allSaleRequisitionImage.get(position);
        String currentTitle = allSaleRequisitionTitle.get(position);
        holder.itemText.setText(currentTitle);
        holder.itemImage.setImageDrawable(context.getDrawable(currentImage));
        /**
         * if user click new sale then user go to new sale fragment
         */
        holder.itemView.setOnClickListener(v -> {
            /**
             * for show sales quisition list
             */
            if (allSaleRequisitionTitle.get(position).equals(SalesRequisitionUtil.salesReqListTitle)) {
                Navigation.createNavigateOnClickListener(R.id.action_salesRequisitionManagementFragment_to_salesRequisitionList).onClick(v);
                return;
            }
            /**
             * for show sales quisition list
             */
            if (allSaleRequisitionTitle.get(position).equals(SalesRequisitionUtil.pendingReqListTitle)) {
                Navigation.createNavigateOnClickListener(R.id.action_salesRequisitionManagementFragment_to_pendingRequisitionListFragment).onClick(v);
                return;
            }
            /**
             * for show sales quisition list
             */
            if (allSaleRequisitionTitle.get(position).equals(SalesRequisitionUtil.declinedReqListTitle)) {
                Navigation.createNavigateOnClickListener(R.id.action_salesRequisitionManagementFragment_to_declinedRequisitionListFragment).onClick(v);
                return;
            }
            /**
             * for add new requisition
             */
            Navigation.createNavigateOnClickListener(R.id.action_salesRequisitionManagementFragment_to_newSaleFragment).onClick(v);
        });
    }

    @Override
    public int getItemCount() {
        return allSaleRequisitionImage.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_home_item)
        ImageView itemImage;
        @BindView(R.id.text_home_item)
        TextView itemText;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
