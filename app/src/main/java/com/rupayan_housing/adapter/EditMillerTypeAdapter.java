package com.rupayan_housing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.MillTypeModelLayoutBinding;
import com.rupayan_housing.serverResponseModel.MillTypeResponse;
import com.rupayan_housing.view.fragment.miller.addNewMiller.MillerProfileInformation;
import com.rupayan_housing.view.fragment.miller.editmiller.MillerProfileInformationEdit;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class EditMillerTypeAdapter extends RecyclerView.Adapter<EditMillerTypeAdapter.ViewHolder> {
    private Context context;
    private List<MillTypeResponse> millTypeResponses;
    //  private boolean click = false;
    MillerProfileInformationEdit click;
    private boolean isClick = false;

    public EditMillerTypeAdapter(Context context, List<MillTypeResponse> millTypeResponses, MillerProfileInformationEdit click) {
        this.context = context;
        this.millTypeResponses = millTypeResponses;
        this.click = click;
    }

    @NonNull
    @NotNull
    @Override
    public EditMillerTypeAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        MillTypeModelLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.mill_type_model_layout, parent, false);
        return new EditMillerTypeAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull EditMillerTypeAdapter.ViewHolder holder, int position) {
        MillTypeResponse currentMiller = millTypeResponses.get(position);
        holder.binding.millType.setText("" + currentMiller.getMillTypeName());

        try {
            for (int i = 0; i < millTypeResponses.size(); i++) {
                if (currentMiller.getMillTypeID().equals(MillerProfileInformationEdit.previousGetPreviousMillerInfoResponse.getProfileInfo().getMillTypeIDs().get(i))) {
                    holder.binding.millType.setSelected(true);
                    holder.binding.millType.setChecked(true);
                }
            }
        } catch (Exception e) {
        }

// here industrial not change on edit time
//        if (position == 0) {
//            holder.binding.millType.setClickable(false);
//        }


        try {
            holder.binding.millType.setOnCheckedChangeListener((buttonView, isChecked) -> {
               /* if (MillerProfileInformationEdit.selectedZone == null) {
                    Toasty.info(context, "At first select zone", Toasty.LENGTH_LONG).show();
                    holder.binding.millType.setSelected(false);
                    return;
                }*/

                if (holder.getAdapterPosition() == 0) {
                    Toasty.info(context, "Change unavailable", Toasty.LENGTH_LONG).show();
                    return;
                }

               /* if (position == 1) {
                    if (holder.binding.millType.isChecked()){
                        Toasty.info(context, "Change unavailable", Toasty.LENGTH_LONG).show();
                    }
                    return;
                }*/
                if (holder.getAdapterPosition() == 1) {
                    if (holder.binding.millType.isChecked()) {
                        click.millTypeId(holder.getAdapterPosition(), currentMiller.getMillTypeID(), currentMiller.getRemarks());
                        return;
                    }
                    if (!holder.binding.millType.isChecked()) {
                        click.millTypeId(holder.getAdapterPosition(), "", "");
                        return;
                    }

                }

              /*  if (!(holder.binding.millType.isChecked())) {
                    click.millTypeId(holder.getAdapterPosition(), "", "");
                    return;
                }*/
            });

        } catch (Exception e) {
        }
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
