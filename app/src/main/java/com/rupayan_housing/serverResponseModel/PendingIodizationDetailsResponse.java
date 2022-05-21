package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class PendingIodizationDetailsResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("items")
    @Expose
    private IodizationItems items;
    @SerializedName("referrer")
    @Expose
    private IodizationRefferResponse referrer;
    @SerializedName("payment_info")
    @Expose
    private Boolean paymentInfo;
    @SerializedName("order_info")
    @Expose
    private IodizationOrderInfoResponse orderInfo;
}
