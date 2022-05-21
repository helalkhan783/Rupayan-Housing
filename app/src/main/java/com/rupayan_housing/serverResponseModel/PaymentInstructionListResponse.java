package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PaymentInstructionListResponse {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("customer_fname")
    @Expose
    private String customer_fname;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("total_paid")
    @Expose
    private String totalPaid;
    @SerializedName("payment_limit")
    @Expose
    private String paymentLimit;
    @SerializedName("due")
    @Expose
    private int due;
}
