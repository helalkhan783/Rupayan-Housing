package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ExpenseDueResponse {
    @SerializedName("orders")
    @Expose
    private List<ExpenseOrdersResponse> orders = null;
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
    private ExpenseCustomerResponse customer;
    @SerializedName("payment_to")
    @Expose
    private List<PaymentToExpenseResponse> paymentTo = null;
    @SerializedName("status")
    @Expose
    private Integer status;
}
