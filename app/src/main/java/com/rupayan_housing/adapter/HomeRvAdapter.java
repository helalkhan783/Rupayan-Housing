package com.rupayan_housing.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.HomePageClickHandle;
import com.rupayan_housing.databinding.HomeFragmentModelBinding;
import com.rupayan_housing.utils.HomeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeRvAdapter extends RecyclerView.Adapter<HomeRvAdapter.MyHolder> {
    private final String root = " Management";
    FragmentActivity context;
    List<Integer> imageList;
    List<String> itemNameList;
    View view;

    public HomeRvAdapter(FragmentActivity context, List<Integer> imageList, List<String> itemNameList,View view) {
        this.context = context;
        this.imageList = imageList;
        this.itemNameList = itemNameList;
        this.view = view;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeFragmentModelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.home_fragment_model, parent, false);
         return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Integer currentImage = imageList.get(holder.getAdapterPosition());
        String currentTitle = itemNameList.get(holder.getAdapterPosition());
        holder.binding.imageview.setImageDrawable(ContextCompat.getDrawable(context,currentImage));
        holder.binding.nameTv.setText(currentTitle);

        holder.binding.getRoot().setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            /**
             * if user click salesRequisition
             */

            if (itemNameList.get(holder.getAdapterPosition()).equals(HomeUtils.itemManagement)){
                bundle.putString("Item", itemNameList.get(holder.getAdapterPosition()));
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
            }


            if (itemNameList.get(holder.getAdapterPosition()).equals(HomeUtils.qcQa)){
                bundle.putString("Item", itemNameList.get(holder.getAdapterPosition()));
                 Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
            }
            if (itemNameList.get(holder.getAdapterPosition()).equals(HomeUtils.monitoring)){
                bundle.putString("Item", itemNameList.get(holder.getAdapterPosition()));
                 Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);

            }

            if (itemNameList.get(holder.getAdapterPosition()).equals(HomeUtils.customers)){
                bundle.putString("Item", itemNameList.get(holder.getAdapterPosition()));
                 Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);

            }

            if (itemNameList.get(holder.getAdapterPosition()).equals(HomeUtils.sales)){
                bundle.putString("Item", itemNameList.get(holder.getAdapterPosition()));
                 Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);

            }

            if (itemNameList.get(holder.getAdapterPosition()).equals(HomeUtils.purchases)){
                bundle.putString("Item", itemNameList.get(holder.getAdapterPosition()));
                 Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);

            }
            if (itemNameList.get(holder.getAdapterPosition()).equals(HomeUtils.production)){
                bundle.putString("Item", itemNameList.get(holder.getAdapterPosition()));
                 Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
            }

            if (itemNameList.get(holder.getAdapterPosition()).equals(HomeUtils.report)){
                bundle.putString("Item", itemNameList.get(holder.getAdapterPosition()));
                 Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_managementFragment, bundle);
            }

    });


    }

    @Override
    public int getItemCount() {
        return itemNameList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {


        private HomeFragmentModelBinding binding;

        public MyHolder(HomeFragmentModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
         }
    }

}
