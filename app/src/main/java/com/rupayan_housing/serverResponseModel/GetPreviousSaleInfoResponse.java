package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rupayan_housing.adapter.OrderResponse;

import lombok.Data;

@Data
public class GetPreviousSaleInfoResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("order")
    @Expose
    private OrderResponse order;
    @SerializedName("order_details")
    @Expose
    private GetPreviousSaleOrderDetails orderDetails;
    @SerializedName("payment_info")
    @Expose
    private GetPreviousSalePaymentInfo paymentInfo;
    @SerializedName("payments_count")
    @Expose
    private Integer paymentsCount;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public OrderResponse getOrder() {
        return order;
    }

    public void setOrder(OrderResponse order) {
        this.order = order;
    }

    public GetPreviousSaleOrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(GetPreviousSaleOrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public GetPreviousSalePaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(GetPreviousSalePaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public Integer getPaymentsCount() {
        return paymentsCount;
    }

    public void setPaymentsCount(Integer paymentsCount) {
        this.paymentsCount = paymentsCount;
    }
}
