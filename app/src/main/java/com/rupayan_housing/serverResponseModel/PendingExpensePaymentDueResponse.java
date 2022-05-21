package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PendingExpensePaymentDueResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("lists")
    @Expose
    private List<ExpensePaymentDueList> lists = null;
    @SerializedName("customer_info")
    @Expose
    private ExpensePaymentDueCustomerInfo customerInfo;
}
