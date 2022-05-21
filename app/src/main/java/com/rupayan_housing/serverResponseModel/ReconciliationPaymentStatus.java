package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ReconciliationPaymentStatus {
    @SerializedName("payment_status")
    @Expose
    private Object paymentStatus;
    @SerializedName("paid_amount")
    @Expose
    private Object paidAmount;
}
