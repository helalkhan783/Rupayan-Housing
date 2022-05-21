package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class AddNewLimitInstructionResponse {
    @SerializedName("customerID")
    @Expose
    private String customerID;
    @SerializedName("last_received_date")
    @Expose
    private String lastReceivedDate;
    @SerializedName("last_paid_amount")
    @Expose
    private int lastPaidAmount;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("customer_fname")
    @Expose
    private String customer_fname;
    @SerializedName("total_paid")
    @Expose
    private int totalPaid;
    @SerializedName("due")
    @Expose
    private int due;
}
