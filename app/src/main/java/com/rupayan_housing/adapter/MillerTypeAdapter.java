package com.rupayan_housing.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rupayan_housing.R;
import com.rupayan_housing.clickHandle.ItemListClickHandle;
import com.rupayan_housing.databinding.ItemListLayoutBinding;
import com.rupayan_housing.databinding.MillTypeModelLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.MillTypeResponse;
import com.rupayan_housing.serverResponseModel.Product;
import com.rupayan_housing.utils.ImageBaseUrl;
import com.rupayan_housing.utils.PermissionUtil;
import com.rupayan_housing.view.fragment.miller.addNewMiller.MillerProfileInformation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class MillerTypeAdapter extends RecyclerView.Adapter<MillerTypeAdapter.ViewHolder> {
    private Context context;
    private List<MillTypeResponse> millTypeResponses;
  //  private boolean click = false;
  MillerProfileInformation click;

    public MillerTypeAdapter(Context context, List<MillTypeResponse> millTypeResponses,MillerProfileInformation click) {
        this.context = context;
        this.millTypeResponses = millTypeResponses;
        this.click = click;
    }
    @NonNull
    @NotNull
    @Override
    public MillerTypeAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MillTypeModelLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.mill_type_model_layout, parent, false);
        return new MillerTypeAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MillerTypeAdapter.ViewHolder holder, int position) {
        MillTypeResponse currentMiller = millTypeResponses.get(position);
        holder.binding.millType.setText(""+currentMiller.getMillTypeName());

        holder.binding.millType.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (MillerProfileInformation.selectedZone == null){
                Toasty.info(context, "At first select zone", Toasty.LENGTH_LONG).show();
                holder.binding.millType.setSelected(false);
                return;
            }
            if (holder.binding.millType.isChecked()){
                click.millTypeId(holder.getAdapterPosition(),currentMiller.getMillTypeID(),currentMiller.getRemarks());
                return;
            }
            if (!(holder.binding.millType.isChecked())){
                click.millTypeId(holder.getAdapterPosition(),"","");
                return;
            }
        });
    }

    @Override
    public int getItemCount() {
        return millTypeResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MillTypeModelLayoutBinding binding;

        public ViewHolder(final MillTypeModelLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
