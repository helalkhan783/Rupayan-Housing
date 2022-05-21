package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ExpenseOrdersResponse {
    @SerializedName("sales_type")
    @Expose
    private String salesType;
    @SerializedName("orderID")
    @Expose
    private String orderID;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("paid_amount")
    @Expose
    private Integer paidAmount;
    @SerializedName("due")
    @Expose
    private Integer due;
}
