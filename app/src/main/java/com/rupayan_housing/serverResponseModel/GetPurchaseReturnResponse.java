package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GetPurchaseReturnResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("order_details")
    @Expose
    private PurchaseRetrunOrderDetails orderDetails;
    @SerializedName("payment_info")
    @Expose
    private PaymentInfo paymentInfo;
    @SerializedName("initial_payment_amount")
    @Expose
    private String initialPaymentAmount;
    @SerializedName("previous_return_amount")
    @Expose
    private Integer previousReturnAmount;
    @SerializedName("discount")
    @Expose
    private Integer discount;
}
