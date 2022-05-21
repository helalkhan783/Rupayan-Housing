package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class EditWashingCrushingResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("order")
    @Expose
    private EditWashingCrushingOrder order;
    @SerializedName("order_details")
    @Expose
    private EditWashingCrushingOrderDetails orderDetails;
    @SerializedName("payment_info")
    @Expose
    private Object paymentInfo;
    @SerializedName("destination_store")
    @Expose
    private String destination_store;
}
