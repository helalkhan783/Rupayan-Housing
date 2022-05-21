package com.rupayan_housing.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rupayan_housing.R;
import com.rupayan_housing.databinding.CertificateInfoInfoLayoutBinding;
import com.rupayan_housing.localDatabase.PreferenceManager;
import com.rupayan_housing.serverResponseModel.view_details.MillerCertificateDatum;
import com.rupayan_housing.view.fragment.DateFormatRight;
import com.rupayan_housing.view.fragment.miller.MillerDetailsViewFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.AllArgsConstructor;


public class CertificateInfoAdapter extends RecyclerView.Adapter<CertificateInfoAdapter.viewHolder> {
    private FragmentActivity context;
    private List<MillerCertificateDatum> lists;
    private MillerDetailsViewFragment getClass;

    public CertificateInfoAdapter(FragmentActivity context, List<MillerCertificateDatum> lists, MillerDetailsViewFragment getClass) {
        this.context = context;
        this.lists = lists;
        this.getClass = getClass;
    }

    @NonNull
    @NotNull
    @Override
    public CertificateInfoAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        CertificateInfoInfoLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.certificate_info_info_layout, parent, false);
        return new CertificateInfoAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CertificateInfoAdapter.viewHolder holder, int position) {
        MillerCertificateDatum currentList = lists.get(position);


        String profileTypeId = PreferenceManager.getInstance(context).getUserCredentials().getProfileTypeId();
        if (profileTypeId.equals("4") || profileTypeId.equals("5") || profileTypeId.equals("6")) {
            if (currentList.getReviewStatus().equals("0")) {
                holder.itembinding.statusLayout.setVisibility(View.GONE);
                holder.itembinding.approvalLayout.setVisibility(View.VISIBLE);

            }
        }
        if (currentList.getCertificateTypeName() == null) {
            holder.itembinding.typeOfCertificate.setText(":");
        } else {
            holder.itembinding.typeOfCertificate.setText(":  " + currentList.getCertificateTypeName());

        }

        if (currentList.getReviewStatus() != null) {
            if (currentList.getReviewStatus().equals("1")) {
                holder.itembinding.status.setText(":  Approved");
                holder.itembinding.status.setTextColor(Color.parseColor("#00A6E3"));
            }
            if (currentList.getReviewStatus().equals("2")) {
                holder.itembinding.status.setText(":  Declined");
                holder.itembinding.status.setTextColor(Color.parseColor("#FD3846"));
            }
            if (currentList.getReviewStatus().equals("0")) {
                holder.itembinding.status.setText(":  Pending");
                holder.itembinding.status.setTextColor(Color.parseColor("#FF4C3B"));

            }
        }
        if (currentList.getIssuerName() == null) {
            holder.itembinding.issuerName.setText(":");
        } else {
            holder.itembinding.issuerName.setText(":  " + currentList.getIssuerName().replaceAll("^\"|\"$", ""));

        }
        if (currentList.getIssueDate() == null) {
            holder.itembinding.issuingDate.setText(":");
        } else {
            holder.itembinding.issuingDate.setText(":  " + new DateFormatRight(context, currentList.getIssueDate()).onlyDayMonthYear());

        }
        if (currentList.getCertificateDate() == null) {
            holder.itembinding.certificateDate.setText(":");
        } else {
            holder.itembinding.certificateDate.setText(":  " + new DateFormatRight(context, currentList.getCertificateDate()).onlyDayMonthYear());

        }
        if (currentList.getRenewDate() == null) {
            holder.itembinding.renewalDate.setText(":");
        } else {
            holder.itembinding.renewalDate.setText(":  " + currentList.getRenewDate());

        }
        if (currentList.getRemarks() == null) {
            holder.itembinding.remarks.setText(":");
        } else {
            holder.itembinding.remarks.setText(":  " + currentList.getRemarks().replaceAll("^\"|\"$", ""));

        }
        try {
            Glide.with(context).load("https://cmis.usi.net.bd/" + currentList.getCertificateImage())
                    .placeholder(R.drawable.error_one)
                    .error(R.drawable.error_one)
                    .into(holder.itembinding.imageView);

        } catch (NullPointerException e) {
            Log.d("ERROR", e.getMessage());
        }

        // approve

        holder.itembinding.approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClass.approve(holder.getAdapterPosition(), currentList.getCertificateTypeID(), "1");

            }
        });
        holder.itembinding.declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClass.approve(holder.getAdapterPosition(), currentList.getCertificateTypeID(), "2");

            }
        });

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private CertificateInfoInfoLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull CertificateInfoInfoLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }
}
