package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class GetPreviousSaleOrderDetails {
    @SerializedName("orderID")
    @Expose
    private String orderID;
    @SerializedName("vendorID")
    @Expose
    private String vendorID;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("order_end_date")
    @Expose
    private String orderEndDate;
    @SerializedName("order_other_infos")
    @Expose
    private OrderOtherInfos orderOtherInfos;
    @SerializedName("items")
    @Expose
    private List<EditSaleItemsResponse> items = null;
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

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderEndDate() {
        return orderEndDate;
    }

    public void setOrderEndDate(String orderEndDate) {
        this.orderEndDate = orderEndDate;
    }

    public OrderOtherInfos getOrderOtherInfos() {
        return orderOtherInfos;
    }

    public void setOrderOtherInfos(OrderOtherInfos orderOtherInfos) {
        this.orderOtherInfos = orderOtherInfos;
    }

    public List<EditSaleItemsResponse> getItems() {
        return items;
    }

    public void setItems(List<EditSaleItemsResponse> items) {
        this.items = items;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getCollected() {
        return collected;
    }

    public void setCollected(Integer collected) {
        this.collected = collected;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
