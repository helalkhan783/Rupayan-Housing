package com.rupayan_housing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rupayan_housing.R;
import com.rupayan_housing.databinding.DistrictWiseSaleReportListLayoutBinding;
import com.rupayan_housing.databinding.SaleReturnReportListLayoutBinding;
import com.rupayan_housing.serverResponseModel.DistrictWiseSaleReport;
import com.rupayan_housing.utils.MtUtils;
import com.rupayan_housing.utils.replace.KgToTon;
import com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.sale_return_report.SaleReturnReportListAdapter;
import com.rupayan_housing.view.fragment.all_report.sale_and_purchase_report.sale_return_report.sale_return_report_list.SaleReturnReportProfuctList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DistrictWiseSaleReportAdapter extends RecyclerView.Adapter<DistrictWiseSaleReportAdapter.viewHolder> {
    private Context context;
    List<DistrictWiseSaleReport> saleReports;

    public DistrictWiseSaleReportAdapter(Context context, List<DistrictWiseSaleReport> saleReports) {
        this.context = context;
        this.saleReports = saleReports;
    }

    @NonNull
    @NotNull
    @Override
    public DistrictWiseSaleReportAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        DistrictWiseSaleReportListLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.district_wise_sale_report_list_layout, parent, false);
        return new DistrictWiseSaleReportAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DistrictWiseSaleReportAdapter.viewHolder holder, int position) {
        DistrictWiseSaleReport currentitem = saleReports.get(position);
        position += 1;
        holder.itembinding.sl.setText(":  " + position);
        holder.itembinding.disName.setText(":  " + currentitem.getName());
        String  qty = "0";
        if (currentitem.getQuantity() != null){
            qty = String.valueOf(currentitem.getQuantity());

        }
        holder.itembinding.qty.setText(":  " + KgToTon.kgToTon(qty) + MtUtils.metricTon);

    }

    @Override
    public int getItemCount() {
        return saleReports.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private DistrictWiseSaleReportListLayoutBinding itembinding;

        public viewHolder(@NonNull @NotNull DistrictWiseSaleReportListLayoutBinding itembinding) {
            super(itembinding.getRoot());
            this.itembinding = itembinding;
        }
    }}
