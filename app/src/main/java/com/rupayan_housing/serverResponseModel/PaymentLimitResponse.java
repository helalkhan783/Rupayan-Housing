package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PaymentLimitResponse {
    @SerializedName("pay_limit_amount")
    @Expose
    private Integer payLimitAmount;
}
