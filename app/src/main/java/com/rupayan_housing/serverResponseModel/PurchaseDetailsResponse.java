package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PurchaseDetailsResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private  String message;
    @SerializedName("customer")
    @Expose
    private PurchaseDetailsCustomer customer;
    @SerializedName("orderinfo")
    @Expose
    private PurchaseDetailsOrderInfo orderinfo;
    @SerializedName("other_information")
    @Expose
    private PurchaseDetailsOtherInformation otherInformation;
    @SerializedName("delivery_address")
    @Expose
    private Object deliveryAddress;
    @SerializedName("payment_info")
    @Expose
    private PurchaseDetailsPaymentInfo paymentInfo;
    @SerializedName("order_details")
    @Expose
    private List<PurchaseOrderDetailList> orderDetails = null;

}
