package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class DayBookList {
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
    private Integer payment;
    @SerializedName("processed_by")
    @Expose
    private ProcessedBy processedBy;
    @SerializedName("enterprise")
    @Expose
    private String enterprise;

    public String getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(String transactionData) {
        this.transactionData = transactionData;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public Integer getReceipt() {
        return receipt;
    }

    public void setReceipt(Integer receipt) {
        this.receipt = receipt;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public ProcessedBy getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(ProcessedBy processedBy) {
        this.processedBy = processedBy;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }
}
