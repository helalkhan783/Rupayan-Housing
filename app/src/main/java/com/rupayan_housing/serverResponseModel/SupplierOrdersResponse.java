package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class SupplierOrdersResponse {
    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;
    @SerializedName("payment_types")
    @Expose
    private PaymentTypes paymentTypes;
    @SerializedName("payment_sub_types")
    @Expose
    private PaymentSubTypes paymentSubTypes;
    @SerializedName("main_banks")
    @Expose
    private List<MainBank> mainBanks = null;
    @SerializedName("customer")
    @Expose
    private SupplierResponse customer;
    @SerializedName("payment_limit")
    @Expose
    private PaymentLimitResponse paymentLimit;
    @SerializedName("payment_to")
    @Expose
    private List<PaymentToResponse> paymentTo = null;
    @SerializedName("status")
    @Expose
    private Integer status;
}
