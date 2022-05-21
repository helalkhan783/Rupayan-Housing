package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class GetPendingWashingCrushingDetailsResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("items")
    @Expose
    private PendingWashingCrushingItems items;
    @SerializedName("referrer")
    @Expose
    private PendingWashingCrushingReferrer referrer;
    @SerializedName("payment_info")
    @Expose
    private Boolean paymentInfo;
    @SerializedName("order_info")
    @Expose
    private GetPendingWashingCrushingDetailsOrderInfo orderInfo;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PendingWashingCrushingItems getItems() {
        return items;
    }

    public void setItems(PendingWashingCrushingItems items) {
        this.items = items;
    }

    public PendingWashingCrushingReferrer getReferrer() {
        return referrer;
    }

    public void setReferrer(PendingWashingCrushingReferrer referrer) {
        this.referrer = referrer;
    }

    public Boolean getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(Boolean paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public GetPendingWashingCrushingDetailsOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(GetPendingWashingCrushingDetailsOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
