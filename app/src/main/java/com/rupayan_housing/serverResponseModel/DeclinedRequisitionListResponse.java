package com.rupayan_housing.serverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class DeclinedRequisitionListResponse {
    @SerializedName("orderID")
    @Expose
    private String orderID;
    @SerializedName("requisition_date")
    @Expose
    private String requisitionDate;
    @SerializedName("requisition_end_date")
    @Expose
    private String requisitionEndDate;
    @SerializedName("enterprise")
    @Expose
    private String enterpriseName;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("order_amount")
    @Expose
    private String orderAmount;
    @SerializedName("profile_photo")
    @Expose
    private String profilePhoto;
    @SerializedName("Full_Name")
    @Expose
    private String fullName;}
