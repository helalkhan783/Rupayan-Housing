package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PendingExpenseDetails {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("expense_lists")
    @Expose
    private List<ExpenseListResponse> expenseLists = null;
    @SerializedName("expense_info")
    @Expose
    private ExpenseInfoList expenseInfo;
    @SerializedName("payment_amount")
    @Expose
    private PaymentAmountResponse paymentAmount;
    @SerializedName("processed_by")
    @Expose
    private ProcessedBy processedBy;
}
