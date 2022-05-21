package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PendingSalesReturnDetailsResponse {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("customer")
    @Expose
    private PendingSalesReturnCustomer customer;
    @SerializedName("orderinfo")
    @Expose
    private PendingSalesReturnDetailsOrderInfo orderinfo;
    @SerializedName("other_information")
    @Expose
    private PendingSalesReturnDetailsOtherInformation otherInformation;
    @SerializedName("delivery_address")
    @Expose
    private DeliveryAddress deliveryAddress;
    @SerializedName("payment_info")
    @Expose
    private PendingSalesReturnDetailsPaymentInfo paymentInfo;
    @SerializedName("order_details")
    @Expose
    private List<PendingSalesReturnOrderDetails> orderDetails = null;
}
