package com.rupayan_housing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.CategoryItemListLayoutBinding;
import com.rupayan_housing.serverResponseModel.CategoryList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CategoryItemListAdapter extends RecyclerView.Adapter<CategoryItemListAdapter.viewHolder> {
    private Context context;
    List<CategoryList> lists;

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CategoryItemListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.category_item_list_layout, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        CategoryList currentList = lists.get(position);
        holder.itembinding.enroledDate.setText(":  "+currentList.getEntryDateTime());
        holder.itembinding.categoryName.setText(":  "+currentList.getCategory());
        holder.itembinding.categoryCode.setText(":  "+currentList.getCategoryCode());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private CategoryItemListLayoutBinding itembinding;
        public viewHolder(@NonNull @NotNull CategoryItemListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
