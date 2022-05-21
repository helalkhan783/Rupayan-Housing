package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PaymentToResponse {
    @SerializedName("mainBankID")
    @Expose
    private String mainBankID;
    @SerializedName("mainBankName")
    @Expose
    private String mainBankName;
}
