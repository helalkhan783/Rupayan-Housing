package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class CashBookList {
    @SerializedName("transaction_data")
    @Expose
    private String transactionData;
    @SerializedName("reference_id")
    @Expose
    private String referenceId;
    @SerializedName("particulars")
    @Expose
    private String particulars;
    @SerializedName("receipt")
    @Expose
    private Integer receipt;
    @SerializedName("payment")
    @Expose
    private String payment;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("processed_by")
    @Expose
    private ProcessedBy processedBy;
    @SerializedName("enterprise")
    @Expose
    private String enterprise;
}
