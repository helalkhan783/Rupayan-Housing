
package com.rupayan_housing.serverResponseModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class CurrentOrderDetails {
    @SerializedName("requisition_date")
    @Expose
    private String requisitionDate;
    @SerializedName("requisition_end_date")
    @Expose
    private String requisitionEndDate;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("total_amount")
    @Expose
    private Integer totalAmount;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("collected")
    @Expose
    private Integer collected;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
}
