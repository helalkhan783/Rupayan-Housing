package com.rupayan_housing.view.fragment.all_report.stock_in_out_report.list_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class StockIOReportListResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("stock_report")
    @Expose
    private List<StockIOReportList> stockReport = null;


}
