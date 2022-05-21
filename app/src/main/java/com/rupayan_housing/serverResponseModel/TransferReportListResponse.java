package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rupayan_housing.view.fragment.stock.all_response.StockDeclineTransferredList;
import com.rupayan_housing.view.fragment.stock.all_response.StockTransferHistoryList;

import java.util.List;

import lombok.Data;

@Data
public class TransferReportListResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("transfered_list")
    @Expose
    private List<StockDeclineTransferredList> transferedList = null;

}
